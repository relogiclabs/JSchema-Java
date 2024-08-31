package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.DataTypeValidationException;
import com.relogiclabs.jschema.exception.DuplicatePropertyKeyException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS02;
import static com.relogiclabs.jschema.message.ErrorCode.EMPTCF03;
import static com.relogiclabs.jschema.message.ErrorCode.ENMNUM01;
import static com.relogiclabs.jschema.message.ErrorCode.KEYFND01;
import static com.relogiclabs.jschema.message.ErrorCode.LENOBJ01;
import static com.relogiclabs.jschema.message.ErrorCode.LENOBJ02;
import static com.relogiclabs.jschema.message.ErrorCode.LENOBJ04;
import static com.relogiclabs.jschema.message.ErrorCode.LENOBJ05;
import static com.relogiclabs.jschema.message.ErrorCode.PRTDUP01;
import static com.relogiclabs.jschema.message.ErrorCode.PRTDUP02;
import static com.relogiclabs.jschema.message.ErrorCode.VALFND01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ObjectTests {
    @Test
    public void When_JsonNotObject_ExceptionThrown() {
        var schema = "#object";
        var json = "100";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotObjectInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": #object,
                "key2": #object,
                "key3": #object
            }
            """;
        var json =
            """
            {
                "key1": [],
                "key2": "value1",
                "key3": [10, 20, 30]
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotObjectInArray_ExceptionThrown() {
        var schema =
            """
            [#object, #object, #object]
            """;
        var json =
            """
            [null, "value1", true]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedJsonNotObjectInArray_ExceptionThrown() {
        var schema =
            """
            #object*
            """;
        var json =
            """
            [ 100, true, false ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedJsonNotObjectInObject_ExceptionThrown() {
        var schema =
            """
            #object*
            """;
        var json =
            """
            {
                "key1": 15.4,
                "key2": 0,
                "key3": [10]
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_KeysWithWrongObject_ExceptionThrown() {
        var schema =
            """
            @keys("key1", "key2") #integer*
            """;
        var json =
            """
            {
                "key4": 100,
                "key5": 150,
                "key6": 200
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(KEYFND01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ValuesWithWrongObject_ExceptionThrown() {
        var schema =
            """
            @values(100, 200) #integer*
            """;
        var json =
            """
            {
                "key1": 1,
                "key2": 2,
                "key3": 3
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(VALFND01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedKeysWithWrongObjectInObject_ExceptionThrown() {
        var schema =
            """
            @keys*("key") #object*
            """;
        var json =
            """
            {
                "key1": {"number": 10},
                "key2": {"number": 150},
                "key3": {"number": 1000}
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(KEYFND01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedKeysAndValuesWithWrongObjectInArray_ExceptionThrown() {
        var schema =
            """
            @keys*("key1", "key2") @values*(100, 200) #object*
            """;
        var json =
            """
            [{"number": 10}, {"number": 20}, {"number": 30}]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(KEYFND01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_EnumWithWrongValuesInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": @enum(5, 10, 15),
                "key2": @enum(100, 150, 200),
                "key3": @enum("abc", "pqr", "xyz")
            } #object
            """;
        var json =
            """
            {
                "key1": 1,
                "key2": 10,
                "key3": "efg"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ENMNUM01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DuplicateJsonPropertyInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": #object,
                "key2": #object,
                "key3": #object
            }
            """;
        var json =
            """
            {
                "key1": [],
                "key2": "value1",
                "key2": [10, 20, 30]
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(DuplicatePropertyKeyException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(PRTDUP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DuplicateSchemaPropertyInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": #object,
                "key2": #object,
                "key2": #object
            }
            """;
        var json =
            """
            {
                "key1": [],
                "key2": "value1",
                "key3": [10, 20, 30]
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(DuplicatePropertyKeyException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(PRTDUP02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_EmptyObjectInArray_ExceptionThrown() {
        var schema =
            """
            [
                @nonempty #object,
                @nonempty #object
            ]
            """;
        var json =
            """
            [
                { },
                { "key1": ["val1", "val2"] }
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(EMPTCF03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_FixedLengthForWrongObjectInArray_ExceptionThrown() {
        var schema =
            """
            [
                @length(1) #object
            ]
            """;
        var json =
            """
            [
                { "key1": 10, "key2": 20 }
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENOBJ01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_LengthRangeForWrongObjectInArray_ExceptionThrown() {
        var schema =
            """
            [
                @length(2, 4) #object
            ]
            """;
        var json =
            """
            [
                { "key1": 10 }
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENOBJ02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_LengthWithMinUndefinedForWrongObjectInArray_ExceptionThrown() {
        var schema =
            """
            [
                @length(!, 4) #object
            ]
            """;
        var json =
            """
            [
                { "key1": 10, "key2": 20, "key3": 30, "key4": 40, "key5": 50 }
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENOBJ05, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_LengthWithMaxUndefinedForWrongObjectInArray_ExceptionThrown() {
        var schema =
            """
            [
                @length(2, !) #object
            ]
            """;
        var json =
            """
            [
                { "key1": 10 }
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENOBJ04, exception.getCode());
        exception.printStackTrace();
    }
}