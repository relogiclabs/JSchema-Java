package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.exception.InvalidFunctionException;
import com.relogiclabs.jschema.exception.TargetInvocationException;
import com.relogiclabs.jschema.function.FunctionProvider;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.relogiclabs.jschema.internal.util.CollectionHelper.subList;
import static com.relogiclabs.jschema.internal.util.MiscellaneousHelper.getDerived;
import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC07;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC08;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC09;
import static java.util.stream.Collectors.joining;

@Getter
public final class NativeFunction implements EFunction {
    private final Method method;
    private final List<Parameter> parameters;
    private final FunctionProvider instance;

    public NativeFunction(Method method, Parameter[] parameters, FunctionProvider instance) {
        this.method = method;
        this.parameters = List.of(parameters);
        this.instance = instance;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public int getArity() {
        var size = parameters.size();
        return parameters.get(size - 1).isVarArgs() ? -1 : size;
    }

    @Override
    public Class<?> getTargetType() {
        return parameters.get(0).getType();
    }

    @Override
    public Object invoke(JFunction caller, Object[] arguments) {
        try {
            instance.setCaller(caller);
            Object result = method.invoke(instance, arguments);
            if(result == null) throw new InvalidFunctionException(FUNC09,
                    concat("Function ", method.getName(), " must not return null"));
            return result;
        } catch (InvocationTargetException e) {
            throw new TargetInvocationException(FUNC07,
                    "Target invocation exception occurred", e.getCause());
        } catch (IllegalAccessException e) {
            throw new TargetInvocationException(FUNC08,
                    "Illegal access exception occurred", e);
        }
    }

    @Override
    public List<Object> prebind(List<? extends EValue> arguments) {
        var size = parameters.size();
        if(parameters.get(size - 1).isVarArgs() && size - 2 > arguments.size()) return null;
        var result = new ArrayList<>(size + 1);
        for(int i = 1; i < size; i++) {
            if(parameters.get(i).isVarArgs()) {
                var varArgs = processVarArgs(parameters.get(i), subList(arguments, i - 1));
                if(varArgs == null) return null;
                result.add(varArgs);
                break;
            }
            if(!isMatch(arguments.get(i - 1), parameters.get(i))) return null;
            result.add(arguments.get(i - 1));
        }
        return result;
    }

    private static Object processVarArgs(Parameter parameter, List<?> arguments) {
        var componentType = parameter.getType().getComponentType();
        if(componentType == null) throw new IllegalStateException("Invalid function parameter");
        var result = Array.newInstance(componentType, arguments.size());
        for(var i = 0; i < arguments.size(); i++) {
            var arg = arguments.get(i);
            if(!componentType.isInstance(arg)) return null;
            Array.set(result, i, arg);
        }
        return result;
    }

    private static boolean isMatch(Object argument, Parameter parameter) {
        return parameter.getType().isInstance(getDerived(argument));
    }

    public static String getSignature(Method method) {
        String typeName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String parameters = Arrays.stream(method.getParameters())
                .map(NativeFunction::stringOf).collect(joining(", "));
        String returnType = method.getReturnType().getName();
        return concat(returnType, " ", typeName, ".", methodName, "(", parameters, ")");
    }

    private static String stringOf(Parameter parameter) {
        return parameter.getType().getSimpleName() + " " + parameter.getName();
    }
}