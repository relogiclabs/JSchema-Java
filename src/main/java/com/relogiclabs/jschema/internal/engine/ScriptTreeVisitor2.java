package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.exception.InvocationRuntimeException;
import com.relogiclabs.jschema.exception.MultilevelRuntimeException;
import com.relogiclabs.jschema.exception.ScriptThrowInitiatedException;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.InvokeFunctionExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.InvokeMethodExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.PostIncDecExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.PreIncDecExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.VariableExpressionContext;
import com.relogiclabs.jschema.internal.script.GArray;
import com.relogiclabs.jschema.internal.script.GLeftValue;
import com.relogiclabs.jschema.internal.script.GRange;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.internal.script.RFunction;
import com.relogiclabs.jschema.internal.util.LogHelper;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;
import org.antlr.v4.runtime.ParserRuleContext;

import static com.relogiclabs.jschema.internal.antlr.SchemaParser.CallerExpressionContext;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.G_ASSIGN;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.G_DEC;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.G_DOT;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.G_IDENTIFIER;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.G_INC;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.G_LBRACKET;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.MemberBracketExpressionContext;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.MemberDotExpressionContext;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.RangeBothExpressionContext;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.RangeEndExpressionContext;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.TargetExpressionContext;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.ThrowExpressionContext;
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.TryofExpressionContext;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpBracket;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpBracketedAssignment;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpDecrement;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpIncrement;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpIndex;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpProperty;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpRange;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpRangeSetup;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnArrayRangeUpdate;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnCallerNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnFunctionNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnIndexOutOfBounds;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidIndexRange;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidLeftValueDecrement;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidLeftValueIncrement;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnOperation;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnPropertyNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnStringUpdate;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnTargetNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnVariableNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.createTryofMonad;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.decrement;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.increment;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.stringify;
import static com.relogiclabs.jschema.internal.tree.EFunction.VARIADIC_ARITY;
import static com.relogiclabs.jschema.internal.tree.ScriptFunction.CALLER_HVAR;
import static com.relogiclabs.jschema.internal.tree.ScriptFunction.TARGET_HVAR;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.subList;
import static com.relogiclabs.jschema.internal.util.CommonHelper.getToken;
import static com.relogiclabs.jschema.internal.util.CommonHelper.hasToken;
import static com.relogiclabs.jschema.message.ErrorCode.ARRASN01;
import static com.relogiclabs.jschema.message.ErrorCode.ARRUPD01;
import static com.relogiclabs.jschema.message.ErrorCode.BKTASN01;
import static com.relogiclabs.jschema.message.ErrorCode.BKTUPD01;
import static com.relogiclabs.jschema.message.ErrorCode.BKTUPD02;
import static com.relogiclabs.jschema.message.ErrorCode.CALRSE02;
import static com.relogiclabs.jschema.message.ErrorCode.DECPRE01;
import static com.relogiclabs.jschema.message.ErrorCode.DECPRE02;
import static com.relogiclabs.jschema.message.ErrorCode.DECPRE03;
import static com.relogiclabs.jschema.message.ErrorCode.DECPST01;
import static com.relogiclabs.jschema.message.ErrorCode.DECPST02;
import static com.relogiclabs.jschema.message.ErrorCode.DECPST03;
import static com.relogiclabs.jschema.message.ErrorCode.FNSNVK04;
import static com.relogiclabs.jschema.message.ErrorCode.IDXASN01;
import static com.relogiclabs.jschema.message.ErrorCode.IDXUPD01;
import static com.relogiclabs.jschema.message.ErrorCode.INCPRE01;
import static com.relogiclabs.jschema.message.ErrorCode.INCPRE02;
import static com.relogiclabs.jschema.message.ErrorCode.INCPRE03;
import static com.relogiclabs.jschema.message.ErrorCode.INCPST01;
import static com.relogiclabs.jschema.message.ErrorCode.INCPST02;
import static com.relogiclabs.jschema.message.ErrorCode.INCPST03;
import static com.relogiclabs.jschema.message.ErrorCode.MTHNVK02;
import static com.relogiclabs.jschema.message.ErrorCode.OPPRTY01;
import static com.relogiclabs.jschema.message.ErrorCode.OPPRTY02;
import static com.relogiclabs.jschema.message.ErrorCode.OPPRTY03;
import static com.relogiclabs.jschema.message.ErrorCode.OPPRTY04;
import static com.relogiclabs.jschema.message.ErrorCode.OPRNGT01;
import static com.relogiclabs.jschema.message.ErrorCode.OPRNGT02;
import static com.relogiclabs.jschema.message.ErrorCode.OPRNGT03;
import static com.relogiclabs.jschema.message.ErrorCode.OPRNGT04;
import static com.relogiclabs.jschema.message.ErrorCode.OPRNGT05;
import static com.relogiclabs.jschema.message.ErrorCode.OPRNGT06;
import static com.relogiclabs.jschema.message.ErrorCode.REDBKT01;
import static com.relogiclabs.jschema.message.ErrorCode.REDBKT02;
import static com.relogiclabs.jschema.message.ErrorCode.REDIDX01;
import static com.relogiclabs.jschema.message.ErrorCode.REDRNG01;
import static com.relogiclabs.jschema.message.ErrorCode.RNGASN01;
import static com.relogiclabs.jschema.message.ErrorCode.RNGUPD01;
import static com.relogiclabs.jschema.message.ErrorCode.STRASN01;
import static com.relogiclabs.jschema.message.ErrorCode.STRUPD01;
import static com.relogiclabs.jschema.message.ErrorCode.THRODF01;
import static com.relogiclabs.jschema.message.ErrorCode.THROSE01;
import static com.relogiclabs.jschema.message.ErrorCode.TRGTSE02;
import static com.relogiclabs.jschema.message.ErrorCode.VARRES01;
import static com.relogiclabs.jschema.message.ErrorCode.VARRES02;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static com.relogiclabs.jschema.type.EUndefined.UNDEFINED;

