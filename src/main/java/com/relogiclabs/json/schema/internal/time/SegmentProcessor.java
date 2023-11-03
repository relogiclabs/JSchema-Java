package com.relogiclabs.json.schema.internal.time;

import com.relogiclabs.json.schema.exception.InvalidDateTimeException;
import org.antlr.v4.runtime.Token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.message.ErrorCode.DDAY01;
import static com.relogiclabs.json.schema.message.ErrorCode.DDAY02;
import static com.relogiclabs.json.schema.message.ErrorCode.DERA01;
import static com.relogiclabs.json.schema.message.ErrorCode.DFRC01;
import static com.relogiclabs.json.schema.message.ErrorCode.DFRC02;
import static com.relogiclabs.json.schema.message.ErrorCode.DFRC03;
import static com.relogiclabs.json.schema.message.ErrorCode.DFRC04;
import static com.relogiclabs.json.schema.message.ErrorCode.DFRC05;
import static com.relogiclabs.json.schema.message.ErrorCode.DFRC06;
import static com.relogiclabs.json.schema.message.ErrorCode.DFRC07;
import static com.relogiclabs.json.schema.message.ErrorCode.DHUR01;
import static com.relogiclabs.json.schema.message.ErrorCode.DHUR02;
import static com.relogiclabs.json.schema.message.ErrorCode.DMIN01;
import static com.relogiclabs.json.schema.message.ErrorCode.DMIN02;
import static com.relogiclabs.json.schema.message.ErrorCode.DMON01;
import static com.relogiclabs.json.schema.message.ErrorCode.DMON02;
import static com.relogiclabs.json.schema.message.ErrorCode.DMON03;
import static com.relogiclabs.json.schema.message.ErrorCode.DMON04;
import static com.relogiclabs.json.schema.message.ErrorCode.DSEC01;
import static com.relogiclabs.json.schema.message.ErrorCode.DSEC02;
import static com.relogiclabs.json.schema.message.ErrorCode.DSYM01;
import static com.relogiclabs.json.schema.message.ErrorCode.DTAP01;
import static com.relogiclabs.json.schema.message.ErrorCode.DTXT01;
import static com.relogiclabs.json.schema.message.ErrorCode.DUTC01;
import static com.relogiclabs.json.schema.message.ErrorCode.DUTC02;
import static com.relogiclabs.json.schema.message.ErrorCode.DUTC03;
import static com.relogiclabs.json.schema.message.ErrorCode.DWKD01;
import static com.relogiclabs.json.schema.message.ErrorCode.DWKD02;
import static com.relogiclabs.json.schema.message.ErrorCode.DWTS01;
import static com.relogiclabs.json.schema.message.ErrorCode.DYAR01;
import static com.relogiclabs.json.schema.message.ErrorCode.DYAR02;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.replaceChars;
import static org.apache.commons.lang3.StringUtils.substring;

abstract class SegmentProcessor {

    private static final Pattern EraRegex = compile("^(BC|AD)", CASE_INSENSITIVE);
    private static final Pattern YearNumber4Regex = compile("^(\\d{4})");
    private static final Pattern YearNumber2Regex = compile("^(\\d{2})");
    private static final Pattern MonthNameRegex = compile(
            "^(January|February|March|April|May|June|July" +
            "|August|September|October|November|December)",
            CASE_INSENSITIVE);
    private static final Pattern MonthShortNameRegex = compile(
            "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)",
            CASE_INSENSITIVE);
    private static final Pattern MonthNumber2Regex = compile("^(\\d{2})");
    private static final Pattern MonthNumberRegex = compile("^(\\d{1,2})");
    private static final Pattern WeekdayNameRegex = compile(
            "^(Sunday|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday)",
            CASE_INSENSITIVE);
    private static final Pattern WeekdayShortNameRegex = compile(
            "^(Sun|Mon|Tue|Wed|Thu|Fri|Sat)", CASE_INSENSITIVE);
    private static final Pattern DayNumber2Regex = compile("^(\\d{2})");
    private static final Pattern DayNumberRegex = compile("^(\\d{1,2})");
    private static final Pattern ClockAmPmRegex = compile("^(AM|PM)", CASE_INSENSITIVE);
    private static final Pattern HourNumber2Regex = compile("^(\\d{2})");
    private static final Pattern HourNumberRegex = compile("^(\\d{1,2})");
    private static final Pattern MinuteNumber2Regex = compile("^(\\d{2})");
    private static final Pattern MinuteNumberRegex = compile("^(\\d{1,2})");
    private static final Pattern SecondNumber2Regex = compile("^(\\d{2})");
    private static final Pattern SecondNumberRegex = compile("^(\\d{1,2})");
    private static final Pattern FractionNumberRegex = compile("^(\\d{1,6})");
    private static final Pattern FractionNumber1Regex = compile("^(\\d)");
    private static final Pattern FractionNumber2Regex = compile("^(\\d{2})");
    private static final Pattern FractionNumber3Regex = compile("^(\\d{3})");
    private static final Pattern FractionNumber4Regex = compile("^(\\d{4})");
    private static final Pattern FractionNumber5Regex = compile("^(\\d{5})");
    private static final Pattern FractionNumber6Regex = compile("^(\\d{6})");
    private static final Pattern UtcOffsetHourRegex = compile("^([+-]\\d{2}|Z)");
    private static final Pattern UtcOffsetTime1Regex = compile("^([+-]\\d{2}):(\\d{2})|Z");
    private static final Pattern UtcOffsetTime2Regex = compile("^([+-]\\d{2})(\\d{2})|Z");


