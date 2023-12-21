package com.relogiclabs.json.schema.internal.tree;

import com.relogiclabs.json.schema.function.CoreFunctions4;
import com.relogiclabs.json.schema.internal.antlr.SchemaParser;
import com.relogiclabs.json.schema.internal.antlr.SchemaParserBaseVisitor;
import com.relogiclabs.json.schema.internal.builder.JAliasBuilder;
import com.relogiclabs.json.schema.internal.builder.JArrayBuilder;
import com.relogiclabs.json.schema.internal.builder.JBooleanBuilder;
import com.relogiclabs.json.schema.internal.builder.JDataTypeBuilder;
import com.relogiclabs.json.schema.internal.builder.JDefinitionBuilder;
import com.relogiclabs.json.schema.internal.builder.JDoubleBuilder;
import com.relogiclabs.json.schema.internal.builder.JFloatBuilder;
import com.relogiclabs.json.schema.internal.builder.JFunctionBuilder;
import com.relogiclabs.json.schema.internal.builder.JIncludeBuilder;
import com.relogiclabs.json.schema.internal.builder.JIntegerBuilder;
import com.relogiclabs.json.schema.internal.builder.JNullBuilder;
import com.relogiclabs.json.schema.internal.builder.JObjectBuilder;
import com.relogiclabs.json.schema.internal.builder.JPragmaBuilder;
import com.relogiclabs.json.schema.internal.builder.JPropertyBuilder;
import com.relogiclabs.json.schema.internal.builder.JReceiverBuilder;
import com.relogiclabs.json.schema.internal.builder.JRootBuilder;
import com.relogiclabs.json.schema.internal.builder.JStringBuilder;
import com.relogiclabs.json.schema.internal.builder.JTitleBuilder;
import com.relogiclabs.json.schema.internal.builder.JUndefinedBuilder;
import com.relogiclabs.json.schema.internal.builder.JValidatorBuilder;
import com.relogiclabs.json.schema.internal.builder.JVersionBuilder;
import com.relogiclabs.json.schema.tree.Context;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.type.JAlias;
import com.relogiclabs.json.schema.type.JDataType;
import com.relogiclabs.json.schema.type.JDefinition;
import com.relogiclabs.json.schema.type.JFunction;
import com.relogiclabs.json.schema.type.JInclude;
import com.relogiclabs.json.schema.type.JNode;
import com.relogiclabs.json.schema.type.JPragma;
import com.relogiclabs.json.schema.type.JProperty;
import com.relogiclabs.json.schema.type.JReceiver;
import com.relogiclabs.json.schema.type.JTitle;
import com.relogiclabs.json.schema.type.JValidator;
import com.relogiclabs.json.schema.type.JVersion;
import com.relogiclabs.json.schema.type.JsonType;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.relogiclabs.json.schema.internal.tree.TreeHelper.requireUniqueness;
import static com.relogiclabs.json.schema.internal.util.StringHelper.toEncoded;
import static com.relogiclabs.json.schema.internal.util.StringHelper.unquote;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP04;
import static java.util.stream.Collectors.joining;

public final class SchemaTreeVisitor extends SchemaParserBaseVisitor<JNode> {
    private final Map<JNode, JNode> relations;
    private final RuntimeContext runtime;

    public SchemaTreeVisitor(RuntimeContext runtime) {
        this.runtime = runtime;
        this.relations = new HashMap<>();
    }

    @Override
    public JNode visitAggregateSchema(SchemaParser.AggregateSchemaContext ctx) {
        return new JRootBuilder()
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
        return new JRootBuilder()
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
        runtime.getFunctions().addClass(CoreFunctions4.class.getName(), null);
        return contexts.stream().map(c -> (JInclude) visit(c)).toList();
    }

    @Override
    public JNode visitTitle(SchemaParser.TitleContext ctx) {
        return new JTitleBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .title(ctx.STRING().getText())
                .build();
    }

    @Override
    public JNode visitVersion(SchemaParser.VersionContext ctx) {
        return new JVersionBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .version(ctx.VERSION_NUMBER1().getText())
                .build();
    }

    @Override
    public JNode visitInclude(SchemaParser.IncludeContext ctx) {
        var include = new JIncludeBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .className(ctx.IDENTIFIER().stream().map(ParseTree::getText)
                        .collect(joining(",")))
                .build();
        return runtime.getFunctions().addClass(include);
    }

