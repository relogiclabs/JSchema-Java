package com.relogiclabs.json.schema.time;

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

public abstract class SegmentProcessor {

    private static final Pattern EraRegex = compile("^(BC|AD)", CASE_INSENSITIVE);
    private static final Pattern YearNum4Regex = compile("^(\\d{4})");
    private static final Pattern YearNum2Regex = compile("^(\\d{2})");
    private static final Pattern MonthNameRegex = compile(
            "^(January|February|March|April|May|June|July" +
            "|August|September|October|November|December)",
            CASE_INSENSITIVE);
    private static final Pattern MonthShortNameRegex = compile(
            "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)",
            CASE_INSENSITIVE);
    private static final Pattern MonthNum2Regex = compile("^(\\d{2})");
    private static final Pattern MonthNumRegex = compile("^(\\d{1,2})");
    private static final Pattern WeekdayNameRegex = compile(
            "^(Sunday|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday)",
            CASE_INSENSITIVE);
    private static final Pattern WeekdayShortNameRegex = compile(
            "^(Sun|Mon|Tue|Wed|Thu|Fri|Sat)", CASE_INSENSITIVE);
    private static final Pattern DayNum2Regex = compile("^(\\d{2})");
    private static final Pattern DayNumRegex = compile("^(\\d{1,2})");
    private static final Pattern AmPmRegex = compile("^(AM|PM)", CASE_INSENSITIVE);
    private static final Pattern HourNum2Regex = compile("^(\\d{2})");
    private static final Pattern HourNumRegex = compile("^(\\d{1,2})");
    private static final Pattern MinuteNum2Regex = compile("^(\\d{2})");
    private static final Pattern MinuteNumRegex = compile("^(\\d{1,2})");
    private static final Pattern SecondNum2Regex = compile("^(\\d{2})");
    private static final Pattern SecondNumRegex = compile("^(\\d{1,2})");
    private static final Pattern FractionNumRegex = compile("^(\\d{1,6})");
    private static final Pattern FractionNum1Regex = compile("^(\\d)");
    private static final Pattern FractionNum2Regex = compile("^(\\d{2})");
    private static final Pattern FractionNum3Regex = compile("^(\\d{3})");
    private static final Pattern FractionNum4Regex = compile("^(\\d{4})");
    private static final Pattern FractionNum5Regex = compile("^(\\d{5})");
    private static final Pattern FractionNum6Regex = compile("^(\\d{6})");
    private static final Pattern UtcOffsetHourRegex = compile("^([+-]\\d{2}|Z)");
    private static final Pattern UtcOffsetTime1Regex = compile("^([+-]\\d{2}):(\\d{2})|Z");
    private static final Pattern UtcOffsetTime2Regex = compile("^([+-]\\d{2})(\\d{2})|Z");


    public static final SegmentProcessor Text = new TextProcessor();
    public static final SegmentProcessor Symbol = new SymbolProcessor();
    public static final SegmentProcessor Whitespace = new WhitespaceProcessor();

    public static final SegmentProcessor Era = new EraProcessor(EraRegex, DERA01);
    public static final SegmentProcessor YearNum4 = new YearNumProcessor(YearNum4Regex, DYAR01);
    public static final SegmentProcessor YearNum2 = new YearNumProcessor(YearNum2Regex, DYAR02);

    public static final SegmentProcessor MonthName = new MonthNameProcessor(MonthNameRegex, DMON01);
    public static final SegmentProcessor MonthShortName = new MonthNameProcessor(MonthShortNameRegex, DMON02);
    public static final SegmentProcessor MonthNum2 = new MonthNumProcessor(MonthNum2Regex, DMON03);
    public static final SegmentProcessor MonthNum = new MonthNumProcessor(MonthNumRegex, DMON04);

    public static final SegmentProcessor WeekdayName = new WeekdayProcessor(WeekdayNameRegex, DWKD01);
    public static final SegmentProcessor WeekdayShortName = new WeekdayProcessor(WeekdayShortNameRegex, DWKD02);
    public static final SegmentProcessor DayNum2 = new DayNumProcessor(DayNum2Regex, DDAY01);
    public static final SegmentProcessor DayNum = new DayNumProcessor(DayNumRegex, DDAY02);

    public static final SegmentProcessor AmPm = new AmPmProcessor(AmPmRegex, DTAP01);
    public static final SegmentProcessor HourNum2 = new HourNumProcessor(HourNum2Regex, DHUR01);
    public static final SegmentProcessor HourNum = new HourNumProcessor(HourNumRegex, DHUR02);

    public static final SegmentProcessor MinuteNum2 = new MinuteNumProcessor(MinuteNum2Regex, DMIN01);
    public static final SegmentProcessor MinuteNum = new MinuteNumProcessor(MinuteNumRegex, DMIN02);

