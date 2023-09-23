package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.tree.Context;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public abstract class JLeaf extends JNode {
    protected JLeaf(Map<JNode, JNode> relations, Context context) {
        super(relations, context);
    }

    @Override
    public Collection<? extends JNode> getChildren() {
        return Collections.emptyList();
    }
}
