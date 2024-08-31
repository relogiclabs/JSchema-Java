package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GObject;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.List;

public final class ObjectMethods extends CommonMethods {
    private static final String Size_M0 = "size#0";
    private static final String Length_M0 = "length#0";
    private static final String Copy_M0 = "copy#0";

    @Getter
    private static final ObjectMethods instance = new ObjectMethods();

    private ObjectMethods() {
        addMethod(Size_M0, ObjectMethods::sizeMethod);
        addMethod(Length_M0, ObjectMethods::sizeMethod);
        addMethod(Copy_M0, ObjectMethods::copyMethod);
    }

    private static GInteger sizeMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GInteger.from(((EObject) self).size());
    }

    private static GObject copyMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return new GObject((EObject) self);
    }
}