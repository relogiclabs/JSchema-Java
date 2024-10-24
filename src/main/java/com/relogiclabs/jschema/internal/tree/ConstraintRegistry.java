package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.exception.FunctionNotFoundException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.internal.loader.ConstraintStorage;
import com.relogiclabs.jschema.internal.loader.SchemaFunction;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.tree.RuntimeContext;
import lombok.Getter;

import java.util.List;

import static com.relogiclabs.jschema.internal.loader.SchemaFunction.VARIADIC_ARITY;
import static com.relogiclabs.jschema.internal.message.MessageHelper.getTypeName;
import static com.relogiclabs.jschema.internal.util.ReflectionHelper.getDerived;
import static com.relogiclabs.jschema.internal.util.ReflectionHelper.isMatched;
import static com.relogiclabs.jschema.message.ErrorCode.FNCDEF01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCDEF02;
import static com.relogiclabs.jschema.message.ErrorCode.FNTRGT01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

@Getter
public class ConstraintRegistry {
    private static final int INTERNALS_CAPACITY = 100;
    private static final int EXTERNALS_CAPACITY = 50;

    private static final ConstraintStorage INTERNALS = new ConstraintStorage(INTERNALS_CAPACITY);
    private final ConstraintStorage externals = new ConstraintStorage(EXTERNALS_CAPACITY);
    private final RuntimeContext runtime;

    public ConstraintRegistry(RuntimeContext runtime) {
        this.runtime = runtime;
    }

    public static ConstraintStorage getInternals() {
        return INTERNALS;
    }

    public void addFunction(SchemaFunction function) {
        externals.addFunction(function);
    }

    private List<SchemaFunction> getFunctions(JFunction invoker) {
        var key1 = generateKey(invoker, false);
        var key2 = generateKey(invoker, true);
        List<SchemaFunction> list;
        if((list = externals.getFunction(key1)) != null) return list;
        if((list = externals.getFunction(key2)) != null) return list;
        if((list = INTERNALS.getFunction(key1)) != null) return list;
        if((list = INTERNALS.getFunction(key2)) != null) return list;
        throw new FunctionNotFoundException(formatForSchema(FNCDEF01,
            "Not found constraint function " + invoker.getOutline(), invoker));
    }

    private static String generateKey(JFunction invoker, boolean variadic) {
        return invoker.getName() + "#" + (variadic ? VARIADIC_ARITY
            : invoker.getArguments().size() + 1);
    }

    private boolean processResult(Object result) {
        return result instanceof FutureFunction future
            ? runtime.addFuture(future)
            : (boolean) result;
    }

    public boolean invoke(JFunction invoker, JNode target) {
        for(var e : invoker.getCache()) if(e.isTargetMatched(target))
            return processResult(e.invoke(invoker, target));

        Class<?> mismatchedTarget = null;
        for(var f : getFunctions(invoker)) {
            var schemaArgs = f.prebind(invoker.getArguments());
            if(schemaArgs == null) continue;
            var targetType = f.getTargetType();
            if(isMatched(targetType, target)) {
                Object[] allArgs = addTarget(schemaArgs, target).toArray();
                var result = f.invoke(invoker, allArgs);
                invoker.getCache().add(f, allArgs);
                return processResult(result);
            }
            mismatchedTarget = targetType;
        }
        if(mismatchedTarget != null) return fail(new FunctionValidationException(
            new ErrorDetail(FNTRGT01, "Constraint function " + invoker.getOutline()
                + " is incompatible with target data type"),
            new ExpectedDetail(invoker, "a supported data type such as "
                + getTypeName(mismatchedTarget)),
            new ActualDetail(target, "found unsupported target "
                + getTypeName(target.getClass()) + " of " + target.getOutline())));
        return fail(new FunctionNotFoundException(formatForSchema(FNCDEF02,
            "Constraint function parameter(s) mismatched with " + invoker.getOutline(), invoker)));
    }

    private static List<Object> addTarget(List<Object> arguments, JNode target) {
        arguments.add(0, getDerived(target));
        return arguments;
    }

    private boolean fail(RuntimeException exception) {
        return runtime.getExceptions().fail(exception);
    }
}