package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.DefinitionNotFoundException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;
import static com.relogiclabs.json.schema.message.ErrorCode.DEFI02;
import static com.relogiclabs.json.schema.message.MessageFormatter.formatForSchema;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JAlias extends JLeaf {
    private final String name;

    private JAlias(Builder builder) {
        super(builder);
        name = requireNonNull(builder.name);
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

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        private String name;

        @Override
        public JAlias build() {
            return build(new JAlias(this));
        }
    }
}