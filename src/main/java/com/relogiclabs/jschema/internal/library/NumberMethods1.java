package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.extension.ScriptMethod;
import com.relogiclabs.jschema.internal.script.GDouble;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.type.EDouble;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENumber;

import static com.relogiclabs.jschema.type.EType.DOUBLE;
import static com.relogiclabs.jschema.type.EType.FLOAT;
import static com.relogiclabs.jschema.type.EType.INTEGER;
import static com.relogiclabs.jschema.type.EType.NUMBER;

public class NumberMethods1 extends AbstractMethods {
    @ScriptMethod(NUMBER)
    public EDouble pow(ENumber exponent) {
        return GDouble.from(Math.pow(getSelf(ENumber.class).toDouble(), exponent.toDouble()));
    }

    @ScriptMethod(NUMBER)
    public EDouble sqrt() {
        return GDouble.from(Math.sqrt(getSelf(ENumber.class).toDouble()));
    }

    @ScriptMethod(NUMBER)
    public EDouble log() {
        return GDouble.from(Math.log(getSelf(ENumber.class).toDouble()));
    }

    @ScriptMethod(NUMBER)
    public EInteger ceil() {
        return GInteger.from((long) Math.ceil(getSelf(ENumber.class).toDouble()));
    }

    @ScriptMethod(NUMBER)
    public EInteger floor() {
        return GInteger.from((long) Math.floor(getSelf(ENumber.class).toDouble()));
    }

    @ScriptMethod(names = "copy", types = INTEGER)
    public EInteger copy1() {
        return GInteger.from(getSelf(EInteger.class).getValue());
    }

    @ScriptMethod(names = "copy", types = {FLOAT, DOUBLE})
    public EDouble copy2() {
        return GDouble.from(getSelf(ENumber.class).toDouble());
    }
}