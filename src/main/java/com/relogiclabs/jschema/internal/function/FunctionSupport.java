package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.extension.ConstraintFunctions;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.tree.RuntimeContext;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter @Setter
public abstract class FunctionSupport implements ConstraintFunctions {
    protected RuntimeContext runtime;
    protected Method method;
    protected JNode invoker;

    public <T extends JNode> T getInvoker(Class<T> type) {
        return type.cast(invoker);
    }

    public String getOutline(Object object) {
        return runtime.getOutlineFormatter().getOutline(object);
    }
}