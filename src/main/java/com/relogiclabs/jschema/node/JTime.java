package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.time.JsonDateTime;
import com.relogiclabs.jschema.type.EType;

public final class JTime extends JDateTime {
    private JTime(JString node, JsonDateTime dateTime) {
        super(node, dateTime);
    }

    public static JTime from(JString node, JsonDateTime dateTime) {
        return new JTime(node, dateTime);
    }

    public EType getType() {
        return EType.TIME;
    }
}