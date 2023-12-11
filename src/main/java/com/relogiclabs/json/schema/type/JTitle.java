package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.internal.builder.JTitleBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JTitle extends JDirective {
    static final String TITLE_MARKER = "%title";
    private final String title;

    private JTitle(JTitleBuilder builder) {
        super(builder);
        title = requireNonNull(builder.title());
    }

    public static JTitle from(JTitleBuilder builder) {
        return new JTitle(builder).initialize();
    }

    @Override
    public String toString() {
        return concat(TITLE_MARKER, ": ", title);
    }
}