    public static final SegmentProcessor SecondNum2 = new SecondNumProcessor(SecondNum2Regex, DSEC01);
    public static final SegmentProcessor SecondNum = new SecondNumProcessor(SecondNumRegex, DSEC02);

    public static final SegmentProcessor FractionNum = new FractionNumProcessor(FractionNumRegex, DFRC01);
    public static final SegmentProcessor FractionNum01 = new FractionNumProcessor(FractionNum1Regex, DFRC02);
    public static final SegmentProcessor FractionNum02 = new FractionNumProcessor(FractionNum2Regex, DFRC03);
    public static final SegmentProcessor FractionNum03 = new FractionNumProcessor(FractionNum3Regex, DFRC04);
    public static final SegmentProcessor FractionNum04 = new FractionNumProcessor(FractionNum4Regex, DFRC05);
    public static final SegmentProcessor FractionNum05 = new FractionNumProcessor(FractionNum5Regex, DFRC06);
    public static final SegmentProcessor FractionNum06 = new FractionNumProcessor(FractionNum6Regex, DFRC07);

    public static final SegmentProcessor UtcOffsetHour = new UtcOffsetHourProcessor(UtcOffsetHourRegex, DUTC01);
    public static final SegmentProcessor UtcOffsetTime1 = new UtcOffsetTimeProcessor(UtcOffsetTime1Regex, DUTC02);
    public static final SegmentProcessor UtcOffsetTime2 = new UtcOffsetTimeProcessor(UtcOffsetTime2Regex, DUTC03);


    public abstract String process(String input, Token token, DateTimeContext context);

    private static class TextProcessor extends SegmentProcessor {
        @Override
        public String process(String input, Token token, DateTimeContext context) {
            var text = replaceChars(substring(token.getText(), 1, -1), "''", "'");
            if(!input.startsWith(text)) throw new InvalidDateTimeException(DTXT01,
                    concat("Invalid ", context.getType(), " text mismatch or input format"));
            return input.substring(text.length());
        }
    }

    private static class SymbolProcessor extends SegmentProcessor {
        @Override
        public String process(String input, Token token, DateTimeContext context) {
            if(!input.startsWith(token.getText())) throw new InvalidDateTimeException(DSYM01,
                    concat("Invalid ", context.getType(), " symbol mismatch or input format"));
            return input.substring(token.getText().length());
        }
    }

    private static class WhitespaceProcessor extends SegmentProcessor {
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

    private static class EraProcessor extends SegmentRegexProcessor {
        protected EraProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " era input"));
            context.setEra(matcher.group(1));
        }
    }

    private static class YearNumProcessor extends SegmentRegexProcessor {
        protected YearNumProcessor(Pattern regex, String code) {
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

    private static class MonthNameProcessor extends SegmentRegexProcessor {
        protected MonthNameProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " month name input"));
            context.setMonth(matcher.group(1));
        }
    }

    private static class MonthNumProcessor extends SegmentRegexProcessor {
        protected MonthNumProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " month number input"));
            context.setMonth(Integer.parseInt(matcher.group(1)));
        }
    }

    private static class WeekdayProcessor extends SegmentRegexProcessor {
        protected WeekdayProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " weekday input"));
            context.setWeekday(matcher.group(1));
        }
    }

    private static class DayNumProcessor extends SegmentRegexProcessor {
        protected DayNumProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " day input"));
            context.setDay(Integer.parseInt(matcher.group(1)));
        }
    }

    private static class AmPmProcessor extends SegmentRegexProcessor {
        protected AmPmProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " AM/PM input"));
            context.setAmPm(matcher.group(1));
        }
    }

    private static class HourNumProcessor extends SegmentRegexProcessor {
        protected HourNumProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " hour input"));
            context.setHour(Integer.parseInt(matcher.group(1)));
        }
    }

    private static class MinuteNumProcessor extends SegmentRegexProcessor {
        protected MinuteNumProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " minute input"));
            context.setMinute(Integer.parseInt(matcher.group(1)));
        }
    }

    private static class SecondNumProcessor extends SegmentRegexProcessor {
        protected SecondNumProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " second input"));
            context.setSecond(Integer.parseInt(matcher.group(1)));
        }
    }

    private static class FractionNumProcessor extends SegmentRegexProcessor {
        protected FractionNumProcessor(Pattern regex, String code) {
            super(regex, code);
        }

        @Override
        protected void process(Matcher matcher, DateTimeContext context) {
            if(!matcher.lookingAt()) throw new InvalidDateTimeException(code,
                    concat("Invalid ", context.getType(), " second faction input"));
            context.setFraction(Integer.parseInt(matcher.group(1)));
        }
    }

    private static class UtcOffsetHourProcessor extends SegmentRegexProcessor {
        protected UtcOffsetHourProcessor(Pattern regex, String code) {
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

    private static class UtcOffsetTimeProcessor extends SegmentRegexProcessor {
        protected UtcOffsetTimeProcessor(Pattern regex, String code) {
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