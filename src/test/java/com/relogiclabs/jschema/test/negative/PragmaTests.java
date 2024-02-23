package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.DateTimeLexerException;
import com.relogiclabs.jschema.exception.DuplicatePragmaException;
import com.relogiclabs.jschema.exception.InvalidPragmaValueException;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.exception.PragmaNotFoundException;
import com.relogiclabs.jschema.exception.SchemaParserException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.AFTR01;
import static com.relogiclabs.jschema.message.ErrorCode.AFTR02;
import static com.relogiclabs.jschema.message.ErrorCode.BFOR01;
import static com.relogiclabs.jschema.message.ErrorCode.BFOR02;
import static com.relogiclabs.jschema.message.ErrorCode.DLEX01;
import static com.relogiclabs.jschema.message.ErrorCode.DRNG01;
import static com.relogiclabs.jschema.message.ErrorCode.DRNG02;
import static com.relogiclabs.jschema.message.ErrorCode.DRNG05;
import static com.relogiclabs.jschema.message.ErrorCode.DRNG06;
import static com.relogiclabs.jschema.message.ErrorCode.DRNG07;
import static com.relogiclabs.jschema.message.ErrorCode.DRNG08;
import static com.relogiclabs.jschema.message.ErrorCode.DSYM01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYP04;
import static com.relogiclabs.jschema.message.ErrorCode.FLOT01;
import static com.relogiclabs.jschema.message.ErrorCode.PRAG01;
import static com.relogiclabs.jschema.message.ErrorCode.PRAG02;
import static com.relogiclabs.jschema.message.ErrorCode.PRAG03;
import static com.relogiclabs.jschema.message.ErrorCode.PROP06;
import static com.relogiclabs.jschema.message.ErrorCode.PROP07;
import static com.relogiclabs.jschema.message.ErrorCode.SPRS01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                "key2": "value1"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(PROP06, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidUndefinedPropertyValueMissing_ExceptionThrown() {
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
                "key2": "value1"
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(SchemaParserException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(SPRS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_IgnoreUndefinedPropertiesMalformed_ExceptionThrown() {
        var schema =
            """
            %pragma IgnoreUndefinedProperty: true
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": "value1"
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(PragmaNotFoundException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(PRAG01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidUndefinedPropertyValue_ExceptionThrown() {
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
                "key2": "value1"
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(InvalidPragmaValueException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(PRAG02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_IgnorePropertyOrderOfObject_ExceptionThrown() {
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
                "key2": "value1"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(PROP07, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FLOT01, exception.getCode());
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
                "key2": "value1"
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(DuplicatePragmaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(PRAG03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateTypeFormat_ExceptionThrown() {
        var schema =
            """
            %pragma DateDataTypeFormat: "ABCD"
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
        assertEquals(DLEX01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonMismatchWithDateFormat_ExceptionThrown() {
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonMismatchWithTimeFormat_ExceptionThrown() {
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
                "key2": "05-11-2023 23.59.59"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(BFOR01, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(AFTR01, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(BFOR02, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(AFTR02, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DRNG01, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DRNG07, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DRNG05, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DRNG02, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DRNG08, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DRNG06, exception.getCode());
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DSYM01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_SchemaTimeNotValidWithEndRange_ExceptionThrown() {
        var schema =
            """
            %pragma TimeDataTypeFormat: "DD-MM-YYYY hh:mm:ss"
            %schema:
            @range*(!, "31-12-2010 23.59.59") #time* #array
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
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DSYM01, exception.getCode());
        exception.printStackTrace();
    }
}