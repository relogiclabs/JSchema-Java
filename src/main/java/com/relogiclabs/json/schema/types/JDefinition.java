package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JDefinition extends JDirective {
    public static final String DEFINE_MARKER = "%define";
    public final JAlias alias;
    public final JValidator validator;

    private JDefinition(Builder builder) {
        super(builder.relations, builder.context);
        this.alias = requireNonNull(builder.alias);
        this.validator = requireNonNull(builder.validator);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return concat(DEFINE_MARKER, " ", alias, " ", validator);
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        protected JAlias alias;
        protected JValidator validator;

        @Override
        public JDefinition build() {
            return new JDefinition(this).initialize();
        }
    }
}