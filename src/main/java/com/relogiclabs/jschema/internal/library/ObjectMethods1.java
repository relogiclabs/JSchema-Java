package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.extension.ScriptMethod;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GObject;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.EObject;

import static com.relogiclabs.jschema.type.EType.OBJECT;

public class ObjectMethods1 extends AbstractMethods {
    @ScriptMethod(names = {"size", "length"}, types = OBJECT)
    public EInteger size() {
        return GInteger.from(getSelf(EObject.class).size());
    }

    @ScriptMethod(OBJECT)
    public EObject copy() {
        return new GObject(getSelf(EObject.class));
    }
}