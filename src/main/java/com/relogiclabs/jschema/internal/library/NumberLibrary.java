package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GDouble;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.List;

import static com.relogiclabs.jschema.message.ErrorCode.POWR01;

public final class NumberLibrary extends CommonLibrary {
    private static final String Pow_Bn = "pow";
    private static final String Pow_M1 = "pow#1";
    private static final String Log_M0 = "log#0";
    private static final String Ceil_M0 = "ceil#0";
    private static final String Floor_M0 = "floor#0";
    private static final String Copy_M0 = "copy#0";

    private static final String Value_Id = "value";

    @Getter
    private static final NumberLibrary instance = new NumberLibrary();

    private NumberLibrary() {
        addMethod(Pow_M1, NumberLibrary::powMethod);
        addMethod(Log_M0, NumberLibrary::logMethod);
        addMethod(Ceil_M0, NumberLibrary::ceilMethod);
        addMethod(Floor_M0, NumberLibrary::floorMethod);
        addMethod(Copy_M0, NumberLibrary::copyMethod);
    }

    @Override
    protected EType getType() {
        return EType.NUMBER;
    }

    private static GDouble powMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        if(!(arguments.get(0) instanceof ENumber a0)) throw failOnInvalidArgumentType(POWR01,
            arguments.get(0), Pow_Bn, Value_Id, self);
        return GDouble.from(Math.pow(((ENumber) self).toDouble(), a0.toDouble()));
    }

    private static GDouble logMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GDouble.from(Math.log(((ENumber) self).toDouble()));
    }

    private static GInteger ceilMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GInteger.from((long) Math.ceil(((ENumber) self).toDouble()));
    }

    private static GInteger floorMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GInteger.from((long) Math.floor(((ENumber) self).toDouble()));
    }

    private static EValue copyMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        if(self instanceof EInteger i) return GInteger.from(i.getValue());
        if(self instanceof ENumber n) return GDouble.from(n.toDouble());
        throw new IllegalStateException("Invalid runtime state");
    }
}