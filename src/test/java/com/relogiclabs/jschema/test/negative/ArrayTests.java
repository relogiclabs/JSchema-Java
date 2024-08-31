package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.DataTypeValidationException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.exception.JsonParserException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS02;
import static com.relogiclabs.jschema.message.ErrorCode.ELEMCF01;
import static com.relogiclabs.jschema.message.ErrorCode.EMPTCF02;
import static com.relogiclabs.jschema.message.ErrorCode.ENMNUM01;
import static com.relogiclabs.jschema.message.ErrorCode.JSNPRS01;
import static com.relogiclabs.jschema.message.ErrorCode.LENARR01;
import static com.relogiclabs.jschema.message.ErrorCode.LENARR02;
import static com.relogiclabs.jschema.message.ErrorCode.LENARR03;
import static com.relogiclabs.jschema.message.ErrorCode.LENARR04;
import static com.relogiclabs.jschema.message.ErrorCode.LENARR05;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArrayTests {
    @Test
    public void When_JsonNotArray_ExceptionThrown() {
        var schema = "#array";
        var json = "10";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotArrayInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": #array,
                "key2": #array,
                "key3": #array
            }
            """;
        var json =
            """
            {
                "key1": "value1",
                "key2": {"key": "value"},
                "key3": 100000
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotArrayInArray_ExceptionThrown() {
        var schema =
            """
            [#array, #array, #array]
            """;
        var json =
            """
            [{}, "value1", 10.5]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedJsonNotArrayInArray_ExceptionThrown() {
        var schema =
            """
            #array*
            """;
        var json =
            """
            [true, "value1", false]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedJsonNotArrayInObject_ExceptionThrown() {
        var schema =
            """
            #array*
            """;
        var json =
            """
            {
                "key1": 10.11,
                "key2": true,
                "key3": "value1"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ElementsWithWrongArray_ExceptionThrown() {
        var schema = "@elements(10, 20, 30, 40) #array";
        var json = "[5, 10, 15, 20, 25]";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ELEMCF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedElementsWithWrongArrayInArray_ExceptionThrown() {
        var schema = "@elements*(5, 10) #array";
        var json = "[[5, 10], [], [5, 10, 15, 20]]";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ELEMCF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_EnumWithWrongValueInArray_ExceptionThrown() {
        var schema =
            """
            [
                @enum(5, 10, 15),
                @enum(100, 150, 200),
                @enum("abc", "pqr", "xyz")
            ] #array
            """;
        var json =
            """
            [11, 102, "efg"]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ENMNUM01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidJsonInArray_ExceptionThrown() {
        var schema = "#array";
        var json = "[,,]";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonParserException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(JSNPRS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_EmptyArrayInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": @nonempty #array,
                "key2": @nonempty
            }
            """;
        var json =
            """
            {
                "key1": [],
                "key2": []
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(EMPTCF02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonWrongLengthInObject_ExceptionThrown() {
        var schema =
            """
            @length*(2) #array* #object
            """;
        var json =
            """
            {
                "key1": [10, 20],
                "key2": [10]
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENARR01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonWrongMinimumLengthInObject_ExceptionThrown() {
        var schema =
            """
            @length*(2, 4) #array* #object
            """;
        var json =
            """
            {
                "key1": [10, 20],
                "key2": [10]
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENARR02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonWrongMaximumLengthInObject_ExceptionThrown() {
        var schema =
            """
            @length*(2, 4) #array* #object
            """;
        var json =
            """
            {
                "key1": [10, 20],
                "key2": [10, 20, 30, 40, 50]
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENARR03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonWrongMinimumLengthWithUndefinedInObject_ExceptionThrown() {
        var schema =
            """
            @length*(2, !) #array* #object
            """;
        var json =
            """
            {
                "key1": [10, 20],
                "key2": [10]
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENARR04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonWrongMaximumLengthWithUndefinedInObject_ExceptionThrown() {
        var schema =
            """
            @length*(!, 4) #array* #object
            """;
        var json =
            """
            {
                "key1": [10, 20],
                "key2": [10, 20, 30, 40, 50]
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENARR05, exception.getCode());
        exception.printStackTrace();
    }
}