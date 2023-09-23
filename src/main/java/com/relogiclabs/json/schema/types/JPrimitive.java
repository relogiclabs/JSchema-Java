package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.tree.Context;

import java.util.Map;

public abstract class JPrimitive extends JLeaf implements JsonTypable {
    protected JPrimitive(Map<JNode, JNode> relations, Context context) {
        super(relations, context);
    }

    @Override
    public JsonType getType() {
        return JsonType.ANY;
    }

    @Override
    public JNode getNode() {
        return this;
    }
}
