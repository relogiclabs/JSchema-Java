package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JInclude extends JDirective {
    public static final String INCLUDE_MARKER = "%include";
    public final String className;

    private JInclude(Builder builder) {
        super(builder.relations, builder.context);
        this.className = requireNonNull(builder.className);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return concat(INCLUDE_MARKER, " ", className);
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        protected String className;

        @Override
        public JInclude build() {
            return new JInclude(this).initialize();
        }
    }
}