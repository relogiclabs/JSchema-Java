package com.relogiclabs.json.schema.negative;

import com.relogiclabs.json.schema.JsonAssert;
import com.relogiclabs.json.schema.JsonSchema;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.json.schema.message.ErrorCode.MAXI01;
import static com.relogiclabs.json.schema.message.ErrorCode.MAXI03;
import static com.relogiclabs.json.schema.message.ErrorCode.MINI01;
import static com.relogiclabs.json.schema.message.ErrorCode.MINI03;
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
    public void When_NestedMaximumFloatInObject_ValidTrue() {
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
    public void When_NestedMinimumExclusiveFloatInObject_ValidTrue() {
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
    public void When_NestedMaximumExclusiveFloatInObject_ValidTrue() {
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
}