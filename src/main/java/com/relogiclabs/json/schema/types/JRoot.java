package com.relogiclabs.json.schema.types;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

import static com.relogiclabs.json.schema.internal.util.CollectionHelper.addToList;
import static com.relogiclabs.json.schema.internal.util.StringHelper.join;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JRoot extends JNode {
    private static final String NEW_LINE = System.lineSeparator();

    private final JTitle title;
    private final JVersion version;
    private final List<JInclude> includes;
    private final List<JPragma> pragmas;
    private final List<JDefinition> definitions;
    private final JNode value;

    private JRoot(Builder builder) {
        super(builder);
        title = builder.title;
        version = builder.version;
        includes = builder.includes;
        pragmas = builder.pragmas;
        definitions = builder.definitions;
        value = requireNonNull(builder.value);
        var nodes = new ArrayList<JNode>();
        addToList(nodes, title, version);
        addToList(nodes, includes, pragmas, definitions);
        addToList(nodes, value);
        children = unmodifiableCollection(children);
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JRoot.class);
        if(other == null) return false;
        return value.match(other.value);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        appendTo(builder, title);
        appendTo(builder, version);
        appendTo(builder, includes);
        appendTo(builder, pragmas);
        appendTo(builder, definitions);
        appendTo(builder, value);
        return builder.toString().trim();
    }

    private void appendTo(StringBuilder builder, JNode node) {
        if(node == null) return;
        builder.append(node).append(NEW_LINE);
    }

    private void appendTo(StringBuilder builder, List<? extends JNode> list) {
        if(list == null || list.isEmpty()) return;
        builder.append(join(list, NEW_LINE, "", NEW_LINE));
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        private JTitle title;
        private JVersion version;
        private List<JInclude> includes;
        private List<JPragma> pragmas;
        private List<JDefinition> definitions;
        private JNode value;

        @Override
        public JRoot build() {
            return build(new JRoot(this));
        }
    }
}