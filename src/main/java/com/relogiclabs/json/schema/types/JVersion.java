package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@EqualsAndHashCode
public class JVersion extends JDirective {
    public static final String VERSION_MARKER = "%version";
    private final String version;

    private JVersion(Builder builder) {
        super(builder.relations, builder.context);
        this.version = builder.version;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return VERSION_MARKER + " " + version;
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        protected String version;

        @Override
        public JVersion build() {
            return new JVersion(this).initialize();
        }
    }
}