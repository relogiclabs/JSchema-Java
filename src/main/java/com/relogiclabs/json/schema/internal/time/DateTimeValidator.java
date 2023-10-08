package com.relogiclabs.json.schema.internal.time;

import com.relogiclabs.json.schema.exception.InvalidDateTimeException;
import com.relogiclabs.json.schema.internal.antlr.DateTimeLexer;
import com.relogiclabs.json.schema.internal.util.DebugUtils;
import com.relogiclabs.json.schema.internal.util.LexerErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.message.ErrorCode.DINV02;

public class DateTimeValidator {
    public static final String ISO_8601_DATE = "YYYY-MM-DD";
    public static final String ISO_8601_TIME = "YYYY-MM-DD'T'hh:mm:ss.fffZZ";

    private static final Map<String, SegmentProcessor> PROCESSORS;
    private final DateTimeLexer dateTimeLexer;
    private final List<Token> lexerTokens;

    static {
        PROCESSORS = new HashMap<>(50);
        PROCESSORS.put("TEXT", SegmentProcessor.Text);
        PROCESSORS.put("SYMBOL", SegmentProcessor.Symbol);
        PROCESSORS.put("WHITESPACE", SegmentProcessor.Whitespace);
        PROCESSORS.put("ERA", SegmentProcessor.Era);
        PROCESSORS.put("YEAR_NUM4", SegmentProcessor.YearNum4);
        PROCESSORS.put("YEAR_NUM2", SegmentProcessor.YearNum2);
        PROCESSORS.put("MONTH_NAME", SegmentProcessor.MonthName);
        PROCESSORS.put("MONTH_SHORT_NAME", SegmentProcessor.MonthShortName);
        PROCESSORS.put("MONTH_NUM2", SegmentProcessor.MonthNum2);
        PROCESSORS.put("MONTH_NUM", SegmentProcessor.MonthNum);
        PROCESSORS.put("WEEKDAY_NAME", SegmentProcessor.WeekdayName);
        PROCESSORS.put("WEEKDAY_SHORT_NAME", SegmentProcessor.WeekdayShortName);
        PROCESSORS.put("DAY_NUM2", SegmentProcessor.DayNum2);
        PROCESSORS.put("DAY_NUM", SegmentProcessor.DayNum);
        PROCESSORS.put("AM_PM", SegmentProcessor.AmPm);
        PROCESSORS.put("HOUR_NUM2", SegmentProcessor.HourNum2);
        PROCESSORS.put("HOUR_NUM", SegmentProcessor.HourNum);
        PROCESSORS.put("MINUTE_NUM2", SegmentProcessor.MinuteNum2);
        PROCESSORS.put("MINUTE_NUM", SegmentProcessor.MinuteNum);
        PROCESSORS.put("SECOND_NUM2", SegmentProcessor.SecondNum2);
        PROCESSORS.put("SECOND_NUM", SegmentProcessor.SecondNum);
        PROCESSORS.put("FRACTION_NUM", SegmentProcessor.FractionNum);
        PROCESSORS.put("FRACTION_NUM01", SegmentProcessor.FractionNum01);
        PROCESSORS.put("FRACTION_NUM02", SegmentProcessor.FractionNum02);
        PROCESSORS.put("FRACTION_NUM03", SegmentProcessor.FractionNum03);
        PROCESSORS.put("FRACTION_NUM04", SegmentProcessor.FractionNum04);
        PROCESSORS.put("FRACTION_NUM05", SegmentProcessor.FractionNum05);
        PROCESSORS.put("FRACTION_NUM06", SegmentProcessor.FractionNum06);
        PROCESSORS.put("UTC_OFFSET_HOUR", SegmentProcessor.UtcOffsetHour);
        PROCESSORS.put("UTC_OFFSET_TIME1", SegmentProcessor.UtcOffsetTime1);
        PROCESSORS.put("UTC_OFFSET_TIME2", SegmentProcessor.UtcOffsetTime2);
    }

    @SuppressWarnings("unchecked")
    public DateTimeValidator(String pattern) {
        dateTimeLexer = new DateTimeLexer(CharStreams.fromString(pattern));
        dateTimeLexer.removeErrorListeners();
        dateTimeLexer.addErrorListener(LexerErrorListener.DATE_TIME);
        lexerTokens = (List<Token>) dateTimeLexer.getAllTokens();
    }

    private void Validate(String input, DateTimeContext context) {
        for(var token : lexerTokens) {
            var processor = PROCESSORS.get(dateTimeLexer.getVocabulary().getSymbolicName(token.getType()));
            input = processor.process(input, token, context);
        }
        if(input.length() != 0) throw new InvalidDateTimeException(DINV02,
                concat("Invalid ", context.getType(), " input format"));

        context.validate();
        DebugUtils.print(context);
    }

    public void ValidateDate(String input) {
        Validate(input, new DateTimeContext(DateTimeType.DATE_TYPE));
    }

    public void ValidateTime(String input) {
        Validate(input, new DateTimeContext(DateTimeType.TIME_TYPE));
    }

    public boolean IsValidDate(String input) {
        try {
            ValidateDate(input);
            return true;
        } catch(InvalidDateTimeException e) {
            DebugUtils.print(e);
            return false;
        }
    }

    public boolean IsValidTime(String input) {
        try {
            ValidateTime(input);
            return true;
        } catch(InvalidDateTimeException e) {
            DebugUtils.print(e);
            return false;
        }
    }
}