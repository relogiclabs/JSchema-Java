package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.extension.ScriptMethod;
import com.relogiclabs.jschema.internal.script.GBoolean;
import com.relogiclabs.jschema.internal.script.GDouble;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.type.EBoolean;
import com.relogiclabs.jschema.type.EDouble;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;

import static com.relogiclabs.jschema.type.EType.STRING;
import static com.relogiclabs.jschema.type.EUndefined.UNDEFINED;

public class StringMethods1 extends AbstractMethods {
    @ScriptMethod(STRING)
    public EInteger length() {
        return GInteger.from(getSelf(EString.class).length());
    }

    @ScriptMethod(names = {"find", "indexOf"}, types = STRING)
    public EValue find(EString substring) {
        var index = getSelf(EString.class).getValue().indexOf(substring.getValue());
        return index == -1 ? UNDEFINED : GInteger.from(index);
    }

    @ScriptMethod(names = {"find", "indexOf"}, types = STRING)
    public EValue find(EString substring, EInteger start) {
        var index = getSelf(EString.class).getValue()
            .indexOf(substring.getValue(), (int) start.getValue());
        return index == -1 ? UNDEFINED : GInteger.from(index);
    }

    @ScriptMethod(names = {"number", "parseNumber"}, types = STRING)
    public ENumber number() {
        var value = Double.parseDouble(getSelf(EString.class).getValue());
        if(value % 1 == 0) return GInteger.from((long) value);
        else return GDouble.from(value);
    }

    @ScriptMethod(STRING)
    public EInteger parseInt() {
        return GInteger.from(Long.parseLong(getSelf(EString.class).getValue()));
    }

    @ScriptMethod(STRING)
    public EDouble parseFloat() {
        return GDouble.from(Double.parseDouble(getSelf(EString.class).getValue()));
    }

    @ScriptMethod(STRING)
    public EBoolean startsWith(EString prefix) {
        return GBoolean.from(getSelf(EString.class).getValue().startsWith(prefix.getValue()));
    }

    @ScriptMethod(STRING)
    public EBoolean endsWith(EString suffix) {
        return GBoolean.from(getSelf(EString.class).getValue().endsWith(suffix.getValue()));
    }

    @ScriptMethod(names = {"upper", "toUpperCase"}, types = STRING)
    public EString upper() {
        return GString.from(getSelf(EString.class).getValue().toUpperCase());
    }

    @ScriptMethod(names = {"lower", "toLowerCase"}, types = STRING)
    public EString lower() {
        return GString.from(getSelf(EString.class).getValue().toLowerCase());
    }

    @ScriptMethod(STRING)
    public EString trim() {
        return GString.from(getSelf(EString.class).getValue().trim());
    }

    @ScriptMethod(STRING)
    public EString copy() {
        return GString.from(getSelf(EString.class).getValue());
    }
}