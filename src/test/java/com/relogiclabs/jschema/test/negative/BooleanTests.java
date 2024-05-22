package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.BOOL01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYP04;
import static com.relogiclabs.jschema.message.ErrorCode.DTYP06;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BooleanTests {
    @Test
    public void When_JsonNotBoolean_ExceptionThrown() {
        var schema = "#boolean";
        var json = "5";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonValueNotEqualForBoolean_ExceptionThrown() {
        var schema = "true #boolean";
        var json = "false";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(BOOL01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotBooleanInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": #boolean,
                "key2": #boolean,
                "key3": #boolean
            }
            """;
        var json =
            """
            {
                "key1": null,
                "key2": 10.5,
                "key3": true
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotBooleanInArray_ExceptionThrown() {
        var schema =
            """
            [#boolean, #boolean, #boolean]
            """;
        var json =
            """
            [[], 11.5, "false"]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedJsonNotBooleanInArray_ExceptionThrown() {
        var schema =
            """
            #boolean*
            """;
        var json =
            """
            ["true", {}, [true]]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedJsonNotBooleanInObject_ExceptionThrown() {
        var schema =
            """
            #boolean*
            """;
        var json =
            """
            {
                "key1": {"key": true},
                "key2": null,
                "key3": false
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
        exception.printStackTrace();
    }
}