package com.relogiclabs.jschema.extension;

import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.tree.RuntimeContext;

public interface ConstraintFunctions extends InvokableNative {
    void setRuntime(RuntimeContext runtime);
    JNode getInvoker();
    void setInvoker(JNode invoker);

    default void setContext(ConstraintFunctions context) {
        setInvoker(context.getInvoker());
        setRuntime(context.getRuntime());
        setMethod(context.getMethod());
    }

    default boolean fail(RuntimeException exception) {
        return getRuntime().getExceptions().fail(exception);
    }
}