    @Override
    public JNode visitPragma(SchemaParser.PragmaContext ctx) {
        var pragma = new JPragmaBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .name(ctx.IDENTIFIER().getText())
                .value(visit(ctx.primitive())).build();
        return runtime.getPragmas().addPragma(pragma);
    }

    @Override
    public JNode visitDefine(SchemaParser.DefineContext ctx) {
        var definition = new JDefinitionBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .alias((JAlias) visit(ctx.alias()))
                .validator((JValidator) visit(ctx.validatorMain()))
                .build();
        return runtime.addDefinition(definition);
    }

    @Override
    public JNode visitAlias(SchemaParser.AliasContext ctx) {
        return new JAliasBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .name(ctx.ALIAS().getText())
                .build();
    }

    @Override
    public JNode visitValidatorMain(SchemaParser.ValidatorMainContext ctx) {
        return new JValidatorBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(visit(ctx.value()))
                .functions(ctx.function().stream().map(c -> (JFunction) visit(c)).toList())
                .dataTypes(ctx.datatype().stream().map(c -> (JDataType) visit(c)).toList())
                .receivers(ctx.receiver().stream().map(c -> (JReceiver) visit(c)).toList())
                .optional(ctx.OPTIONAL() != null)
                .build();
    }

    @Override
    public JNode visitValidator(SchemaParser.ValidatorContext ctx) {
        if(ctx.alias() != null) return visit(ctx.alias());
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
        return new JObjectBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .properties(requireUniqueness(ctx.property().stream()
                        .map(c -> (JProperty) visit(c)).toList(), PROP04))
                .build();
    }

    @Override
    public JNode visitProperty(SchemaParser.PropertyContext ctx) {
        return new JPropertyBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .key(unquote(ctx.STRING().getText()))
                .value(visit(ctx.validator()))
                .build();
    }

    @Override
    public JNode visitArray(SchemaParser.ArrayContext ctx) {
        return new JArrayBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .elements(ctx.validator().stream().map(this::visit).toList())
                .build();
    }

    @Override
    public JNode visitDatatype(SchemaParser.DatatypeContext ctx) {
        return new JDataTypeBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .jsonType(JsonType.from(ctx.DATATYPE()))
                .alias((JAlias) visit(ctx.alias()))
                .nested(ctx.STAR() != null)
                .build();
    }

    @Override
    public JNode visitFunction(SchemaParser.FunctionContext ctx) {
        return new JFunctionBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .name(ctx.FUNCTION().getText())
                .arguments(ctx.argument().stream().map(this::visit).toList())
                .nested(ctx.STAR() != null)
                .build();
    }

    @Override
    public JNode visitArgument(SchemaParser.ArgumentContext ctx) {
        if(ctx.value() != null) return visit(ctx.value());
        if(ctx.receiver() != null) return visit(ctx.receiver());
        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public JNode visitReceiver(SchemaParser.ReceiverContext ctx) {
        return new JReceiverBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .name(ctx.RECEIVER().getText()).build();
    }

    @Override
    public JNode visitPrimitiveTrue(SchemaParser.PrimitiveTrueContext ctx) {
        return new JBooleanBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(true).build();
    }

    @Override
    public JNode visitPrimitiveFalse(SchemaParser.PrimitiveFalseContext ctx) {
        return new JBooleanBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(false).build();
    }

    @Override
    public JNode visitPrimitiveString(SchemaParser.PrimitiveStringContext ctx) {
        return new JStringBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(toEncoded(ctx.STRING().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveInteger(SchemaParser.PrimitiveIntegerContext ctx) {
        return new JIntegerBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Long.valueOf(ctx.INTEGER().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveFloat(SchemaParser.PrimitiveFloatContext ctx) {
        return new JFloatBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Double.valueOf(ctx.FLOAT().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveDouble(SchemaParser.PrimitiveDoubleContext ctx) {
        return new JDoubleBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .value(Double.valueOf(ctx.DOUBLE().getText()))
                .build();
    }

    @Override
    public JNode visitPrimitiveNull(SchemaParser.PrimitiveNullContext ctx) {
        return new JNullBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .build();
    }

    @Override
    public JNode visitPrimitiveUndefined(SchemaParser.PrimitiveUndefinedContext ctx) {
        return new JUndefinedBuilder()
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