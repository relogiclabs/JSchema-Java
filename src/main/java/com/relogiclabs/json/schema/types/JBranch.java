package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.tree.Context;

import java.util.Map;

public abstract class JBranch extends JNode {
    protected JBranch(Map<JNode, JNode> relations, Context context) {
        super(relations, context);
    }
}