package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JInclude extends JDirective {
    public static final String INCLUDE_MARKER = "%include";
    private final String className;

    private JInclude(Builder builder) {
        super(builder);
        className = requireNonNull(builder.className);
    }

    @Override
    public String toString() {
        return concat(INCLUDE_MARKER, ": ", className);
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        private String className;

        @Override
        public JInclude build() {
            return build(new JInclude(this));
        }
    }
}