    public static final SegmentProcessor Text = new TextProcessor();
    public static final SegmentProcessor Symbol = new SymbolProcessor();
    public static final SegmentProcessor Whitespace = new WhitespaceProcessor();

    public static final SegmentProcessor Era = new EraProcessor(EraRegex, DERA01);
    public static final SegmentProcessor YearNumber4 = new YearNumberProcessor(YearNumber4Regex, DYAR01);
    public static final SegmentProcessor YearNumber2 = new YearNumberProcessor(YearNumber2Regex, DYAR02);

    public static final SegmentProcessor MonthName = new MonthNameProcessor(MonthNameRegex, DMON01);
    public static final SegmentProcessor MonthShortName = new MonthNameProcessor(MonthShortNameRegex, DMON02);
    public static final SegmentProcessor MonthNumber2 = new MonthNumberProcessor(MonthNumber2Regex, DMON03);
    public static final SegmentProcessor MonthNumber = new MonthNumberProcessor(MonthNumberRegex, DMON04);

    public static final SegmentProcessor WeekdayName = new WeekdayProcessor(WeekdayNameRegex, DWKD01);
    public static final SegmentProcessor WeekdayShortName = new WeekdayProcessor(WeekdayShortNameRegex, DWKD02);
    public static final SegmentProcessor DayNumber2 = new DayNumberProcessor(DayNumber2Regex, DDAY01);
    public static final SegmentProcessor DayNumber = new DayNumberProcessor(DayNumberRegex, DDAY02);

    public static final SegmentProcessor ClockAmPm = new ClockAmPmProcessor(ClockAmPmRegex, DTAP01);
    public static final SegmentProcessor HourNumber2 = new HourNumberProcessor(HourNumber2Regex, DHUR01);
    public static final SegmentProcessor HourNumber = new HourNumberProcessor(HourNumberRegex, DHUR02);

    public static final SegmentProcessor MinuteNumber2 = new MinuteNumberProcessor(MinuteNumber2Regex, DMIN01);
    public static final SegmentProcessor MinuteNumber = new MinuteNumberProcessor(MinuteNumberRegex, DMIN02);

    public static final SegmentProcessor SecondNumber2 = new SecondNumberProcessor(SecondNumber2Regex, DSEC01);
    public static final SegmentProcessor SecondNumber = new SecondNumberProcessor(SecondNumberRegex, DSEC02);

    public static final SegmentProcessor FractionNumber = new FractionNumberProcessor(FractionNumberRegex, DFRC01);
    public static final SegmentProcessor FractionNumber1 = new FractionNumberProcessor(FractionNumber1Regex, DFRC02);
    public static final SegmentProcessor FractionNumber2 = new FractionNumberProcessor(FractionNumber2Regex, DFRC03);
    public static final SegmentProcessor FractionNumber3 = new FractionNumberProcessor(FractionNumber3Regex, DFRC04);
    public static final SegmentProcessor FractionNumber4 = new FractionNumberProcessor(FractionNumber4Regex, DFRC05);
    public static final SegmentProcessor FractionNumber5 = new FractionNumberProcessor(FractionNumber5Regex, DFRC06);
    public static final SegmentProcessor FractionNumber6 = new FractionNumberProcessor(FractionNumber6Regex, DFRC07);

    public static final SegmentProcessor UtcOffsetHour = new UtcOffsetHourProcessor(UtcOffsetHourRegex, DUTC01);
    public static final SegmentProcessor UtcOffsetTime1 = new UtcOffsetTimeProcessor(UtcOffsetTime1Regex, DUTC02);
    public static final SegmentProcessor UtcOffsetTime2 = new UtcOffsetTimeProcessor(UtcOffsetTime2Regex, DUTC03);


