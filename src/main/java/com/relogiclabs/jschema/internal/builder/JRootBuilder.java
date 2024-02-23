package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JDefinition;
import com.relogiclabs.jschema.node.JImport;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JPragma;
import com.relogiclabs.jschema.node.JRoot;
import com.relogiclabs.jschema.node.JScript;
import com.relogiclabs.jschema.node.JTitle;
import com.relogiclabs.jschema.node.JVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter @Setter
@Accessors(fluent = true)
public final class JRootBuilder extends JNodeBuilder<JRootBuilder> {
    private JTitle title;
    private JVersion version;
    private List<JImport> imports;
    private List<JPragma> pragmas;
    private List<JDefinition> definitions;
    private List<JScript> scripts;
    private JNode value;

    @Override
    public JRoot build() {
        return JRoot.from(this);
    }
}