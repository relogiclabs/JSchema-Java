package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.function.CoreFunctions4;
import com.relogiclabs.jschema.internal.antlr.SchemaParser;
import com.relogiclabs.jschema.internal.antlr.SchemaParserBaseVisitor;
import com.relogiclabs.jschema.internal.builder.JAliasBuilder;
import com.relogiclabs.jschema.internal.builder.JArrayBuilder;
import com.relogiclabs.jschema.internal.builder.JBooleanBuilder;
import com.relogiclabs.jschema.internal.builder.JDataTypeBuilder;
import com.relogiclabs.jschema.internal.builder.JDefinitionBuilder;
import com.relogiclabs.jschema.internal.builder.JDoubleBuilder;
import com.relogiclabs.jschema.internal.builder.JFloatBuilder;
import com.relogiclabs.jschema.internal.builder.JFunctionBuilder;
import com.relogiclabs.jschema.internal.builder.JImportBuilder;
import com.relogiclabs.jschema.internal.builder.JIntegerBuilder;
import com.relogiclabs.jschema.internal.builder.JNullBuilder;
import com.relogiclabs.jschema.internal.builder.JObjectBuilder;
import com.relogiclabs.jschema.internal.builder.JPragmaBuilder;
import com.relogiclabs.jschema.internal.builder.JPropertyBuilder;
import com.relogiclabs.jschema.internal.builder.JReceiverBuilder;
import com.relogiclabs.jschema.internal.builder.JRootBuilder;
import com.relogiclabs.jschema.internal.builder.JScriptBuilder;
import com.relogiclabs.jschema.internal.builder.JStringBuilder;
import com.relogiclabs.jschema.internal.builder.JTitleBuilder;
import com.relogiclabs.jschema.internal.builder.JUndefinedBuilder;
import com.relogiclabs.jschema.internal.builder.JValidatorBuilder;
import com.relogiclabs.jschema.internal.builder.JVersionBuilder;
import com.relogiclabs.jschema.internal.engine.Evaluator;
import com.relogiclabs.jschema.internal.engine.ScriptTreeVisitor;
import com.relogiclabs.jschema.node.JAlias;
import com.relogiclabs.jschema.node.JDataType;
import com.relogiclabs.jschema.node.JDefinition;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JImport;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JPragma;
import com.relogiclabs.jschema.node.JProperty;
import com.relogiclabs.jschema.node.JReceiver;
import com.relogiclabs.jschema.node.JScript;
import com.relogiclabs.jschema.node.JTitle;
import com.relogiclabs.jschema.node.JValidator;
import com.relogiclabs.jschema.node.JVersion;
import com.relogiclabs.jschema.node.JsonType;
import com.relogiclabs.jschema.tree.Context;
import com.relogiclabs.jschema.tree.RuntimeContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.relogiclabs.jschema.internal.tree.TreeHelper.requireUniqueness;
import static com.relogiclabs.jschema.internal.util.StringHelper.toEncoded;
import static com.relogiclabs.jschema.internal.util.StringHelper.unquote;
import static com.relogiclabs.jschema.tree.TreeType.SCHEMA_TREE;
import static java.util.stream.Collectors.joining;

public final class SchemaTreeVisitor extends SchemaParserBaseVisitor<JNode> {
    private final Map<JNode, JNode> relations;
    private final RuntimeContext runtime;
    private final ParseTreeProperty<Evaluator> scripts;

    public SchemaTreeVisitor(ScriptTreeVisitor scriptVisitor) {
        this.runtime = scriptVisitor.getRuntime();
        this.scripts = scriptVisitor.getScripts();
        this.relations = new HashMap<>();
    }

    @Override
    public JNode visitCompleteSchema(SchemaParser.CompleteSchemaContext ctx) {
        return new JRootBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .title((JTitle) visit(ctx.titleNode()))
                .version((JVersion) visit(ctx.versionNode()))
                .imports(processImports(ctx.importNode()))
                .pragmas(ctx.pragmaNode().stream().map(c -> (JPragma) visit(c)).toList())
                .definitions(ctx.defineNode().stream().map(c -> (JDefinition) visit(c)).toList())
                .scripts(ctx.scriptNode().stream().map(s -> (JScript) visit(s)).toList())
                .value(visit(ctx.schemaMain()))
                .build();
    }

