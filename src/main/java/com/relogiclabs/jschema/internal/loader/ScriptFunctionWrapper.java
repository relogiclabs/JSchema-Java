package com.relogiclabs.jschema.internal.loader;

import com.relogiclabs.jschema.exception.ArgumentTypeException;
import com.relogiclabs.jschema.exception.FunctionArgumentTypeException;
import com.relogiclabs.jschema.exception.InvalidReturnTypeException;
import com.relogiclabs.jschema.exception.TargetInvocationException;
import com.relogiclabs.jschema.exception.VariadicInvocationException;
import com.relogiclabs.jschema.extension.ScriptFunction;
import com.relogiclabs.jschema.extension.ScriptFunctions;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.RFunction;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import static com.relogiclabs.jschema.internal.util.AnnotationHelper.nonEmptyFrom;
import static com.relogiclabs.jschema.internal.util.CommonHelper.nonNullFrom;
import static com.relogiclabs.jschema.message.ErrorCode.FNSARG01;
import static com.relogiclabs.jschema.message.ErrorCode.FNSCAL01;
import static com.relogiclabs.jschema.message.ErrorCode.FNSCAL02;
import static com.relogiclabs.jschema.message.ErrorCode.FNSNUL01;
import static com.relogiclabs.jschema.message.ErrorCode.FNSSIG01;
import static com.relogiclabs.jschema.message.ErrorCode.FNSVAL01;
import static com.relogiclabs.jschema.message.ErrorCode.FNSVAR01;
import static java.util.Arrays.stream;

@Getter
public class ScriptFunctionWrapper extends AbstractScriptWrapper implements RFunction {
    private final ScriptFunction annotation;
    private final ScriptFunctions instance;

    public ScriptFunctionWrapper(Method method, ScriptFunction annotation, ScriptFunctions instance) {
        super(method);
        requireValidReturnType();
        this.annotation = annotation;
        this.instance = instance;
    }

    private void requireValidReturnType() {
        if(!EValue.class.isAssignableFrom(method.getReturnType()))
            throw new InvalidReturnTypeException(FNSSIG01, "Script function: "
                + getSignature() + " requires valid return type");
    }

    @Override
    public String[] getKeys() {
        var names = nonEmptyFrom(annotation.value(), getName());
        return stream(names).map(n -> n + "#" + arity).toArray(String[]::new);
    }

    @Override
    public EValue invoke(List<EValue> arguments, ScriptScope parentScope) {
        try {
            instance.setScope(parentScope);
            instance.setMethod(method);
            var result = method.invoke(instance, processArguments(arguments));
            if(result == null) throw new InvalidReturnTypeException(FNSNUL01,
                "Script function: " + getSignature() + " must not return null");
            if(!(result instanceof EValue value)) throw new InvalidReturnTypeException(FNSVAL01,
                "Script function: " + getSignature() + " requires valid return type");
            return value;
        } catch(InvocationTargetException e) {
            if(e.getCause() instanceof RuntimeException r) throw r;
            throw new TargetInvocationException(FNSCAL01,
                "Target invocation checked exception occurred", nonNullFrom(e.getCause(), e));
        } catch(IllegalAccessException e) {
            throw new TargetInvocationException(FNSCAL02, "Illegal access exception occurred", e);
        }
    }

    @Override
    protected VariadicInvocationException failOnVariadicArity() {
        var exception = new VariadicInvocationException(FNSVAR01,
            "Too few arguments for invocation of variadic function");
        exception.setMethod(method);
        return exception;
    }

    @Override
    protected ArgumentTypeException failOnInvalidArgumentType(Parameter parameter, EValue argument) {
        var exception = new FunctionArgumentTypeException(FNSARG01, "Invalid argument type "
            + argument.getType() + " for parameter '" + parameter.getName() + "'");
        exception.setMethod(method);
        return exception;
    }
}