package com.relogiclabs.jschema;

import com.relogiclabs.jschema.internal.util.LogHelper;
import com.relogiclabs.jschema.tree.DataTree;
import com.relogiclabs.jschema.tree.JsonTree;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.tree.SchemaTree;
import com.relogiclabs.jschema.tree.TreeType;
import lombok.Getter;

import static com.relogiclabs.jschema.message.MessageFormatter.JSON_ASSERTION;
import static com.relogiclabs.jschema.message.MessageFormatter.SCHEMA_ASSERTION;
import static com.relogiclabs.jschema.tree.TreeType.JSON_TREE;
import static com.relogiclabs.jschema.tree.TreeType.SCHEMA_TREE;

/**
 * The class provides assertion functionalities to validate JSON documents against
 * a JSchema or JSON document.
 */
@Getter
public class JsonAssert {
    private final RuntimeContext runtime;
    private final DataTree expectedTree;

    /**
     * Initializes a new instance of the {@link JsonAssert} class for the
     * specified JSchema string.
     * @param schema A JSchema string for validation or conformation
     */
    public JsonAssert(String schema) {
        this(schema, SCHEMA_TREE);
    }

    /**
     * Initializes a new instance of the {@link JsonAssert} class for the specified
     * {@code expected} string, which can be either a JSchema or a JSON representation.
     * @param expected An expected JSchema or JSON string for validation or conformation
     * @param type The type of string provided by {@code expected}, indicating whether it represents
     * a JSchema or JSON. Use {@link TreeType#SCHEMA_TREE} for JSchema and {@link TreeType#JSON_TREE}
     * for JSON.
     */
    public JsonAssert(String expected, TreeType type) {
        if(type == SCHEMA_TREE) {
            runtime = new RuntimeContext(SCHEMA_ASSERTION, true);
            expectedTree = new SchemaTree(runtime, expected);
        } else {
            runtime = new RuntimeContext(JSON_ASSERTION, true);
            expectedTree = new JsonTree(runtime, expected);
        }
    }

    /**
     * Tests whether the input JSON string conforms to the expected JSchema or JSON
     * specified in the {@link JsonAssert} constructor.
     * @param json The actual input JSON to conform or validate
     */
    public void isValid(String json) {
        runtime.clear();
        var jsonTree = new JsonTree(runtime, json);
        LogHelper.debug(expectedTree, jsonTree);
        if(!expectedTree.match(jsonTree))
            throw new IllegalStateException("Invalid runtime state");
    }

    /**
     * Tests whether the specified JSON string conforms to the given JSchema string
     * and throws an exception if the JSON string does not conform to the JSchema.
     * @param schema The expected JSchema to conform or validate
     * @param json The actual JSON to conform or validate
     */
    public static void isValid(String schema, String json) {
        new JsonAssert(schema).isValid(json);
    }

    /**
     * Tests if the provided JSON strings are logically equivalent, meaning their structural
     * composition and internal data are identical. If the JSON strings are not equivalent,
     * an exception is thrown.
     * @param expected The expected JSON to compare
     * @param actual The actual JSON to compare
     */
    public static void areEqual(String expected, String actual) {
        new JsonAssert(expected, JSON_TREE).isValid(actual);
    }
}