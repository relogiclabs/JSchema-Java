package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.internal.time.DateTimeParser;
import com.relogiclabs.json.schema.internal.time.JsonDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class JDateTime extends JString {
    private final JsonDateTime dateTime;

    protected JDateTime(JString baseNode, JsonDateTime dateTime) {
        super(baseNode);
        this.dateTime = dateTime;
    }

    public DateTimeParser getDateTimeParser() {
        if(getType() == JsonType.DATE) return getRuntime().getPragmas().getDateTypeParser();
        if(getType() == JsonType.TIME) return getRuntime().getPragmas().getTimeTypeParser();
        throw new IllegalStateException("Invalid date time type");
    }
}