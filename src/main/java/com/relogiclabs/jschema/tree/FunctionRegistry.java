package com.relogiclabs.jschema.tree;

import com.relogiclabs.jschema.exception.DuplicateImportException;
import com.relogiclabs.jschema.exception.FunctionNotFoundException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.exception.InvalidImportException;
import com.relogiclabs.jschema.exception.NoClassFoundException;
import com.relogiclabs.jschema.function.FunctionProvider;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.internal.function.CoreLibrary;
import com.relogiclabs.jschema.internal.function.FunctionId;
import com.relogiclabs.jschema.internal.function.FunctionLoader;
import com.relogiclabs.jschema.internal.function.FunctionMap;
import com.relogiclabs.jschema.internal.tree.EFunction;
import com.relogiclabs.jschema.internal.tree.ScriptFunction;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JImport;
import com.relogiclabs.jschema.node.JNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.relogiclabs.jschema.internal.message.MessageHelper.getTypeName;
import static com.relogiclabs.jschema.internal.util.CommonHelper.getDerived;
import static com.relogiclabs.jschema.message.ErrorCode.FNCDEF01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCDEF02;
import static com.relogiclabs.jschema.message.ErrorCode.FNTRGT01;
import static com.relogiclabs.jschema.message.ErrorCode.IMPCLS01;
import static com.relogiclabs.jschema.message.ErrorCode.IMPCLS02;
import static com.relogiclabs.jschema.message.ErrorCode.IMPDUP01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public final class FunctionRegistry {
    private final Set<String> imports;
    private final FunctionMap functions;
    private final RuntimeContext runtime;

    public FunctionRegistry(RuntimeContext runtime) {
        this.runtime = runtime;
        this.imports = new HashSet<>();
        this.functions = new FunctionMap();
    }

    public JImport addClass(JImport importNode) {
        addClass(importNode.getClassName(), importNode.getContext());
        return importNode;
    }

    @SuppressWarnings("unchecked")
    public void addClass(String className, Context context) {
        if(!imports.add(className)) throw new DuplicateImportException(formatForSchema(IMPDUP01,
            "Class already imported " + className, context));
        Class<?> providerImpl;
        try {
            providerImpl = Class.forName(className);
        } catch(ClassNotFoundException ex) {
            throw new NoClassFoundException(formatForSchema(IMPCLS01, "Not found class "
                + className, context));
        }
        var providerBase = FunctionProvider.class;
        if(!providerBase.isAssignableFrom(providerImpl))
            throw new InvalidImportException(formatForSchema(IMPCLS02, providerImpl.getName()
                + " needs to inherit " + providerBase.getName(), context));
        functions.mergeWith(FunctionLoader.load((Class<FunctionProvider>) providerImpl, context));
    }

    public void addFunction(ScriptFunction function) {
        functions.add(function);
    }

    private boolean processResult(Object result) {
        return result instanceof FutureFunction future
            ? runtime.addFuture(future)
            : (boolean) result;
    }

    public boolean invokeFunction(JFunction caller, JNode target) {
        for(var e : caller.getCache()) if(e.isTargetMatch(target))
            return processResult(e.invoke(caller, target));

        Class<?> mismatchTarget = null;
        for(var f : getFunctions(caller)) {
            var schemaArgs = f.prebind(caller.getArguments());
            if(schemaArgs == null) continue;
            var targetType = f.getTargetType();
            if(isMatch(targetType, target)) {
                Object[] allArgs = addTarget(schemaArgs, target).toArray();
                var result = f.invoke(caller, allArgs);
                caller.getCache().add(f, allArgs);
                return processResult(result);
            }
            mismatchTarget = targetType;
        }
        if(mismatchTarget != null)
            return fail(new FunctionValidationException(new ErrorDetail(FNTRGT01,
                "Function " + caller.getOutline() + " is incompatible with target data type"),
                new ExpectedDetail(caller, "a supported data type such as "
                    + getTypeName(mismatchTarget)),
                new ActualDetail(target, "found unsupported target "
                    + getTypeName(target.getClass()) + " of " + target.getOutline())));
        return fail(new FunctionNotFoundException(formatForSchema(FNCDEF02,
            "Not found function " + caller.getOutline(), caller)));
    }

    private List<EFunction> getFunctions(JFunction caller) {
        var key1 = FunctionId.generate(caller, false);
        var key2 = FunctionId.generate(caller, true);

        var list = functions.get(key1);
        if(list != null) return list;
        list = CoreLibrary.getFunctions(key1);
        if(list != null) return list;
        list = functions.get(key2);
        if(list != null) return list;
        list = CoreLibrary.getFunctions(key2);
        if(list != null) return list;
        throw new FunctionNotFoundException(formatForSchema(FNCDEF01,
            "Not found function " + caller.getOutline(), caller));
    }

    private static List<Object> addTarget(List<Object> arguments, JNode target) {
        arguments.add(0, getDerived(target));
        return arguments;
    }

    private static boolean isMatch(Class<?> type, JNode value) {
        return type.isInstance(getDerived(value));
    }

    private boolean fail(RuntimeException exception) {
        return runtime.getExceptions().fail(exception);
    }
}