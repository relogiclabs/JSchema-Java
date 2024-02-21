package com.relogiclabs.jschema.internal.util;

import com.relogiclabs.jschema.node.Derivable;

public final class MiscellaneousHelper {
    public static <T> T nonNullFrom(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static Object getDerived(Object target) {
        if(target instanceof Derivable derivable)
            return nonNullFrom(derivable.getDerived(), target);
        return target;
    }

    public static boolean hasFlag(int flagSet, int flag) {
        return (flagSet & flag) == flag;
    }
}