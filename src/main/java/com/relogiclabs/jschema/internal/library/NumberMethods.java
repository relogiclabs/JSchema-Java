package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.InvalidArgumentException;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GDouble;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.List;

import static com.relogiclabs.jschema.internal.library.LibraryHelper.asDouble;
import static com.relogiclabs.jschema.message.ErrorCode.POWNUM01;

public final class NumberMethods extends CommonMethods {
    private static final String Pow_M1 = "pow#1";
    private static final String Log_M0 = "log#0";
    private static final String Ceil_M0 = "ceil#0";
    private static final String Floor_M0 = "floor#0";
    private static final String Copy_M0 = "copy#0";

    private static final String Value_Id = "value";

    @Getter
    private static final NumberMethods instance = new NumberMethods();

    private NumberMethods() {
        addMethod(Pow_M1, NumberMethods::powMethod);
        addMethod(Log_M0, NumberMethods::logMethod);
        addMethod(Ceil_M0, NumberMethods::ceilMethod);
        addMethod(Floor_M0, NumberMethods::floorMethod);
        addMethod(Copy_M0, NumberMethods::copyMethod);
    }

    private static GDouble powMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        double value;
        try {
            value = asDouble(arguments.get(0));
        } catch(InvalidArgumentException e) {
            e.setContext(POWNUM01, Value_Id);
            throw e.failWithMethodException(self);
        }
        return GDouble.from(Math.pow(((ENumber) self).toDouble(), value));
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