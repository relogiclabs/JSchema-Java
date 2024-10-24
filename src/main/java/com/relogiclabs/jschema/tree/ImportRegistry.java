package com.relogiclabs.jschema.tree;

import com.relogiclabs.jschema.exception.DuplicateImportException;
import com.relogiclabs.jschema.exception.MultilevelRuntimeException;
import com.relogiclabs.jschema.exception.NoClassFoundException;
import com.relogiclabs.jschema.extension.InvokableNative;
import com.relogiclabs.jschema.internal.loader.ImportLoader;
import com.relogiclabs.jschema.internal.tree.ConstraintRegistry;
import com.relogiclabs.jschema.internal.tree.ScriptRegistry;
import com.relogiclabs.jschema.node.JImport;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.jschema.internal.loader.InternalModuleLoader.loadModules;
import static com.relogiclabs.jschema.message.ErrorCode.IMPCLS01;
import static com.relogiclabs.jschema.message.ErrorCode.IMPDUP01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

@Getter
public class ImportRegistry {
    private static final int EXTERNAL_IMPORT_CAPACITY = 10;
    private static final Map<String, InvokableNative> INTERNALS = loadModules();

    private final Map<String, InvokableNative> externals = new HashMap<>(EXTERNAL_IMPORT_CAPACITY);
    private final RuntimeContext runtime;
    private final ConstraintRegistry constraints;
    private final ScriptRegistry scripts;
    private final ImportLoader loader;

    public ImportRegistry(RuntimeContext runtime) {
        this.runtime = runtime;
        this.constraints = new ConstraintRegistry(runtime);
        this.scripts = new ScriptRegistry(runtime);
        this.loader = new ImportLoader(constraints.getExternals(),
                                       scripts.getExternals());
    }

    public JImport addImport(JImport importNode) {
        var className = importNode.getClassName();
        var context = importNode.getContext();
        if(externals.containsKey(className) || INTERNALS.containsKey(className))
            throw new DuplicateImportException(formatForSchema(IMPDUP01,
                "Class already imported " + className, context));
        try {
            var module = Class.forName(className);
            var instance = loader.load(module);
            externals.put(className, instance);
        } catch(ClassNotFoundException e) {
            throw new NoClassFoundException(formatForSchema(IMPCLS01, "Not found class "
                + className, context), e);
        } catch(MultilevelRuntimeException e) {
            throw e.translate(context.getToken());
        }
        return importNode;
    }

    public <T extends InvokableNative> T getInstance(Class<T> module) {
        var instance = externals.get(module.getName());
        if(instance == null) instance = INTERNALS.get(module.getName());
        return module.cast(instance);
    }
}