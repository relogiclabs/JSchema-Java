package com.relogiclabs.json.schema.negative;

import com.relogiclabs.json.schema.JsonAssert;
import com.relogiclabs.json.schema.JsonSchema;
import com.relogiclabs.json.schema.exception.InvalidDataTypeException;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.json.schema.message.ErrorCode.DTYP01;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP04;
import static com.relogiclabs.json.schema.message.ErrorCode.DUBL01;
import static com.relogiclabs.json.schema.message.ErrorCode.FLOT01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OtherTests {
    @Test
    public void When_DataTypeNotValid_ExceptionThrown() {
        var schema = "#abcd";
        var json = "0";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(InvalidDataTypeException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotFloat_ExceptionThrown() {
        var schema = "#float";
        var json = "2.5E10";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotDouble_ExceptionThrown() {
        var schema = "#double";
        var json = "\"string\"";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotNull_ExceptionThrown() {
        var schema = "#null";
        var json = "0";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonValueNotEqualForFloat_ExceptionThrown() {
        var schema = "3.5 #float";
        var json = "2.5";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FLOT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonValueNotEqualForDouble_ExceptionThrown() {
        var schema = "2.5E0 #double";
        var json = "2.5E1";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DUBL01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NonStaticValidMethodWithWrongJson_ExceptionThrown() {
        var schema =
                """
                {
                    "key1": #array,
                    "key2": #array
                }
                """;
        var json1 =
                """
                {
                    "key1": [1, 10, 100],
                    "key2": [100, 1000, [10, 10000]]
                }
                """;
        var json2 =
                """
                {
                    "key1": [1, 10, 100],
                    "key2": "string"
                }
                """;
        var jsonAssert = new JsonAssert(schema);
        jsonAssert.isValid(json1);
        var exception = assertThrows(JsonSchemaException.class,
                () -> jsonAssert.isValid(json2));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }
}