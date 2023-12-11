package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.internal.time.DateTimeParser;
import com.relogiclabs.json.schema.time.JsonDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class JDateTime extends JString {
    private final JsonDateTime dateTime;

    JDateTime(JString node, JsonDateTime dateTime) {
        super(node);
        this.dateTime = dateTime;
    }

    public DateTimeParser getDateTimeParser() {
        if(getType() == JsonType.DATE) return getRuntime().getPragmas().getDateTypeParser();
        if(getType() == JsonType.TIME) return getRuntime().getPragmas().getTimeTypeParser();
        throw new IllegalStateException("Invalid date time type");
    }
}