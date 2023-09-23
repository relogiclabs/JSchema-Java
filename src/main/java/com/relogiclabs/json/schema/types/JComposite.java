package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.tree.Context;

import java.util.Collection;
import java.util.Map;

public abstract class JComposite extends JBranch implements JsonTypable {
    public JComposite(Map<JNode, JNode> relations, Context context) {
        super(relations, context);
    }

    public abstract Collection<? extends JNode> components();

    @Override
    public JsonType getType() {
        return JsonType.ANY;
    }

    @Override
    public JNode getNode() {
        return this;
    }
}
