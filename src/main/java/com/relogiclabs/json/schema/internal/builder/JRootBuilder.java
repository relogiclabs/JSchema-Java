package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.type.JDefinition;
import com.relogiclabs.json.schema.type.JInclude;
import com.relogiclabs.json.schema.type.JNode;
import com.relogiclabs.json.schema.type.JPragma;
import com.relogiclabs.json.schema.type.JRoot;
import com.relogiclabs.json.schema.type.JTitle;
import com.relogiclabs.json.schema.type.JVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter @Setter
@Accessors(fluent = true)
public class JRootBuilder extends JNodeBuilder<JRootBuilder> {
    private JTitle title;
    private JVersion version;
    private List<JInclude> includes;
    private List<JPragma> pragmas;
    private List<JDefinition> definitions;
    private JNode value;

    @Override
    public JRoot build() {
        return JRoot.from(this);
    }
}