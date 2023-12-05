package com.relogiclabs.json.schema.internal.time;

import com.relogiclabs.json.schema.types.JDate;
import com.relogiclabs.json.schema.types.JDateTime;
import com.relogiclabs.json.schema.types.JString;
import com.relogiclabs.json.schema.types.JTime;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static com.relogiclabs.json.schema.internal.time.DateTimeType.DATE_TYPE;
import static com.relogiclabs.json.schema.internal.time.DateTimeType.TIME_TYPE;

public class JsonDateTime {
    private static final int DEFAULT_YEAR = 2000;
    private static final int DEFAULT_MONTH = 1;
    private static final int DEFAULT_DAY = 1;
    private static final int DEFAULT_HOUR = 0;
    private static final int DEFAULT_MINUTE = 0;
    private static final int DEFAULT_SECOND = 0;
    private static final int DEFAULT_FRACTION = 0;
    private static final int DEFAULT_UTC_HOUR = 0;
    private static final int DEFAULT_UTC_MINUTE = 0;

    public static final int UNSET = -1000;

    @Getter private final DateTimeType type;
    @Getter private final int year;
    @Getter private final int month;
    @Getter private final int day;
    @Getter private final int hour;
    @Getter private final int minute;
    @Getter private final int second;
    @Getter private final int fraction;
    @Getter private final int utcHour;
    @Getter private final int utcMinute;

    private final ZonedDateTime dateTime;

    public JsonDateTime(DateTimeType type, int year, int month, int day) {
        this(type, year, month, day, UNSET, UNSET, UNSET, UNSET, UNSET, UNSET);
    }

    public JsonDateTime(DateTimeType type, int year, int month,
                        int day, int hour, int minute, int second,
                        int fraction, int utcHour, int utcMinute) {
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.fraction = fraction;
        this.utcHour = utcHour;
        this.utcMinute = utcMinute;
        this.dateTime = ZonedDateTime.of(
                defaultIfUnset(year, DEFAULT_YEAR),
                defaultIfUnset(month, DEFAULT_MONTH),
                defaultIfUnset(day, DEFAULT_DAY),
                defaultIfUnset(hour, DEFAULT_HOUR),
                defaultIfUnset(minute, DEFAULT_MINUTE),
                defaultIfUnset(second, DEFAULT_SECOND),
                defaultIfUnset(fraction, DEFAULT_FRACTION),
                getZoneOffset(utcHour, utcMinute));
    }

    private ZoneOffset getZoneOffset(int utcHour, int utcMinute) {
        int signum = 1;
        if(utcHour != UNSET) signum = (int) Math.signum(utcHour);
        if(utcMinute != UNSET) utcMinute *= signum;
        return ZoneOffset.ofHoursMinutes(
                defaultIfUnset(utcHour, DEFAULT_UTC_HOUR),
                defaultIfUnset(utcMinute, DEFAULT_UTC_MINUTE));
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

    private static int defaultIfUnset(int value, int defaultValue) {
        return value == UNSET ? defaultValue : value;
    }

    public JDateTime create(JString dateTime) {
        if(type == DATE_TYPE) return new JDate(dateTime, this);
        if(type == TIME_TYPE) return new JTime(dateTime, this);
        throw new IllegalStateException("Invalid date time type");
    }
}