package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.ScriptArgumentException;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.message.OutlineFormatter.createOutline;

public abstract class ScriptLibrary1 {
    static final String Print_F1 = "print#1";
    static final String Fail_F1 = "fail#1";
    static final String Fail_F2 = "fail#2";
    static final String Fail_F4 = "fail#4";
    static final String Expected_F1 = "expected#1";
    static final String Expected_F2 = "expected#2";
    static final String Actual_F1 = "actual#1";
    static final String Actual_F2 = "actual#2";
    static final String Ticks_F0 = "ticks#0";

    static final String Message_Id = "message";
    static final String Code_Id = "code";
    static final String Expected_Id = "expected";
    static final String Actual_Id = "actual";
    static final String Node_Id = "node";


    static long toInteger(EValue value, String parameter, String code) {
        if(!(value instanceof EInteger i)) throw failOnInvalidArgumentType(code, value, parameter);
        return i.getValue();
    }

    static double toNumber(EValue value, String parameter, String code) {
        if(!(value instanceof ENumber n)) throw failOnInvalidArgumentType(code, value, parameter);
        return n.toDouble();
    }

    static String toString(EValue value, String parameter, String code) {
        if(!(value instanceof EString s)) throw failOnInvalidArgumentType(code, value, parameter);
        return s.getValue();
    }

    static <T> T cast(EValue value, Class<T> type, String parameter, String code) {
        if(!type.isInstance(value)) throw failOnInvalidArgumentType(code, value, parameter);
        return type.cast(value);
    }

    static <T> T getMember(EObject object, String key, Class<T> type, String parameter, String code) {
        var value = dereference(object.get(key));
        if(!type.isInstance(value)) throw failOnInvalidArgumentValue(code, object, parameter);
        return type.cast(value);
    }

    static ScriptArgumentException failOnInvalidArgumentType(String code, EValue argument,
                String parameter) {
        return new ScriptArgumentException(code, "Invalid argument type " + argument.getType()
            + " for parameter '" + parameter + "' of function '%s'");
    }

    static ScriptArgumentException failOnInvalidArgumentValue(String code, EValue argument,
                String parameter) {
        return new ScriptArgumentException(code, "Invalid argument value " + createOutline(argument)
            + " for parameter '" + parameter + "' of function '%s'");
    }

    static void fail(ScriptScope scope, RuntimeException exception) {
        scope.getRuntime().getExceptions().fail(exception);
    }
}