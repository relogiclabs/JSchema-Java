package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.InvalidPragmaValueException;
import com.relogiclabs.json.schema.exception.PragmaNotFoundException;
import com.relogiclabs.json.schema.internal.tree.PragmaDescriptor;
import com.relogiclabs.json.schema.internal.util.StringHelper;
import com.relogiclabs.json.schema.message.MessageFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;
import static com.relogiclabs.json.schema.message.ErrorCode.PRAG01;
import static com.relogiclabs.json.schema.message.ErrorCode.PRAG02;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JPragma extends JDirective {
    public static final String PRAGMA_MARKER = "%pragma";

    private final String name;
    private final PragmaValue<?> value;

    private JPragma(Builder builder) {
        super(builder.relations, builder.context);
        this.name = requireNonNull(builder.name);
        this.value = (PragmaValue<?>) requireNonNull(builder.value);
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unchecked")
    public <T> PragmaValue<T> getValue() {
        return (PragmaValue<T>) value;
    }

    @Override
    public String toString() {
        return concat(PRAGMA_MARKER, " ", name, ": ", value);
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        protected String name;
        protected JNode value;

        private void checkPragma() {
            var descriptor = PragmaDescriptor.from(name);
            if(descriptor == null)
                throw new PragmaNotFoundException(MessageFormatter.formatForSchema(
                        PRAG01, concat("Invalid pragma ", quote(name), " with value ",
                                value.getOutline(), " found"), context));
            if(!descriptor.matchType(value.getClass()))
                throw new InvalidPragmaValueException(MessageFormatter.formatForSchema(
                        PRAG02, StringHelper.concat("Invalid value ", value.getOutline(),
                                " for pragma ", quote(name), " found"),
                        context));
        }

        @Override
        public JPragma build() {
            checkPragma();
            return new JPragma(this).initialize();
        }
    }
}