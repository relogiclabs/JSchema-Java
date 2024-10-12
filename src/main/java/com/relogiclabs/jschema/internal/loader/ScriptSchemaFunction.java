package com.relogiclabs.jschema.internal.loader;

import com.relogiclabs.jschema.exception.BaseRuntimeException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.exception.InvalidReturnTypeException;
import com.relogiclabs.jschema.exception.ScriptThrowInitiatedException;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.internal.engine.ConstraintScope;
import com.relogiclabs.jschema.internal.script.GArray;
import com.relogiclabs.jschema.internal.script.GFunction;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.type.EBoolean;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.relogiclabs.jschema.internal.util.CollectionHelper.subList;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.toArray;
import static com.relogiclabs.jschema.internal.util.StringHelper.joinWith;
import static com.relogiclabs.jschema.message.ErrorCode.FNSRET01;
import static com.relogiclabs.jschema.type.EValue.VOID;
import static java.util.Arrays.stream;

@Getter
@RequiredArgsConstructor
public final class ScriptSchemaFunction implements SchemaFunction {
    public static final String CALLER_HVAR = "+caller";
    public static final String TARGET_HVAR = "+target";

    private final String name;
    private final GFunction function;
    private final int arity;
    private final Class<?> targetType;

    public ScriptSchemaFunction(String name, GFunction function) {
        this.name = name;
        this.function = function;
        this.arity = function.isVariadic() ? VARIADIC_ARITY
            : function.getParameters().length + 1;
        this.targetType = JNode.class;
    }

    @Override
    public String[] getKeys() {
        return toArray(CONSTRAINT_PREFIX + getName() + "#" + getArity());
    }

    @Override
    public List<Object> prebind(List<? extends EValue> arguments) {
        var parameters = function.getParameters();
        if(arity == VARIADIC_ARITY && arguments.size() < parameters.length - 1) return null;
        var result = new ArrayList<>(parameters.length + 1);
        if(parameters.length == 0) return result;
        var endIndex = parameters.length - 1;
        for(int i = 0; i < endIndex; i++) result.add(arguments.get(i));
        if(parameters[endIndex].isVariadic())
            result.add(new GArray(subList(arguments, endIndex)));
        else result.add(arguments.get(endIndex));
        return result;
    }

    @Override
    public Object invoke(JFunction invoker, Object[] arguments) {
        var parameters = function.getParameters();
        var scope = new ConstraintScope(invoker.getRuntime().getScriptGlobalScope());
        scope.update(CALLER_HVAR, invoker);
        scope.update(TARGET_HVAR, (EValue) arguments[0]);
        int i = 1;
        for(var p : parameters) scope.addVariable(p.getName(), (EValue) arguments[i++]);
        return function.isFuture()
            ? (FutureFunction) () -> invoke(scope)
            : invoke(scope);
    }

    private boolean invoke(ConstraintScope scope) {
        try {
            scope.unpackReceivers();
            var result = function.invoke(scope);
            if(result == VOID) return true;
            if(!(result instanceof EBoolean b)) throw new InvalidReturnTypeException(FNSRET01,
                "Constraint function: " + getSignature() + " needs to return a boolean value");
            return b.getValue();
        } catch(FunctionValidationException | ScriptThrowInitiatedException e) {
            throw e;
        } catch(BaseRuntimeException e) {
            scope.getRuntime().getExceptions().fail(e);
            return false;
        }
    }

    public String getSignature() {
        var builder = new StringBuilder();
        if(function.isFuture()) builder.append("future constraint function ");
        else if(function.isConstraint()) builder.append("constraint function ");
        else if(function.isSubroutine()) builder.append("subroutine function ");
        builder.append(name).append(joinWith(stream(function.getParameters()), ", ", "(", ")"));
        return builder.toString();
    }

    @Override
    public String toString() {
        return getSignature();
    }
}