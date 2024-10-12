package com.relogiclabs.jschema.extension;

import com.relogiclabs.jschema.tree.RuntimeContext;

import java.lang.reflect.Method;

public interface InvokableNative {
    RuntimeContext getRuntime();
    Method getMethod();
    void setMethod(Method method);
}