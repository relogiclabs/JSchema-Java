package com.relogiclabs.json.schema.time;

import lombok.Getter;

import java.time.ZoneOffset;

import static com.relogiclabs.json.schema.time.JsonDateTime.UNSET;
import static com.relogiclabs.json.schema.time.JsonDateTime.defaultIfUnset;
import static lombok.AccessLevel.PACKAGE;
import static org.apache.commons.lang3.StringUtils.removeEnd;


public class JsonUtcOffset {
    static final JsonUtcOffset DEFAULT_UTC_OFFSET = new JsonUtcOffset();
    private static final int DEFAULT_UTC_OFFSET_HOUR = 0;
    private static final int DEFAULT_UTC_OFFSET_MINUTE = 0;

    @Getter private final int hour;
    @Getter private final int minute;
    @Getter(PACKAGE) private final ZoneOffset zoneOffset;

    public JsonUtcOffset() {
        this(UNSET, UNSET);
    }

    public JsonUtcOffset(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        this.zoneOffset = createOffset(hour, minute);
    }

    private static ZoneOffset createOffset(int hour, int minute) {
        int signum = 1;
        if(hour != UNSET) signum = (int) Math.signum(hour);
        if(minute != UNSET) minute *= signum;
        return ZoneOffset.ofHoursMinutes(
                defaultIfUnset(hour, DEFAULT_UTC_OFFSET_HOUR),
                defaultIfUnset(minute, DEFAULT_UTC_OFFSET_MINUTE));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(hour != UNSET) append(builder, "UTC Offset Hour: ", hour);
        if(minute != UNSET) append(builder, "UTC Offset Minute: ", minute);
        return removeEnd(builder.toString(), ", ");
    }

    private static void append(StringBuilder builder, String label, int value) {
        builder.append(label).append(value).append(", ");
    }
}