public abstract class ScriptTreeVisitor2 extends ScriptTreeVisitor1 {
    private static final Evaluator THROW_CODE_SUPPLIER = s -> GString.from(THRODF01);

    public ScriptTreeVisitor2(RuntimeContext runtime) {
        super(runtime);
    }

    @Override
    public Evaluator visitMemberBracketExpression(MemberBracketExpressionContext ctx) {
        return handleBracketReadExpression(ctx);
    }

    @Override
    public Evaluator visitMemberDotExpression(MemberDotExpressionContext ctx) {
        return handleDotExpression(ctx);
    }

    @Override
    public Evaluator visitVariableExpression(VariableExpressionContext ctx) {
        return handleVariableExpression(ctx);
    }

    Evaluator handleLeftValueUpdateExpression(ParserRuleContext ctx) {
        if(hasToken(ctx, G_LBRACKET)) return handleBracketUpdateExpression(ctx);
        if(hasToken(ctx, G_DOT)) return handleDotExpression(ctx);
        return handleVariableExpression(ctx);
    }

    private Evaluator handleBracketReadExpression(ParserRuleContext ctx) {
        var expressions = ctx.getRuleContexts(ExpressionContext.class);
        var value1 = visit(expressions.get(0));
        var index2 = visit(expressions.get(1));
        return tryCatch(scope -> {
            var v1 = dereference(value1.evaluate(scope));
            var v2 = dereference(index2.evaluate(scope));
            var v3 = evaluateArrayIndex(v1, v2, expressions.get(1));
            if(v3 != null) return v3;
            v3 = evaluateArrayRange(v1, v2, expressions.get(1));
            if(v3 != null) return v3;
            v3 = evaluateStringIndex(v1, v2, expressions.get(1));
            if(v3 != null) return v3;
            v3 = evaluateStringRange(v1, v2, expressions.get(1));
            if(v3 != null) return v3;
            v3 = evaluateObjectProperty(v1, v2, expressions.get(1));
            if(v3 != null) return v3;
            if(v2 instanceof EInteger) throw failOnOperation(REDIDX01,
                OpIndex, v1, getToken(ctx, G_LBRACKET));
            if(v2 instanceof GRange) throw failOnOperation(REDRNG01,
                OpRange, v1, getToken(ctx, G_LBRACKET));
            throw failOnOperation(REDBKT01, OpBracket, v1, v2, getToken(ctx, G_LBRACKET));
        }, REDBKT02, expressions.get(0));
    }

