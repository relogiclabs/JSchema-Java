package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GArray;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.List;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areEqual;
import static com.relogiclabs.jschema.message.ErrorCode.AFND01;
import static com.relogiclabs.jschema.message.ErrorCode.FILL01;
import static com.relogiclabs.jschema.type.EUndefined.UNDEFINED;

public final class ArrayLibrary extends CommonLibrary {
    private static final String Length_M0 = "length#0";
    private static final String Find_Bn = "find";
    private static final String Find_M1 = "find#1";
    private static final String Find_M2 = "find#2";
    private static final String Fill_Bn = "fill";
    private static final String Fill_M2 = "fill#2";
    private static final String Copy_M0 = "copy#0";

    private static final String Start_Id = "start";
    private static final String Length_Id = "length";

    @Getter
    private static final ArrayLibrary instance = new ArrayLibrary();

    private ArrayLibrary() {
        addMethod(Length_M0, ArrayLibrary::lengthMethod);
        addMethod(Find_M1, ArrayLibrary::findMethod1);
        addMethod(Find_M2, ArrayLibrary::findMethod2);
        addMethod(Fill_M2, ArrayLibrary::fillMethod);
        addMethod(Copy_M0, ArrayLibrary::copyMethod);
    }

    @Override
    protected EType getType() {
        return EType.ARRAY;
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
        if(!(arguments.get(1) instanceof EInteger a1)) throw failOnInvalidArgumentType(AFND01,
            arguments.get(1), Find_Bn, Start_Id, self);
        var start = (int) a1.getValue();
        for(var i = start; i < array.size(); i++)
            if(areEqual(array.get(i), value, runtime)) return GInteger.from(i);
        return UNDEFINED;
    }

    private static EValue fillMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        if(!(arguments.get(1) instanceof EInteger a1)) throw failOnInvalidArgumentType(FILL01,
            arguments.get(1), Fill_Bn, Length_Id, self);
        var length = (int) a1.getValue();
        return GArray.filledFrom(arguments.get(0), length);
    }

    private static EValue copyMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return new GArray(((EArray) self).elements());
    }
}