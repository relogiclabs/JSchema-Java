package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.extension.ScriptMethod;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.type.EString;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.stringify;
import static com.relogiclabs.jschema.type.EType.ANY;

public class CommonMethods1 extends AbstractMethods {
    @ScriptMethod(ANY)
    public EString type() {
        return GString.from(getSelf().getType().getName());
    }

    @ScriptMethod(names = {"string", "toString"}, types = ANY)
    public EString string() {
        return GString.from(stringify(getSelf()));
    }
}