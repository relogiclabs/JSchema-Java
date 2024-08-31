package com.relogiclabs.jschema;

import com.relogiclabs.jschema.internal.util.LogHelper;
import com.relogiclabs.jschema.tree.ExceptionRegistry;
import com.relogiclabs.jschema.tree.JsonTree;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.tree.SchemaTree;
import lombok.Getter;

import static com.relogiclabs.jschema.message.MessageFormatter.SCHEMA_VALIDATION;

/**
 * {@code JsonSchema} provides JSchema validation functionalities for JSON document.
 */
@Getter
public class JsonSchema {
    private final RuntimeContext runtime;
    private final SchemaTree schemaTree;
    private final ExceptionRegistry exceptions;

    /**
     * Initializes a new instance of the {@link JsonSchema} class for the
     * specified JSchema string.
     * @param schema A JSchema string for validation
     */
    public JsonSchema(String schema) {
        runtime = new RuntimeContext(SCHEMA_VALIDATION, false);
        exceptions = runtime.getExceptions();
        schemaTree = new SchemaTree(runtime, schema);
    }

    /**
     * Indicates whether the input JSON string conforms to the JSchema specified
     * in the {@link JsonSchema} constructor.
     * @param json The JSON to validate with JSchema
     * @return Returns {@code true} if the JSON string conforms to the JSchema and {@code false} otherwise.
     */
    public boolean isValid(String json) {
        runtime.clear();
        var jsonTree = new JsonTree(runtime, json);
        LogHelper.debug(schemaTree, jsonTree);
        return schemaTree.match(jsonTree);
    }

    /**
     *  Writes error messages that occur during JSchema validation process, to the
     *  standard error output stream.
     * @return Returns the number of errors written in the standard error stream.
     */
    public int writeError() {
        if(exceptions.getCount() == 0) {
            System.out.println("No error has occurred");
            return 0;
        }
        for(var exception : exceptions) System.err.println(formatMessage(exception));
        return exceptions.getCount();
    }

    private static String formatMessage(Exception exception) {
        return exception.getClass().getSimpleName() + ": " + exception.getMessage();
    }

    /**
     * Indicates whether the input JSON string conforms to the given JSchema string.
     * @param schema The JSchema string to conform or validate
     * @param json The JSON string to conform or validate
     * @return Returns {@code true} if the JSON string conforms to the JSchema and {@code false} otherwise.
     */
    public static boolean isValid(String schema, String json) {
        var jsonSchema = new JsonSchema(schema);
        var result = jsonSchema.isValid(json);
        if(!result) jsonSchema.writeError();
        return result;
    }
}