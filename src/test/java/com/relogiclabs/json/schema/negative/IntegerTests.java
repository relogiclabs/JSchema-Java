package com.relogiclabs.json.schema.negative;

import com.relogiclabs.json.schema.JsonAssert;
import com.relogiclabs.json.schema.JsonSchema;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.json.schema.message.ErrorCode.DTYP04;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP06;
import static com.relogiclabs.json.schema.message.ErrorCode.FUNC06;
import static com.relogiclabs.json.schema.message.ErrorCode.INTE01;
import static com.relogiclabs.json.schema.message.ErrorCode.NEGI01;
import static com.relogiclabs.json.schema.message.ErrorCode.POSI01;
import static com.relogiclabs.json.schema.message.ErrorCode.RANG01;
import static com.relogiclabs.json.schema.message.ErrorCode.RANG03;
import static com.relogiclabs.json.schema.message.ErrorCode.RANG04;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegerTests {
    @Test
    public void When_JsonNotInteger_ExceptionThrown() {
        var schema = "#integer";
        var json = "10.5";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonValueNotEqualForInteger_ExceptionThrown() {
        var schema = "10 #integer";
        var json = "9";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(INTE01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotIntegerInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": #integer,
                "key2": #integer,
                "key3": #integer
            }
            """;
        var json =
            """
            {
                "key1": null,
                "key2": "value1",
                "key3": 4000.45
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotIntegerInArray_ExceptionThrown() {
        var schema =
            """
            [#integer, #integer, #integer]
            """;
        var json =
            """
            [true, -4568.57, 100]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedJsonNotIntegerInArray_ExceptionThrown() {
        var schema =
            """
            #integer*
            """;
        var json =
            """
            [null, 2.2, "40000000"]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedJsonNotIntegerInObject_ExceptionThrown() {
        var schema =
            """
            #integer*
            """;
        var json =
            """
            {
                "key1": "value1",
                "key2": false,
                "key3": "-50000"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedRangeWithJsonNotIntegerInObject_ExceptionThrown() {
        var schema =
            """
            @range*(-100, 200) #integer*
            """;
        var json =
            """
            {
                "key1": "value1",
                "key2": false,
                "key3": "-50000"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedRangeWithNonCompositeJsonInObject_ExceptionThrown() {
        var schema =
            """
            @range*(100, !)
            """;
        var json =
            """
            "value1"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FUNC06, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedRangeWithJsonWrongIntegerInObject_ExceptionThrown() {
        var schema =
            """
            @range*(-100, 100) #integer*
            """;
        var json =
            """
            {
                "key1": -100,
                "key2": 100,
                "key3": -500
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(RANG01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedRangeWithMinUndefinedAndWrongIntegerInArray_ExceptionThrown() {
        var schema =
            """
            @range*(!, 400) #integer*
            """;
        var json =
            """
            [100, 500, 900]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(RANG04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedRangeWithMaxUndefinedAndWrongIntegerInArray_ExceptionThrown() {
        var schema =
            """
            @range*(1000, !) #integer*
            """;
        var json =
            """
            [2000, 1000, 900]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(RANG03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedPositiveWithWrongIntegerInArray_ExceptionThrown() {
        var schema =
            """
            @positive* #integer*
            """;
        var json =
            """
            [100, -500, 900]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(POSI01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedNegativeWithWrongIntegerInArray_ExceptionThrown() {
        var schema =
            """
            @negative* #integer*
            """;
        var json =
            """
            [-100, -500, 900]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(NEGI01, exception.getCode());
        exception.printStackTrace();
    }
}