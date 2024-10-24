package com.relogiclabs.jschema.internal.loader;

import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

public interface SchemaFunction {
    String CONSTRAINT_PREFIX = "@";
    int VARIADIC_ARITY = -1;

    String[] getKeys();
    String getName();
    int getArity();
    Class<?> getTargetType();
    List<Object> prebind(List<? extends EValue> arguments);
    Object invoke(JFunction invoker, Object[] arguments);
}