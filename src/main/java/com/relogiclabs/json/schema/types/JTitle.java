package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JTitle extends JDirective {
    public static final String TITLE_MARKER = "%title";
    private final String title;

    private JTitle(Builder builder) {
        super(builder.relations, builder.context);
        this.title = requireNonNull(builder.title);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return concat(TITLE_MARKER, " ", title);
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        protected String title;

        @Override
        public JTitle build() {
            return new JTitle(this).initialize();
        }
    }
}