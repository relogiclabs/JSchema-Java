package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.time.JsonDateTime;

public final class JTime extends JDateTime {
    private JTime(JString node, JsonDateTime dateTime) {
        super(node, dateTime);
    }

    public static JTime from(JString node, JsonDateTime dateTime) {
        return new JTime(node, dateTime);
    }

    public JsonType getType() {
        return JsonType.TIME;
    }
}