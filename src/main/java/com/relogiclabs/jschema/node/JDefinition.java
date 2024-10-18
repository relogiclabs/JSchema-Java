package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JDefinitionBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.jschema.internal.util.CollectionHelper.asNonNullList;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JDefinition extends JDirective {
    static final String DEFINE_MARKER = "%define";
    private final JAlias alias;
    private final JValidator validator;

    private JDefinition(JDefinitionBuilder builder) {
        super(builder);
        alias = requireNonNull(builder.alias());
        validator = requireNonNull(builder.validator());
        children = asNonNullList(alias, validator);
    }

    public static JDefinition from(JDefinitionBuilder builder) {
        return new JDefinition(builder).initialize();
    }

    @Override
    public String toString() {
        return DEFINE_MARKER + " " + alias + ": " + validator;
    }
}