    public abstract String process(String input, Token token, DateTimeContext context);

    private static final class TextProcessor extends SegmentProcessor {
        @Override
        public String process(String input, Token token, DateTimeContext context) {
            var text = replaceChars(substring(token.getText(), 1, -1), "''", "'");
            if(!input.startsWith(text)) throw new InvalidDateTimeException(DTXT01,
                    concat("Invalid ", context.getType(), " text mismatch or input format"));
            return input.substring(text.length());
        }
    }

    private static final class SymbolProcessor extends SegmentProcessor {
        @Override
        public String process(String input, Token token, DateTimeContext context) {
            if(!input.startsWith(token.getText())) throw new InvalidDateTimeException(DSYM01,
                    concat("Invalid ", context.getType(), " symbol mismatch or input format"));
            return input.substring(token.getText().length());
        }
    }

    private static final class WhitespaceProcessor extends SegmentProcessor {
        @Override
        public String process(String input, Token token, DateTimeContext context) {
            if(!input.startsWith(token.getText())) throw new InvalidDateTimeException(DWTS01,
                    concat("Invalid ", context.getType(), " whitespace mismatch or input format"));
            return input.substring(token.getText().length());
        }
    }

    private abstract static class SegmentRegexProcessor extends SegmentProcessor {
        protected final String code;
        private final Pattern regex;

        protected SegmentRegexProcessor(Pattern regex, String code) {
            this.regex = regex;
            this.code = code;
        }

        @Override
        public String process(String input, Token token, DateTimeContext context) {
            var matcher = regex.matcher(input);
            process(matcher, context);
            return input.substring(matcher.end() - matcher.start());
        }

        protected abstract void process(Matcher matcher, DateTimeContext context);
    }

    private static final class EraProcessor extends SegmentRegexProcessor {
        private EraProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " era input"));
            context.setEra(matcher.group(1));
        }
    }

    private static final class YearNumberProcessor extends SegmentRegexProcessor {
        private YearNumberProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " year input"));
            var year = matcher.group(1);
            context.setYear(Integer.parseInt(year), year.length());
        }
    }

    private static final class MonthNameProcessor extends SegmentRegexProcessor {
        private MonthNameProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " month name input"));
            context.setMonth(matcher.group(1));
        }
    }

    private static final class MonthNumberProcessor extends SegmentRegexProcessor {
        private MonthNumberProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " month number input"));
            context.setMonth(Integer.parseInt(matcher.group(1)));
        }
    }

    private static final class WeekdayProcessor extends SegmentRegexProcessor {
        private WeekdayProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " weekday input"));
            context.setWeekday(matcher.group(1));
        }
    }

    private static final class DayNumberProcessor extends SegmentRegexProcessor {
        private DayNumberProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " day input"));
            context.setDay(Integer.parseInt(matcher.group(1)));
        }
    }

    private static final class ClockAmPmProcessor extends SegmentRegexProcessor {
        private ClockAmPmProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " AM/PM input"));
            context.setAmPm(matcher.group(1));
        }
    }

    private static final class HourNumberProcessor extends SegmentRegexProcessor {
        private HourNumberProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " hour input"));
            context.setHour(Integer.parseInt(matcher.group(1)));
        }
    }

    private static final class MinuteNumberProcessor extends SegmentRegexProcessor {
        private MinuteNumberProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " minute input"));
            context.setMinute(Integer.parseInt(matcher.group(1)));
        }
    }

    private static final class SecondNumberProcessor extends SegmentRegexProcessor {
        private SecondNumberProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " second input"));
            context.setSecond(Integer.parseInt(matcher.group(1)));
        }
    }

    private static final class FractionNumberProcessor extends SegmentRegexProcessor {
        private FractionNumberProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " second faction input"));
            context.setFraction(Integer.parseInt(matcher.group(1)));
        }
    }

    private static final class UtcOffsetHourProcessor extends SegmentRegexProcessor {
        private UtcOffsetHourProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " UTC offset hour input"));
            context.setUtcOffset("Z".equals(matcher.group())
                    ? 0 : Integer.parseInt(matcher.group(1)), 0);
        }
    }

    private static final class UtcOffsetTimeProcessor extends SegmentRegexProcessor {
        private UtcOffsetTimeProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " UTC offset input"));
            if("Z".equals(matcher.group())) context.setUtcOffset(0, 0);
            else context.setUtcOffset(Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)));
        }
    }
}