    private Evaluator handleBracketUpdateExpression(ParserRuleContext ctx) {
        var expressions = ctx.getRuleContexts(ExpressionContext.class);
        var value1 = visit(expressions.get(0));
        var index2 = visit(expressions.get(1));
        return tryCatch(scope -> {
            var v1 = dereference(value1.evaluate(scope));
            var v2 = dereference(index2.evaluate(scope));
            var v3 = evaluateArrayIndex(v1, v2, expressions.get(1));
            if(v3 != null) return v3;
            v3 = evaluateObjectProperty(v1, v2, expressions.get(1));
            if(v3 != null) return v3;
            throwBracketUpdateException(v1, v2, ctx);
            throw new IllegalStateException("Invalid runtime state");
        }, BKTUPD02, expressions.get(0));
    }

    static void throwBracketUpdateException(EValue v1, EValue v2, ParserRuleContext ctx) {
        if(v1 instanceof EString) throw failOnStringUpdate(hasToken(ctx, G_ASSIGN)
            ? STRASN01 : STRUPD01, getToken(ctx, G_LBRACKET));
        if(v1 instanceof EArray && v2 instanceof GRange)
            throw failOnArrayRangeUpdate(hasToken(ctx, G_ASSIGN)
                ? ARRASN01 : ARRUPD01, getToken(ctx, G_LBRACKET));
        if(v2 instanceof EInteger) throw failOnOperation(hasToken(ctx, G_ASSIGN)
            ? IDXASN01 : IDXUPD01, OpIndex, v1, getToken(ctx, G_LBRACKET));
        if(v2 instanceof GRange) throw failOnOperation(hasToken(ctx, G_ASSIGN)
            ? RNGASN01 : RNGUPD01, OpRange, v1, getToken(ctx, G_LBRACKET));

        if(hasToken(ctx, G_ASSIGN)) throw failOnOperation(BKTASN01,
            OpBracketedAssignment, v1, v2, getToken(ctx, G_LBRACKET));
        throw failOnOperation(BKTUPD01, OpBracket, v1, v2, getToken(ctx, G_LBRACKET));
    }

    private Evaluator handleDotExpression(ParserRuleContext ctx) {
        var expressions = ctx.getRuleContexts(ExpressionContext.class);
        var identifier = getToken(ctx, G_IDENTIFIER);
        var object1 = visit(expressions.get(0));
        var key2 = identifier.getText();
        return tryCatch(scope -> {
            var v1 = dereference(object1.evaluate(scope));
            if(!(v1 instanceof EObject o1)) throw failOnOperation(OPPRTY01, OpProperty,
                v1, getToken(ctx, G_DOT));
            var v2 = o1.get(key2);
            if(v2 == null) throw failOnPropertyNotFound(OPPRTY02, o1, key2, identifier.getSymbol());
            return v2;
        }, OPPRTY03, expressions.get(0));
    }

    private static Evaluator handleVariableExpression(ParserRuleContext ctx) {
        var identifier = getToken(ctx, G_IDENTIFIER);
        var name = identifier.getText();
        return tryCatch(scope -> {
            var v1 = scope.resolve(name);
            if(v1 == null) throw failOnVariableNotFound(VARRES01, identifier.getSymbol());
            return v1;
        }, VARRES02, ctx);
    }

