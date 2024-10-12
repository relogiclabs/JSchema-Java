package com.relogiclabs.jschema.extension;

import com.relogiclabs.jschema.tree.RuntimeContext;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter @Setter
public abstract class AbstractExtension implements InvokableNative {
    private RuntimeContext runtime;
    private Method method;

    public String getOutline(Object object) {
        return getRuntime().getOutlineFormatter().getOutline(object);
    }
}