package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@EqualsAndHashCode
public final class JVersion extends JDirective {
    public static final String VERSION_MARKER = "%version";
    private final String version;

    private JVersion(Builder builder) {
        super(builder);
        version = builder.version;
    }

    @Override
    public String toString() {
        return VERSION_MARKER + ": " + version;
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        private String version;

        @Override
        public JVersion build() {
            return build(new JVersion(this));
        }
    }
}