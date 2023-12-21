package com.relogiclabs.json.schema.negative;

import com.relogiclabs.json.schema.JsonAssert;
import com.relogiclabs.json.schema.JsonSchema;
import com.relogiclabs.json.schema.exception.DefinitionNotFoundException;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.json.schema.message.ErrorCode.DEFI03;
import static com.relogiclabs.json.schema.message.ErrorCode.DEFI04;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP03;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP04;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP06;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataTypeTests {
    @Test
    public void When_WrongJsonWithDirectDataType_ExceptionThrown() {
        var schema =
                """
                #string* #array
                """;
        var json =
                """
                10
                """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongJsonWithNestedDataType_ExceptionThrown() {
        var schema =
                """
                #string* #array
                """;
        var json =
                """
                [10, 20]
                """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_UndefinedDataTypeArgument_ExceptionThrown() {
        var schema =
                """
                #array($undefined)
                """;
        var json =
                """
                [10, 20]
                """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DefinitionNotFoundException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DEFI03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_UndefinedNestedDataTypeArgument_ExceptionThrown() {
        var schema =
                """
                #integer*($undefined) #array
                """;
        var json =
                """
                [10, 20]
                """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DefinitionNotFoundException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DEFI04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DataTypeArgumentWithValidationFailed_ExceptionThrown()
    {
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedDataTypeArgumentWithValidationFailed_ExceptionThrown()
    {
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
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
        if(!jsonSchema.isValid(json)) jsonSchema.writeError();
        assertEquals(8, jsonSchema.getExceptions().getCount());
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
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
                "key4": #string #null,
                "key5": #number #string
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }
}