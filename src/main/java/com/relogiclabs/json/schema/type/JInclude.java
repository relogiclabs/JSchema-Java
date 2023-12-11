package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.internal.builder.JIncludeBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JInclude extends JDirective {
    static final String INCLUDE_MARKER = "%include";
    private final String className;

    private JInclude(JIncludeBuilder builder) {
        super(builder);
        className = requireNonNull(builder.className());
    }

    public static JInclude from(JIncludeBuilder builder) {
        return new JInclude(builder).initialize();
    }

    @Override
    public String toString() {
        return concat(INCLUDE_MARKER, ": ", className);
    }
}