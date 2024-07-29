package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.MethodNotFoundException;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.stringify;
import static com.relogiclabs.jschema.internal.tree.EFunction.VARIADIC_ARITY;
import static com.relogiclabs.jschema.message.ErrorCode.MTHNVK01;

public class CommonMethods {
    private static final String Type_M0 = "type#0";
    private static final String String_M0 = "string#0";
    private final Map<String, MethodEvaluator> methods;

    @Getter
    private static final CommonMethods instance = new CommonMethods();

    CommonMethods() {
        this.methods = new HashMap<>(20);
        addMethod(Type_M0, CommonMethods::typeMethod);
        addMethod(String_M0, CommonMethods::stringMethod);
    }

    public MethodEvaluator getMethod(String name, int argCount) {
        var method = methods.get(name + "#" + argCount);
        if(method != null) return method;
        method = methods.get(name + "#" + VARIADIC_ARITY);
        if(method != null) return method;
        throw failOnMethodNotFound(name, argCount);
    }

    protected void addMethod(String methodId, MethodEvaluator body) {
        methods.put(methodId, body);
    }

    private static GString typeMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GString.from(self.getType().getName());
    }

    private static GString stringMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GString.from(stringify(self));
    }

    private static MethodNotFoundException failOnMethodNotFound(String name, int argCount) {
        return new MethodNotFoundException(MTHNVK01, "Method '" + name + "' with " + argCount
            + " parameter(s) not found");
    }
}