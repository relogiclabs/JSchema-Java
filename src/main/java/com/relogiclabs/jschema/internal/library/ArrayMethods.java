package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.InvalidArgumentException;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GArray;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.List;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areEqual;
import static com.relogiclabs.jschema.internal.library.LibraryHelper.asInteger;
import static com.relogiclabs.jschema.message.ErrorCode.FILARR01;
import static com.relogiclabs.jschema.message.ErrorCode.FNDARR01;
import static com.relogiclabs.jschema.type.EUndefined.UNDEFINED;

public final class ArrayMethods extends CommonMethods {
    private static final String Length_M0 = "length#0";
    private static final String Find_M1 = "find#1";
    private static final String Find_M2 = "find#2";
    private static final String Fill_M2 = "fill#2";
    private static final String Copy_M0 = "copy#0";

    private static final String Start_Id = "start";
    private static final String Length_Id = "length";

    @Getter
    private static final ArrayMethods instance = new ArrayMethods();

    private ArrayMethods() {
        addMethod(Length_M0, ArrayMethods::lengthMethod);
        addMethod(Find_M1, ArrayMethods::findMethod1);
        addMethod(Find_M2, ArrayMethods::findMethod2);
        addMethod(Fill_M2, ArrayMethods::fillMethod);
        addMethod(Copy_M0, ArrayMethods::copyMethod);
    }

    private static GInteger lengthMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GInteger.from(((EArray) self).size());
    }

    private static EValue findMethod1(EValue self, List<EValue> arguments, ScriptScope scope) {
        var runtime = scope.getRuntime();
        var array = (EArray) self;
        var value = arguments.get(0);
        for(var i = 0; i < array.size(); i++)
            if(areEqual(array.get(i), value, runtime)) return GInteger.from(i);
        return UNDEFINED;
    }

    private static EValue findMethod2(EValue self, List<EValue> arguments, ScriptScope scope) {
        var runtime = scope.getRuntime();
        var array = (EArray) self;
        var value = arguments.get(0);
        int start;
        try {
            start = asInteger(arguments.get(1));
        } catch(InvalidArgumentException e) {
            e.setContext(FNDARR01, Start_Id);
            throw e.failWithMethodException(self);
        }
        for(var i = start; i < array.size(); i++)
            if(areEqual(array.get(i), value, runtime)) return GInteger.from(i);
        return UNDEFINED;
    }

    private static EValue fillMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        int length;
        try {
            length = asInteger(arguments.get(1));
        } catch(InvalidArgumentException e) {
            e.setContext(FILARR01, Length_Id);
            throw e.failWithMethodException(self);
        }
        return GArray.filledFrom(arguments.get(0), length);
    }

    private static EValue copyMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return new GArray(((EArray) self).elements());
    }
}