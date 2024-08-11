package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.exception.ClassInstantiationException;
import com.relogiclabs.jschema.exception.InvalidFunctionException;
import com.relogiclabs.jschema.function.FunctionProvider;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.internal.tree.NativeFunction;
import com.relogiclabs.jschema.tree.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static com.relogiclabs.jschema.internal.tree.NativeFunction.getSignature;
import static com.relogiclabs.jschema.message.ErrorCode.FNCSIG01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCSIG02;
import static com.relogiclabs.jschema.message.ErrorCode.INSTCR01;
import static com.relogiclabs.jschema.message.ErrorCode.INSTCR02;
import static com.relogiclabs.jschema.message.ErrorCode.INSTCR03;
import static com.relogiclabs.jschema.message.ErrorCode.INSTCR04;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public final class FunctionLoader {
    private FunctionLoader() {
        throw new UnsupportedOperationException("This class is not intended for instantiation");
    }

    public static FunctionMap load(Class<? extends FunctionProvider> providerImpl,
                Context context) {
        var instance = createInstance(providerImpl, context);
        var providerBase = FunctionProvider.class;
        var functions = new FunctionMap();
        for(var m : providerImpl.getMethods()) {
            // Methods in ancestor class or in base/super class
            if(!providerBase.isAssignableFrom(m.getDeclaringClass())
                || providerBase == m.getDeclaringClass()) continue;
            Parameter[] parameters = m.getParameters();
            if(!isValidReturnType(m.getReturnType())) throw failOnReturnType(m, context);
            if(parameters.length == 0 || parameters[0].isVarArgs())
                throw failOnTargetParameter(m, context);
            functions.add(new NativeFunction(m, parameters, instance));
        }
        return functions;
    }

    private static FunctionProvider createInstance(Class<? extends FunctionProvider> type,
                Context context) {
        try {
            var constructor = type.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw failOnCreateInstance(INSTCR01, e, type, context);
        } catch (InstantiationException e) {
            throw failOnCreateInstance(INSTCR02, e, type, context);
        } catch (InvocationTargetException e) {
            throw failOnCreateInstance(INSTCR03, e, type, context);
        } catch (IllegalAccessException e) {
            throw failOnCreateInstance(INSTCR04, e, type, context);
        }
    }

    private static boolean isValidReturnType(Class<?> type) {
        if(type == boolean.class) return true;
        if(type == Boolean.class) return true;
        if(type == FutureFunction.class) return true;
        return false;
    }

    private static RuntimeException failOnCreateInstance(String code, Exception ex,
                Class<?> type, Context context) {
        return new ClassInstantiationException(formatForSchema(code,
            "Fail to create an instance of class " + type.getName(), context), ex);
    }

    private static InvalidFunctionException failOnReturnType(Method method, Context context) {
        return new InvalidFunctionException(formatForSchema(FNCSIG01,
            "Function [" + getSignature(method) + "] requires valid return type", context));
    }

    private static InvalidFunctionException failOnTargetParameter(Method method, Context context) {
        return new InvalidFunctionException(formatForSchema(FNCSIG02,
            "Function [" + getSignature(method) + "] requires valid target parameter", context));
    }
}