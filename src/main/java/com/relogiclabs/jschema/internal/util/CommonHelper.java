package com.relogiclabs.jschema.internal.util;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public final class CommonHelper {
    private CommonHelper() {
        throw new UnsupportedOperationException("This class is not intended for instantiation");
    }

    public static <T> T nonNullFrom(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static boolean hasFlag(int flagSet, int flag) {
        return (flagSet & flag) == flag;
    }

    public static TerminalNode getToken(ParserRuleContext context, int type) {
        return context.getToken(type, 0);
    }

    public static boolean hasToken(ParserRuleContext context, int type) {
        return context.getToken(type, 0) != null;
    }
}