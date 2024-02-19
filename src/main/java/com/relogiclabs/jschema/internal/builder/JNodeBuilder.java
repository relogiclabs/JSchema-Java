package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.tree.Context;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Accessors(fluent = true)
public abstract class JNodeBuilder<T extends JNodeBuilder<T>> {
    private Map<JNode, JNode> relations;
    private Context context;

    @SuppressWarnings("unchecked")
    public T relations(Map<JNode, JNode> relations) {
        this.relations = relations;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T context(Context context) {
        this.context = context;
        return (T) this;
    }

    public abstract JNode build();
}