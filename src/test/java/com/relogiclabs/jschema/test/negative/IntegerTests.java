package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.DataTypeValidationException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.exception.ValueValidationException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS02;
import static com.relogiclabs.jschema.message.ErrorCode.FNCFAL01;
import static com.relogiclabs.jschema.message.ErrorCode.INTVAL01;
import static com.relogiclabs.jschema.message.ErrorCode.RNGNUM01;
import static com.relogiclabs.jschema.message.ErrorCode.RNGNUM03;
import static com.relogiclabs.jschema.message.ErrorCode.RNGNUM04;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegerTests {
    @Test
    public void When_JsonNotInteger_ExceptionThrown() {
        var schema = "#integer";
        var json = "10.5";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonValueNotEqualForInteger_ExceptionThrown() {
        var schema = "10 #integer";
        var json = "9";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ValueValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(INTVAL01, exception.getCode());
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
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
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
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
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
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
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
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
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
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedRangeWithNonCompositeJsonValue_ExceptionThrown() {
        var schema =
            """
            @range*(100, !)
            """;
        var json =
            """
            "value1"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNCFAL01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RNGNUM01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RNGNUM04, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RNGNUM03, exception.getCode());
        exception.printStackTrace();
    }
}