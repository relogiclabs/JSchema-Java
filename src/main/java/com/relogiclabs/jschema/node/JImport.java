package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JImportBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JImport extends JDirective {
    static final String IMPORT_MARKER = "%import";
    private final String className;

    private JImport(JImportBuilder builder) {
        super(builder);
        className = requireNonNull(builder.className());
    }

    public static JImport from(JImportBuilder builder) {
        return new JImport(builder).initialize();
    }

    @Override
    public String toString() {
        return concat(IMPORT_MARKER, ": ", className);
    }
}