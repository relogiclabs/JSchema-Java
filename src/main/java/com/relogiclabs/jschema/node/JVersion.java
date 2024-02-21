package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JVersionBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class JVersion extends JDirective {
    static final String VERSION_MARKER = "%version";
    private final String version;

    private JVersion(JVersionBuilder builder) {
        super(builder);
        version = builder.version();
    }

    public static JVersion from(JVersionBuilder builder) {
        return new JVersion(builder).initialize();
    }

    @Override
    public String toString() {
        return VERSION_MARKER + ": " + version;
    }
}