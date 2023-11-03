package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JTitle extends JDirective {
    public static final String TITLE_MARKER = "%title";
    private final String title;

    private JTitle(Builder builder) {
        super(builder);
        title = requireNonNull(builder.title);
    }

    @Override
    public String toString() {
        return concat(TITLE_MARKER, ": ", title);
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        private String title;

        @Override
        public JTitle build() {
            return build(new JTitle(this));
        }
    }
}