package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.exception.DefinitionNotFoundException;
import com.relogiclabs.json.schema.internal.builder.JAliasBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;
import static com.relogiclabs.json.schema.message.ErrorCode.DEFI02;
import static com.relogiclabs.json.schema.message.MessageFormatter.formatForSchema;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JAlias extends JLeaf {
    private final String name;

    private JAlias(JAliasBuilder builder) {
        super(builder);
        name = requireNonNull(builder.name());
    }

    public static JAlias from(JAliasBuilder builder) {
        return new JAlias(builder).initialize();
    }

    @Override
    public boolean match(JNode node) {
        var definitions = getRuntime().getDefinitions();
        if(!definitions.containsKey(this))
            throw new DefinitionNotFoundException(formatForSchema(DEFI02, "Definition of "
                    + quote(name) + " not found", getContext()));
        return definitions.get(this).match(node);
    }

    @Override
    public String toString() {
        return name;
    }
}