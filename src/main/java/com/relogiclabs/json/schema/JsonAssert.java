package com.relogiclabs.json.schema;

import com.relogiclabs.json.schema.internal.util.DebugUtilities;
import com.relogiclabs.json.schema.tree.DataTree;
import com.relogiclabs.json.schema.tree.JsonTree;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.tree.SchemaTree;
import com.relogiclabs.json.schema.tree.TreeType;
import lombok.Getter;

import static com.relogiclabs.json.schema.message.MessageFormatter.JSON_ASSERTION;
import static com.relogiclabs.json.schema.message.MessageFormatter.SCHEMA_ASSERTION;
import static com.relogiclabs.json.schema.tree.TreeType.JSON_TREE;
import static com.relogiclabs.json.schema.tree.TreeType.SCHEMA_TREE;

/**
 * The class provides assertion functionalities to validate JSON documents against
 * a Schema or JSON.
 */
@Getter
public class JsonAssert {
    private final RuntimeContext runtime;
    private final DataTree expectedTree;

    /**
     * Initializes a new instance of the {@link JsonAssert} class for the
     * specified Schema string.
     * @param schema A Schema string for validation or conformation
     */
    public JsonAssert(String schema) {
        this(schema, SCHEMA_TREE);
    }

    /**
     * Initializes a new instance of the {@link JsonAssert} class for the specified
     * {@code expected} string, which can be either a Schema or a JSON representation.
     * @param expected An expected Schema or JSON string for validation or conformation
     * @param type The type of string provided by {@code expected}, indicating whether it represents
     * a Schema or JSON. Use {@link TreeType#SCHEMA_TREE} for Schema and {@link TreeType#JSON_TREE}
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
     * Tests whether the input JSON string conforms to the expected Schema or JSON
     * specified in the {@link JsonAssert} constructor.
     * @param json The actual input JSON to conform or validate
     */
    public void isValid(String json) {
        runtime.clear();
        var jsonTree = new JsonTree(runtime, json);
        DebugUtilities.print(expectedTree, jsonTree);
        if(!expectedTree.match(jsonTree))
            throw new IllegalStateException("Invalid runtime state");
    }

    /**
     * Tests whether the specified JSON string conforms to the given Schema string
     * and throws an exception if the JSON string does not conform to the Schema.
     * @param schema The expected Schema to conform or validate
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