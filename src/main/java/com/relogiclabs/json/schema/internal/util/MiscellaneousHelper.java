package com.relogiclabs.json.schema.internal.util;

import com.relogiclabs.json.schema.type.Derivable;
import com.relogiclabs.json.schema.type.JNode;

public final class MiscellaneousHelper {
    public static <T> T nonNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static JNode getDerived(JNode target) {
        if(target instanceof Derivable derivable)
            return nonNull(derivable.getDerived(), target);
        return target;
    }
}