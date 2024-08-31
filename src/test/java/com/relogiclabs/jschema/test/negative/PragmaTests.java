package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.DataTypeValidationException;
import com.relogiclabs.jschema.exception.DateTimeLexerException;
import com.relogiclabs.jschema.exception.DuplicatePragmaException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.exception.InvalidPragmaValueException;
import com.relogiclabs.jschema.exception.PragmaNotFoundException;
import com.relogiclabs.jschema.exception.SchemaParserException;
import com.relogiclabs.jschema.exception.ValueValidationException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.AFTRDT01;
import static com.relogiclabs.jschema.message.ErrorCode.AFTRDT02;
import static com.relogiclabs.jschema.message.ErrorCode.BFORDT01;
import static com.relogiclabs.jschema.message.ErrorCode.BFORDT02;
import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS01;
import static com.relogiclabs.jschema.message.ErrorCode.EMALCF01;
import static com.relogiclabs.jschema.message.ErrorCode.FLOTVL01;
import static com.relogiclabs.jschema.message.ErrorCode.LEXRDT01;
import static com.relogiclabs.jschema.message.ErrorCode.PHONCF01;
import static com.relogiclabs.jschema.message.ErrorCode.PRGDEF01;
import static com.relogiclabs.jschema.message.ErrorCode.PRGDEF02;
import static com.relogiclabs.jschema.message.ErrorCode.PRGDUP01;
import static com.relogiclabs.jschema.message.ErrorCode.PRTORD01;
import static com.relogiclabs.jschema.message.ErrorCode.PRTUDF01;
import static com.relogiclabs.jschema.message.ErrorCode.RNDEND03;
import static com.relogiclabs.jschema.message.ErrorCode.RNDEND04;
import static com.relogiclabs.jschema.message.ErrorCode.RNDSTA01;
import static com.relogiclabs.jschema.message.ErrorCode.RNDSTA02;
import static com.relogiclabs.jschema.message.ErrorCode.RNDSTA03;
import static com.relogiclabs.jschema.message.ErrorCode.RNDSTA04;
import static com.relogiclabs.jschema.message.ErrorCode.SCMPRS01;
import static com.relogiclabs.jschema.message.ErrorCode.SYMBDT01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PragmaTests {
    @Test
    public void When_UndefinedPropertyOfObject_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreUndefinedProperties: false
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": "value2"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ValueValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(PRTUDF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_PragmaValueMissing_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreUndefinedProperties:
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": "value2"
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(SchemaParserException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(SCMPRS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_MisspelledOrUnknownPragma_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreUndefinedProp: true
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": "value2"
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(PragmaNotFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(PRGDEF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidPragmaValueType_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreUndefinedProperties: 1
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": "value2"
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(InvalidPragmaValueException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(PRGDEF02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NotIgnorePropertyOrderOfObject_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreObjectPropertyOrder: false
            %schema:
            {
                "key1": #integer,
                "key2": #string,
                "key3": #float
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key3": 2.1,
                "key2": "value2"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ValueValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(PRTORD01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_FloatingPointToleranceOfNumber_ExceptionThrown() {
        var schema =
            """
            %pragma FloatingPointTolerance: 0.00001
            %schema:
            {
                "key1": 5.00 #float,
                "key2": 10.00E+0 #double
            }
            """;
        var json =
            """
            {
                "key1": 5.00002,
                "key2": 10.0002E+0
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ValueValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FLOTVL01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DuplicatePragmaAssign_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreUndefinedProperties: false
            %pragma IgnoreUndefinedProperties: false

            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": "value2"
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(DuplicatePragmaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(PRGDUP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateDataTypeFormat_ExceptionThrown() {
        var schema =
            """
            %pragma DateDataTypeFormat: "TEST"
            %schema:
            {
                "key1": #date,
                "key2": #date
            }
            """;
        var json =
            """
            {
                "key1": "2023-11-05",
                "key2": "2021-06-01"
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(DateTimeLexerException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LEXRDT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonMismatchedWithDateFormat_ExceptionThrown() {
        var schema =
            """
            %pragma DateDataTypeFormat: "DD-MM-YYYY"
            %schema:
            {
                "key1": #date,
                "key2": #date
            }
            """;
        var json =
            """
            {
                "key1": "2023-11-05",
                "key2": "2021-06-01"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonMismatchedWithTimeFormat_ExceptionThrown() {
        var schema =
            """
            %pragma TimeDataTypeFormat: "DD-MM-YYYY hh:mm:ss"
            %schema:
            {
                "key1": #time,
                "key2": #time
            }
            """;
        var json =
            """
            {
                "key1": "05-11-2023 12:10:30",
                "key2": "05.11.2023 23.59.59"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonDateNotValidWithBefore_ExceptionThrown() {
        var schema =
            """
            %pragma DateDataTypeFormat: "DD-MM-YYYY"
            %schema:
            @before*("01-01-2011") #date* #array
            """;
        var json =
            """
            [
                "01-01-2010",
                "01-02-2010",
                "31-12-2010",
                "01-01-2011"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(BFORDT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonDateNotValidWithAfter_ExceptionThrown() {
        var schema =
            """
            %pragma DateDataTypeFormat: "DD-MM-YYYY"
            %schema:
            @after*("31-12-2009") #date* #array
            """;
        var json =
            """
            [
                "01-01-2010",
                "01-02-2010",
                "31-12-2010",
                "31-12-2009"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(AFTRDT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonTimeNotValidWithBefore_ExceptionThrown() {
        var schema =
            """
            %pragma TimeDataTypeFormat: "DD-MM-YYYY hh:mm:ss"
            %schema:
            @before*("01-01-2011 00:00:00") #time* #array
            """;
        var json =
            """
            [
                "01-01-1970 10:30:49",
                "01-02-2010 12:59:49",
                "31-12-2010 23:59:59",
                "01-01-2011 00:00:00"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(BFORDT02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonTimeNotValidWithAfter_ExceptionThrown() {
        var schema =
            """
            %pragma TimeDataTypeFormat: "DD-MM-YYYY hh:mm:ss"
            %schema:
            @after*("01-09-1939 00:00:00") #time* #array
            """;
        var json =
            """
            [
                "01-09-1939 00:00:01",
                "01-02-2010 12:59:49",
                "31-12-2030 23:59:59",
                "01-09-1939 00:00:00"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(AFTRDT02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonDateNotValidWithBothRange_ExceptionThrown() {
        var schema =
            """
            %pragma DateDataTypeFormat: "DD-MM-YYYY"
            %schema:
            @range*("01-01-2010", "31-12-2010") #date* #array
            """;
        var json =
            """
            [
                "01-01-2010",
                "01-02-2010",
                "30-06-2010",
                "31-12-2009",
                "01-01-2011"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RNDSTA01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonDateNotValidWithStartRange_ExceptionThrown() {
        var schema =
            """
            %pragma DateDataTypeFormat: "DD-MM-YYYY"
            %schema:
            @range*("01-01-2010", !) #date* #array
            """;
        var json =
            """
            [
                "01-01-2010",
                "01-02-2010",
                "30-06-2011",
                "01-11-2050",
                "31-12-2009"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RNDSTA03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonDateNotValidWithEndRange_ExceptionThrown() {
        var schema =
            """
            %pragma DateDataTypeFormat: "DD-MM-YYYY"
            %schema:
            @range*(!, "31-12-2010") #date* #array
            """;
        var json =
            """
            [
                "01-01-1930",
                "01-02-2000",
                "30-06-2005",
                "31-12-2010",
                "01-01-2011"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RNDEND03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonTimeNotValidWithBothRange_ExceptionThrown() {
        var schema =
            """
            %pragma TimeDataTypeFormat: "DD-MM-YYYY hh:mm:ss"
            %schema:
            @range*("01-01-2010 00:00:00", "31-12-2010 23:59:59") #time* #array
            """;
        var json =
            """
            [
                "01-01-2010 00:00:00",
                "01-02-2010 01:30:45",
                "30-06-2010 12:01:07",
                "31-12-2009 23:59:59",
                "01-01-2011 00:00:00"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RNDSTA02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonTimeNotValidWithStartRange_ExceptionThrown() {
        var schema =
            """
            %pragma TimeDataTypeFormat: "DD-MM-YYYY hh:mm:ss"
            %schema:
            @range*("01-01-2010 00:00:00", !) #time* #array
            """;
        var json =
            """
            [
                "01-01-2010 00:00:00",
                "01-02-2021 01:30:45",
                "30-06-2030 12:01:07",
                "31-12-2009 23:59:59"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RNDSTA04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonTimeNotValidWithEndRange_ExceptionThrown() {
        var schema =
            """
            %pragma TimeDataTypeFormat: "DD-MM-YYYY hh:mm:ss"
            %schema:
            @range*(!, "31-12-2010 23:59:59") #time* #array
            """;
        var json =
            """
            [
                "01-01-1930 00:00:00",
                "01-02-1990 01:30:45",
                "30-06-2000 12:01:07",
                "31-12-2010 23:59:59",
                "01-01-2011 00:00:00"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RNDEND04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_SchemaDateNotValidWithStartRange_ExceptionThrown() {
        var schema =
            """
            %pragma DateDataTypeFormat: "DD-MM-YYYY"
            %schema:
            @range*("2010-01-01", !) #date* #array
            """;
        var json =
            """
            [
                "01-01-2010",
                "30-06-2011",
                "01-11-2050"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(SYMBDT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_SchemaTimeNotValidWithEndRange_ExceptionThrown() {
        var schema =
            """
            %pragma TimeDataTypeFormat: "DD-MM-YYYY hh:mm:ss"
            %schema:
            @range*(!, "31.12.2010 23.59.59") #time* #array
            """;
        var json =
            """
            [
                "01-01-1930 00:00:00",
                "30-06-2000 12:01:07",
                "31-12-2010 23:59:59"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(SYMBDT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_EnableContextualExceptionWithError_ExceptionThrown() {
        var schema =
            """
            %pragma EnableContextualException: true
            %schema: {
                "user": {
                    "userName": @length(5, 20) #string,
                    "profile": {
                        "email": @email #string
                    }
                }
            }
            """;
        var json =
            """
            {
                "user": {
                    "userName": "example_user_name",
                    "profile": {
                        "email": "invalid email address"
                    }
                }
            }
            """;
        var jsonSchema = new JsonSchema(schema);
        assertFalse(jsonSchema.isValid(json));
        assertEquals(4, jsonSchema.writeError());
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(EMALCF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DisableContextualExceptionWithError_ExceptionThrown() {
        var schema =
            """
            // By default contextual exception is disabled
            // %pragma EnableContextualException: false
            %schema: {
                "user": {
                    "userName": @length(5, 20) #string,
                    "profile": {
                        "email": @email #string
                    }
                }
            }
            """;
        var json =
            """
            {
                "user": {
                    "userName": "example_user_name",
                    "profile": {
                        "email": "invalid email address"
                    }
                }
            }
            """;
        var jsonSchema = new JsonSchema(schema);
        assertFalse(jsonSchema.isValid(json));
        assertEquals(1, jsonSchema.writeError());
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(EMALCF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_CustomOutlineMaxLengthWithError_ExceptionThrown() {
        var schema =
            """
            %pragma EnableContextualException: true
            %pragma OutlineMaximumLength: 30
            %schema: {
                "user": {
                    "userName": @length(5, 20) #string,
                    "profile": {
                        "phoneNumber": @phone #string
                    }
                }
            }
            """;
        var json =
            """
            {
                "user": {
                    "userName": "example_user_name",
                    "profile": {
                        "phoneNumber": "invalid phone number"
                    }
                }
            }
            """;
        var jsonSchema = new JsonSchema(schema);
        assertFalse(jsonSchema.isValid(json));
        assertEquals(4, jsonSchema.writeError());
        for(Exception e : jsonSchema.getExceptions())
            assertTrue(e.getMessage().length() < 190);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(PHONCF01, exception.getCode());
        exception.printStackTrace();
    }
}