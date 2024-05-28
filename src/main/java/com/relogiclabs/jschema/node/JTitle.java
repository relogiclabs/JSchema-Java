package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JTitleBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.jschema.internal.util.StringHelper.quote;
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
        return TITLE_MARKER + ": " + quote(title);
    }
}