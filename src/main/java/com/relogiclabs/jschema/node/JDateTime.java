package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.time.DateTimeParser;
import com.relogiclabs.jschema.time.JsonDateTime;
import com.relogiclabs.jschema.type.EType;
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
        if(getType() == EType.DATE) return getRuntime().getPragmas().getDateTypeParser();
        if(getType() == EType.TIME) return getRuntime().getPragmas().getTimeTypeParser();
        throw new IllegalStateException("Invalid date time type");
    }
}