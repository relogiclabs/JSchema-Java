package com.relogiclabs.jschema.extension;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ScriptExtension extends AbstractExtension implements ScriptFunctions, ScriptMethods {
    private ScriptScope scope;
    private EValue self;

    @Override
    public void setScope(ScriptScope scope) {
        this.scope = scope;
        setRuntime(null);
    }

    @Override
    public RuntimeContext getRuntime() {
        var runtime = super.getRuntime();
        if(runtime != null) return runtime;
        setRuntime(runtime = scope.getRuntime());
        return runtime;
    }

    public <T extends EValue> T getSelf(Class<T> type) {
        return type.cast(self);
    }
}