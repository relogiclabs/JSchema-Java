package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.internal.antlr.SchemaParser.AliasNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ArrayNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.CompleteSchemaContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.DatatypeNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.DefineNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.DoubleNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.FalseNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.FloatNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.FunctionNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ImportNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.IntegerNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.NullNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ObjectNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.PragmaNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.PropertyNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ReceiverNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ScriptNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ShortSchemaContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.StringNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.TitleNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.TrueNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.UndefinedNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ValidatorMainNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.VersionNodeContext;
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
    public JNode visitCompleteSchema(CompleteSchemaContext ctx) {
        return new JRootBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .title((JTitle) visit(ctx.titleNode()))
            .version((JVersion) visit(ctx.versionNode()))
            .imports(ctx.importNode().stream().map(i -> (JImport) visit(i)).toList())
            .pragmas(ctx.pragmaNode().stream().map(p -> (JPragma) visit(p)).toList())
            .definitions(ctx.defineNode().stream().map(d -> (JDefinition) visit(d)).toList())
            .scripts(ctx.scriptNode().stream().map(s -> (JScript) visit(s)).toList())
            .value(visit(ctx.schemaCoreNode()))
            .build();
    }

    @Override
    public JNode visitShortSchema(ShortSchemaContext ctx) {
        return new JRootBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .imports(Collections.emptyList())
            .value(visit(ctx.validatorNode()))
            .build();
    }

    @Override
    public JNode visitTitleNode(TitleNodeContext ctx) {
        return new JTitleBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .title(unquote(ctx.S_STRING().getText()))
            .build();
    }

    @Override
    public JNode visitVersionNode(VersionNodeContext ctx) {
        return new JVersionBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .version(unquote(ctx.S_STRING().getText()))
            .build();
    }

    @Override
    public JNode visitImportNode(ImportNodeContext ctx) {
        var importNode = new JImportBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .className(ctx.S_GENERAL_ID().stream().map(ParseTree::getText).collect(joining(",")))
            .build();
        return runtime.getFunctions().addClass(importNode);
    }

    @Override
    public JNode visitPragmaNode(PragmaNodeContext ctx) {
        var pragma = new JPragmaBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .name(ctx.S_GENERAL_ID().getText())
            .value(visit(ctx.primitiveNode())).build();
        return runtime.getPragmas().addPragma(pragma);
    }

    @Override
    public JNode visitDefineNode(DefineNodeContext ctx) {
        var definition = new JDefinitionBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .alias((JAlias) visit(ctx.aliasNode()))
            .validator((JValidator) visit(ctx.validatorMainNode()))
            .build();
        return runtime.addDefinition(definition);
    }

    @Override
    public JNode visitScriptNode(ScriptNodeContext ctx) {
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
    public JNode visitAliasNode(AliasNodeContext ctx) {
        return new JAliasBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .name(ctx.S_ALIAS().getText())
            .build();
    }

    @Override
    public JNode visitValidatorMainNode(ValidatorMainNodeContext ctx) {
        return new JValidatorBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .value(visit(ctx.valueNode()))
            .functions(ctx.functionNode().stream().map(f -> (JFunction) visit(f)).toList())
            .dataTypes(ctx.datatypeNode().stream().map(d -> (JDataType) visit(d)).toList())
            .receivers(ctx.receiverNode().stream().map(r -> (JReceiver) visit(r)).toList())
            .optional(ctx.S_OPTIONAL() != null)
            .build();
    }

    @Override
    public JNode visitObjectNode(ObjectNodeContext ctx) {
        return new JObjectBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .properties(requireUniqueness(ctx.propertyNode().stream()
                .map(c -> (JProperty) visit(c)).toList(), SCHEMA_TREE))
            .build();
    }

    @Override
    public JNode visitPropertyNode(PropertyNodeContext ctx) {
        return new JPropertyBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .key(unquote(ctx.S_STRING().getText()))
            .value(visit(ctx.validatorNode()))
            .build();
    }

    @Override
    public JNode visitArrayNode(ArrayNodeContext ctx) {
        return new JArrayBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .elements(ctx.validatorNode().stream().map(this::visit).toList())
            .build();
    }

    @Override
    public JNode visitDatatypeNode(DatatypeNodeContext ctx) {
        return new JDataTypeBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .jsonType(JsonType.from(ctx.S_DATATYPE().getSymbol()))
            .alias((JAlias) visit(ctx.aliasNode()))
            .nested(ctx.S_STAR() != null)
            .build();
    }

    @Override
    public JNode visitFunctionNode(FunctionNodeContext ctx) {
        return new JFunctionBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .name(ctx.S_FUNCTION().getText())
            .arguments(ctx.argumentNode().stream().map(this::visit).toList())
            .nested(ctx.S_STAR() != null)
            .build();
    }

    @Override
    public JNode visitReceiverNode(ReceiverNodeContext ctx) {
        return new JReceiverBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .name(ctx.S_RECEIVER().getText()).build();
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
            .value(toEncoded(ctx.S_STRING().getText()))
            .build();
    }

    @Override
    public JNode visitIntegerNode(IntegerNodeContext ctx) {
        return new JIntegerBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .value(Long.valueOf(ctx.S_INTEGER().getText()))
            .build();
    }

    @Override
    public JNode visitFloatNode(FloatNodeContext ctx) {
        return new JFloatBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .value(Double.valueOf(ctx.S_FLOAT().getText()))
            .build();
    }

    @Override
    public JNode visitDoubleNode(DoubleNodeContext ctx) {
        return new JDoubleBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .value(Double.valueOf(ctx.S_DOUBLE().getText()))
            .build();
    }

    @Override
    public JNode visitNullNode(NullNodeContext ctx) {
        return new JNullBuilder()
            .relations(relations)
            .context(new Context(ctx, runtime))
            .build();
    }

    @Override
    public JNode visitUndefinedNode(UndefinedNodeContext ctx) {
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