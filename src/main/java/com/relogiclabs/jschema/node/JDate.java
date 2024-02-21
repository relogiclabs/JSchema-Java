package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.time.JsonDateTime;
import com.relogiclabs.jschema.type.EType;

public final class JDate extends JDateTime {
    private JDate(JString node, JsonDateTime dateTime) {
        super(node, dateTime);
    }

    public static JDate from(JString node, JsonDateTime dateTime) {
        return new JDate(node, dateTime);
    }

    public EType getType() {
        return EType.DATE;
    }
}