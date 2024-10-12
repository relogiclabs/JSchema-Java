package com.relogiclabs.jschema.internal.loader;

import com.relogiclabs.jschema.exception.ArgumentTypeException;
import com.relogiclabs.jschema.exception.InvalidReturnTypeException;
import com.relogiclabs.jschema.exception.MethodArgumentTypeException;
import com.relogiclabs.jschema.exception.TargetInvocationException;
import com.relogiclabs.jschema.exception.VariadicInvocationException;
import com.relogiclabs.jschema.extension.ScriptMethod;
import com.relogiclabs.jschema.extension.ScriptMethods;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.RMethod;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import static com.relogiclabs.jschema.internal.script.ScriptType.getBranchCodes;
import static com.relogiclabs.jschema.internal.util.AnnotationHelper.nonEmptyFrom;
import static com.relogiclabs.jschema.internal.util.AnnotationHelper.processAliases;
import static com.relogiclabs.jschema.internal.util.CommonHelper.nonNullFrom;
import static com.relogiclabs.jschema.message.ErrorCode.MTHARG01;
import static com.relogiclabs.jschema.message.ErrorCode.MTHCAL01;
import static com.relogiclabs.jschema.message.ErrorCode.MTHCAL02;
import static com.relogiclabs.jschema.message.ErrorCode.MTHNUL01;
import static com.relogiclabs.jschema.message.ErrorCode.MTHSIG01;
import static com.relogiclabs.jschema.message.ErrorCode.MTHVAL01;
import static com.relogiclabs.jschema.message.ErrorCode.MTHVAR01;
import static java.util.Arrays.stream;

@Getter
public class ScriptMethodWrapper extends AbstractScriptWrapper implements RMethod {
    private final ScriptMethod annotation;
    private final ScriptMethods instance;

    public ScriptMethodWrapper(Method method, ScriptMethod annotation, ScriptMethods instance) {
        super(method);
        requireValidReturnType();
        this.annotation = annotation;
        this.instance = instance;
    }

    private void requireValidReturnType() {
        if(!EValue.class.isAssignableFrom(method.getReturnType()))
            throw new InvalidReturnTypeException(MTHSIG01, "Script method: "
                + getSignature() + " requires valid return type");
    }

    @Override
    public String[] getKeys() {
        var names = nonEmptyFrom(annotation.names(), getName());
        var types = processAliases(annotation.value(), annotation.types());
        return stream(names)
            .map(n -> n + "#" + arity)
            .flatMap(s -> stream(types)
                .flatMap(t -> stream(getBranchCodes(t)).map(p -> p + s)))
            .toArray(String[]::new);
    }

    @Override
    public EValue invoke(EValue self, List<EValue> arguments, ScriptScope parentScope) {
        try {
            instance.setScope(parentScope);
            instance.setSelf(self);
            instance.setMethod(method);
            var result = method.invoke(instance, processArguments(arguments));
            if(result == null) throw new InvalidReturnTypeException(MTHNUL01,
                "Script method: " + getSignature() + " must not return null");
            if(!(result instanceof EValue value)) throw new InvalidReturnTypeException(MTHVAL01,
                "Script method: " + getSignature() + " requires valid return type");
            return value;
        } catch(InvocationTargetException e) {
            if(e.getCause() instanceof RuntimeException r) throw r;
            throw new TargetInvocationException(MTHCAL01,
                "Target invocation checked exception occurred", nonNullFrom(e.getCause(), e));
        } catch(IllegalAccessException e) {
            throw new TargetInvocationException(MTHCAL02, "Illegal access exception occurred", e);
        }
    }

    @Override
    protected VariadicInvocationException failOnVariadicArity() {
        var exception = new VariadicInvocationException(MTHVAR01,
            "Too few arguments for invocation of variadic method");
        exception.setSelf(instance.getSelf());
        exception.setMethod(method);
        return exception;
    }

    @Override
    protected ArgumentTypeException failOnInvalidArgumentType(Parameter parameter, EValue argument) {
        var exception = new MethodArgumentTypeException(MTHARG01, "Invalid argument type "
            + argument.getType() + " for parameter '" + parameter.getName() + "'");
        exception.setSelf(instance.getSelf());
        exception.setMethod(method);
        return exception;
    }
}