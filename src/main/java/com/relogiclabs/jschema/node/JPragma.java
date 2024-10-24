package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JPragmaBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.jschema.internal.util.CollectionHelper.asNonNullList;
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
        children = asNonNullList(builder.value());
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
        return PRAGMA_MARKER + " " + name + ": " + value;
    }
}