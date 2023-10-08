package com.relogiclabs.json.schema.internal.util;

import com.relogiclabs.json.schema.internal.time.DateTimeContext;
import com.relogiclabs.json.schema.tree.JsonTree;
import com.relogiclabs.json.schema.tree.SchemaTree;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Recognizer;

import java.util.Collections;
import java.util.List;

public class DebugUtils {

    public static boolean debugPrint = false;

    public static void print(SchemaTree schemaTree, JsonTree jsonTree) {
        if(!debugPrint) return;
        System.out.println("[DEBUG] Schema interpretation:");
        System.out.println(schemaTree.getRoot());
        System.out.println("---");
        System.out.println("[DEBUG] Json interpretation:");
        System.out.println(jsonTree.getRoot());
        System.out.println("---");
    }

    public static void print(JsonTree expectedTree, JsonTree actualTree) {
        if(!debugPrint) return;
        System.out.println("[DEBUG] Expected Json interpretation:");
        System.out.println(expectedTree.getRoot());
        System.out.println("---");
        System.out.println("[DEBUG] Actual Json interpretation:");
        System.out.println(actualTree.getRoot());
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