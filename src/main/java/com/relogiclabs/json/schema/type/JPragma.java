package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.internal.builder.JPragmaBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.json.schema.internal.util.CollectionHelper.asList;
import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JPragma extends JDirective {
    static final String PRAGMA_MARKER = "%pragma";
    private final String name;
    private final PragmaValue<?> value;

    private JPragma(JPragmaBuilder builder) {
        super(builder);
        name = requireNonNull(builder.name());
        value = (PragmaValue<?>) requireNonNull(builder.value());
        children = asList(builder.value());
    }

    public static JPragma from(JPragmaBuilder builder) {
        return new JPragma(builder).initialize();
    }

    @SuppressWarnings("unchecked")
    public <T> PragmaValue<T> getValue() {
        return (PragmaValue<T>) value;
    }

    @Override
    public String toString() {
        return concat(PRAGMA_MARKER, " ", name, ": ", value);
    }
}