package com.relogiclabs.jschema.internal.loader;

import com.relogiclabs.jschema.exception.InvalidFunctionException;
import com.relogiclabs.jschema.exception.InvalidReturnTypeException;
import com.relogiclabs.jschema.exception.TargetInvocationException;
import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.extension.ConstraintFunctions;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static com.relogiclabs.jschema.internal.util.AnnotationHelper.nonEmptyFrom;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.subList;
import static com.relogiclabs.jschema.internal.util.CommonHelper.nonNullFrom;
import static com.relogiclabs.jschema.internal.util.ReflectionHelper.getDerived;
import static com.relogiclabs.jschema.internal.util.ReflectionHelper.getMethodArity;
import static com.relogiclabs.jschema.internal.util.ReflectionHelper.getSignature;
import static com.relogiclabs.jschema.internal.util.ReflectionHelper.isMatched;
import static com.relogiclabs.jschema.message.ErrorCode.FNCNUL01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCCAL01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCCAL02;
import static com.relogiclabs.jschema.message.ErrorCode.FNCRET01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCSIG01;
import static java.util.Arrays.stream;

@Getter
public final class NativeSchemaFunction implements SchemaFunction {
    private final Method method;
    private final Parameter[] parameters;
    private final int arity;
    private final ConstraintFunction annotation;
    private final ConstraintFunctions instance;
    private final Class<?> targetType;

    public NativeSchemaFunction(Method method, ConstraintFunction annotation, ConstraintFunctions instance) {
        this.method = method;
        requireValidReturnType();
        this.parameters = method.getParameters();
        requireValidTargetParameter();
        this.arity = getMethodArity(parameters);
        this.annotation = annotation;
        this.instance = instance;
        this.targetType = parameters[0].getType();
    }

    private void requireValidReturnType() {
        var type = method.getReturnType();
        if(type != boolean.class && type != FutureFunction.class && type != Boolean.class)
            throw new InvalidReturnTypeException(FNCRET01, "Constraint function: "
                + getSignature(method) + " requires valid return type");
    }

    private void requireValidTargetParameter() {
        if(parameters.length == 0 || parameters[0].isVarArgs())
            throw new InvalidFunctionException(FNCSIG01, "Constraint function: "
                + getSignature(method) + " requires valid target parameter");
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public String[] getKeys() {
        var names = nonEmptyFrom(annotation.value(), getName());
        return stream(names)
            .map(n -> CONSTRAINT_PREFIX + n + "#" + getArity())
            .toArray(String[]::new);
    }

    private boolean isVariadic() {
        return arity == VARIADIC_ARITY;
    }

    @Override
    public List<Object> prebind(List<? extends EValue> arguments) {
        if(isVariadic() && arguments.size() < parameters.length - 2) return null;
        var result = new ArrayList<>(parameters.length);
        if(parameters.length == 1) return result;
        var endIndex = parameters.length - (isVariadic() ? 1 : 0);
        for(int i = 1; i < endIndex; i++) {
            if(!isMatched(parameters[i], arguments.get(i - 1))) return null;
            result.add(arguments.get(i - 1));
        }
        if(isVariadic()) {
            var remainingArgs = subList(arguments, endIndex - 1);
            var varArgs = processVarArgs(parameters[endIndex], remainingArgs);
            if(varArgs == null) return null;
            result.add(varArgs);
        }
        return result;
    }

    private static Object processVarArgs(Parameter parameter, List<?> arguments) {
        var componentType = parameter.getType().getComponentType();
        if(componentType == null) throw new IllegalStateException("Invalid function parameter");
        var result = Array.newInstance(componentType, arguments.size());
        for(var i = 0; i < arguments.size(); i++) {
            var arg = arguments.get(i);
            if(!componentType.isInstance(getDerived(arg))) return null;
            Array.set(result, i, arg);
        }
        return result;
    }

    @Override
    public Object invoke(JFunction invoker, Object[] arguments) {
        try {
            instance.setInvoker(invoker);
            instance.setMethod(method);
            instance.setRuntime(invoker.getRuntime());
            var result = method.invoke(instance, arguments);
            if(result == null) throw new InvalidReturnTypeException(FNCNUL01,
                "Constraint function: " + getSignature(method) + " must not return null");
            return result;
        } catch (InvocationTargetException e) {
            if(e.getCause() instanceof RuntimeException r) throw r;
            throw new TargetInvocationException(FNCCAL01,
                "Target invocation checked exception occurred", nonNullFrom(e.getCause(), e));
        } catch (IllegalAccessException e) {
            throw new TargetInvocationException(FNCCAL02, "Illegal access exception occurred", e);
        }
    }
}