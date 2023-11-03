package com.relogiclabs.json.schema.types;

import lombok.Setter;
import lombok.experimental.Accessors;

public abstract class JPrimitive extends JLeaf implements JsonTypable {
    protected JPrimitive(JNode.Builder<?> builder) {
        super(builder);
    }

    @Override
    public JsonType getType() {
        return JsonType.PRIMITIVE;
    }

    @Override
    public JNode getNode() {
        return this;
    }

    @Setter
    @Accessors(fluent = true)
    public abstract static class Builder<T> extends JNode.Builder<Builder<T>> {
        protected T value;
    }
}