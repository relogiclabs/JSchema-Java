package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.internal.builder.JNodeBuilder;

public abstract class JPrimitive extends JLeaf implements JsonTypable {
    JPrimitive(JNodeBuilder<?> builder) {
        super(builder);
    }

    JPrimitive(JPrimitive node) {
        super(node);
    }

    @Override
    public JsonType getType() {
        return JsonType.PRIMITIVE;
    }

    @Override
    public JNode getNode() {
        return this;
    }
}