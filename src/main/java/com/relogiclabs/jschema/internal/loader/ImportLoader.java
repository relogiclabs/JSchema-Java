package com.relogiclabs.jschema.internal.loader;

import com.relogiclabs.jschema.exception.ClassInstantiationException;
import com.relogiclabs.jschema.exception.InvalidImportException;
import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.extension.ConstraintFunctions;
import com.relogiclabs.jschema.extension.InvokableNative;
import com.relogiclabs.jschema.extension.ScriptFunction;
import com.relogiclabs.jschema.extension.ScriptFunctions;
import com.relogiclabs.jschema.extension.ScriptMethod;
import com.relogiclabs.jschema.extension.ScriptMethods;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;

import static com.relogiclabs.jschema.message.ErrorCode.IMPLOD01;
import static com.relogiclabs.jschema.message.ErrorCode.IMPLOD02;
import static com.relogiclabs.jschema.message.ErrorCode.IMPLOD03;
import static com.relogiclabs.jschema.message.ErrorCode.IMPLOD04;
import static com.relogiclabs.jschema.message.ErrorCode.INSTCR01;
import static com.relogiclabs.jschema.message.ErrorCode.INSTCR02;
import static com.relogiclabs.jschema.message.ErrorCode.INSTCR03;
import static com.relogiclabs.jschema.message.ErrorCode.INSTCR04;

@Getter
public class ImportLoader {
    private final ConstraintStorage constraints;
    private final ScriptStorage scripts;

    public ImportLoader(ConstraintStorage constraints, ScriptStorage scripts) {
        this.constraints = constraints;
        this.scripts = scripts;
    }

    public InvokableNative load(Class<?> module) {
        var instance = createInstance(module);
        for(var method : module.getMethods()) {
            var constraintFunction = method.getAnnotation(ConstraintFunction.class);
            if(constraintFunction != null) {
                if(!(instance instanceof ConstraintFunctions ins))
                    throw failOnNotImplement(IMPLOD02, ConstraintFunctions.class, module);
                constraints.addFunction(new NativeSchemaFunction(method, constraintFunction, ins));
            }
            var scriptFunction = method.getAnnotation(ScriptFunction.class);
            if(scriptFunction != null) {
                if(!(instance instanceof ScriptFunctions ins))
                    throw failOnNotImplement(IMPLOD03, ScriptFunctions.class, module);
                scripts.addFunction(new ScriptFunctionWrapper(method, scriptFunction, ins));
            }
            var scriptMethod = method.getAnnotation(ScriptMethod.class);
            if(scriptMethod != null) {
                if(!(instance instanceof ScriptMethods ins))
                    throw failOnNotImplement(IMPLOD04, ScriptMethods.class, module);
                scripts.addMethod(new ScriptMethodWrapper(method, scriptMethod, ins));
            }
        }
        return instance;
    }

    private static InvokableNative createInstance(Class<?> module) {
        Object instance;
        try {
            instance = module.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw failOnCreateInstance(INSTCR01, e, module);
        } catch (InstantiationException e) {
            throw failOnCreateInstance(INSTCR02, e, module);
        } catch (InvocationTargetException e) {
            throw failOnCreateInstance(INSTCR03, e, module);
        } catch (IllegalAccessException e) {
            throw failOnCreateInstance(INSTCR04, e, module);
        }
        if(!(instance instanceof InvokableNative result))
            throw failOnNotImplement(IMPLOD01, InvokableNative.class, module);
        return result;
    }

    private static RuntimeException failOnNotImplement(String code, Class<?> type, Class<?> module) {
        return new InvalidImportException(code, module + " not implements " + type);
    }

    private static RuntimeException failOnCreateInstance(String code, Exception ex, Class<?> module) {
        return new ClassInstantiationException(code, "Fail to create an instance of " + module, ex);
    }
}