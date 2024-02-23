package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JImport;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(fluent = true)
public final class JImportBuilder extends JNodeBuilder<JImportBuilder> {
    private String className;

    @Override
    public JImport build() {
        return JImport.from(this);
    }
}