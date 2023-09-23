package com.relogiclabs.json.schema.internal.tree;

import com.relogiclabs.json.schema.internal.antlr.JsonParser;
import com.relogiclabs.json.schema.internal.antlr.JsonParserBaseVisitor;
import com.relogiclabs.json.schema.internal.util.StringHelper;
import com.relogiclabs.json.schema.tree.Context;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.types.JArray;
import com.relogiclabs.json.schema.types.JBoolean;
import com.relogiclabs.json.schema.types.JDouble;
import com.relogiclabs.json.schema.types.JFloat;
import com.relogiclabs.json.schema.types.JInteger;
import com.relogiclabs.json.schema.types.JNode;
import com.relogiclabs.json.schema.types.JNull;
import com.relogiclabs.json.schema.types.JObject;
import com.relogiclabs.json.schema.types.JProperty;
import com.relogiclabs.json.schema.types.JRoot;
import com.relogiclabs.json.schema.types.JString;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.json.schema.internal.tree.TreeHelper.checkForDuplicate;
import static com.relogiclabs.json.schema.internal.util.StringHelper.toEncoded;

public class JsonTreeVisitor extends JsonParserBaseVisitor<JNode> {
    private final Map<JNode, JNode> relations;
    private final RuntimeContext runtime;

    public JsonTreeVisitor(RuntimeContext runtime) {
        this.runtime = runtime;
        this.relations = new HashMap<>();
    }

    @Override
    public JNode visitJson(JsonParser.JsonContext ctx) {
        return JRoot.builder()
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
        return JObject.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .properties(checkForDuplicate(ctx.property().stream()
                        .map(p -> (JProperty) visit(p)).toList()))
                .build();
    }

    @Override
    public JNode visitProperty(JsonParser.PropertyContext ctx) {
        return JProperty.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .key(StringHelper.unquote(ctx.STRING().getText()))
                .value(visit(ctx.value()))
                .build();
    }

    @Override
    public JNode visitArray(JsonParser.ArrayContext ctx) {
        return JArray.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .elements(ctx.value().stream().map(this::visit).toList())
                .build();
    }

    @Override
    public JNode visitPrimitiveTrue(JsonParser.PrimitiveTrueContext ctx) {
        return JBoolean.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(true).build();
    }

    @Override
    public JNode visitPrimitiveFalse(JsonParser.PrimitiveFalseContext ctx) {
        return JBoolean.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(false).build();
    }

    @Override
    public JNode visitPrimitiveString(JsonParser.PrimitiveStringContext ctx) {
        return JString.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(toEncoded(ctx.STRING().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveInteger(JsonParser.PrimitiveIntegerContext ctx) {
        return JInteger.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Long.valueOf(ctx.INTEGER().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveFloat(JsonParser.PrimitiveFloatContext ctx) {
        return JFloat.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Double.valueOf(ctx.FLOAT().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveDouble(JsonParser.PrimitiveDoubleContext ctx) {
        return JDouble.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Double.valueOf(ctx.DOUBLE().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveNull(JsonParser.PrimitiveNullContext ctx) {
        return JNull.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .build();
    }
}