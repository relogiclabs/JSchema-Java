package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.json.schema.internal.util.CollectionHelper.asList;
import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JDefinition extends JDirective {
    public static final String DEFINE_MARKER = "%define";
    private final JAlias alias;
    private final JValidator validator;

    private JDefinition(Builder builder) {
        super(builder);
        alias = requireNonNull(builder.alias);
        validator = requireNonNull(builder.validator);
        children = asList(alias, validator);
    }

    @Override
    public String toString() {
        return concat(DEFINE_MARKER, " ", alias, ": ", validator);
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        private JAlias alias;
        private JValidator validator;

        @Override
        public JDefinition build() {
            return build(new JDefinition(this));
        }
    }
}