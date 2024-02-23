package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.DuplicatePropertyKeyException;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.DTYP04;
import static com.relogiclabs.jschema.message.ErrorCode.DTYP06;
import static com.relogiclabs.jschema.message.ErrorCode.ENUM02;
import static com.relogiclabs.jschema.message.ErrorCode.KEYS01;
import static com.relogiclabs.jschema.message.ErrorCode.NEMT03;
import static com.relogiclabs.jschema.message.ErrorCode.OLEN01;
import static com.relogiclabs.jschema.message.ErrorCode.OLEN02;
import static com.relogiclabs.jschema.message.ErrorCode.OLEN04;
import static com.relogiclabs.jschema.message.ErrorCode.OLEN05;
import static com.relogiclabs.jschema.message.ErrorCode.PROP03;
import static com.relogiclabs.jschema.message.ErrorCode.PROP04;
import static com.relogiclabs.jschema.message.ErrorCode.VALU01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ObjectTests {
    @Test
    public void When_JsonNotObject_ExceptionThrown() {
        var schema = "#object";
        var json = "100";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(KEYS01, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(VALU01, exception.getCode());
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
                "key1": {"value": 10},
                "key2": {"value": 150},
                "key3": {"value": 1000}
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(KEYS01, exception.getCode());
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
            [{"value": 10}, {"value": 20}, {"value": 30}]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(KEYS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_EnumWithWrongObject_ExceptionThrown() {
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ENUM02, exception.getCode());
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
        assertEquals(PROP03, exception.getCode());
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
        assertEquals(PROP04, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(NEMT03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongLengthOfObjectInArray1_ExceptionThrown() {
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(OLEN01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongLengthOfObjectInArray2_ExceptionThrown() {
        var schema =
            """
            [
                @length(1, 4) #object
            ]
            """;
        var json =
            """
            [
                { }
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(OLEN02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongLengthOfObjectInArray3_ExceptionThrown() {
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(OLEN05, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongLengthOfObjectInArray4_ExceptionThrown() {
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(OLEN04, exception.getCode());
        exception.printStackTrace();
    }
}