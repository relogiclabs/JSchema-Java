package com.relogiclabs.json.schema.internal.time;

import com.relogiclabs.json.schema.exception.InvalidDateTimeException;
import com.relogiclabs.json.schema.internal.antlr.DateTimeLexer;
import com.relogiclabs.json.schema.internal.util.DebugUtilities;
import com.relogiclabs.json.schema.internal.util.LexerErrorListener;
import com.relogiclabs.json.schema.internal.util.Reference;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.CLOCK_AM_PM;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.DAY_NUMBER;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.DAY_NUMBER2;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.ERA;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.FRACTION_NUMBER;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.FRACTION_NUMBER1;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.FRACTION_NUMBER2;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.FRACTION_NUMBER3;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.FRACTION_NUMBER4;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.FRACTION_NUMBER5;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.FRACTION_NUMBER6;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.HOUR_NUMBER;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.HOUR_NUMBER2;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.MINUTE_NUMBER;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.MINUTE_NUMBER2;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.MONTH_NAME;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.MONTH_NUMBER;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.MONTH_NUMBER2;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.MONTH_SHORT_NAME;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.SECOND_NUMBER;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.SECOND_NUMBER2;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.SYMBOL;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.TEXT;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.UTC_OFFSET_HOUR;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.UTC_OFFSET_TIME1;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.UTC_OFFSET_TIME2;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.WEEKDAY_NAME;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.WEEKDAY_SHORT_NAME;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.WHITESPACE;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.YEAR_NUMBER2;
import static com.relogiclabs.json.schema.internal.antlr.DateTimeLexer.YEAR_NUMBER4;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.ClockAmPm;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.DayNumber;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.DayNumber2;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.Era;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.FractionNumber;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.FractionNumber1;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.FractionNumber2;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.FractionNumber3;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.FractionNumber4;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.FractionNumber5;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.FractionNumber6;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.HourNumber;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.HourNumber2;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.MinuteNumber;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.MinuteNumber2;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.MonthName;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.MonthNumber;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.MonthNumber2;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.MonthShortName;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.SecondNumber;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.SecondNumber2;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.Symbol;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.Text;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.UtcOffsetHour;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.UtcOffsetTime1;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.UtcOffsetTime2;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.WeekdayName;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.WeekdayShortName;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.Whitespace;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.YearNumber2;
import static com.relogiclabs.json.schema.internal.time.SegmentProcessor.YearNumber4;
import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.message.ErrorCode.DINV02;

public final class DateTimeValidator {
    public static final String ISO_8601_DATE = "YYYY-MM-DD";
    public static final String ISO_8601_TIME = "YYYY-MM-DD'T'hh:mm:ss.FZZ";

    private static final Map<String, SegmentProcessor> PROCESSORS;
    private final DateTimeLexer dateTimeLexer;
    private final List<Token> lexerTokens;

    static {
        PROCESSORS = new HashMap<>(50);
        addProcessor(TEXT, Text);
        addProcessor(SYMBOL, Symbol);
        addProcessor(WHITESPACE, Whitespace);
        addProcessor(ERA, Era);
        addProcessor(YEAR_NUMBER4, YearNumber4);
        addProcessor(YEAR_NUMBER2, YearNumber2);
        addProcessor(MONTH_NAME, MonthName);
        addProcessor(MONTH_SHORT_NAME, MonthShortName);
        addProcessor(MONTH_NUMBER2, MonthNumber2);
        addProcessor(MONTH_NUMBER, MonthNumber);
        addProcessor(WEEKDAY_NAME, WeekdayName);
        addProcessor(WEEKDAY_SHORT_NAME, WeekdayShortName);
        addProcessor(DAY_NUMBER2, DayNumber2);
        addProcessor(DAY_NUMBER, DayNumber);
        addProcessor(CLOCK_AM_PM, ClockAmPm);
        addProcessor(HOUR_NUMBER2, HourNumber2);
        addProcessor(HOUR_NUMBER, HourNumber);
        addProcessor(MINUTE_NUMBER2, MinuteNumber2);
        addProcessor(MINUTE_NUMBER, MinuteNumber);
        addProcessor(SECOND_NUMBER2, SecondNumber2);
        addProcessor(SECOND_NUMBER, SecondNumber);
        addProcessor(FRACTION_NUMBER, FractionNumber);
        addProcessor(FRACTION_NUMBER1, FractionNumber1);
        addProcessor(FRACTION_NUMBER2, FractionNumber2);
        addProcessor(FRACTION_NUMBER3, FractionNumber3);
        addProcessor(FRACTION_NUMBER4, FractionNumber4);
        addProcessor(FRACTION_NUMBER5, FractionNumber5);
        addProcessor(FRACTION_NUMBER6, FractionNumber6);
        addProcessor(UTC_OFFSET_HOUR, UtcOffsetHour);
        addProcessor(UTC_OFFSET_TIME1, UtcOffsetTime1);
        addProcessor(UTC_OFFSET_TIME2, UtcOffsetTime2);
    }

    private static void addProcessor(int index, SegmentProcessor processor) {
        PROCESSORS.put(DateTimeLexer.ruleNames[index - 1], processor);
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
        DebugUtilities.print(context);
    }

    public void ValidateDate(String input) {
        Validate(input, new DateTimeContext(DateTimeType.DATE_TYPE));
    }

    public void ValidateTime(String input) {
        Validate(input, new DateTimeContext(DateTimeType.TIME_TYPE));
    }

    public boolean IsValidDate(String input, Reference<String> error) {
        try {
            ValidateDate(input);
            return true;
        } catch(InvalidDateTimeException e) {
            DebugUtilities.print(e);
            error.setValue(e.getMessage());
            return false;
        }
    }

    public boolean IsValidTime(String input, Reference<String> error) {
        try {
            ValidateTime(input);
            return true;
        } catch(InvalidDateTimeException e) {
            DebugUtilities.print(e);
            error.setValue(e.getMessage());
            return false;
        }
    }
}