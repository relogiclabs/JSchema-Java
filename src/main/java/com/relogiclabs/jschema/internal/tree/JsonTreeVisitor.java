package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.internal.antlr.JsonParser;
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
    public JNode visitJson(JsonParser.JsonContext ctx) {
        return new JRootBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(visit(ctx.valueNode()))
                .build();
    }

    @Override
    public JNode visitObjectNode(JsonParser.ObjectNodeContext ctx) {
        return new JObjectBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .properties(requireUniqueness(ctx.propertyNode().stream()
                        .map(p -> (JProperty) visit(p)).toList(), JSON_TREE))
                .build();
    }

    @Override
    public JNode visitPropertyNode(JsonParser.PropertyNodeContext ctx) {
        return new JPropertyBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .key(unquote(ctx.STRING().getText()))
                .value(visit(ctx.valueNode()))
                .build();
    }

    @Override
    public JNode visitArrayNode(JsonParser.ArrayNodeContext ctx) {
        return new JArrayBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .elements(ctx.valueNode().stream().map(this::visit).toList())
                .build();
    }

    @Override
    public JNode visitPrimitiveTrue(JsonParser.PrimitiveTrueContext ctx) {
        return new JBooleanBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(true).build();
    }

    @Override
    public JNode visitPrimitiveFalse(JsonParser.PrimitiveFalseContext ctx) {
        return new JBooleanBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(false).build();
    }

    @Override
    public JNode visitPrimitiveString(JsonParser.PrimitiveStringContext ctx) {
        return new JStringBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(toEncoded(ctx.STRING().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveInteger(JsonParser.PrimitiveIntegerContext ctx) {
        return new JIntegerBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Long.valueOf(ctx.INTEGER().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveFloat(JsonParser.PrimitiveFloatContext ctx) {
        return new JFloatBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Double.valueOf(ctx.FLOAT().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveDouble(JsonParser.PrimitiveDoubleContext ctx) {
        return new JDoubleBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Double.valueOf(ctx.DOUBLE().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveNull(JsonParser.PrimitiveNullContext ctx) {
        return new JNullBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .build();
    }
}