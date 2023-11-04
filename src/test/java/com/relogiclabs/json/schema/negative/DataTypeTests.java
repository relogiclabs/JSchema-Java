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
    public void When_JsonWithWrongMainDataType_ExceptionThrown() {
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
    public void When_JsonWithWrongNestedDataType_ExceptionThrown() {
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
}