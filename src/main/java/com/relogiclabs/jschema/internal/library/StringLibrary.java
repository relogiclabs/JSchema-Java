package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.List;

import static com.relogiclabs.jschema.message.ErrorCode.SFND01;
import static com.relogiclabs.jschema.message.ErrorCode.SFND02;
import static com.relogiclabs.jschema.message.ErrorCode.SFND03;
import static com.relogiclabs.jschema.type.EUndefined.UNDEFINED;

public final class StringLibrary extends CommonLibrary {
    private static final String Length_M0 = "length#0";
    private static final String Find_Bn = "find";
    private static final String Find_M1 = "find#1";
    private static final String Find_M2 = "find#2";
    private static final String Copy_M0 = "copy#0";

    private static final String Value_Id = "value";
    private static final String Start_Id = "start";

    @Getter
    private static final StringLibrary instance = new StringLibrary();

    private StringLibrary() {
        addMethod(Length_M0, StringLibrary::lengthMethod);
        addMethod(Find_M1, StringLibrary::findMethod1);
        addMethod(Find_M2, StringLibrary::findMethod2);
        addMethod(Copy_M0, StringLibrary::copyMethod);
    }

    @Override
    protected EType getType() {
        return EType.STRING;
    }

    private static GInteger lengthMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GInteger.from(((EString) self).length());
    }

    private static EValue findMethod1(EValue self, List<EValue> arguments, ScriptScope scope) {
        if(!(arguments.get(0) instanceof EString a0)) throw failOnInvalidArgumentType(SFND01,
            arguments.get(0), Find_Bn, Value_Id, self);
        var index = ((EString) self).getValue().indexOf(a0.getValue());
        return index == -1 ? UNDEFINED : GInteger.from(index);
    }

    private static EValue findMethod2(EValue self, List<EValue> arguments, ScriptScope scope) {
        if(!(arguments.get(0) instanceof EString a0)) throw failOnInvalidArgumentType(SFND02,
            arguments.get(0), Find_Bn, Value_Id, self);
        if(!(arguments.get(1) instanceof EInteger a1)) throw failOnInvalidArgumentType(SFND03,
            arguments.get(1), Find_Bn, Start_Id, self);
        var index = ((EString) self).getValue().indexOf(a0.getValue(), (int) a1.getValue());
        return index == -1 ? UNDEFINED : GInteger.from(index);
    }

    private static EValue copyMethod(EValue self, List<EValue> arguments, ScriptScope scope) {
        return GString.from(((EString) self).getValue());
    }
}