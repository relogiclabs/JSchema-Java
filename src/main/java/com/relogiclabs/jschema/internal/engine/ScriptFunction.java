package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.exception.InvalidFunctionException;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.exception.ScriptCommonException;
import com.relogiclabs.jschema.exception.ScriptInitiatedException;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.internal.script.GArray;
import com.relogiclabs.jschema.internal.script.GFunction;
import com.relogiclabs.jschema.internal.tree.EFunction;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.type.EBoolean;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.relogiclabs.jschema.internal.library.ScriptConstant.CALLER_HVAR;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.TARGET_HVAR;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.subList;
import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC10;
import static com.relogiclabs.jschema.type.EValue.VOID;

@Getter
@RequiredArgsConstructor
public final class ScriptFunction implements EFunction {
    private final String name;
    private final GFunction function;

    @Override
    public Object invoke(JFunction caller, Object[] arguments) {
        var parameters = function.getParameters();
        var scope = new ScopeContext(caller.getRuntime().getScriptContext());
        scope.update(CALLER_HVAR, caller);
        scope.update(TARGET_HVAR, (EValue) arguments[0]);
        int i = 1;
        for(var p : parameters) scope.addVariable(p.getName(), (EValue) arguments[i++]);
        return function.isFuture()
                ? (FutureFunction) () -> invoke(function, scope)
                : invoke(function, scope);
    }

    private boolean invoke(GFunction function, ScopeContext scope) {
        try {
            var result = function.invoke(scope);
            if(result == VOID) return true;
            if(!(result instanceof EBoolean b)) throw new InvalidFunctionException(FUNC10,
                    concat("Function '", name, "' must return a boolean value"));
            return b.getValue();
        } catch(JsonSchemaException | ScriptInitiatedException e) {
            throw e;
        } catch(ScriptCommonException e) {
            scope.getRuntime().getExceptions().fail(e);
            return false;
        }
    }

    @Override
    public List<Object> prebind(List<? extends EValue> arguments) {
        var parameters = function.getParameters();
        var minArgSize = function.isVariadic() ? parameters.length - 1 : parameters.length;
        if(arguments.size() < minArgSize) return null;
        var result = new ArrayList<>(parameters.length);
        for(int i = 0; i < parameters.length; i++) {
            if(parameters[i].isVariadic()) result.add(new GArray(subList(arguments, i)));
            else result.add(arguments.get(i));
        }
        return result;
    }

    @Override
    public Class<?> getTargetType() {
        return JNode.class;
    }

    @Override
    public int getArity() {
        return function.isVariadic() ? -1 : function.getParameters().length + 1;
    }
}