package com.relogiclabs.jschema.internal.util;

import com.relogiclabs.jschema.exception.CommonException;
import com.relogiclabs.jschema.exception.ScriptRuntimeException;
import com.relogiclabs.jschema.internal.time.DateTimeContext;
import com.relogiclabs.jschema.tree.DataTree;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

import java.util.Collections;
import java.util.List;

import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.TRYS01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

// Logging library may require
// Kept it lightweight for now
public final class LogHelper {
    public static final int ERROR = 3;
    public static final int INFO = 2;
    public static final int DEBUG = 1;

    public static int level = ERROR;

    public static void debug(DataTree expected, DataTree actual) {
        if(level > DEBUG) return;
        System.out.println(concat("[DEBUG] Expected ", expected.getType(),
                " tree interpretation:"));
        System.out.println(expected.getRoot());
        System.out.println("---");
        System.out.println(concat("[DEBUG] Actual ", actual.getType(),
                " tree interpretation:"));
        System.out.println(actual.getRoot());
        System.out.println("---");
    }

    public static void debug(Recognizer<?, ?> recognizer) {
        if(level > DEBUG) return;
        List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        System.err.println("[DEBUG] Rule stack: " + String.join(" > ", stack));
    }

    public static void debug(DateTimeContext context) {
        if(level > DEBUG) return;
        System.out.println("[DEBUG] Date and time interpretation: " + context);
    }

    public static void debug(Exception e) {
        if(level > DEBUG) return;
        System.out.print("[DEBUG] Print of exception: ");
        e.printStackTrace(System.out);
    }

    public static void log(Exception exception, Token token) {
        if(level > INFO) return;
        System.out.println("[INFO] [TRYOF ERROR]: " + exception.getMessage());
        if(level > DEBUG) return;
        Exception ex = exception instanceof CommonException ? exception
                : new ScriptRuntimeException(formatForSchema(
                TRYS01, exception.getMessage(), token));
        System.out.print("[DEBUG] [TRYOF ERROR]: ");
        ex.printStackTrace(System.out);
    }
}