package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

public interface EFunction {
    String CONSTRAINT_PREFIX = "@";
    int VARIADIC_ARITY = -1;

    String getName();
    int getArity();
    Class<?> getTargetType();
    List<Object> prebind(List<? extends EValue> arguments);
    Object invoke(JFunction caller, Object[] arguments);
}