    @Override
    public JNode visitShortSchema(SchemaParser.ShortSchemaContext ctx) {
        return new JRootBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .imports(processImports(Collections.emptyList()))
                .value(visit(ctx.validatorNode()))
                .build();
    }

    private List<JImport> processImports(List<SchemaParser.ImportNodeContext> contexts) {
        runtime.getFunctions().addClass(CoreFunctions4.class.getName(), null);
        return contexts.stream().map(c -> (JImport) visit(c)).toList();
    }

    @Override
    public JNode visitTitleNode(SchemaParser.TitleNodeContext ctx) {
        return new JTitleBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .title(ctx.STRING().getText())
                .build();
    }

    @Override
    public JNode visitVersionNode(SchemaParser.VersionNodeContext ctx) {
        return new JVersionBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .version(ctx.STRING().getText())
                .build();
    }

    @Override
    public JNode visitImportNode(SchemaParser.ImportNodeContext ctx) {
        var importNode = new JImportBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .className(ctx.FULL_IDENTIFIER().stream().map(ParseTree::getText)
                        .collect(joining(",")))
                .build();
        return runtime.getFunctions().addClass(importNode);
    }

    @Override
    public JNode visitPragmaNode(SchemaParser.PragmaNodeContext ctx) {
        var pragma = new JPragmaBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .name(ctx.FULL_IDENTIFIER().getText())
                .value(visit(ctx.primitiveNode())).build();
        return runtime.getPragmas().addPragma(pragma);
    }

    @Override
    public JNode visitDefineNode(SchemaParser.DefineNodeContext ctx) {
        var definition = new JDefinitionBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .alias((JAlias) visit(ctx.aliasNode()))
                .validator((JValidator) visit(ctx.validatorMain()))
                .build();
        return runtime.addDefinition(definition);
    }

    @Override
    public JNode visitScriptNode(SchemaParser.ScriptNodeContext ctx) {
        var source = ctx.start.getInputStream().getText(new Interval(
                ctx.start.getStartIndex(),
                ctx.stop.getStopIndex()));
        return new JScriptBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .evaluator(scripts.get(ctx))
                .source(source)
                .build();
    }

    @Override
    public JNode visitAliasNode(SchemaParser.AliasNodeContext ctx) {
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
                .value(visit(ctx.valueNode()))
                .functions(ctx.functionNode().stream().map(c -> (JFunction) visit(c)).toList())
                .dataTypes(ctx.datatypeNode().stream().map(c -> (JDataType) visit(c)).toList())
                .receivers(ctx.receiverNode().stream().map(c -> (JReceiver) visit(c)).toList())
                .optional(ctx.OPTIONAL() != null)
                .build();
    }

    @Override
    public JNode visitObjectNode(SchemaParser.ObjectNodeContext ctx) {
        return new JObjectBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .properties(requireUniqueness(ctx.propertyNode().stream()
                        .map(c -> (JProperty) visit(c)).toList(), SCHEMA_TREE))
                .build();
    }

    @Override
    public JNode visitPropertyNode(SchemaParser.PropertyNodeContext ctx) {
        return new JPropertyBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .key(unquote(ctx.STRING().getText()))
                .value(visit(ctx.validatorNode()))
                .build();
    }

    @Override
    public JNode visitArrayNode(SchemaParser.ArrayNodeContext ctx) {
        return new JArrayBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .elements(ctx.validatorNode().stream().map(this::visit).toList())
                .build();
    }

    @Override
    public JNode visitDatatypeNode(SchemaParser.DatatypeNodeContext ctx) {
        return new JDataTypeBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .jsonType(JsonType.from(ctx.DATATYPE()))
                .alias((JAlias) visit(ctx.aliasNode()))
                .nested(ctx.STAR() != null)
                .build();
    }

    @Override
    public JNode visitFunctionNode(SchemaParser.FunctionNodeContext ctx) {
        return new JFunctionBuilder()
                .relations(relations)
                .context(new Context(ctx, runtime))
                .name(ctx.FUNCTION().getText())
                .arguments(ctx.argumentNode().stream().map(this::visit).toList())
                .nested(ctx.STAR() != null)
                .build();
    }

    @Override
    public JNode visitReceiverNode(SchemaParser.ReceiverNodeContext ctx) {
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