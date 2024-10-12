package com.relogiclabs.jschema.internal.loader;

import com.relogiclabs.jschema.extension.InvokableNative;
import com.relogiclabs.jschema.function.SchemaFunctions;
import com.relogiclabs.jschema.internal.tree.ConstraintRegistry;
import com.relogiclabs.jschema.internal.tree.ScriptRegistry;
import com.relogiclabs.jschema.library.ArrayMethods;
import com.relogiclabs.jschema.library.CommonMethods;
import com.relogiclabs.jschema.library.GlobalFunctions;
import com.relogiclabs.jschema.library.NumberMethods;
import com.relogiclabs.jschema.library.ObjectMethods;
import com.relogiclabs.jschema.library.StringMethods;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class InternalModuleLoader {
    private static final int INTERNAL_IMPORT_CAPACITY = 10;
    private final Map<String, InvokableNative> imports = new HashMap<>(INTERNAL_IMPORT_CAPACITY);
    private final ImportLoader loader = new ImportLoader(ConstraintRegistry.getInternals(),
                                                         ScriptRegistry.getInternals());

    private void loadModule(Class<?> module) {
        var instance = loader.load(module);
        imports.put(module.getName(), instance);
    }

    private void loadBatch() {
        loadModule(SchemaFunctions.class);
        loadModule(GlobalFunctions.class);
        loadModule(CommonMethods.class);
        loadModule(NumberMethods.class);
        loadModule(StringMethods.class);
        loadModule(ArrayMethods.class);
        loadModule(ObjectMethods.class);
    }

    public static Map<String, InvokableNative> loadModules() {
        var loader = new InternalModuleLoader();
        loader.loadBatch();
        return loader.imports;
    }
}