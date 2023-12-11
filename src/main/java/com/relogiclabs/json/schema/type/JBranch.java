package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.internal.builder.JNodeBuilder;

public abstract class JBranch extends JNode {
    JBranch(JNodeBuilder<?> builder) {
        super(builder);
    }
}