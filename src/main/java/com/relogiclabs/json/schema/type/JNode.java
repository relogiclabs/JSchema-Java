package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.builder.JNodeBuilder;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.tree.Context;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.DataTypeMismatch;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP02;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

public abstract class JNode {
    private final Map<JNode, JNode> relations;
    @Getter private final Context context;
    @Getter protected Collection<? extends JNode> children = emptyList();

    JNode(JNodeBuilder<?> builder) {
        requireNonNull(builder);
        relations = requireNonNull(builder.relations());
        context = requireNonNull(builder.context());
    }

    JNode(JNode node) {
        requireNonNull(node);
        relations = requireNonNull(node.relations);
        context = requireNonNull(node.context);
        children = requireNonNull(node.children);
    }

    public JNode getParent() {
        return relations.get(this);
    }

    @SuppressWarnings("unchecked")
    <T> T initialize() {
        for(var c : getChildren()) relations.put(c, this);
        return (T) this;
    }

    public RuntimeContext getRuntime() {
        return context.getRuntime();
    }

    <T> T castType(JNode node, Class<T> type) {
        if(type.isInstance(node)) return type.cast(node);
        failWith(new JsonSchemaException(
                new ErrorDetail(DTYP02, DataTypeMismatch),
                ExpectedHelper.asDataTypeMismatch(this),
                ActualHelper.asDataTypeMismatch(node)));
        return null;
    }

    <T> boolean checkType(JNode node, Class<T> type) {
        return castType(node, type) != null;
    }

    public abstract boolean match(JNode node);
    public abstract String toString();

    public String getOutline() {
        return getRuntime().getMessageFormatter().createOutline(toString());
    }

    boolean failWith(RuntimeException exception) {
        return getRuntime().getExceptions().failWith(exception);
    }
}