package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.internal.time.JsonDateTime;

public class JDate extends JDateTime {
    public JDate(JString baseNode, JsonDateTime dateTime) {
        super(baseNode, dateTime);
    }

    public JsonType getType() {
        return JsonType.DATE;
    }
}