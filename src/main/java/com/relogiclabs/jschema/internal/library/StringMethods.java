package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.InvalidArgumentException;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.List;

import static com.relogiclabs.jschema.internal.library.LibraryHelper.asInteger;
import static com.relogiclabs.jschema.internal.library.LibraryHelper.asString;
import static com.relogiclabs.jschema.message.ErrorCode.FNDSTR01;
import static com.relogiclabs.jschema.message.ErrorCode.FNDSTR02;
import static com.relogiclabs.jschema.message.ErrorCode.FNDSTR03;
import static com.relogiclabs.jschema.type.EUndefined.UNDEFINED;

public final class StringMethods extends CommonMethods {
    private static final String Length_M0 = "length#0";
    private static final String Find_M1 = "find#1";
    private static final String Find_M2 = "find#2";
    private static final String Copy_M0 = "copy#0";

    private static final String Value_Id = "value";
    private static final String Start_Id = "start";

    @Getter
    private static final StringMethods instance = new StringMethods();

    private StringMethods() {
        addMethod(Length_M0, StringMethods::lengthMethod);
        addMethod(Find_M1, StringMethods::findMethod1);
        addMethod(Find_M2, StringMethods::findMethod2);
        addMethod(Copy_M0, StringMethods::copyMethod);
    }

    private static GInteger lengthMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GInteger.from(((EString) self).length());
    }

    private static EValue findMethod1(EValue self, List<EValue> arguments, ScriptScope scope) {
        String value;
        try {
            value = asString(arguments.get(0));
        } catch(InvalidArgumentException e) {
            e.setContext(FNDSTR01, Value_Id);
            throw e.failWithMethodException(self);
        }
        var index = ((EString) self).getValue().indexOf(value);
        return index == -1 ? UNDEFINED : GInteger.from(index);
    }

    private static EValue findMethod2(EValue self, List<EValue> arguments, ScriptScope scope) {
        String value;
        try {
            value = asString(arguments.get(0));
        } catch(InvalidArgumentException e) {
            e.setContext(FNDSTR02, Value_Id);
            throw e.failWithMethodException(self);
        }
        int start;
        try {
            start = asInteger(arguments.get(1));
        } catch(InvalidArgumentException e) {
            e.setContext(FNDSTR03, Start_Id);
            throw e.failWithMethodException(self);
        }
        var index = ((EString) self).getValue().indexOf(value, start);
        return index == -1 ? UNDEFINED : GInteger.from(index);
    }

    private static EValue copyMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GString.from(((EString) self).getValue());
    }
}