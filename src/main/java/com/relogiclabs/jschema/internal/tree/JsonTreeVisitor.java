package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.internal.antlr.JsonParser.ArrayNodeContext;
import com.relogiclabs.jschema.internal.antlr.JsonParser.DoubleNodeContext;
import com.relogiclabs.jschema.internal.antlr.JsonParser.FalseNodeContext;
import com.relogiclabs.jschema.internal.antlr.JsonParser.FloatNodeContext;
import com.relogiclabs.jschema.internal.antlr.JsonParser.IntegerNodeContext;
import com.relogiclabs.jschema.internal.antlr.JsonParser.JsonContext;
import com.relogiclabs.jschema.internal.antlr.JsonParser.NullNodeContext;
import com.relogiclabs.jschema.internal.antlr.JsonParser.ObjectNodeContext;
import com.relogiclabs.jschema.internal.antlr.JsonParser.PropertyNodeContext;
import com.relogiclabs.jschema.internal.antlr.JsonParser.StringNodeContext;
import com.relogiclabs.jschema.internal.antlr.JsonParser.TrueNodeContext;
import com.relogiclabs.jschema.internal.antlr.JsonParserBaseVisitor;
import com.relogiclabs.jschema.internal.builder.JArrayBuilder;
import com.relogiclabs.jschema.internal.builder.JBooleanBuilder;
import com.relogiclabs.jschema.internal.builder.JDoubleBuilder;
import com.relogiclabs.jschema.internal.builder.JFloatBuilder;
import com.relogiclabs.jschema.internal.builder.JIntegerBuilder;
import com.relogiclabs.jschema.internal.builder.JNullBuilder;
import com.relogiclabs.jschema.internal.builder.JObjectBuilder;
import com.relogiclabs.jschema.internal.builder.JPropertyBuilder;
import com.relogiclabs.jschema.internal.builder.JRootBuilder;
import com.relogiclabs.jschema.internal.builder.JStringBuilder;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JProperty;
import com.relogiclabs.jschema.tree.Context;
import com.relogiclabs.jschema.tree.RuntimeContext;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.jschema.internal.tree.TreeHelper.requireUniqueness;
import static com.relogiclabs.jschema.internal.util.StringHelper.toEncoded;
import static com.relogiclabs.jschema.internal.util.StringHelper.unquote;
import static com.relogiclabs.jschema.tree.TreeType.JSON_TREE;

public final class JsonTreeVisitor extends JsonParserBaseVisitor<JNode> {
    private final Map<JNode, JNode> relations;
    private final RuntimeContext runtime;

    public JsonTreeVisitor(RuntimeContext runtime) {
        this.runtime = runtime;
        this.relations = new HashMap<>();
    }

    @Override
    public JNode visitJson(JsonContext ctx) {
        return new JRootBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .value(visit(ctx.valueNode()))
            .build();
    }

    @Override
    public JNode visitObjectNode(ObjectNodeContext ctx) {
        return new JObjectBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .properties(requireUniqueness(ctx.propertyNode().stream()
                .map(p -> (JProperty) visit(p)).toList(), JSON_TREE))
            .build();
    }

    @Override
    public JNode visitPropertyNode(PropertyNodeContext ctx) {
        return new JPropertyBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .key(unquote(ctx.STRING().getText()))
            .value(visit(ctx.valueNode()))
            .build();
    }

    @Override
    public JNode visitArrayNode(ArrayNodeContext ctx) {
        return new JArrayBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .elements(ctx.valueNode().stream().map(this::visit).toList())
            .build();
    }

    @Override
    public JNode visitTrueNode(TrueNodeContext ctx) {
        return new JBooleanBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .value(true).build();
    }

    @Override
    public JNode visitFalseNode(FalseNodeContext ctx) {
        return new JBooleanBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .value(false).build();
    }

    @Override
    public JNode visitStringNode(StringNodeContext ctx) {
        return new JStringBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .value(toEncoded(ctx.STRING().getText()))
            .build();
    }

    @Override
    public JNode visitIntegerNode(IntegerNodeContext ctx) {
        return new JIntegerBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .value(Long.valueOf(ctx.INTEGER().getText()))
            .build();
    }

    @Override
    public JNode visitFloatNode(FloatNodeContext ctx) {
        return new JFloatBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .value(Double.valueOf(ctx.FLOAT().getText()))
            .build();
    }

    @Override
    public JNode visitDoubleNode(DoubleNodeContext ctx) {
        return new JDoubleBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .value(Double.valueOf(ctx.DOUBLE().getText()))
            .build();
    }

    @Override
    public JNode visitNullNode(NullNodeContext ctx) {
        return new JNullBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .build();
    }
}