    private static EValue evaluateArrayIndex(EValue v1, EValue v2, ExpressionContext ctx) {
        if(!(v1 instanceof EArray a1) || !(v2 instanceof EInteger i2)) return null;
        try {
            return a1.get((int) i2.getValue());
        } catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            throw failOnIndexOutOfBounds(a1, i2, ctx.getStart(), e);
        }
    }

    private static GArray evaluateArrayRange(EValue v1, EValue v2, ExpressionContext ctx) {
        if(!(v1 instanceof EArray a1) || !(v2 instanceof GRange r2)) return null;
        try {
            return GArray.from(a1, r2);
        } catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            throw failOnInvalidIndexRange(a1, r2, ctx.getStart(), e);
        }
    }

    private static EValue evaluateObjectProperty(EValue v1, EValue v2, ExpressionContext ctx) {
        if(!(v1 instanceof EObject o1) || !(v2 instanceof EString s2)) return null;
        var v3 = o1.get(s2.getValue());
        if(v3 == null) throw failOnPropertyNotFound(OPPRTY04, o1, s2.getValue(), ctx.getStart());
        return v3;
    }

    private static GString evaluateStringIndex(EValue v1, EValue v2, ExpressionContext ctx) {
        if(!(v1 instanceof EString s1) || !(v2 instanceof EInteger i2)) return null;
        try {
            return GString.from(s1.getValue().charAt((int) i2.getValue()));
        } catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            throw failOnIndexOutOfBounds(s1, i2, ctx.getStart(), e);
        }
    }

    private static GString evaluateStringRange(EValue v1, EValue v2, ExpressionContext ctx) {
        if(!(v1 instanceof EString s1) || !(v2 instanceof GRange r2)) return null;
        try {
            return GString.from(s1, r2);
        } catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            throw failOnInvalidIndexRange(s1, r2, ctx.getStart(), e);
        }
    }

    @Override
    public Evaluator visitInvokeFunctionExpression(InvokeFunctionExpressionContext ctx) {
        var fn1 = ctx.G_IDENTIFIER();
        var name1 = fn1.getText();
        var param2 = ctx.expression().stream().map(this::visit).toList();
        var fid1 = name1 + "#" + param2.size();
        var fid2 = name1 + "#" + VARIADIC_ARITY;
        return tryCatch(scope -> {
            try {
                var v1 = scope.resolve(fid1);
                if(v1 == null) v1 = scope.resolve(fid2);
                if(!(v1 instanceof RFunction f1)) throw failOnFunctionNotFound(name1,
                    param2.size(), fn1.getSymbol());
                var p2 = param2.stream().map(a -> dereference(a.evaluate(scope))).toList();
                return f1.invoke(f1.bind(scope, p2), p2);
            } catch(InvocationRuntimeException e) {
                e.setSubject(name1);
                throw e.translate(fn1.getSymbol());
            }
        }, FNSNVK04, ctx);
    }

    @Override
    public Evaluator visitInvokeMethodExpression(InvokeMethodExpressionContext ctx) {
        var self1 = visit(ctx.expression(0));
        var method2 = ctx.G_IDENTIFIER();
        var name2 = method2.getText();
        var param3 = subList(ctx.expression(), 1).stream().map(this::visit).toList();
        return tryCatch(scope -> {
            try {
                var s1 = dereference(self1.evaluate(scope));
                var m2 = s1.getMethod(name2, param3.size());
                var p3 = param3.stream().map(p -> dereference(p.evaluate(scope))).toList();
                return m2.evaluate(s1, p3, scope);
            } catch(InvocationRuntimeException e) {
                e.setSubject(name2);
                throw e.translate(method2.getSymbol());
            }
        }, MTHNVK02, ctx);
    }

    @Override
    public Evaluator visitPreIncDecExpression(PreIncDecExpressionContext ctx) {
        if(ctx.G_INC() != null) return handlePreIncrementExpression(ctx);
        if(ctx.G_DEC() != null) return handlePreDecrementExpression(ctx);
        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public Evaluator visitPostIncDecExpression(PostIncDecExpressionContext ctx) {
        if(ctx.G_INC() != null) return handlePostIncrementExpression(ctx);
        if(ctx.G_DEC() != null) return handlePostDecrementExpression(ctx);
        throw new IllegalStateException("Invalid parser state");
    }

    private Evaluator handlePostIncrementExpression(ParserRuleContext ctx) {
        var lvalue1 = handleLeftValueUpdateExpression(ctx);
        return tryCatch(scope -> {
            var v1 = lvalue1.evaluate(scope);
            if(!(v1 instanceof GLeftValue l1)) throw failOnInvalidLeftValueIncrement(INCPST01,
                getToken(ctx, G_INC).getSymbol());
            if(!(l1.getValue() instanceof ENumber n1)) throw failOnOperation(INCPST02,
                OpIncrement, v1, getToken(ctx, G_INC));
            l1.setValue(increment(n1));
            return n1;
        }, INCPST03, ctx);
    }

    private Evaluator handlePreIncrementExpression(ParserRuleContext ctx) {
        var lvalue1 = handleLeftValueUpdateExpression(ctx);
        return tryCatch(scope -> {
            var v1 = lvalue1.evaluate(scope);
            if(!(v1 instanceof GLeftValue l1)) throw failOnInvalidLeftValueIncrement(INCPRE01,
                getToken(ctx, G_INC).getSymbol());
            if(!(l1.getValue() instanceof ENumber n1)) throw failOnOperation(INCPRE02,
                OpIncrement, v1, getToken(ctx, G_INC));
            l1.setValue(n1 = increment(n1));
            return n1;
        }, INCPRE03, ctx);
    }

    private Evaluator handlePostDecrementExpression(ParserRuleContext ctx) {
        var lvalue1 = handleLeftValueUpdateExpression(ctx);
        return tryCatch(scope -> {
            var v1 = lvalue1.evaluate(scope);
            if(!(v1 instanceof GLeftValue l1)) throw failOnInvalidLeftValueDecrement(DECPST01,
                getToken(ctx, G_DEC).getSymbol());
            if(!(l1.getValue() instanceof ENumber n1)) throw failOnOperation(DECPST02,
                OpDecrement, v1, getToken(ctx, G_DEC));
            l1.setValue(decrement(n1));
            return n1;
        }, DECPST03, ctx);
    }

    private Evaluator handlePreDecrementExpression(ParserRuleContext ctx) {
        var lvalue1 = handleLeftValueUpdateExpression(ctx);
        return tryCatch(scope -> {
            var v1 = lvalue1.evaluate(scope);
            if(!(v1 instanceof GLeftValue l1)) throw failOnInvalidLeftValueDecrement(DECPRE01,
                getToken(ctx, G_DEC).getSymbol());
            if(!(l1.getValue() instanceof ENumber n1)) throw failOnOperation(DECPRE02,
                OpDecrement, v1, getToken(ctx, G_DEC));
            l1.setValue(n1 = decrement(n1));
            return n1;
        }, DECPRE03, ctx);
    }

    @Override
    public Evaluator visitTryofExpression(TryofExpressionContext ctx) {
        var expression = visit(ctx.expression());
        return scope -> {
            try {
                var value = dereference(expression.evaluate(scope));
                return createTryofMonad(value, UNDEFINED);
            } catch(Exception exInstance) {
                var exception = exInstance;
                if(exception instanceof MultilevelRuntimeException ex)
                    exception = ex.translate(ctx.expression().getStart());
                LogHelper.log(exception, ctx.expression().getStart());
                return createTryofMonad(UNDEFINED, GString.from(exception.getMessage()));
            }
        };
    }

    @Override
    public Evaluator visitThrowExpression(ThrowExpressionContext ctx) {
        var hasCode = ctx.G_COMMA() != null;
        var code = hasCode ? visit(ctx.expression(0)) : THROW_CODE_SUPPLIER;
        var message = hasCode ? visit(ctx.expression(1)) : visit(ctx.expression(0));
        return tryCatch(scope -> {
            var c1 = stringify(dereference(code.evaluate(scope)));
            var m2 = stringify(dereference(message.evaluate(scope)));
            throw new ScriptThrowInitiatedException(formatForSchema(c1, m2,
                ctx.G_THROW().getSymbol()));
        }, THROSE01, ctx);
    }

    @Override
    public Evaluator visitTargetExpression(TargetExpressionContext ctx) {
        return tryCatch(scope -> {
            var v1 = scope.resolve(TARGET_HVAR);
            if(v1 == null) throw failOnTargetNotFound(ctx.G_TARGET().getSymbol());
            return v1;
        }, TRGTSE02, ctx);
    }

    @Override
    public Evaluator visitCallerExpression(CallerExpressionContext ctx) {
        return tryCatch(scope -> {
            var v1 = scope.resolve(CALLER_HVAR);
            if(v1 == null) throw failOnCallerNotFound(ctx.G_CALLER().getSymbol());
            return v1;
        }, CALRSE02, ctx);
    }

    @Override
    public Evaluator visitRangeBothExpression(RangeBothExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var expr2 = visit(ctx.expression(1), VOID_SUPPLIER);
        if(expr2 == VOID_SUPPLIER) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            if(!(v1 instanceof EInteger i1)) throw failOnOperation(OPRNGT01,
                OpRangeSetup, v1, ctx.G_RANGE());
            return GRange.from(i1, null);
        }, OPRNGT02, ctx);

        return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(!(v1 instanceof EInteger i1) || !(v2 instanceof EInteger i2))
                throw failOnOperation(OPRNGT03, OpRangeSetup, v1, v2, ctx.G_RANGE());
            return GRange.from(i1, i2);
        }, OPRNGT04, ctx);
    }

    @Override
    public Evaluator visitRangeEndExpression(RangeEndExpressionContext ctx) {
        var expr2 = visit(ctx.expression());
        return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(v2 instanceof EInteger i2)) throw failOnOperation(OPRNGT05,
                OpRangeSetup, v2, ctx.G_RANGE());
            return GRange.from(null, i2);
        }, OPRNGT06, ctx);
    }
}