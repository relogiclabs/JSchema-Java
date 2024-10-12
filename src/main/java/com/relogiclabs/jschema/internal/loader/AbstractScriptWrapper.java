package com.relogiclabs.jschema.internal.loader;

import com.relogiclabs.jschema.exception.ArgumentTypeException;
import com.relogiclabs.jschema.exception.InvocationRuntimeException;
import com.relogiclabs.jschema.internal.util.ReflectionHelper;
import com.relogiclabs.jschema.type.EValue;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import static com.relogiclabs.jschema.internal.loader.SchemaFunction.VARIADIC_ARITY;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.subList;
import static com.relogiclabs.jschema.internal.util.ReflectionHelper.getDerived;
import static com.relogiclabs.jschema.internal.util.ReflectionHelper.getMethodArity;
import static com.relogiclabs.jschema.internal.util.ReflectionHelper.isMatched;

public abstract class AbstractScriptWrapper {
    protected final Method method;
    protected final Parameter[] parameters;
    protected final int arity;

    public AbstractScriptWrapper(Method method) {
        this.method = method;
        this.parameters = method.getParameters();
        this.arity = getMethodArity(parameters);
    }

    public abstract String[] getKeys();

    public String getName() {
        return method.getName();
    }

    private boolean isVariadic() {
        return arity == VARIADIC_ARITY;
    }

    protected Object[] processArguments(List<? extends EValue> arguments) {
        if(isVariadic() && arguments.size() < parameters.length - 1)
            throw failOnVariadicArity();
        var result = new Object[parameters.length];
        if(parameters.length == 0) return result;
        var endIndex = parameters.length - (isVariadic() ? 1 : 0);
        for(int i = 0; i < endIndex; i++) {
            if(!isMatched(parameters[i], arguments.get(i)))
                throw failOnInvalidArgumentType(parameters[i], arguments.get(i));
            result[i] = arguments.get(i);
        }
        if(isVariadic()) {
            var remainingArgs = subList(arguments, endIndex);
            result[endIndex] = processVarArgs(parameters[endIndex], remainingArgs);
        }
        return result;
    }

    private Object processVarArgs(Parameter parameter, List<? extends EValue> arguments) {
        var componentType = parameter.getType().getComponentType();
        if(componentType == null) throw new IllegalStateException("Invalid function parameter");
        var result = Array.newInstance(componentType, arguments.size());
        for(var i = 0; i < arguments.size(); i++) {
            var arg = arguments.get(i);
            if(!componentType.isInstance(getDerived(arg)))
                throw failOnInvalidArgumentType(parameter, arg);
            Array.set(result, i, arg);
        }
        return result;
    }

    public String getSignature() {
        return ReflectionHelper.getSignature(method);
    }

    protected abstract InvocationRuntimeException failOnVariadicArity();
    protected abstract ArgumentTypeException failOnInvalidArgumentType(Parameter parameter,
                                                                       EValue argument);
}