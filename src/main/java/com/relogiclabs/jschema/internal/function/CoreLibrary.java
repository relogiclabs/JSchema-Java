package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.function.CoreFunctions4;
import com.relogiclabs.jschema.internal.tree.EFunction;

import java.util.List;

public class CoreLibrary {
    private static final FunctionMap functions = FunctionLoader.load(CoreFunctions4.class, null);

    public static List<EFunction> getFunctions(String key) {
        return functions.get(key);
    }
}