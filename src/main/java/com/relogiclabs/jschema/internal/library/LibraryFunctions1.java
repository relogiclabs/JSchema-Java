package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.engine.ScriptTreeHelper;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

import static com.relogiclabs.jschema.type.EValue.VOID;

public abstract class LibraryFunctions1 {
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

    static void fail(ScriptScope scope, RuntimeException exception) {
        scope.getRuntime().getExceptions().fail(exception);
    }

    static EValue printFunction(ScriptScope scope, List<EValue> arguments) {
        System.out.println(ScriptTreeHelper.stringify(arguments.get(0)));
        return VOID;
    }

    static EValue ticksFunction(ScriptScope scope, List<EValue> arguments) {
        return GInteger.from(System.currentTimeMillis());
    }
}