package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.CommonException;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.message.MessageFormatter;
import com.relogiclabs.json.schema.tree.Context;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collection;
import java.util.Map;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.DataTypeMismatch;
import static com.relogiclabs.json.schema.internal.util.StringHelper.createOutline;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP03;
import static java.util.Objects.requireNonNull;

public abstract class JNode {

    private final Map<JNode, JNode> relations;

    @Getter
    private final Context context;

    protected JNode(Map<JNode, JNode> relations, Context context) {
        this.relations = requireNonNull(relations);
        this.context = requireNonNull(context);
    }

    public JNode getParent() {
        return relations.get(this);
    }
    public abstract Collection<? extends JNode> getChildren();

    @SuppressWarnings("unchecked")
    protected <T extends JNode> T initialize() {
        for(var c : getChildren()) relations.put(c, this);
        return (T) this;
    }

    public ParserRuleContext getParser() {
        return context.getParser();
    }

    public RuntimeContext getRuntime() {
        return context.getRuntime();
    }

    protected MessageFormatter getMessageFormatter() {
        return getRuntime().getMessageFormatter();
    }

    protected <T> T castType(JNode node, Class<T> type) {
        if(type.isInstance(node)) return type.cast(node);
        failWith(new JsonSchemaException(
                new ErrorDetail(DTYP03, DataTypeMismatch),
                ExpectedHelper.asDataTypeMismatch(this),
                ActualHelper.asDataTypeMismatch(node)));
        return null;
    }

    protected <T> boolean isOfType(JNode node, Class<T> type) {
        return castType(node, type) != null;
    }

    public abstract boolean match(JNode node);
    public abstract String toString();

    public String getOutline() {
        return createOutline(toString(), getMessageFormatter().getOutlineLength());
    }

    protected boolean failWith(CommonException exception) {
        return getRuntime().failWith(exception);
    }

    protected abstract static class Builder<T extends Builder<T>> {
        protected Map<JNode, JNode> relations;
        protected Context context;

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
}