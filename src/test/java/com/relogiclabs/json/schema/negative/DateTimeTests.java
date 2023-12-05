package com.relogiclabs.json.schema.negative;

import com.relogiclabs.json.schema.JsonAssert;
import com.relogiclabs.json.schema.JsonSchema;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.json.schema.message.ErrorCode.AFTR01;
import static com.relogiclabs.json.schema.message.ErrorCode.BFOR01;
import static com.relogiclabs.json.schema.message.ErrorCode.DCNF01;
import static com.relogiclabs.json.schema.message.ErrorCode.DDAY03;
import static com.relogiclabs.json.schema.message.ErrorCode.DDAY04;
import static com.relogiclabs.json.schema.message.ErrorCode.DERA01;
import static com.relogiclabs.json.schema.message.ErrorCode.DFRC04;
import static com.relogiclabs.json.schema.message.ErrorCode.DHUR01;
import static com.relogiclabs.json.schema.message.ErrorCode.DHUR02;
import static com.relogiclabs.json.schema.message.ErrorCode.DHUR03;
import static com.relogiclabs.json.schema.message.ErrorCode.DHUR06;
import static com.relogiclabs.json.schema.message.ErrorCode.DINV02;
import static com.relogiclabs.json.schema.message.ErrorCode.DLEX01;
import static com.relogiclabs.json.schema.message.ErrorCode.DMIN01;
import static com.relogiclabs.json.schema.message.ErrorCode.DMIN03;
import static com.relogiclabs.json.schema.message.ErrorCode.DMON01;
import static com.relogiclabs.json.schema.message.ErrorCode.DMON02;
import static com.relogiclabs.json.schema.message.ErrorCode.DMON03;
import static com.relogiclabs.json.schema.message.ErrorCode.DMON05;
import static com.relogiclabs.json.schema.message.ErrorCode.DRNG01;
import static com.relogiclabs.json.schema.message.ErrorCode.DRNG02;
import static com.relogiclabs.json.schema.message.ErrorCode.DSEC01;
import static com.relogiclabs.json.schema.message.ErrorCode.DSEC03;
import static com.relogiclabs.json.schema.message.ErrorCode.DTAP01;
import static com.relogiclabs.json.schema.message.ErrorCode.DTXT01;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP04;
import static com.relogiclabs.json.schema.message.ErrorCode.DUTC01;
import static com.relogiclabs.json.schema.message.ErrorCode.DUTC04;
import static com.relogiclabs.json.schema.message.ErrorCode.DUTC05;
import static com.relogiclabs.json.schema.message.ErrorCode.DWKD02;
import static com.relogiclabs.json.schema.message.ErrorCode.DWKD03;
import static com.relogiclabs.json.schema.message.ErrorCode.DWTS01;
import static com.relogiclabs.json.schema.message.ErrorCode.DYAR01;
import static com.relogiclabs.json.schema.message.ErrorCode.DYAR02;
import static com.relogiclabs.json.schema.message.ErrorCode.DYAR03;
import static com.relogiclabs.json.schema.message.ErrorCode.ENDE01;
import static com.relogiclabs.json.schema.message.ErrorCode.ENDE02;
import static com.relogiclabs.json.schema.message.ErrorCode.STRT01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateTimeTests {
    @Test
    public void When_JsonNotDate_ExceptionThrown() {
        var schema = "#date";
        var json = "\"This is not a date\"";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotValidDate_ExceptionThrown() {
        var schema = "#date";
        var json = "\"1939-02-29\"";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotTime_ExceptionThrown() {
        var schema = "#time";
        var json = "\"This is not a time\"";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotValidTime_ExceptionThrown() {
        var schema = "#time";
        var json = "\"1939-09-02T2:12:12.000Z\"";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DateInputWrong_ExceptionThrown() {
        var schema =
            """
            @date("DD-MM-YY")
            """;
        var json =
            """ 
            "99-09-01"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DDAY04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_TimeInputWrong_ExceptionThrown() {
        var schema =
            """
            @time("hh:mm:ss t")
            """;
        var json =
            """ 
            "13:10:10 PM"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DHUR03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DateDayOutOfRange_ExceptionThrown() {
        var schema =
            """ 
            @date("DD-MM-YYYY")
            """;
        var json =
            """ 
            "29-02-1939"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DDAY03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DateDayOutOfRange2_ExceptionThrown() {
        var schema =
            """ 
            @date("DD-MM-YYYY")
            """;
        var json =
            """ 
            "32-12-1939"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DDAY04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateMonthFullName_ExceptionThrown() {
        var schema =
            """ 
            @date("MMMM DD, YYYY G")
            """;
        var json =
            """ 
            "Septembar 01, 1939 AD"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DMON01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateMonthShortName_ExceptionThrown() {
        var schema =
            """ 
            @date("MMM DD, YYYY G")
            """;
        var json =
            """ 
            "Sap 01, 1939 AD"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DMON02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateMonthNumber_ExceptionThrown() {
        var schema =
            """ 
            @date("MM-DD, YYYY G")
            """;
        var json =
            """ 
            "Sep-01, 1939 AD"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DMON03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateMonthNumberRange_ExceptionThrown() {
        var schema =
            """ 
            @date("MM-DD, YYYY G")
            """;
        var json =
            """ 
            "13-01, 1939 AD"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DMON05, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateWeekdayInput_ExceptionThrown() {
        var schema =
            """ 
            @date("DDD, MMM DD, YYYY G")
            """;
        var json =
            """ 
            "Fry, Sep 01, 1939 AD"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DWKD02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ConflictingDateInfoInInput_ExceptionThrown() {
        var schema =
            """ 
            @date("MMMM, DD-MM-YYYY")
            """;
        var json =
            """ 
            "January, 01-12-1939"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DCNF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ConflictingTimeInfoInInput_ExceptionThrown() {
        var schema =
            """ 
            @time("hh, hh:mm:ss")
            """;
        var json =
            """ 
            "12, 11:10:12"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DCNF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateWeekday_ExceptionThrown() {
        var schema =
            """ 
            @date("DDD, MMM DD, YYYY G")
            """;
        var json =
            """ 
            "Sat, Sep 01, 1939 AD"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DWKD03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateYearInput_ExceptionThrown() {
        var schema =
            """ 
            @date("DD-MM-YY")
            """;
        var json =
            """ 
            "01-09-Twenty"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DYAR02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateYearInput2_ExceptionThrown() {
        var schema =
            """ 
            @date("DD-MM-YYYY")
            """;
        var json =
            """ 
            "01-09-0000"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DYAR03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateYearInput3_ExceptionThrown() {
        var schema =
            """ 
            @date("DD-MM-YY")
            """;
        var json =
            """ 
            "01-09-1939"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DINV02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateEraInput_ExceptionThrown() {
        var schema =
            """ 
            @date("DD-MM-YYYY G")
            """;
        var json =
            """ 
            "02-12-1939 AA"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DERA01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeTextMissing_ExceptionThrown() {
        var schema =
            """ 
            @time("DD-MM-YYYY 'Time' hh:mm:ss")
            """;
        var json =
            """ 
            "01-11-1939 10:00:00"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTXT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeHourInput_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss")
            """;
        var json =
            """ 
            "Twelve:00:00"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DHUR01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeHourRange_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss")
            """;
        var json =
            """ 
            "24:00:00"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DHUR06, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeMinuteInput_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss")
            """;
        var json =
            """ 
            "23:one:00"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DMIN01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeMinuteRange_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss")
            """;
        var json =
            """ 
            "23:60:00"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DMIN03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeSecondInput_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss")
            """;
        var json =
            """ 
            "23:59:Three"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DSEC01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeSecondRange_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss")
            """;
        var json =
            """ 
            "23:59:60"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DSEC03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeSecondFraction_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss.fff")
            """;
        var json =
            """ 
            "23:59:00.11"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DFRC04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeNoHourInput_ExceptionThrown() {
        var schema =
            """ 
            @time("h:m:s")
            """;
        var json =
            """ 
            ":3:8"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DHUR02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeInput_ExceptionThrown() {
        var schema =
            """ 
            @date("hh mm ss")
            """;
        var json =
            """ 
            "01:10:08"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DWTS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeAmPmInput_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss t")
            """;
        var json =
            """ 
            "12:00:00 AD"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTAP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTime12HourInput_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss t")
            """;
        var json =
            """ 
            "13:00:00 AM"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DHUR03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeAmPmMissing_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:sst")
            """;
        var json =
            """ 
            "11:11:11"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTAP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeUTCOffsetInput_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss Z")
            """;
        var json =
            """ 
            "11:00:00 Six"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DUTC01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeUTCOffsetHourRange_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss Z")
            """;
        var json =
            """ 
            "11:00:00 +14"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DUTC04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimeUTCOffsetMinuteRange_ExceptionThrown() {
        var schema =
            """ 
            @time("hh:mm:ss ZZ")
            """;
        var json =
            """ 
            "11:00:00 +10:60"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DUTC05, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDatePatternCauseLexerError_ExceptionThrown() {
        var schema =
            """ 
            @date("ABCD")
            """;
        var json =
            """
            "23-09-01T14:35:10.555"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DLEX01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimePatternCauseLexerError_ExceptionThrown() {
        var schema =
            """ 
            @time("ABCD")
            """;
        var json =
            """
            "23-09-01T14:35:10.555"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DLEX01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonDateNotValidWithBothRange_ExceptionThrown() {
        var schema =
            """
            @range*("2010-01-01", "2010-12-31") #date* #array
            """;
        var json =
            """
            [
                "2010-01-01",
                "2010-02-01",
                "2010-06-30",
                "2009-12-31",
                "2011-01-01"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DRNG01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonTimeNotValidWithBothRange_ExceptionThrown() {
        var schema =
            """
            @range*("2010-01-01T00:00:00.000Z", 
                    "2010-12-31T23:59:59.999Z") 
            #time* #array
            """;
        var json =
            """
            [
                "2010-01-01T00:00:00.000Z",
                "2010-02-01T01:30:45.1Z",
                "2010-06-30T12:01:07.999999Z",
                "2009-12-31T22:39:50.0-04:00",
                "2011-01-01T02:10:00.0+06:00",
                "2009-12-31T22:59:59.000Z",
                "2011-01-01T00:00:00.000Z"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DRNG02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonDateNotValidWithStart_ExceptionThrown() {
        var schema =
            """
            @start*("2010-01-01") #date* #array
            """;
        var json =
            """
            [
                "2010-01-01",
                "2010-02-01",
                "2011-06-30",
                "2050-11-01",
                "2009-12-31"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(STRT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonDateNotValidWithEnd_ExceptionThrown() {
        var schema =
            """
            @end*("2010-12-31") #date* #array
            """;
        var json =
            """
            [
                "1930-01-01",
                "2000-02-01",
                "2005-06-30",
                "2010-12-31",
                "2011-01-01"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ENDE01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonDateNotValidWithBefore_ExceptionThrown() {
        var schema =
            """
            @before*("2011-01-01") #date* #array
            """;
        var json =
            """
            [
                "2010-01-01",
                "2010-06-30",
                "2010-12-31",
                "2011-01-01",
                "2023-11-15"
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
            @after*("2009-12-31") #date* #array
            """;
        var json =
            """
            [
                "2010-01-01",
                "2010-02-10",
                "2010-12-31",
                "2009-12-31",
                "1980-11-19"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(AFTR01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonTimeNotValidWithEndInFraction_ExceptionThrown() {
        var schema =
            """
            @end*("1939-09-02T02:12:12.555Z") #time* #array
            """;
        var json =
            """
            [
                "1939-09-02T02:12:12.554Z",
                "1939-09-02T02:12:12.555Z",
                "1939-09-02T02:12:12.556Z"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ENDE02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_SchemaDateNotValidWithBefore_ExceptionThrown() {
        var schema =
            """
            @before*("01-01-2011") #date* #array
            """;
        var json =
            """
            [
                "1900-01-01",
                "2010-06-30",
                "2010-12-31"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DYAR01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_SchemaDateNotValidWithAfter_ExceptionThrown() {
        var schema =
            """
            @after*("12-31-2009") #date* #array
            """;
        var json =
            """
            [
                "2010-01-01",
                "2010-02-10",
                "2050-12-31"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DYAR01, exception.getCode());
        exception.printStackTrace();
    }
}