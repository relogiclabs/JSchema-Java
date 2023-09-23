package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.DefinitionNotFoundException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.json.schema.message.ErrorCode.DEFI02;
import static com.relogiclabs.json.schema.message.MessageFormatter.formatForSchema;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JAlias extends JLeaf {
    private final String name;

    private JAlias(Builder builder) {
        super(builder.relations, builder.context);
        this.name = requireNonNull(builder.name);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean match(JNode node) {
        var definitions = getRuntime().getDefinitions();
        if(!definitions.containsKey(this))
            throw new DefinitionNotFoundException(
                    formatForSchema(DEFI02, "Definition of " + name
                            + " not found", getContext()));
        return definitions.get(this).match(node);
    }

    @Override
    public String toString() {
        return name;
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        protected String name;

        @Override
        public JAlias build() {
            return new JAlias(this).initialize();
        }
    }
}
