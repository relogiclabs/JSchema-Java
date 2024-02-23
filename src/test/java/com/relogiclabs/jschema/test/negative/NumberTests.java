package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.MAXI01;
import static com.relogiclabs.jschema.message.ErrorCode.MAXI03;
import static com.relogiclabs.jschema.message.ErrorCode.MINI01;
import static com.relogiclabs.jschema.message.ErrorCode.MINI03;
import static com.relogiclabs.jschema.message.ErrorCode.NEGI01;
import static com.relogiclabs.jschema.message.ErrorCode.NEGI02;
import static com.relogiclabs.jschema.message.ErrorCode.POSI01;
import static com.relogiclabs.jschema.message.ErrorCode.POSI02;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumberTests {
    @Test
    public void When_JsonLessThanMinimumFloat_ExceptionThrown() {
        var schema =
            """
            @minimum(10) #float
            """;
        var json =
            """
            9.999999
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(MINI01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonGreaterThanMaximumInteger_ExceptionThrown() {
        var schema =
            """
            @maximum(10) #integer
            """;
        var json =
            """
            1000
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(MAXI01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongJsonWithNestedMinimumIntegerInArray_ExceptionThrown() {
        var schema =
            """
            @minimum*(10.5) #integer*
            """;
        var json =
            """
            [
                1111,
                100,
                10
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(MINI01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongJsonWithNestedMinimumFloatInObject_ExceptionThrown() {
        var schema =
            """
            @minimum*(100) #number*
            """;
        var json =
            """
            {
                "key1": 100.000,
                "key2": 99.99,
                "key3": 200.884
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(MINI01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongJsonWithNestedMaximumNumberInArray_ExceptionThrown() {
        var schema =
            """
            @maximum*(1000.05) #number*
            """;
        var json =
            """
            [
                -1000.05,
                1000.05,
                1001
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(MAXI01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedMaximumWrongFloatInObject_ExceptionThrown() {
        var schema =
            """
            @maximum*(100) #float*
            """;
        var json =
            """
            {
                "key1": 100.000,
                "key2": -150.407,
                "key3": 100.00001
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(MAXI01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedMinimumExclusiveWrongFloatInObject_ExceptionThrown() {
        var schema =
            """
            @minimum*(100, true) #float*
            """;
        var json =
            """
            {
                "key1": 500.999,
                "key2": 100.001,
                "key3": 100.000
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(MINI03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedMaximumExclusiveWrongFloatInObject_ExceptionThrown() {
        var schema =
            """
            @maximum*(100, true) #float*
            """;
        var json =
            """
            {
                "key1": 99.999,
                "key2": 10.407,
                "key3": 100.000
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(MAXI03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedPositiveWithWrongNumberInArray_ExceptionThrown() {
        var schema =
            """
            @positive* #number*
            """;
        var json =
            """
            [1, 100.5, -500]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(POSI01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedPositiveReferenceWithWrongNumberInArray_ExceptionThrown() {
        var schema =
            """
            @positive*(0) #number*
            """;
        var json =
            """
            [0, 100, 0.1, -1]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(POSI02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedNegativeWithWrongNumberInArray_ExceptionThrown() {
        var schema =
            """
            @negative* #number*
            """;
        var json =
            """
            [-100, -500, -0.1, 0]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(NEGI01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedNegativeReferenceWithWrongNumberInArray_ExceptionThrown() {
        var schema =
            """
            @negative*(0) #number*
            """;
        var json =
            """
            [-100, -500, -0.01, 1]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(NEGI02, exception.getCode());
        exception.printStackTrace();
    }
}