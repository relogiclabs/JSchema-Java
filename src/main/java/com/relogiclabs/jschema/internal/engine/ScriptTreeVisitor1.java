package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.exception.CommonException;
import com.relogiclabs.jschema.exception.ScriptRuntimeException;
import com.relogiclabs.jschema.exception.ScriptTemplateException;
import com.relogiclabs.jschema.internal.antlr.SchemaParser;
import com.relogiclabs.jschema.internal.antlr.SchemaParserBaseVisitor;
import com.relogiclabs.jschema.internal.script.GArray;
import com.relogiclabs.jschema.internal.script.GControl;
import com.relogiclabs.jschema.internal.script.GDouble;
import com.relogiclabs.jschema.internal.script.GFunction;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GIterator;
import com.relogiclabs.jschema.internal.script.GObject;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EBoolean;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import static com.relogiclabs.jschema.internal.antlr.SchemaLexer.G_STRING;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidReturnType;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnRuntime;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnSystemException;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.formatFunctionName;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.getFunctionMode;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.toConstraintName;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.toParameters;
import static com.relogiclabs.jschema.internal.script.GBoolean.FALSE;
import static com.relogiclabs.jschema.internal.script.GBoolean.TRUE;
import static com.relogiclabs.jschema.internal.script.GControl.BREAK;
import static com.relogiclabs.jschema.internal.script.GFunction.isConstraint;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.subList;
import static com.relogiclabs.jschema.internal.util.StringHelper.toEncoded;
import static com.relogiclabs.jschema.message.ErrorCode.ARRL01;
import static com.relogiclabs.jschema.message.ErrorCode.BLOK01;
import static com.relogiclabs.jschema.message.ErrorCode.EXPR01;
import static com.relogiclabs.jschema.message.ErrorCode.EXPR02;
import static com.relogiclabs.jschema.message.ErrorCode.FORS01;
import static com.relogiclabs.jschema.message.ErrorCode.FREC01;
import static com.relogiclabs.jschema.message.ErrorCode.FUNS07;
import static com.relogiclabs.jschema.message.ErrorCode.IFST01;
import static com.relogiclabs.jschema.message.ErrorCode.IFST02;
import static com.relogiclabs.jschema.message.ErrorCode.OBJL01;
import static com.relogiclabs.jschema.message.ErrorCode.RETN02;
import static com.relogiclabs.jschema.message.ErrorCode.RETN03;
import static com.relogiclabs.jschema.message.ErrorCode.SRPT01;
import static com.relogiclabs.jschema.message.ErrorCode.SRPT02;
import static com.relogiclabs.jschema.message.ErrorCode.VARD03;
import static com.relogiclabs.jschema.message.ErrorCode.VRIN01;
import static com.relogiclabs.jschema.message.ErrorCode.WHIL01;
import static com.relogiclabs.jschema.type.ENull.NULL;
import static com.relogiclabs.jschema.type.EUndefined.UNDEFINED;
import static com.relogiclabs.jschema.type.EValue.VOID;
import static lombok.AccessLevel.NONE;

