package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.node.JFunction;

import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

public record FunctionKey(String functionName, int parameterCount) {
    private static final String ESCAPED_PREFIX = "_";

    public FunctionKey(EFunction function) {
        this(formatName(function.getName()), function.getArity());
    }

    public FunctionKey(JFunction function) {
        this(function.getName(), function.getArguments().size() + 1);
    }

    private static String formatName(String name) {
        return "@" + uncapitalize(removeStart(name, ESCAPED_PREFIX));
    }
}