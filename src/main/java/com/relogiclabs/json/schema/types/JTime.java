package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.internal.time.JsonDateTime;

public class JTime extends JDateTime {
    public JTime(JString baseNode, JsonDateTime dateTime) {
        super(baseNode, dateTime);
    }

    public JsonType getType() {
        return JsonType.TIME;
    }
}