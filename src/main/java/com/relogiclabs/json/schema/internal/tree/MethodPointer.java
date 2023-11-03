package com.relogiclabs.json.schema.internal.tree;

import com.relogiclabs.json.schema.exception.TargetInvocationException;
import com.relogiclabs.json.schema.function.FunctionBase;
import com.relogiclabs.json.schema.types.JFunction;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.message.ErrorCode.FUNC07;
import static com.relogiclabs.json.schema.message.ErrorCode.FUNC08;
import static java.util.stream.Collectors.joining;

@Getter
public final class MethodPointer {

    private final FunctionBase instance;
    private final Method method;
    public final List<Parameter> parameters;

    public MethodPointer(FunctionBase instance, Method method, Parameter[] parameters) {
        this.instance = instance;
        this.method = method;
        this.parameters = List.of(parameters);
    }

    public boolean invoke(JFunction function, List<Object> arguments) {
        try {
            instance.setFunction(function);
            Object result = method.invoke(instance, arguments.toArray());
            if(!(result instanceof Boolean boolResult)) throw new IllegalStateException();
            return boolResult;
        } catch (InvocationTargetException e) {
            throw new TargetInvocationException(FUNC07,
                    "Target invocation exception occurred", e.getCause());
        } catch (IllegalAccessException e) {
            throw new TargetInvocationException(FUNC08,
                    "Illegal access exception occurred", e);
        }
    }

    public static String getSignature(Method method) {
        String typeName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String parameters = Arrays.stream(method.getParameters())
                .map(MethodPointer::stringOf).collect(joining(", "));
        String returnType = method.getReturnType().getName();
        return concat(returnType, " ", typeName, ".", methodName, "(", parameters, ")");
    }

    private static String stringOf(Parameter parameter) {
        var builder = new StringBuilder(parameter.getType().getSimpleName());
        builder.append(" ").append(parameter.getName());
        return builder.toString();
    }
}