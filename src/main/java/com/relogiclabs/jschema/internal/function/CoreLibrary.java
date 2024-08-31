package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.function.CoreFunctions5;
import com.relogiclabs.jschema.internal.tree.EFunction;

import java.util.List;

public class CoreLibrary {
    private static final FunctionMap functions = FunctionLoader.load(CoreFunctions5.class, null);

    public static List<EFunction> getFunctions(String key) {
        return functions.get(key);
    }
}