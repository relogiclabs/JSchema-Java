package com.relogiclabs.json.schema.negative;

import com.relogiclabs.json.schema.JsonAssert;
import com.relogiclabs.json.schema.JsonSchema;
import com.relogiclabs.json.schema.exception.JsonParserException;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.json.schema.message.ErrorCode.ALEN01;
import static com.relogiclabs.json.schema.message.ErrorCode.ALEN02;
import static com.relogiclabs.json.schema.message.ErrorCode.ALEN03;
import static com.relogiclabs.json.schema.message.ErrorCode.ALEN04;
import static com.relogiclabs.json.schema.message.ErrorCode.ALEN05;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP04;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP06;
import static com.relogiclabs.json.schema.message.ErrorCode.ELEM01;
import static com.relogiclabs.json.schema.message.ErrorCode.ENUM02;
import static com.relogiclabs.json.schema.message.ErrorCode.JPRS01;
import static com.relogiclabs.json.schema.message.ErrorCode.NEMT02;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArrayTests {
    @Test
    public void When_JsonNotArray_ExceptionThrown() {
        var schema = "#array";
        var json = "10";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ElementsWithWrongArray_ExceptionThrown() {
        var schema = "@elements(10, 20, 30, 40) #array";
        var json = "[5, 10, 15, 20, 25]";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ELEM01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedElementsWithWrongArrayInArray_ExceptionThrown() {
        var schema = "@elements*(5, 10) #array";
        var json = "[[5, 10], [], [5, 10, 15, 20]]";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ELEM01, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ENUM02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidJsonInArray_ExceptionThrown() {
        var schema = "#array";
        var json = "[,,]";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonParserException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(JPRS01, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(NEMT02, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ALEN01, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ALEN02, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ALEN03, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ALEN04, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ALEN05, exception.getCode());
        exception.printStackTrace();
    }
}