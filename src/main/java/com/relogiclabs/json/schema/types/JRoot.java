package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.internal.util.StringHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JRoot extends JNode {
    private static final String NEW_LINE = System.lineSeparator();

    private final JTitle title;
    private final JVersion version;
    private final List<JInclude> includes;
    private final List<JPragma> pragmas;
    private final List<JDefinition> definitions;
    private final JNode value;
    private Collection<JNode> children;

    protected JRoot(Builder builder) {
        super(builder.relations, builder.context);
        this.title = builder.title;
        this.version = builder.version;
        this.includes = builder.includes;
        this.pragmas = builder.pragmas;
        this.definitions = builder.definitions;
        this.value = requireNonNull(builder.value);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Collection<? extends JNode> getChildren() {
        return children;
    }

    @Override
    protected <T extends JNode> T initialize() {
        children = new ArrayList<>();
        addToList(children, title);
        addToList(children, version);
        addToList(children, includes);
        addToList(children, pragmas);
        addToList(children, definitions);
        addToList(children, value);
        children = unmodifiableCollection(children);
        return super.initialize();
    }

    private void addToList(Collection<JNode> list, JNode node) {
        if(node != null) list.add(node);
    }

    private void addToList(Collection<JNode> list, List<? extends JNode> nodes) {
        if(nodes != null) list.addAll(nodes);
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
        return builder.toString();
    }

    private void appendTo(StringBuilder builder, JNode node) {
        if(node == null) return;
        builder.append(node).append(NEW_LINE);
    }

    private void appendTo(StringBuilder builder, List<? extends JNode> list) {
        if(list == null || list.isEmpty()) return;
        builder.append(StringHelper.toUnitedString(list, NEW_LINE, "", NEW_LINE));
    }

    @Setter()
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        protected JTitle title;
        protected JVersion version;
        protected List<JInclude> includes;
        protected List<JPragma> pragmas;
        protected List<JDefinition> definitions;
        protected JNode value;

        @Override
        public JRoot build() {
            return new JRoot(this).initialize();
        }
    }
}