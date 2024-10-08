package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.AliasNotFoundException;
import com.relogiclabs.jschema.exception.DataTypeValidationException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.ALSDEF02;
import static com.relogiclabs.jschema.message.ErrorCode.ALSDEF03;
import static com.relogiclabs.jschema.message.ErrorCode.DTYCPS01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS02;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class DataTypeTests {
    @Test
    public void When_DirectDataTypeWithWrongJson_ExceptionThrown() {
        var schema =
            """
            #string* #array
            """;
        var json =
            """
            10
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedDataTypeWithWrongJson_ExceptionThrown() {
        var schema =
            """
            #string* #array
            """;
        var json =
            """
            [10, 20]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedTypeWithNonCompositeJson_ExceptionThrown() {
        var schema =
            """
            #string*
            """;
        var json =
            """
            10
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYCPS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DirectDataTypeWithUndefinedAliasArgument_ExceptionThrown() {
        var schema =
            """
            #array($undefined)
            """;
        var json =
            """
            [10, 20]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(AliasNotFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ALSDEF02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedDataTypeWithUndefinedAliasArgument_ExceptionThrown() {
        var schema =
            """
            #integer*($undefined) #array
            """;
        var json =
            """
            [10, 20]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(AliasNotFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ALSDEF03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DataTypeArgumentWithValidationFailed_ExceptionThrown() {
        var schema =
            """
            %define $test: {"k1": #string}
            %schema: #object($test)
            """;
        var json =
            """
            {"k1": 10}
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedDataTypeArgumentWithValidationFailed_ExceptionThrown() {
        var schema =
            """
            %define $test: {"k1": #string}
            %schema: #object*($test) #array
            """;
        var json =
            """
            [{"k1": 10}]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_MultipleNestedDataTypeWithWrongValueInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": #date* #time* #null* #array,
                "key2": #string* #null* #array,
                "key3": #integer* #float* #array
            }
            """;
        var json =
            """
            {
                "key1": ["2021-08-01", "2021-08-01T15:50:30.300Z", "test"],
                "key2": ["test", null, "text", 10],
                "key3": [false, true, null, "text"]
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DataTypeExceptionCountInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": #boolean* #integer #string #array,
                "key2": #boolean* #integer #string,
                "key3": #boolean* #integer #string #array
            }
            """;
        var json =
            """
            {
                "key1": [10, "test", "2021-08-01"],
                "key2": [10, "test", "2021-08-01"],
                "key3": []
            }
            """;
        var jsonSchema = new JsonSchema(schema);
        if(jsonSchema.isValid(json)) fail("Test failed");
        assertEquals(7, jsonSchema.writeError());
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_MultipleDataTypeWithWrongValueInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": #date #time #null,
                "key2": #array #object #null,
                "key3": #integer #float,
                "key4": #integer #date #null,
                "key5": #number #string #null
            }
            """;
        var json =
            """
            {
                "key1": "2021-08-01",
                "key2": null,
                "key3": 100,
                "key4": "test",
                "key5": false
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }
}