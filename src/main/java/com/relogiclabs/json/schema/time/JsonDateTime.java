package com.relogiclabs.json.schema.time;

import com.relogiclabs.json.schema.type.JDate;
import com.relogiclabs.json.schema.type.JDateTime;
import com.relogiclabs.json.schema.type.JString;
import com.relogiclabs.json.schema.type.JTime;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static com.relogiclabs.json.schema.time.DateTimeType.DATE_TYPE;
import static com.relogiclabs.json.schema.time.DateTimeType.TIME_TYPE;
import static com.relogiclabs.json.schema.time.JsonUtcOffset.DEFAULT_UTC_OFFSET;
import static lombok.AccessLevel.PACKAGE;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class JsonDateTime {
    private static final int DEFAULT_YEAR = 2000;
    private static final int DEFAULT_MONTH = 1;
    private static final int DEFAULT_DAY = 1;
    private static final int DEFAULT_HOUR = 0;
    private static final int DEFAULT_MINUTE = 0;
    private static final int DEFAULT_SECOND = 0;
    private static final int DEFAULT_FRACTION = 0;

    public static final int UNSET = -1000;

    @Getter private final DateTimeType type;
    @Getter private final int year;
    @Getter private final int month;
    @Getter private final int day;
    @Getter private final int hour;
    @Getter private final int minute;
    @Getter private final int second;
    @Getter private final int fraction;
    @Getter private final JsonUtcOffset utcOffset;

    @Getter(PACKAGE) private final ZonedDateTime dateTime;

    public JsonDateTime(DateTimeType type, int year, int month, int day) {
        this(type, year, month, day, UNSET, UNSET, UNSET, UNSET, null);
    }

    public JsonDateTime(DateTimeType type, int year, int month,
                        int day, int hour, int minute, int second,
                        int fraction, JsonUtcOffset utcOffset) {
        if(utcOffset == null) utcOffset = DEFAULT_UTC_OFFSET;
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.fraction = fraction;
        this.utcOffset = utcOffset;
        this.dateTime = ZonedDateTime.of(
                defaultIfUnset(year, DEFAULT_YEAR),
                defaultIfUnset(month, DEFAULT_MONTH),
                defaultIfUnset(day, DEFAULT_DAY),
                defaultIfUnset(hour, DEFAULT_HOUR),
                defaultIfUnset(minute, DEFAULT_MINUTE),
                defaultIfUnset(second, DEFAULT_SECOND),
                defaultIfUnset(fraction, DEFAULT_FRACTION),
                utcOffset.getZoneOffset());
    }

    public DayOfWeek getDayOfWeek() {
        if(isAllSet(year, month, day)) return dateTime.getDayOfWeek();
        return null;
    }

    public int compare(JsonDateTime other) {
        return dateTime.compareTo(other.dateTime);
    }

    private static boolean isAllSet(int... values) {
        return Arrays.stream(values).allMatch(v -> v != UNSET);
    }

    static int defaultIfUnset(int value, int defaultValue) {
        return value == UNSET ? defaultValue : value;
    }

    public JDateTime createNode(JString dateTime) {
        if(type == DATE_TYPE) return JDate.from(dateTime, this);
        if(type == TIME_TYPE) return JTime.from(dateTime, this);
        throw new IllegalStateException("Invalid date time type");
    }

    @Override
    public String toString() {
        var builder = new StringBuilder("{");
        if(year != UNSET) append(builder, "Year: ", year);
        if(month != UNSET) append(builder, "Month: ", month);
        if(day != UNSET) append(builder, "Day: ", day);
        if(hour != UNSET) append(builder, "Hour: ", hour);
        if(minute != UNSET) append(builder, "Minute: ", minute);
        if(second != UNSET) append(builder, "Second: ", second);
        if(fraction != UNSET) append(builder, "Fraction: ", fraction);
        builder.append(utcOffset);
        return removeEnd(builder.toString(), ", ") + "}";
    }

    private static void append(StringBuilder builder, String label, int value) {
        builder.append(label).append(value).append(", ");
    }
}