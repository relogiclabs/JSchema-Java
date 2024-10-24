package com.relogiclabs.jschema.internal.util;

import com.relogiclabs.jschema.node.Derivable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static com.relogiclabs.jschema.internal.loader.SchemaFunction.VARIADIC_ARITY;
import static com.relogiclabs.jschema.internal.util.CommonHelper.nonNullFrom;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class ReflectionHelper {
    public static String getSignature(Method method) {
        var typeName = method.getDeclaringClass().getName();
        var methodName = method.getName();
        var parameters = stream(method.getParameters())
            .map(ReflectionHelper::stringOf).collect(joining(", "));
        var returnType = method.getReturnType().getSimpleName();
        return returnType + " " + typeName + "." + methodName + "(" + parameters + ")";
    }

    private static String stringOf(Parameter parameter) {
        return parameter.getType().getSimpleName() + " " + parameter.getName();
    }

    public static int getMethodArity(Parameter[] parameters) {
        return parameters.length != 0 && parameters[parameters.length - 1].isVarArgs()
            ? VARIADIC_ARITY : parameters.length;
    }

    public static Object getDerived(Object value) {
        if(value instanceof Derivable derivable)
            return nonNullFrom(derivable.getDerived(), value);
        return value;
    }

    public static boolean isMatched(Parameter parameter, Object argument) {
        return parameter.getType().isInstance(getDerived(argument));
    }

    public static boolean isMatched(Class<?> type, Object value) {
        return type.isInstance(getDerived(value));
    }
}