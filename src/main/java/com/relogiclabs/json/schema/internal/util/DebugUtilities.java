package com.relogiclabs.json.schema.internal.util;

import com.relogiclabs.json.schema.internal.time.DateTimeContext;
import com.relogiclabs.json.schema.tree.DataTree;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Recognizer;

import java.util.Collections;
import java.util.List;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;

// External logging library may consider
// Kept it lightweight for now
public final class DebugUtilities {
    public static boolean debugPrint = false;

    public static void print(DataTree expected, DataTree actual) {
        if(!debugPrint) return;
        System.out.println(concat("[DEBUG] Expected ", expected.getType(),
                " tree interpretation:"));
        System.out.println(expected.getRoot());
        System.out.println("---");
        System.out.println(concat("[DEBUG] Actual ", actual.getType(),
                " tree interpretation:"));
        System.out.println(actual.getRoot());
        System.out.println("---");
    }

    public static void print(Recognizer<?, ?> recognizer) {
        if(!debugPrint) return;
        List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        System.err.println("[DEBUG] Rule stack: " + String.join(" > ", stack));
    }

    public static void print(DateTimeContext context) {
        if(!debugPrint) return;
        System.out.println("[DEBUG] Date and time interpretation: " + context);
    }

    public static void print(Exception e) {
        if(!debugPrint) return;
        System.err.println("[DEBUG] Print of exception: " + e);
    }
}