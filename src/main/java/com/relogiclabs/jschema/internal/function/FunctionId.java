package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.internal.script.GParameter;
import com.relogiclabs.jschema.internal.tree.EFunction;
import com.relogiclabs.jschema.node.JFunction;

import static com.relogiclabs.jschema.internal.script.RFunction.hasVariadic;
import static com.relogiclabs.jschema.internal.tree.EFunction.CONSTRAINT_PREFIX;
import static com.relogiclabs.jschema.internal.tree.EFunction.VARIADIC_ARITY;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

public final class FunctionId {
    private static final char ESCAPED_PREFIX = '_';

    private FunctionId() {
        throw new UnsupportedOperationException("This class is not intended for instantiation");
    }

    public static String generate(JFunction caller, boolean variadic) {
        return caller.getName() + "#" + (variadic ? VARIADIC_ARITY
            : caller.getArguments().size() + 1);
    }

    public static String generate(EFunction function) {
        return CONSTRAINT_PREFIX + uncapitalize(removeStart(function.getName(), ESCAPED_PREFIX))
            + "#" + function.getArity();
    }

    public static String generate(String baseName, GParameter[] parameters, boolean constraint) {
        var arity = hasVariadic(parameters) ? VARIADIC_ARITY : parameters.length;
        return constraint ? CONSTRAINT_PREFIX + removeStart(baseName, ESCAPED_PREFIX) + "#" + arity
            : baseName + "#" + arity;
    }
}