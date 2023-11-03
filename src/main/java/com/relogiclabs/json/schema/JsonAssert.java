package com.relogiclabs.json.schema;

import com.relogiclabs.json.schema.internal.util.DebugUtilities;
import com.relogiclabs.json.schema.message.MessageFormatter;
import com.relogiclabs.json.schema.tree.JsonTree;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.tree.SchemaTree;
import lombok.Getter;

/**
 * The class provides assertion functionalities to validate JSON documents against
 * a specific JSON Schema.
 */
@Getter
public class JsonAssert {
    private final RuntimeContext runtime;
    private final SchemaTree schemaTree;

    /**
     * Initializes a new instance of the {@link JsonAssert} class for the
     * specified Schema string.
     * @param schema A Schema string for validation or conformation
     */
    public JsonAssert(String schema) {
        runtime = new RuntimeContext(MessageFormatter.SCHEMA_ASSERTION, true);
        schemaTree = new SchemaTree(runtime, schema);
    }

    /**
     * Tests whether the input JSON string conforms to the Schema specified
     * in the {@link JsonAssert} constructor.
     * @param jsonActual The actual JSON to conform or validate
     */
    public void isValid(String jsonActual) {
        runtime.getExceptions().clear();
        var jsonTree = new JsonTree(runtime, jsonActual);
        DebugUtilities.print(schemaTree, jsonTree);
        schemaTree.getRoot().match(jsonTree.getRoot());
        if(!schemaTree.getRoot().match(jsonTree.getRoot()))
            throw new IllegalStateException("Exception not thrown");
    }

    /**
     * Tests whether the specified JSON string conforms to the given Schema string
     * and throws an exception if the JSON string does not conform to the Schema.
     * @param schemaExpected The expected Schema to conform or validate
     * @param jsonActual The actual JSON to conform or validate
     */
    public static void isValid(String schemaExpected, String jsonActual) {
        var runtime = new RuntimeContext(MessageFormatter.SCHEMA_ASSERTION, true);
        var schemaTree = new SchemaTree(runtime, schemaExpected);
        var jsonTree = new JsonTree(runtime, jsonActual);
        DebugUtilities.print(schemaTree, jsonTree);
        if(!schemaTree.getRoot().match(jsonTree.getRoot()))
            throw new IllegalStateException("Exception not thrown");
    }

    /**
     * Tests if the provided JSON strings are logically equivalent, meaning their structural
     * composition and internal data are identical. If the JSON strings are not equivalent,
     * an exception is thrown.
     * @param jsonExpected The expected JSON to compare
     * @param jsonActual The actual JSON to compare
     */
    public static void areEqual(String jsonExpected, String jsonActual) {
        var runtime = new RuntimeContext(MessageFormatter.JSON_ASSERTION, true);
        var expectedTree = new JsonTree(runtime, jsonExpected);
        var actualTree = new JsonTree(runtime, jsonActual);
        DebugUtilities.print(expectedTree, actualTree);
        if(!expectedTree.getRoot().match(actualTree.getRoot()))
            throw new IllegalStateException("Exception not thrown");
    }
}