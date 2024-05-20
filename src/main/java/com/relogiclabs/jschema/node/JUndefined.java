package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JUndefinedBuilder;
import com.relogiclabs.jschema.type.EUndefined;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class JUndefined extends JLeaf implements EUndefined {
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
        return MARKER;
    }
}