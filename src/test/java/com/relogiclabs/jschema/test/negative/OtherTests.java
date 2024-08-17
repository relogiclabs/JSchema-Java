package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.DataTypeValidationException;
import com.relogiclabs.jschema.exception.InvalidDataTypeException;
import com.relogiclabs.jschema.exception.MisplacedOptionalException;
import com.relogiclabs.jschema.exception.ValueValidationException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.ARRELM01;
import static com.relogiclabs.jschema.message.ErrorCode.ARROPT01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYVDF01;
import static com.relogiclabs.jschema.message.ErrorCode.DUBLVL01;
import static com.relogiclabs.jschema.message.ErrorCode.FLOTVL01;
import static com.relogiclabs.jschema.message.ErrorCode.PRTFND01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OtherTests {
    @Test
    public void When_DataTypeNotValid_ExceptionThrown() {
        var schema = "#abcd";
        var json = "0";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(InvalidDataTypeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYVDF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotFloat_ExceptionThrown() {
        var schema = "#float";
        var json = "2.5E10";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotDouble_ExceptionThrown() {
        var schema = "#double";
        var json = "\"string\"";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotNull_ExceptionThrown() {
        var schema = "#null";
        var json = "0";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonValueNotEqualForFloat_ExceptionThrown() {
        var schema = "3.5 #float";
        var json = "2.5";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ValueValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FLOTVL01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonValueNotEqualForDouble_ExceptionThrown() {
        var schema = "2.5E0 #double";
        var json = "2.5E1";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ValueValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DUBLVL01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NonStaticValidMethodWithMultipleJson_ExceptionThrown() {
        var schema =
            """
            {
                "key1": #array,
                "key2": #array
            }
            """;
        var json1 =
            """
            {
                "key1": [1, 10, 100],
                "key2": [100, 1000, [10, 10000]]
            }
            """;
        var json2 =
            """
            {
                "key1": [1, 10, 100],
                "key2": "string"
            }
            """;
        var jsonAssert = new JsonAssert(schema);
        // json1 is a valid JSON and not throw any exception
        jsonAssert.isValid(json1);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> jsonAssert.isValid(json2));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_MandatoryElementMissingInArray_ExceptionThrown() {
        var schema = "[@range(1, 10) #number, @range(10, 100) #number, #number?]";
        var json = "[10]";
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ValueValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ARRELM01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_OptionalElementMisplacedInArray_ExceptionThrown() {
        var schema = "[#number, #number?, #number]";
        var json = "[10, 20]";
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(MisplacedOptionalException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ARROPT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_MandatoryPropertyMissingInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": #integer,
                "key2": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ValueValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(PRTFND01, exception.getCode());
        exception.printStackTrace();
    }
}