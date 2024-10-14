package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.extension.ScriptMethods;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractMethods extends AbstractFunctions implements ScriptMethods {
    private EValue self;

    protected <T extends EValue> T getSelf(Class<T> type) {
        return type.cast(self);
    }
}