package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.DataTypeValidationException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.AFTRDT01;
import static com.relogiclabs.jschema.message.ErrorCode.AMPMDT01;
import static com.relogiclabs.jschema.message.ErrorCode.BFORDT01;
import static com.relogiclabs.jschema.message.ErrorCode.CNFLDT01;
import static com.relogiclabs.jschema.message.ErrorCode.DAYRNG01;
import static com.relogiclabs.jschema.message.ErrorCode.DAYRNG02;
import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS01;
import static com.relogiclabs.jschema.message.ErrorCode.ENDFDT01;
import static com.relogiclabs.jschema.message.ErrorCode.ENDFDT02;
import static com.relogiclabs.jschema.message.ErrorCode.ERANAM01;
import static com.relogiclabs.jschema.message.ErrorCode.FRACDT04;
import static com.relogiclabs.jschema.message.ErrorCode.HURNUM01;
import static com.relogiclabs.jschema.message.ErrorCode.HURNUM02;
import static com.relogiclabs.jschema.message.ErrorCode.HURRNG01;
import static com.relogiclabs.jschema.message.ErrorCode.HURRNG03;
import static com.relogiclabs.jschema.message.ErrorCode.INVLDT01;
import static com.relogiclabs.jschema.message.ErrorCode.LEXRDT01;
import static com.relogiclabs.jschema.message.ErrorCode.MNTNUM01;
import static com.relogiclabs.jschema.message.ErrorCode.MNTRNG01;
import static com.relogiclabs.jschema.message.ErrorCode.MONNAM01;
import static com.relogiclabs.jschema.message.ErrorCode.MONNAM02;
import static com.relogiclabs.jschema.message.ErrorCode.MONNUM01;
import static com.relogiclabs.jschema.message.ErrorCode.MONRNG01;
import static com.relogiclabs.jschema.message.ErrorCode.RNDEND02;
import static com.relogiclabs.jschema.message.ErrorCode.RNDSTA01;
import static com.relogiclabs.jschema.message.ErrorCode.SECNUM01;
import static com.relogiclabs.jschema.message.ErrorCode.SECRNG01;
import static com.relogiclabs.jschema.message.ErrorCode.STRTDT01;
import static com.relogiclabs.jschema.message.ErrorCode.TEXTDT01;
import static com.relogiclabs.jschema.message.ErrorCode.UTCRNG01;
import static com.relogiclabs.jschema.message.ErrorCode.UTCRNG02;
import static com.relogiclabs.jschema.message.ErrorCode.UTCTIM01;
import static com.relogiclabs.jschema.message.ErrorCode.WEKDMS01;
import static com.relogiclabs.jschema.message.ErrorCode.WEKNAM02;
import static com.relogiclabs.jschema.message.ErrorCode.WSPACE01;
import static com.relogiclabs.jschema.message.ErrorCode.YARNUM01;
import static com.relogiclabs.jschema.message.ErrorCode.YARNUM02;
import static com.relogiclabs.jschema.message.ErrorCode.YARRNG01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateTimeTests {
    @Test
    public void When_JsonNotValidDate_ExceptionThrown() {
        var schema =
            """
            [ #date, #date, #date ]
            """;
        var json =
            """
            [
                "This is a string",
                "1939-13-10",
                "1939-02-29"
            ]
            """;

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotValidTime_ExceptionThrown() {
        var schema =
            """
            [ #time, #time, #time ]
            """;
        var json =
            """
            [
                "This is a string",
                "1939-09-02T24:02:02.000Z",
                "1939-09-20T02:60:09.000Z"
            ]
            """;

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DAYRNG01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(HURRNG01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DAYRNG02, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DAYRNG01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(MONNAM01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(MONNAM02, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(MONNUM01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(MONRNG01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(WEKNAM02, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(CNFLDT01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(CNFLDT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDateWeekdayMismatched_ExceptionThrown() {
        var schema =
            """
            @date("DDD, MMM DD, YYYY G")
            """;
        var json =
            """
            "Sat, Sep 01, 1939 AD"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(WEKDMS01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(YARNUM02, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(YARRNG01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(INVLDT01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ERANAM01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(TEXTDT01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(HURNUM01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(HURRNG03, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(MNTNUM01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(MNTRNG01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(SECNUM01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(SECRNG01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FRACDT04, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(HURNUM02, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(WSPACE01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(AMPMDT01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(HURRNG01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(AMPMDT01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(UTCTIM01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(UTCRNG01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(UTCRNG02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidDatePatternFormat_ExceptionThrown() {
        var schema =
            """
            @date("TEST")
            """;
        var json =
            """
            "23-09-01T14:35:10.555"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LEXRDT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidTimePatternFormat_ExceptionThrown() {
        var schema =
            """
            @time("TEST")
            """;
        var json =
            """
            "23-09-01T14:35:10.555"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LEXRDT01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RNDSTA01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonTimeNotValidWithBothRange_ExceptionThrown() {
        var schema =
            """
            @range*("2010-01-01T00:00:00.000Z",
                    "2010-12-31T23:59:59.999Z")
            #time* #array
            // range also consider UTC offset if given
            """;
        var json =
            """
            [
                "2010-01-01T00:00:00.000Z",
                "2010-02-01T01:30:45.1Z",
                "2010-06-30T12:01:07.999999Z",
                "2009-12-31T22:39:50.0-04:00",
                "2011-01-01T02:10:00.0+06:00",
                "2011-01-01T00:00:00.000Z",
                "2009-12-31T22:59:59.000Z"
            ]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RNDEND02, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(STRTDT01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ENDFDT01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(BFORDT01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(AFTRDT01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ENDFDT02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_SchemaDateNotValidFormatWithBefore_ExceptionThrown() {
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(YARNUM01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_SchemaDateNotValidFormatWithAfter_ExceptionThrown() {
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(YARNUM01, exception.getCode());
        exception.printStackTrace();
    }
}