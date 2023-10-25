package com.relogiclabs.json.schema.negative;

import com.relogiclabs.json.schema.JsonAssert;
import com.relogiclabs.json.schema.JsonSchema;
import com.relogiclabs.json.schema.exception.DuplicatePragmaException;
import com.relogiclabs.json.schema.exception.InvalidPragmaValueException;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.exception.PragmaNotFoundException;
import com.relogiclabs.json.schema.exception.SchemaParserException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.json.schema.message.ErrorCode.FLOT01;
import static com.relogiclabs.json.schema.message.ErrorCode.PRAG01;
import static com.relogiclabs.json.schema.message.ErrorCode.PRAG02;
import static com.relogiclabs.json.schema.message.ErrorCode.PRAG03;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP05;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP06;
import static com.relogiclabs.json.schema.message.ErrorCode.SPRS01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PragmaTests {
    @Test
    public void When_UndefinedPropertyOfObject_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreUndefinedProperties: false
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": "value1"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(PROP05, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidUndefinedPropertyValueMissing_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreUndefinedProperties:
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": "value1"
            }
            """;
        //JsonSchema.IsValid(schema, json);
        var exception = assertThrows(SchemaParserException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(SPRS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_IgnoreUndefinedPropertiesMalformed_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreUndefinedProperty: true
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": "value1"
            }
            """;
        //JsonSchema.IsValid(schema, json);
        var exception = assertThrows(PragmaNotFoundException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(PRAG01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidUndefinedPropertyValue_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreUndefinedProperties: 1
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": "value1"
            }
            """;
        //JsonSchema.IsValid(schema, json);
        var exception = assertThrows(InvalidPragmaValueException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(PRAG02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_IgnorePropertyOrderOfObject_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreObjectPropertyOrder: false
            %schema:
            {
                "key1": #integer,
                "key2": #string,
                "key3": #float
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key3": 2.1,
                "key2": "value1"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(PROP06, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_FloatingPointToleranceOfNumber_ExceptionThrown() {
        var schema =
            """
            %pragma FloatingPointTolerance: 0.00001
            %schema:
            {
                "key1": 5.00 #float,
                "key2": 10.00E+0 #double
            }
            """;
        var json =
            """
            {
                "key1": 5.00002,
                "key2": 10.0002E+0
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FLOT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DuplicatePragmaAssign_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreUndefinedProperties: false
            %pragma IgnoreUndefinedProperties: false

            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": "value1"
            }
            """;
        //JsonSchema.IsValid(schema, json);
        var exception = assertThrows(DuplicatePragmaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(PRAG03, exception.getCode());
        exception.printStackTrace();
    }
}