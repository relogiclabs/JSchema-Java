package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.exception.BaseRuntimeException;
import com.relogiclabs.jschema.exception.MultilevelRuntimeException;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ArrayLiteralContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.BlockStatementContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.BreakStatementContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.CompleteSchemaContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.DoubleLiteralContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ExpressionListContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ExpressionStatementContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.FalseLiteralContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ForStatementContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ForeachStatementContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.FunctionDeclarationContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.IfStatementContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.IntegerLiteralContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.NullLiteralContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ObjectLiteralContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ReturnStatementContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ScriptNodeContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ShortSchemaContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.StringLiteralContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.TrueLiteralContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.UndefinedLiteralContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.VarDeclarationContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.VarStatementContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.WhileStatementContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParserBaseVisitor;
import com.relogiclabs.jschema.internal.function.FunctionId;
import com.relogiclabs.jschema.internal.script.GArray;
import com.relogiclabs.jschema.internal.script.GControl;
import com.relogiclabs.jschema.internal.script.GDouble;
import com.relogiclabs.jschema.internal.script.GFunction;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GIterator;
import com.relogiclabs.jschema.internal.script.GObject;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.internal.tree.ScriptFunction;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EBoolean;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import static com.relogiclabs.jschema.internal.antlr.SchemaLexer.G_STRING;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidReturnType;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnSystemException;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.getFunctionMode;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.isConstraint;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.toParameters;
import static com.relogiclabs.jschema.internal.script.GBoolean.FALSE;
import static com.relogiclabs.jschema.internal.script.GBoolean.TRUE;
import static com.relogiclabs.jschema.internal.script.GControl.BREAK;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.subList;
import static com.relogiclabs.jschema.internal.util.StringHelper.toEncoded;
import static com.relogiclabs.jschema.message.ErrorCode.ARRLIT01;
import static com.relogiclabs.jschema.message.ErrorCode.BLOKSE01;
import static com.relogiclabs.jschema.message.ErrorCode.EXPRSE01;
import static com.relogiclabs.jschema.message.ErrorCode.EXPRSE02;
import static com.relogiclabs.jschema.message.ErrorCode.FNSDEC01;
import static com.relogiclabs.jschema.message.ErrorCode.FORECH01;
import static com.relogiclabs.jschema.message.ErrorCode.FORSTM01;
import static com.relogiclabs.jschema.message.ErrorCode.IFSTMT01;
import static com.relogiclabs.jschema.message.ErrorCode.IFSTMT02;
import static com.relogiclabs.jschema.message.ErrorCode.OBJLIT01;
import static com.relogiclabs.jschema.message.ErrorCode.RETNSE01;
import static com.relogiclabs.jschema.message.ErrorCode.RETNSE03;
import static com.relogiclabs.jschema.message.ErrorCode.SRPTSE01;
import static com.relogiclabs.jschema.message.ErrorCode.SRPTSE02;
import static com.relogiclabs.jschema.message.ErrorCode.VARDEC01;
import static com.relogiclabs.jschema.message.ErrorCode.VARDEC02;
import static com.relogiclabs.jschema.message.ErrorCode.WHILSE01;
import static com.relogiclabs.jschema.type.ENull.NULL;
import static com.relogiclabs.jschema.type.EUndefined.UNDEFINED;
import static com.relogiclabs.jschema.type.EValue.VOID;
import static lombok.AccessLevel.NONE;

