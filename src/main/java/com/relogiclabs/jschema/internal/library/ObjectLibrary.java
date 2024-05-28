package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GObject;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.List;

public final class ObjectLibrary extends CommonLibrary {
    private static final String Size_M0 = "size#0";
    private static final String Length_M0 = "length#0";
    private static final String Copy_M0 = "copy#0";

    @Getter
    private static final ObjectLibrary instance = new ObjectLibrary();

    private ObjectLibrary() {
        addMethod(Size_M0, ObjectLibrary::sizeMethod);
        addMethod(Length_M0, ObjectLibrary::sizeMethod);
        addMethod(Copy_M0, ObjectLibrary::copyMethod);
    }

    @Override
    protected EType getType() {
        return EType.OBJECT;
    }

    private static GInteger sizeMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GInteger.from(((EObject) self).size());
    }

    private static GObject copyMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return new GObject((EObject) self);
    }
}