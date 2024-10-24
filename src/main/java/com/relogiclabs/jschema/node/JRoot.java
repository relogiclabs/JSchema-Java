package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JRootBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.relogiclabs.jschema.internal.util.CollectionHelper.addIfNonNull;
import static com.relogiclabs.jschema.internal.util.StringHelper.join;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JRoot extends JNode {
    private static final String NEW_LINE = System.lineSeparator();

    private final JTitle title;
    private final JVersion version;
    private final List<JImport> imports;
    private final List<JPragma> pragmas;
    private final List<JDefinition> definitions;
    private final List<JScript> scripts;
    private final JNode value;

    private JRoot(JRootBuilder builder) {
        super(builder);
        title = builder.title();
        version = builder.version();
        imports = builder.imports();
        pragmas = builder.pragmas();
        definitions = builder.definitions();
        scripts = builder.scripts();
        value = requireNonNull(builder.value());
        var nodes = new ArrayList<JNode>();
        addIfNonNull(nodes, title, version);
        addIfNonNull(nodes, imports, pragmas, definitions);
        addIfNonNull(nodes, value);
        children = unmodifiableCollection(children);
    }

    public static JRoot from(JRootBuilder builder) {
        return new JRoot(builder).initialize();
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
        appendTo(builder, imports);
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
}