@Getter
public abstract class ScriptTreeVisitor1 extends SchemaParserBaseVisitor<Evaluator>
                                         implements ScriptTreeVisitor {
    static final Evaluator VOID_SUPPLIER = s -> VOID;
    static final Evaluator TRUE_SUPPLIER = s -> TRUE;
    static final Evaluator FALSE_SUPPLIER = s -> FALSE;
    static final Evaluator NULL_SUPPLIER = S -> NULL;
    static final Evaluator UNDEFINED_SUPPLIER = s -> UNDEFINED;
    static final Evaluator BREAK_SUPPLIER = s -> BREAK;

    final RuntimeContext runtime;
    private final ParseTreeProperty<Evaluator> scripts;
    @Getter(NONE) private Class<?> returnType;

    public ScriptTreeVisitor1(RuntimeContext runtime) {
        this.runtime = runtime;
        this.scripts = new ParseTreeProperty<>();
    }

    @Override
    public Evaluator visitCompleteSchema(CompleteSchemaContext ctx) {
        var scripts = ctx.scriptNode().stream().map(this::processScript).toList();
        if(scripts.isEmpty()) return VOID_SUPPLIER;
        return tryCatch(scope -> {
            for(var s : scripts) s.evaluate(scope);
            return VOID;
        }, SRPTSE01, ctx);
    }

    private Evaluator processScript(ParserRuleContext context) {
        var evaluator = visit(context);
        scripts.put(context, evaluator);
        return evaluator;
    }

    @Override
    public Evaluator visitShortSchema(ShortSchemaContext ctx) {
        return VOID_SUPPLIER;
    }

    @Override
    public Evaluator visitScriptNode(ScriptNodeContext ctx) {
        var statements = ctx.globalStatement().stream().map(this::visit).toList();
        return tryCatch(scope -> {
            for(var s : statements) s.evaluate(scope);
            return VOID;
        }, SRPTSE02, ctx);
    }

    @Override
    public Evaluator visitFunctionDeclaration(FunctionDeclarationContext ctx) {
        var baseName = ctx.name.getText();
        var mode = getFunctionMode(ctx.G_CONSTRAINT(), ctx.G_FUTURE(), ctx.G_SUBROUTINE());
        var parameters = toParameters(subList(ctx.G_IDENTIFIER(), 1), ctx.G_ELLIPSIS());
        var constraint = isConstraint(mode);
        var functionId = FunctionId.generate(baseName, parameters, constraint);
        if(constraint) returnType = EBoolean.class;
        var functionBody = visit(ctx.blockStatement());
        returnType = null;
        var function = new GFunction(parameters, functionBody, mode);
        return tryCatch(scope -> {
            scope.addFunction(functionId, function);
            if(constraint) runtime.getFunctions()
                .addFunction(new ScriptFunction(baseName, function));
            return VOID;
        }, FNSDEC01, ctx);
    }

    @Override
    public Evaluator visitVarStatement(VarStatementContext ctx) {
        var varDeclarations = ctx.varDeclaration().stream().map(this::visit).toList();
        return tryCatch(scope -> {
            for(var d : varDeclarations) d.evaluate(scope);
            return VOID;
        }, VARDEC02, ctx);
    }

    @Override
    public Evaluator visitVarDeclaration(VarDeclarationContext ctx) {
        var varName = ctx.G_IDENTIFIER().getText();
        var expression = visit(ctx.expression(), VOID_SUPPLIER);
        return tryCatch(scope -> {
            scope.addVariable(varName, dereference(expression.evaluate(scope)));
            return VOID;
        }, VARDEC01, ctx);
    }

    @Override
    public Evaluator visitExpressionStatement(ExpressionStatementContext ctx) {
        var expression = visit(ctx.expression());
        return tryCatch(scope -> {
            expression.evaluate(scope);
            return VOID;
        }, EXPRSE01, ctx);
    }

    @Override
    public Evaluator visitIfStatement(IfStatementContext ctx) {
        var condition = visit(ctx.expression());
        var thenStatement = visit(ctx.statement(0));
        if(ctx.G_ELSE() == null) return tryCatch(scope -> {
            if(condition.evaluate(scope).toBoolean())
                return thenStatement.evaluate(scope);
            return VOID;
        }, IFSTMT01, ctx);

        var elseStatement = visit(ctx.statement(1));
        return tryCatch(scope -> {
            if(condition.evaluate(scope).toBoolean())
                return thenStatement.evaluate(scope);
            else return elseStatement.evaluate(scope);
        }, IFSTMT02, ctx);
    }

    @Override
    public Evaluator visitWhileStatement(WhileStatementContext ctx) {
        var condition = visit(ctx.expression());
        var statement = visit(ctx.statement());
        return tryCatch(scope -> {
            while(condition.evaluate(scope).toBoolean()) {
                var result = statement.evaluate(scope);
                if(result instanceof GControl ctrl) return ctrl.toIteration();
            }
            return VOID;
        }, WHILSE01, ctx);
    }

    @Override
    public Evaluator visitForStatement(ForStatementContext ctx) {
        var initialization = visit(ctx.varStatement(),
            visit(ctx.initialization, VOID_SUPPLIER));
        var condition = visit(ctx.condition, TRUE_SUPPLIER);
        var updation = visit(ctx.updation, VOID_SUPPLIER);
        var statement = visit(ctx.statement());
        return tryCatch(scope -> {
            var forScope = new ScriptScope(scope);
            for(initialization.evaluate(forScope);
                condition.evaluate(forScope).toBoolean();
                updation.evaluate(forScope)) {
                var result = statement.evaluate(forScope);
                if(result instanceof GControl ctrl) return ctrl.toIteration();
            }
            return VOID;
        }, FORSTM01, ctx);
    }

    @Override
    public Evaluator visitExpressionList(ExpressionListContext ctx) {
        var expressions = ctx.expression().stream().map(this::visit).toList();
        return tryCatch(scope -> {
            for(var e : expressions) e.evaluate(scope);
            return VOID;
        }, EXPRSE02, ctx);
    }

    @Override
    public Evaluator visitForeachStatement(ForeachStatementContext ctx) {
        var varName = ctx.G_IDENTIFIER().getText();
        var collection = visit(ctx.expression());
        var statement = visit(ctx.statement());
        return tryCatch(scope -> {
            var forScope = new ScriptScope(scope);
            var lvalue = forScope.addVariable(varName, VOID);
            for(var v : new GIterator(dereference(collection.evaluate(scope)))) {
                lvalue.setValue(dereference(v));
                var result = statement.evaluate(forScope);
                if(result instanceof GControl ctrl) return ctrl.toIteration();
            }
            return VOID;
        }, FORECH01, ctx);
    }

    @Override
    public Evaluator visitReturnStatement(ReturnStatementContext ctx) {
        var expression = visit(ctx.expression());
        if(returnType == null) return tryCatch(scope -> GControl.ofReturn(
            expression.evaluate(scope)), RETNSE01, ctx);
        var thisReturnType = returnType;
        return tryCatch(scope -> {
            var v1 = expression.evaluate(scope);
            if(!thisReturnType.isInstance(v1))
                throw failOnInvalidReturnType(v1, ctx.expression().getStart());
            return GControl.ofReturn(v1);
        }, RETNSE03, ctx);
    }

    @Override
    public Evaluator visitBreakStatement(BreakStatementContext ctx) {
        return BREAK_SUPPLIER;
    }

    @Override
    public Evaluator visitBlockStatement(BlockStatementContext ctx) {
        var statements = ctx.statement().stream().map(this::visit).toList();
        return tryCatch(scope -> {
            var blockScope = new ScriptScope(scope);
            for(var s : statements) {
                var result = s.evaluate(blockScope);
                if(result instanceof GControl ctrl) return ctrl;
            }
            return VOID;
        }, BLOKSE01, ctx);
    }

    @Override
    public Evaluator visitTrueLiteral(TrueLiteralContext ctx) {
        return TRUE_SUPPLIER;
    }

    @Override
    public Evaluator visitFalseLiteral(FalseLiteralContext ctx) {
        return FALSE_SUPPLIER;
    }

    @Override
    public Evaluator visitNullLiteral(NullLiteralContext ctx) {
        return NULL_SUPPLIER;
    }

    @Override
    public Evaluator visitUndefinedLiteral(UndefinedLiteralContext ctx) {
        return UNDEFINED_SUPPLIER;
    }

    @Override
    public Evaluator visitIntegerLiteral(IntegerLiteralContext ctx) {
        var value = GInteger.from(Long.parseLong(ctx.G_INTEGER().getText()));
        return scope -> value;
    }

    @Override
    public Evaluator visitDoubleLiteral(DoubleLiteralContext ctx) {
        var value = GDouble.from(Double.parseDouble(ctx.G_DOUBLE().getText()));
        return scope -> value;
    }

    @Override
    public Evaluator visitStringLiteral(StringLiteralContext ctx) {
        var value = GString.from(toEncoded(ctx.getText()));
        return scope -> value;
    }

    @Override
    public Evaluator visitArrayLiteral(ArrayLiteralContext ctx) {
        var list = ctx.expression().stream().map(this::visit).toList();
        return tryCatch(scope -> new GArray(list.stream().map(e
                -> dereference(e.evaluate(scope))).toList()), ARRLIT01, ctx);
    }

    @Override
    public Evaluator visitObjectLiteral(ObjectLiteralContext ctx) {
        var keys = ctx.keys.stream().map(k -> k.getType() == G_STRING
                ? toEncoded(k.getText()) : k.getText()).toList();
        var values = ctx.values.stream().map(this::visit).toList();
        return tryCatch(scope -> new GObject(keys, values.stream().map(v
                -> dereference(v.evaluate(scope))).toList()), OBJLIT01, ctx);
    }

    static Evaluator tryCatch(Evaluator evaluator, String code, ParserRuleContext ctx) {
        return scope -> {
            try {
                return evaluator.evaluate(scope);
            } catch(MultilevelRuntimeException e) {
                throw e.translate(ctx.getStart());
            } catch(BaseRuntimeException e) {
                throw e;
            } catch(Exception e) {
                throw failOnSystemException(code, e, ctx.getStart());
            }
        };
    }

    Evaluator visit(ParseTree tree, Evaluator defaultValue) {
        return tree == null ? defaultValue : super.visit(tree);
    }
}