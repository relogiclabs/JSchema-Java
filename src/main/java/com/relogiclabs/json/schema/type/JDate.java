package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.time.JsonDateTime;

public final class JDate extends JDateTime {
    private JDate(JString node, JsonDateTime dateTime) {
        super(node, dateTime);
    }

    public static JDate from(JString node, JsonDateTime dateTime) {
        return new JDate(node, dateTime);
    }

    public JsonType getType() {
        return JsonType.DATE;
    }
}