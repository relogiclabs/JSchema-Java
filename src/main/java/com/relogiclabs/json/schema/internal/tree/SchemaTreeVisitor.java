package com.relogiclabs.json.schema.internal.tree;

import com.relogiclabs.json.schema.function.CoreFunctions3;
import com.relogiclabs.json.schema.internal.antlr.SchemaParser;
import com.relogiclabs.json.schema.internal.antlr.SchemaParserBaseVisitor;
import com.relogiclabs.json.schema.internal.util.StringHelper;
import com.relogiclabs.json.schema.tree.Context;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.types.JAlias;
import com.relogiclabs.json.schema.types.JArray;
import com.relogiclabs.json.schema.types.JBoolean;
import com.relogiclabs.json.schema.types.JDataType;
import com.relogiclabs.json.schema.types.JDefinition;
import com.relogiclabs.json.schema.types.JDouble;
import com.relogiclabs.json.schema.types.JFloat;
import com.relogiclabs.json.schema.types.JFunction;
import com.relogiclabs.json.schema.types.JInclude;
import com.relogiclabs.json.schema.types.JInteger;
import com.relogiclabs.json.schema.types.JNode;
import com.relogiclabs.json.schema.types.JNull;
import com.relogiclabs.json.schema.types.JObject;
import com.relogiclabs.json.schema.types.JPragma;
import com.relogiclabs.json.schema.types.JProperty;
import com.relogiclabs.json.schema.types.JRoot;
import com.relogiclabs.json.schema.types.JString;
import com.relogiclabs.json.schema.types.JTitle;
import com.relogiclabs.json.schema.types.JUndefined;
import com.relogiclabs.json.schema.types.JValidator;
import com.relogiclabs.json.schema.types.JVersion;
import com.relogiclabs.json.schema.types.JsonType;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.relogiclabs.json.schema.internal.tree.TreeHelper.checkForDuplicate;
import static com.relogiclabs.json.schema.internal.util.StringHelper.toEncoded;
import static java.util.stream.Collectors.joining;

public class SchemaTreeVisitor extends SchemaParserBaseVisitor<JNode> {
    private final Map<JNode, JNode> relations;
    private final RuntimeContext runtime;

    public SchemaTreeVisitor(RuntimeContext runtime) {
        this.runtime = runtime;
        this.relations = new HashMap<>();
    }

    @Override
    public JNode visitAggregateSchema(SchemaParser.AggregateSchemaContext ctx) {
        return JRoot.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .title((JTitle) visit(ctx.title()))
                .version((JVersion) visit(ctx.version()))
                .includes(processIncludes(ctx.include()))
                .pragmas(ctx.pragma().stream().map(c -> (JPragma) visit(c)).toList())
                .definitions(ctx.define().stream().map(c -> (JDefinition) visit(c)).toList())
                .value(visit(ctx.schemaBase()))
                .build();
    }

    @Override
    public JNode visitCoreSchema(SchemaParser.CoreSchemaContext ctx) {
        return JRoot.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .includes(processIncludes(Collections.emptyList()))
                .value(visit(ctx.validator()))
                .build();
    }

    @Override
    public JNode visitSchemaBase(SchemaParser.SchemaBaseContext ctx) {
        return visit(ctx.validator());
    }

    private List<JInclude> processIncludes(List<SchemaParser.IncludeContext> contexts) {
        runtime.addClass(CoreFunctions3.class.getName(), null);
        return contexts.stream().map(c -> (JInclude) visit(c)).toList();
    }

    @Override
    public JNode visitTitle(SchemaParser.TitleContext ctx) {
        return JTitle.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .title(ctx.STRING().getText())
                .build();
    }

    @Override
    public JNode visitVersion(SchemaParser.VersionContext ctx) {
        return JVersion.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .version(ctx.VERSION_NUMBER1().getText())
                .build();
    }

    @Override
    public JNode visitInclude(SchemaParser.IncludeContext ctx) {
        var include = JInclude.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .className(ctx.IDENTIFIER().stream().map(ParseTree::getText)
                        .collect(joining(",")))
                .build();
        return runtime.addClass(include);
    }

