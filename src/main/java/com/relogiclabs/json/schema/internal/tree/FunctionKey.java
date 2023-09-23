package com.relogiclabs.json.schema.internal.tree;

import com.relogiclabs.json.schema.types.JFunction;

import java.lang.reflect.Method;

import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

public record FunctionKey(String functionName, int parameterCount) {
    private static final String ESCAPED_PREFIX = "_";

    public FunctionKey(Method method, int parameterCount) {
        this(toFunctionName(method), parameterCount);
    }

    public FunctionKey(JFunction function) {
        this(function.getName(), function.getArguments().size() + 1);
    }

    private static String toFunctionName(Method method) {
        return "@" + uncapitalize(removeStart(method.getName(), ESCAPED_PREFIX));
    }
}