@Getter
public abstract class ScriptTreeVisitor1 extends SchemaParserBaseVisitor<Evaluator>
                                         implements ScriptTreeVisitor {
    private static final Evaluator VOID_SUPPLIER = s -> VOID;
    private static final Evaluator TRUE_SUPPLIER = s -> TRUE;
    private static final Evaluator FALSE_SUPPLIER = s -> FALSE;
    private static final Evaluator NULL_SUPPLIER = S -> NULL;
    private static final Evaluator UNDEFINED_SUPPLIER = s -> UNDEFINED;
    private static final Evaluator BREAK_SUPPLIER = s -> BREAK;

    final RuntimeContext runtime;
    private final ParseTreeProperty<Evaluator> scripts;
    @Getter(NONE) private Class<?> returnType;

    public ScriptTreeVisitor1(RuntimeContext runtime) {
        this.runtime = runtime;
        this.scripts = new ParseTreeProperty<>();
    }

    @Override
    public Evaluator visitCompleteSchema(SchemaParser.CompleteSchemaContext ctx) {
        var scripts = ctx.scriptNode().stream().map(this::processScript).toList();
        if(scripts.isEmpty()) return VOID_SUPPLIER;
        return tryCatch(scope -> {
            for(var s : scripts) s.evaluate(scope);
            return VOID;
        }, SRPT01, ctx);
    }

    @Override
    public Evaluator visitShortSchema(SchemaParser.ShortSchemaContext ctx) {
        return VOID_SUPPLIER;
    }

    private Evaluator processScript(ParserRuleContext context) {
        var evaluator = visit(context);
        scripts.put(context, evaluator);
        return evaluator;
    }

    @Override
    public Evaluator visitScriptNode(SchemaParser.ScriptNodeContext ctx) {
        var statements = ctx.globalStatement().stream().map(this::visit).toList();
        return tryCatch(scope -> {
            for(var s : statements) s.evaluate(scope);
            return VOID;
        }, SRPT02, ctx);
    }

    @Override
    public Evaluator visitFunctionDeclaration(SchemaParser.FunctionDeclarationContext ctx) {
        var baseName = ctx.name.getText();
        var mode = getFunctionMode(ctx.G_CONSTRAINT(), ctx.G_FUTURE(), ctx.G_SUBROUTINE());
        var parameters = toParameters(subList(ctx.G_IDENTIFIER(), 1), ctx.G_ELLIPSIS());
        var constraint = isConstraint(mode);
        var functionName = constraint
                ? toConstraintName(formatFunctionName(baseName, parameters))
                : formatFunctionName(baseName, parameters);
        if(constraint) returnType = EBoolean.class;
        var functionBody = visit(ctx.blockStatement());
        returnType = null;
        var function = new GFunction(parameters, functionBody, mode);
        return tryCatch(scope -> {
            scope.addFunction(functionName, function);
            if(constraint) runtime.getFunctions()
                    .addFunction(new ScriptFunction(baseName, function));
            return VOID;
        }, FUNS07, ctx);
    }

    @Override
    public Evaluator visitVarStatement(SchemaParser.VarStatementContext ctx) {
        var varInitialization = ctx.varInitialization().stream().map(this::visit).toList();
        return tryCatch(scope -> {
            for(var i : varInitialization) i.evaluate(scope);
            return VOID;
        }, VARD03, ctx);
    }

    @Override
    public Evaluator visitVarInitialization(SchemaParser.VarInitializationContext ctx) {
        var varName = ctx.G_IDENTIFIER().getText();
        var expression = visit(ctx.expression(), VOID_SUPPLIER);
        return tryCatch(scope -> {
            scope.addVariable(varName, expression.evaluate(scope));
            return VOID;
        }, VRIN01, ctx);
    }

    @Override
    public Evaluator visitExpressionStatement(SchemaParser.ExpressionStatementContext ctx) {
        var expression = visit(ctx.expression());
        return tryCatch(scope -> {
            expression.evaluate(scope);
            return VOID;
        }, EXPR01, ctx);
    }

    @Override
    public Evaluator visitIfStatement(SchemaParser.IfStatementContext ctx) {
        var condition = visit(ctx.expression());
        var thenStatement = visit(ctx.statement(0));
        if(ctx.G_ELSE() == null) return tryCatch(scope -> {
            if(condition.evaluate(scope).toBoolean())
                return thenStatement.evaluate(scope);
            return VOID;
        }, IFST01, ctx);

        var elseStatement = visit(ctx.statement(1));
        return tryCatch(scope -> {
            if(condition.evaluate(scope).toBoolean())
                return thenStatement.evaluate(scope);
            else return elseStatement.evaluate(scope);
        }, IFST02, ctx);
    }

    @Override
    public Evaluator visitWhileStatement(SchemaParser.WhileStatementContext ctx) {
        var condition = visit(ctx.expression());
        var statement = visit(ctx.statement());
        return tryCatch(scope -> {
            while(condition.evaluate(scope).toBoolean()) {
                var result = statement.evaluate(scope);
                if(result instanceof GControl ctrl) return ctrl.toIteration();
            }
            return VOID;
        }, WHIL01, ctx);
    }

    @Override
    public Evaluator visitForStatement(SchemaParser.ForStatementContext ctx) {
        var initialization = visit(ctx.varStatement(), visit(ctx.initialization, VOID_SUPPLIER));
        var condition = visit(ctx.condition, TRUE_SUPPLIER);
        var updation = visit(ctx.updation, VOID_SUPPLIER);
        var statement = visit(ctx.statement());
        return tryCatch(scope -> {
            var forScope = new ScopeContext(scope);
            for(initialization.evaluate(forScope);
                condition.evaluate(forScope).toBoolean();
                updation.evaluate(forScope)) {
                var result = statement.evaluate(forScope);
                if(result instanceof GControl ctrl) return ctrl.toIteration();
            }
            return VOID;
        }, FORS01, ctx);
    }

    @Override
    public Evaluator visitExpressionList(SchemaParser.ExpressionListContext ctx) {
        var expressions = ctx.expression().stream().map(this::visit).toList();
        return tryCatch(scope -> {
            for(var e : expressions) e.evaluate(scope);
            return VOID;
        }, EXPR02, ctx);
    }

    @Override
    public Evaluator visitForeachStatement(SchemaParser.ForeachStatementContext ctx) {
        var varName = ctx.G_IDENTIFIER().getText();
        var collection = visit(ctx.expression());
        var statement = visit(ctx.statement());
        return tryCatch(scope -> {
            var forScope = new ScopeContext(scope);
            var reference = forScope.addVariable(varName, VOID);
            for(var v : GIterator.of(collection.evaluate(scope))) {
                reference.setValue(v);
                var result = statement.evaluate(forScope);
                if(result instanceof GControl ctrl) return ctrl.toIteration();
            }
            return VOID;
        }, FREC01, ctx);
    }

    @Override
    public Evaluator visitReturnStatement(SchemaParser.ReturnStatementContext ctx) {
        var expression = visit(ctx.expression());
        if(returnType == null) return tryCatch(scope -> GControl.ofReturn(
                expression.evaluate(scope)), RETN02, ctx);
        var thisReturnType = returnType;
        return tryCatch(scope -> {
            var v1 = expression.evaluate(scope);
            if(!thisReturnType.isInstance(v1))
                throw failOnInvalidReturnType(v1, ctx.expression().getStart());
            return GControl.ofReturn(v1);
        }, RETN03, ctx);
    }

    @Override
    public Evaluator visitBreakStatement(SchemaParser.BreakStatementContext ctx) {
        return BREAK_SUPPLIER;
    }

    @Override
    public Evaluator visitBlockStatement(SchemaParser.BlockStatementContext ctx) {
        var statements = ctx.statement().stream().map(this::visit).toList();
        return tryCatch(scope -> {
            var blockScope = new ScopeContext(scope);
            for(var s : statements) {
                var result = s.evaluate(blockScope);
                if(result instanceof GControl ctrl) return ctrl;
            }
            return VOID;
        }, BLOK01, ctx);
    }

    @Override
    public Evaluator visitTrueLiteral(SchemaParser.TrueLiteralContext ctx) {
        return TRUE_SUPPLIER;
    }

    @Override
    public Evaluator visitFalseLiteral(SchemaParser.FalseLiteralContext ctx) {
        return FALSE_SUPPLIER;
    }

    @Override
    public Evaluator visitNullLiteral(SchemaParser.NullLiteralContext ctx) {
        return NULL_SUPPLIER;
    }

    @Override
    public Evaluator visitUndefinedLiteral(SchemaParser.UndefinedLiteralContext ctx) {
        return UNDEFINED_SUPPLIER;
    }

    @Override
    public Evaluator visitIntegerLiteral(SchemaParser.IntegerLiteralContext ctx) {
        var value = GInteger.of(Long.parseLong(ctx.G_INTEGER().getText()));
        return scope -> value;
    }

    @Override
    public Evaluator visitDoubleLiteral(SchemaParser.DoubleLiteralContext ctx) {
        var value = GDouble.of(Double.parseDouble(ctx.G_DOUBLE().getText()));
        return scope -> value;
    }

    @Override
    public Evaluator visitStringLiteral(SchemaParser.StringLiteralContext ctx) {
        var value = GString.of(toEncoded(ctx.getText()));
        return scope -> value;
    }

    @Override
    public Evaluator visitArrayLiteral(SchemaParser.ArrayLiteralContext ctx) {
        var list = ctx.expression().stream().map(this::visit).toList();
        return tryCatch(scope -> new GArray(list.stream().map(e
                -> e.evaluate(scope)).toList()), ARRL01, ctx);
    }

    @Override
    public Evaluator visitObjectLiteral(SchemaParser.ObjectLiteralContext ctx) {
        var keys = ctx.keys.stream().map(t -> t.getType() == G_STRING
                ? toEncoded(t.getText()) : t.getText()).toList();
        var values = ctx.values.stream().map(this::visit).toList();
        return tryCatch(scope -> new GObject(keys, values.stream().map(v
                -> v.evaluate(scope)).toList()), OBJL01, ctx);
    }

    static Evaluator tryCatch(Evaluator evaluator, String code,
                ParserRuleContext context) {
        return scope -> {
            try {
                return evaluator.evaluate(scope);
            } catch(ScriptRuntimeException | ScriptTemplateException e) {
                throw e;
            } catch(CommonException e) {
                throw failOnRuntime(e.getCode(), e.getMessage(), context.start, e);
            } catch(Exception e) {
                throw failOnSystemException(code, e, context.start);
            }
        };
    }

    @Override
    public Evaluator visit(ParseTree tree) {
        if(tree == null) return null;
        return super.visit(tree);
    }

    private Evaluator visit(ParseTree tree, Evaluator defaultValue) {
        if(tree == null) return defaultValue;
        return super.visit(tree);
    }
}