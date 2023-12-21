package com.relogiclabs.json.schema.internal.tree;

import com.relogiclabs.json.schema.internal.antlr.JsonParser;
import com.relogiclabs.json.schema.internal.antlr.JsonParserBaseVisitor;
import com.relogiclabs.json.schema.internal.builder.JArrayBuilder;
import com.relogiclabs.json.schema.internal.builder.JBooleanBuilder;
import com.relogiclabs.json.schema.internal.builder.JDoubleBuilder;
import com.relogiclabs.json.schema.internal.builder.JFloatBuilder;
import com.relogiclabs.json.schema.internal.builder.JIntegerBuilder;
import com.relogiclabs.json.schema.internal.builder.JNullBuilder;
import com.relogiclabs.json.schema.internal.builder.JObjectBuilder;
import com.relogiclabs.json.schema.internal.builder.JPropertyBuilder;
import com.relogiclabs.json.schema.internal.builder.JRootBuilder;
import com.relogiclabs.json.schema.internal.builder.JStringBuilder;
import com.relogiclabs.json.schema.tree.Context;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.type.JNode;
import com.relogiclabs.json.schema.type.JProperty;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.json.schema.internal.tree.TreeHelper.requireUniqueness;
import static com.relogiclabs.json.schema.internal.util.StringHelper.toEncoded;
import static com.relogiclabs.json.schema.internal.util.StringHelper.unquote;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP03;

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
                .value(visit(ctx.value()))
                .build();
    }

    @Override
    public JNode visitValue(JsonParser.ValueContext ctx) {
        if(ctx.primitive() != null) return visit(ctx.primitive());
        if(ctx.array() != null) return visit(ctx.array());
        if(ctx.object() != null) return visit(ctx.object());
        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public JNode visitObject(JsonParser.ObjectContext ctx) {
        return new JObjectBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .properties(requireUniqueness(ctx.property().stream()
                        .map(p -> (JProperty) visit(p)).toList(), PROP03))
                .build();
    }

    @Override
    public JNode visitProperty(JsonParser.PropertyContext ctx) {
        return new JPropertyBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .key(unquote(ctx.STRING().getText()))
                .value(visit(ctx.value()))
                .build();
    }

    @Override
    public JNode visitArray(JsonParser.ArrayContext ctx) {
        return new JArrayBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .elements(ctx.value().stream().map(this::visit).toList())
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