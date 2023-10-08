package com.relogiclabs.json.schema;

import com.relogiclabs.json.schema.internal.util.DebugUtils;
import com.relogiclabs.json.schema.message.MessageFormatter;
import com.relogiclabs.json.schema.tree.JsonTree;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.tree.SchemaTree;
import lombok.Getter;

import java.util.Queue;

/**
 * {@code JsonSchema} provides Schema validation functionalities for JSON document.
 */
@Getter
public class JsonSchema {
    private final RuntimeContext runtime;
    private final SchemaTree schemaTree;
    private final Queue<Exception> exceptions;

    /**
     * Initializes a new instance of the {@link JsonSchema} class for the
     * specified Schema string.
     * @param schema A Schema string for validation
     */
    public JsonSchema(String schema) {
        runtime = new RuntimeContext(MessageFormatter.SCHEMA_VALIDATION, false);
        exceptions = runtime.getExceptions();
        schemaTree = new SchemaTree(runtime, schema);
    }

    /**
     * Indicates whether the input JSON string conforms to the Schema specified
     * in the {@link JsonSchema} constructor.
     * @param json The JSON to validate with Schema
     * @return Returns {@code true} if the JSON string conforms to the Schema and {@code false} otherwise.
     */
    public boolean isValid(String json) {
        exceptions.clear();
        var jsonTree = new JsonTree(runtime, json);
        DebugUtils.print(schemaTree, jsonTree);
        return schemaTree.getRoot().match(jsonTree.getRoot());
    }

    /**
     *  Writes error messages that occur during Schema validation process, to the
     *  standard error output stream.
     */
    public void writeError() {
        for(var exception : exceptions)
            System.err.println(exception.getMessage());
    }

    /**
     * Indicates whether the input JSON string conforms to the given Schema string.
     * @param schema The Schema string to conform or validate
     * @param json The JSON string to conform or validate
     * @return Returns {@code true} if the JSON string conforms to the Schema and {@code false} otherwise.
     */
    public static boolean isValid(String schema, String json) {
        var jsonSchema = new JsonSchema(schema);
        var result = jsonSchema.isValid(json);
        if(!result) jsonSchema.writeError();
        return result;
    }
}