package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.ScriptArgumentException;
import com.relogiclabs.jschema.exception.ScriptCommonException;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.stringify;
import static com.relogiclabs.jschema.internal.tree.EFunction.VARIADIC_ARITY;
import static com.relogiclabs.jschema.message.ErrorCode.MNVK01;

public class CommonLibrary {
    private static final String Type_M0 = "type#0";
    private static final String String_M0 = "string#0";
    private final Map<String, MethodEvaluator> methods;

    @Getter
    private static final CommonLibrary instance = new CommonLibrary();

    CommonLibrary() {
        this.methods = new HashMap<>(20);
        addMethod(Type_M0, CommonLibrary::typeMethod);
        addMethod(String_M0, CommonLibrary::stringMethod);
    }

    public MethodEvaluator getMethod(String name, int argCount) {
        var method = methods.get(name + "#" + argCount);
        if(method != null) return method;
        method = methods.get(name + "#" + VARIADIC_ARITY);
        if(method != null) return method;
        throw failOnMethodNotFound(name, argCount, getType());
    }

    protected void addMethod(String name, MethodEvaluator evaluator) {
        methods.put(name, evaluator);
    }

    protected EType getType() {
        return EType.ANY;
    }

    private static GString typeMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GString.from(self.getType().getName());
    }

    private static GString stringMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GString.from(stringify(self));
    }

    private static ScriptCommonException failOnMethodNotFound(String name, int argCount, EType type) {
        return new ScriptCommonException(MNVK01, "Method '" + name + "' with " + argCount
            + " parameter(s) of " + type + " not found");
    }

    protected static ScriptArgumentException failOnInvalidArgumentType(String code, EValue argument,
                String method, String parameter, EValue self) {
        return new ScriptArgumentException(code, "Invalid " + argument.getType() + " argument for '"
            + parameter + "' parameter in '" + method + "' method of " + self.getType());
    }
}