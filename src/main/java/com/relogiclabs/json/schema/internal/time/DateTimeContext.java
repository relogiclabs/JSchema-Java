package com.relogiclabs.json.schema.internal.time;

import com.relogiclabs.json.schema.exception.InvalidDateTimeException;
import com.relogiclabs.json.schema.time.DateTimeType;
import com.relogiclabs.json.schema.time.JsonDateTime;
import com.relogiclabs.json.schema.time.JsonUtcOffset;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.message.ErrorCode.DCNF01;
import static com.relogiclabs.json.schema.message.ErrorCode.DDAY03;
import static com.relogiclabs.json.schema.message.ErrorCode.DDAY04;
import static com.relogiclabs.json.schema.message.ErrorCode.DERA02;
import static com.relogiclabs.json.schema.message.ErrorCode.DHUR03;
import static com.relogiclabs.json.schema.message.ErrorCode.DHUR04;
import static com.relogiclabs.json.schema.message.ErrorCode.DHUR05;
import static com.relogiclabs.json.schema.message.ErrorCode.DHUR06;
import static com.relogiclabs.json.schema.message.ErrorCode.DINV01;
import static com.relogiclabs.json.schema.message.ErrorCode.DMIN03;
import static com.relogiclabs.json.schema.message.ErrorCode.DMON05;
import static com.relogiclabs.json.schema.message.ErrorCode.DSEC03;
import static com.relogiclabs.json.schema.message.ErrorCode.DTAP02;
import static com.relogiclabs.json.schema.message.ErrorCode.DUTC04;
import static com.relogiclabs.json.schema.message.ErrorCode.DUTC05;
import static com.relogiclabs.json.schema.message.ErrorCode.DWKD03;
import static com.relogiclabs.json.schema.message.ErrorCode.DYAR03;
import static com.relogiclabs.json.schema.time.JsonDateTime.UNSET;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.apache.commons.lang3.StringUtils.removeEnd;

@RequiredArgsConstructor
public class DateTimeContext {
    private static final int PIVOT_YEAR = 50;
    private static final int[] DAYS_IN_MONTH = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    private static final Map<String, Integer> MONTHS = new HashMap<>();
    private static final Map<String, Integer> WEEKDAYS = new HashMap<>();

    static {
        addMonth("january", "jan", 1);
        addMonth("february", "feb", 2);
        addMonth("march", "mar", 3);
        addMonth("april", "apr", 4);
        addMonth("may", "may", 5);
        addMonth("june", "jun", 6);
        addMonth("july", "jul", 7);
        addMonth("august", "aug", 8);
        addMonth("september", "sep", 9);
        addMonth("october", "oct", 10);
        addMonth("november", "nov", 11);
        addMonth("december", "dec", 12);

        addWeekday("sunday", "sun", SUNDAY.getValue());
        addWeekday("monday", "mon", MONDAY.getValue());
        addWeekday("tuesday", "tue", TUESDAY.getValue());
        addWeekday("wednesday", "wed", WEDNESDAY.getValue());
        addWeekday("thursday", "thu", THURSDAY.getValue());
        addWeekday("friday", "fri", FRIDAY.getValue());
        addWeekday("saturday", "sat", SATURDAY.getValue());
    }

    private static void addMonth(String key1, String key2, int value) {
        MONTHS.put(key1, value);
        MONTHS.put(key2, value);
    }

    private static void addWeekday(String key1, String key2, int value) {
        WEEKDAYS.put(key1, value);
        WEEKDAYS.put(key2, value);
    }

    private int era = UNSET;
    private int year = UNSET;
    private int month = UNSET;
    private int weekday = UNSET;
    private int day = UNSET;
    private int amPm = UNSET;
    private int hour = UNSET;
    private int minute = UNSET;
    private int second = UNSET;
    private int fraction = UNSET;
    private int utcHour = UNSET;
    private int utcMinute = UNSET;

    @Getter
    public final DateTimeType type;

    public void setEra(String era) {
        var eraNum = switch(era.toUpperCase()) {
            case "BC" -> 1;
            case "AD" -> 2;
            default -> throw new InvalidDateTimeException(DERA02,
                    concat("Invalid ", type, " era input"));
        };
        this.era = checkField(this.era, eraNum);
    }

    public void setYear(int year, int digitNum) {
        if(year < 1 || year > 9999) throw new InvalidDateTimeException(DYAR03,
                concat("Invalid ", type, " year out of range"));
        year = digitNum <= 2 ? toFourDigitYear(year) : year;
        this.year = checkField(this.year, year);
    }

    public void setMonth(String month) {
        var monthNum = MONTHS.get(month.toLowerCase());
        this.month = checkField(this.month, monthNum);
    }

    public void setMonth(int month) {
        if(month < 1 || month > 12) throw new InvalidDateTimeException(DMON05,
                concat("Invalid ", type, " month out of range"));
        this.month = checkField(this.month, month);
    }

    public void setWeekday(String weekday) {
        var dayOfWeek = WEEKDAYS.get(weekday.toLowerCase());
        this.weekday = checkField(this.weekday, dayOfWeek);
    }

