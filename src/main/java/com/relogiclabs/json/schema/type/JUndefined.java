package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.internal.builder.JUndefinedBuilder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class JUndefined extends JLeaf {
    static final String UNDEFINED_MARKER = "!";

    private JUndefined(JUndefinedBuilder builder) {
        super(builder);
    }

    public static JUndefined from(JUndefinedBuilder builder) {
        return new JUndefined(builder).initialize();
    }

    @Override
    public boolean match(JNode node) {
        return true;
    }

    @Override
    public String toString() {
        return UNDEFINED_MARKER;
    }
}