package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.exception.InvalidReturnTypeException;
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
import static com.relogiclabs.jschema.internal.util.CommonHelper.getDerived;
import static com.relogiclabs.jschema.internal.util.CommonHelper.nonNullFrom;
import static com.relogiclabs.jschema.message.ErrorCode.FNCNVK01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCNVK02;
import static com.relogiclabs.jschema.message.ErrorCode.FNCNVK03;
import static java.util.stream.Collectors.joining;

@Getter
public final class NativeFunction implements EFunction {
    private final Method method;
    private final Parameter[] parameters;
    private final FunctionProvider instance;
    private final int arity;
    private final Class<?> targetType;

    public NativeFunction(Method method, Parameter[] parameters, FunctionProvider instance) {
        this.method = method;
        this.parameters = parameters;
        this.instance = instance;
        this.arity = parameters[parameters.length - 1].isVarArgs()
            ? VARIADIC_ARITY : parameters.length;
        this.targetType = parameters[0].getType();
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public List<Object> prebind(List<? extends EValue> arguments) {
        var length = parameters.length;
        if(arity == VARIADIC_ARITY && arguments.size() < length - 2) return null;
        var result = new ArrayList<>(length);
        for(int i = 1; i < length; i++) {
            if(parameters[i].isVarArgs()) {
                var args = processVarArgs(parameters[i], subList(arguments, i - 1));
                if(args == null) return null;
                result.add(args);
                break;
            }
            if(!isMatch(parameters[i], arguments.get(i - 1))) return null;
            result.add(arguments.get(i - 1));
        }
        return result;
    }

    @Override
    public Object invoke(JFunction caller, Object[] arguments) {
        try {
            instance.setRuntime(caller.getRuntime());
            instance.setCaller(caller);
            var result = method.invoke(instance, arguments);
            if(result == null) throw new InvalidReturnTypeException(FNCNVK01,
                "Function " + method.getName() + " must not return null");
            return result;
        } catch (InvocationTargetException e) {
            throw new TargetInvocationException(FNCNVK02,
                "Target invocation exception occurred", nonNullFrom(e.getCause(), e));
        } catch (IllegalAccessException e) {
            throw new TargetInvocationException(FNCNVK03, "Illegal access exception occurred", e);
        }
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

    private static boolean isMatch(Parameter parameter, Object argument) {
        return parameter.getType().isInstance(getDerived(argument));
    }

    public static String getSignature(Method method) {
        String typeName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String parameters = Arrays.stream(method.getParameters())
            .map(NativeFunction::stringOf).collect(joining(", "));
        String returnType = method.getReturnType().getName();
        return returnType + " " + typeName + "." + methodName + "(" + parameters + ")";
    }

    private static String stringOf(Parameter parameter) {
        return parameter.getType().getSimpleName() + " " + parameter.getName();
    }
}