    public void setDay(int day) {
        if(day < 1 || day > 31) throw new InvalidDateTimeException(DDAY04,
                concat("Invalid ", type, " day out of range"));
        this.day = checkField(this.day, day);
    }

    public void setAmPm(String amPm) {
        var amPmNum = switch(amPm.toLowerCase()) {
            case "am" -> 1;
            case "pm" -> 2;
            default -> throw new InvalidDateTimeException(DTAP02,
                    concat("Invalid ", type, " hour AM/PM input"));
        };
        if(hour != UNSET && (hour < 1 || hour > 12))
            throw new InvalidDateTimeException(DHUR03,
                    concat("Invalid ", type, " hour AM/PM out of range"));
        this.amPm = checkField(this.amPm, amPmNum);
    }

    public void setHour(int hour) {
        if(amPm != UNSET && (this.hour < 1 || this.hour > 12))
            throw new InvalidDateTimeException(DHUR04,
                    concat("Invalid ", type, " hour AM/PM out of range"));
        if(hour < 0 || hour > 23)
            throw new InvalidDateTimeException(DHUR06,
                    concat("Invalid ", type, " hour out of range"));
        this.hour = checkField(this.hour, hour);
    }

    public void setMinute(int minute) {
        if(minute < 0 || minute > 59) throw new InvalidDateTimeException(DMIN03,
                concat("Invalid ", type, " minute out of range"));
        this.minute = checkField(this.minute, minute);
    }

    public void setSecond(int second) {
        if(second < 0 || second > 59) throw new InvalidDateTimeException(DSEC03,
                concat("Invalid ", type, " second out of range"));
        this.second = checkField(this.second, second);
    }

    public void setFraction(int fraction) {
        this.fraction = checkField(this.fraction, fraction);
    }

    public void setUtcOffset(int hour, int minute) {
        if(hour < -12 || hour > 12) throw new InvalidDateTimeException(DUTC04,
                concat("Invalid ", type, " UTC offset hour out of range"));
        if(minute < 0 || minute > 59) throw new InvalidDateTimeException(DUTC05,
                concat("Invalid ", type, " UTC offset minute out of range"));
        utcHour = checkField(utcHour, hour);
        utcMinute = checkField(utcMinute, minute);
    }

    private int checkField(int current, int newValue) {
        if(current != UNSET && current != newValue)
            throw new InvalidDateTimeException(DCNF01,
                    concat("Conflicting ", type, " segments input"));
        return newValue;
    }

    private static boolean isAllSet(int... values) {
        return Arrays.stream(values).allMatch(v -> v != UNSET);
    }

    public JsonDateTime validate() {
        try {
            JsonDateTime dateTime;
            if(isAllSet(year, month, day)) {
                DAYS_IN_MONTH[2] = isLeapYear(year)? 29 : 28;
                if(day < 1 || day > DAYS_IN_MONTH[month])
                    throw new InvalidDateTimeException(DDAY03,
                            concat("Invalid ", type, " day out of range"));
                dateTime = new JsonDateTime(type, year, month, day);
                if(weekday != UNSET && dateTime.getDayOfWeek().getValue() != weekday)
                    throw new InvalidDateTimeException(DWKD03, concat("Invalid ",
                            type, " weekday input"));
            }
            if(isAllSet(hour, amPm)) convertTo24Hour();
            if(hour != UNSET && (hour < 0 || hour > 23))
                throw new InvalidDateTimeException(DHUR05, concat("Invalid ",
                        type, " hour out of range"));
            return new JsonDateTime(type, year, month, day, hour, minute, second,
                    fraction, new JsonUtcOffset(utcHour, utcMinute));
        } catch(InvalidDateTimeException e) {
            throw e;
        } catch(Exception e) {
            throw new InvalidDateTimeException(DINV01,
                    concat("Invalid ", type, " year, month or day out of range", e));
        }
    }

    private void convertTo24Hour() {
        if(amPm == 2 && hour != 12) hour += 12;
        else if(amPm == 1 && hour == 12) hour = 0;
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    private static int toFourDigitYear(int year) {
        var century = LocalDate.now().getYear() / 100 * 100;
        return year < PIVOT_YEAR ? century + year : century - 100 + year;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder("{");
        if(era != UNSET) append(builder, "Era: ", era);
        if(year != UNSET) append(builder, "Year: ", year);
        if(month != UNSET) append(builder, "Month: ", month);
        if(weekday != UNSET) append(builder, "Weekday: ", weekday);
        if(day != UNSET) append(builder, "Day: ", day);
        if(amPm != UNSET) append(builder, "AM/PM: ", amPm);
        if(hour != UNSET) append(builder, "Hour: ", hour);
        if(minute != UNSET) append(builder, "Minute: ", minute);
        if(second != UNSET) append(builder, "Second: ", second);
        if(fraction != UNSET) append(builder, "Fraction: ", fraction);
        if(utcHour != UNSET) append(builder, "UTC Offset Hour: ", utcHour);
        if(utcMinute != UNSET) append(builder, "UTC Offset Minute: ", utcMinute);
        return removeEnd(builder.toString(), ", ") + "}";
    }

    private void append(StringBuilder builder, String label, int value) {
        builder.append(label).append(value).append(", ");
    }
}