    @Override
    public JNode visitPragma(SchemaParser.PragmaContext ctx) {
        var pragma = JPragma.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .name(ctx.IDENTIFIER().getText())
                .value(visit(ctx.primitive())).build();
        return runtime.addPragma(pragma);
    }

    @Override
    public JNode visitDefine(SchemaParser.DefineContext ctx) {
        var definition = JDefinition.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .alias((JAlias) visit(ctx.aliasName()))
                .validator((JValidator) visit(ctx.validatorMain()))
                .build();
        return runtime.addDefinition(definition);
    }

    @Override
    public JNode visitAliasName(SchemaParser.AliasNameContext ctx) {
        return JAlias.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .name(ctx.ALIAS().getText())
                .build();
    }

    @Override
    public JNode visitValidatorMain(SchemaParser.ValidatorMainContext ctx) {
        return JValidator.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(visit(ctx.value()))
                .functions(ctx.function().stream().map(c -> (JFunction) visit(c)).toList())
                .dataTypes(ctx.datatype().stream().map(c -> (JDataType) visit(c)).toList())
                .optional(ctx.OPTIONAL() != null)
                .build();
    }

    @Override
    public JNode visitValidator(SchemaParser.ValidatorContext ctx) {
        if(ctx.aliasName() != null) return visit(ctx.aliasName());
        if(ctx.validatorMain() != null) return visit(ctx.validatorMain());
        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public JNode visitValue(SchemaParser.ValueContext ctx) {
        if(ctx.primitive() != null) return visit(ctx.primitive());
        if(ctx.array() != null) return visit(ctx.array());
        if(ctx.object() != null) return visit(ctx.object());
        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public JNode visitObject(SchemaParser.ObjectContext ctx) {
        return JObject.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .properties(checkForDuplicate(ctx.property().stream()
                        .map(c -> (JProperty) visit(c)).toList()))
                .build();
    }

    @Override
    public JNode visitProperty(SchemaParser.PropertyContext ctx) {
        return JProperty.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .key(StringHelper.unquote(ctx.STRING().getText()))
                .value(visit(ctx.validator()))
                .build();
    }

    @Override
    public JNode visitArray(SchemaParser.ArrayContext ctx) {
        return JArray.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .elements(ctx.validator().stream().map(this::visit).toList())
                .build();
    }

    @Override
    public JNode visitDatatype(SchemaParser.DatatypeContext ctx) {
        return JDataType.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .jsonType(JsonType.from(ctx.DATATYPE()))
                .alias((JAlias) visit(ctx.aliasName()))
                .nested(ctx.STAR() != null)
                .build();
    }

    @Override
    public JNode visitFunction(SchemaParser.FunctionContext ctx) {
        return JFunction.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .name(ctx.FUNCTION().getText())
                .arguments(ctx.value().stream().map(this::visit).toList())
                .nested(ctx.STAR() != null)
                .build();
    }

    @Override
    public JNode visitPrimitiveTrue(SchemaParser.PrimitiveTrueContext ctx) {
        return JBoolean.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(true).build();
    }

    @Override
    public JNode visitPrimitiveFalse(SchemaParser.PrimitiveFalseContext ctx) {
        return JBoolean.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(false).build();
    }

    @Override
    public JNode visitPrimitiveString(SchemaParser.PrimitiveStringContext ctx) {
        return JString.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(toEncoded(ctx.STRING().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveInteger(SchemaParser.PrimitiveIntegerContext ctx) {
        return JInteger.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Long.valueOf(ctx.INTEGER().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveFloat(SchemaParser.PrimitiveFloatContext ctx) {
        return JFloat.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Double.valueOf(ctx.FLOAT().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveDouble(SchemaParser.PrimitiveDoubleContext ctx) {
        return JDouble.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Double.valueOf(ctx.DOUBLE().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveNull(SchemaParser.PrimitiveNullContext ctx) {
        return JNull.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .build();
    }

    @Override
    public JNode visitPrimitiveUndefined(SchemaParser.PrimitiveUndefinedContext ctx) {
        return JUndefined.builder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .build();
    }

    @Override
    public JNode visit(ParseTree tree) {
        if(tree == null) return null;
        return super.visit(tree);
    }
}