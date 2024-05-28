package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.exception.ScriptArgumentException;
import com.relogiclabs.jschema.exception.ScriptInitiatedException;
import com.relogiclabs.jschema.exception.ScriptInvocationException;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.InvokeFunctionExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.InvokeMethodExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.PostIncDecExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.PreIncDecExpressionContext;
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
import static com.relogiclabs.jschema.internal.antlr.SchemaParser.IdentifierExpressionContext;
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
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnIdentifierNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnIndexOutOfBounds;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidLeftValueDecrement;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidLeftValueIncrement;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidRangeIndex;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnOperation;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnPropertyNotExist;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnRuntime;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnStringUpdate;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnTargetNotFound;
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
import static com.relogiclabs.jschema.message.ErrorCode.ARAS01;
import static com.relogiclabs.jschema.message.ErrorCode.ARUD01;
import static com.relogiclabs.jschema.message.ErrorCode.BKTA01;
import static com.relogiclabs.jschema.message.ErrorCode.BKTR01;
import static com.relogiclabs.jschema.message.ErrorCode.BKTR02;
import static com.relogiclabs.jschema.message.ErrorCode.BKTU01;
import static com.relogiclabs.jschema.message.ErrorCode.BKTU02;
import static com.relogiclabs.jschema.message.ErrorCode.CALR02;
import static com.relogiclabs.jschema.message.ErrorCode.DECE01;
import static com.relogiclabs.jschema.message.ErrorCode.DECE02;
import static com.relogiclabs.jschema.message.ErrorCode.DECE03;
import static com.relogiclabs.jschema.message.ErrorCode.DECT01;
import static com.relogiclabs.jschema.message.ErrorCode.DECT02;
import static com.relogiclabs.jschema.message.ErrorCode.DECT03;
import static com.relogiclabs.jschema.message.ErrorCode.FNVK04;
import static com.relogiclabs.jschema.message.ErrorCode.IDEN01;
import static com.relogiclabs.jschema.message.ErrorCode.IDEN02;
import static com.relogiclabs.jschema.message.ErrorCode.IDXA01;
import static com.relogiclabs.jschema.message.ErrorCode.IDXR01;
import static com.relogiclabs.jschema.message.ErrorCode.IDXU01;
import static com.relogiclabs.jschema.message.ErrorCode.INCE01;
import static com.relogiclabs.jschema.message.ErrorCode.INCE02;
import static com.relogiclabs.jschema.message.ErrorCode.INCE03;
import static com.relogiclabs.jschema.message.ErrorCode.INCT01;
import static com.relogiclabs.jschema.message.ErrorCode.INCT02;
import static com.relogiclabs.jschema.message.ErrorCode.INCT03;
import static com.relogiclabs.jschema.message.ErrorCode.MNVK02;
import static com.relogiclabs.jschema.message.ErrorCode.PRPT01;
import static com.relogiclabs.jschema.message.ErrorCode.PRPT02;
import static com.relogiclabs.jschema.message.ErrorCode.PRPT03;
import static com.relogiclabs.jschema.message.ErrorCode.PRPT04;
import static com.relogiclabs.jschema.message.ErrorCode.RNGA01;
import static com.relogiclabs.jschema.message.ErrorCode.RNGR01;
import static com.relogiclabs.jschema.message.ErrorCode.RNGT01;
import static com.relogiclabs.jschema.message.ErrorCode.RNGT02;
import static com.relogiclabs.jschema.message.ErrorCode.RNGT03;
import static com.relogiclabs.jschema.message.ErrorCode.RNGT04;
import static com.relogiclabs.jschema.message.ErrorCode.RNGT05;
import static com.relogiclabs.jschema.message.ErrorCode.RNGT06;
import static com.relogiclabs.jschema.message.ErrorCode.RNGU01;
import static com.relogiclabs.jschema.message.ErrorCode.SASN01;
import static com.relogiclabs.jschema.message.ErrorCode.SUPD01;
import static com.relogiclabs.jschema.message.ErrorCode.THRO01;
import static com.relogiclabs.jschema.message.ErrorCode.THRO02;
import static com.relogiclabs.jschema.message.ErrorCode.TRGT02;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static com.relogiclabs.jschema.type.EUndefined.UNDEFINED;

public abstract class ScriptTreeVisitor2 extends ScriptTreeVisitor1 {
    private static final Evaluator THROW_CODE_SUPPLIER = s -> GString.from(THRO01);

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
    public Evaluator visitIdentifierExpression(IdentifierExpressionContext ctx) {
        return handleIdentifierExpression(ctx);
    }

    Evaluator handleLeftValueUpdateExpression(ParserRuleContext ctx) {
        if(hasToken(ctx, G_LBRACKET)) return handleBracketUpdateExpression(ctx);
        if(hasToken(ctx, G_DOT)) return handleDotExpression(ctx);
        return handleIdentifierExpression(ctx);
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
            if(v2 instanceof EInteger) throw failOnOperation(IDXR01, OpIndex, v1, getToken(ctx, G_LBRACKET));
            if(v2 instanceof GRange) throw failOnOperation(RNGR01, OpRange, v1, getToken(ctx, G_LBRACKET));
            throw failOnOperation(BKTR01, OpBracket, v1, v2, getToken(ctx, G_LBRACKET));
        }, BKTR02, expressions.get(0));
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
        }, BKTU02, expressions.get(0));
    }

    static void throwBracketUpdateException(EValue v1, EValue v2, ParserRuleContext ctx) {
        if(v1 instanceof EString) throw failOnStringUpdate(hasToken(ctx, G_ASSIGN)
            ? SASN01 : SUPD01, getToken(ctx, G_LBRACKET));
        if(v1 instanceof EArray && v2 instanceof GRange)
            throw failOnArrayRangeUpdate(hasToken(ctx, G_ASSIGN)
                ? ARAS01 : ARUD01, getToken(ctx, G_LBRACKET));
        if(v2 instanceof EInteger) throw failOnOperation(hasToken(ctx, G_ASSIGN)
            ? IDXA01 : IDXU01, OpIndex, v1, getToken(ctx, G_LBRACKET));
        if(v2 instanceof GRange) throw failOnOperation(hasToken(ctx, G_ASSIGN)
            ? RNGA01 : RNGU01, OpRange, v1, getToken(ctx, G_LBRACKET));

        if(hasToken(ctx, G_ASSIGN)) throw failOnOperation(BKTA01,
                OpBracketedAssignment, v1, v2, getToken(ctx, G_LBRACKET));
        throw failOnOperation(BKTU01, OpBracket, v1, v2, getToken(ctx, G_LBRACKET));
    }

    private Evaluator handleDotExpression(ParserRuleContext ctx) {
        var expressions = ctx.getRuleContexts(ExpressionContext.class);
        var identifier = getToken(ctx, G_IDENTIFIER);
        var object1 = visit(expressions.get(0));
        var key2 = identifier.getText();
        return tryCatch(scope -> {
            var v1 = dereference(object1.evaluate(scope));
            if(!(v1 instanceof EObject o1)) throw failOnOperation(PRPT01, OpProperty,
                v1, getToken(ctx, G_DOT));
            var v2 = o1.get(key2);
            if(v2 == null) throw failOnPropertyNotExist(PRPT02, o1, key2, identifier.getSymbol());
            return v2;
        }, PRPT04, expressions.get(0));
    }

    private static Evaluator handleIdentifierExpression(ParserRuleContext ctx) {
        var identifier = getToken(ctx, G_IDENTIFIER);
        var name = identifier.getText();
        return tryCatch(scope -> {
            var v1 = scope.resolve(name);
            if(v1 == null) throw failOnIdentifierNotFound(IDEN01, identifier.getSymbol());
            return v1;
        }, IDEN02, ctx);
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
            throw failOnInvalidRangeIndex(a1, r2, ctx.getStart(), e);
        }
    }

    private static EValue evaluateObjectProperty(EValue v1, EValue v2, ExpressionContext ctx) {
        if(!(v1 instanceof EObject o1) || !(v2 instanceof EString s2)) return null;
        var v3 = o1.get(s2.getValue());
        if(v3 == null) throw failOnPropertyNotExist(PRPT03, o1, s2.getValue(), ctx.getStart());
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
            throw failOnInvalidRangeIndex(s1, r2, ctx.getStart(), e);
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
            } catch(ScriptInvocationException e) {
                throw failOnRuntime(e.getCode(), e.getMessage(name1), e.getToken(fn1.getSymbol()), e);
            } catch(ScriptArgumentException e) {
                throw failOnRuntime(e.getCode(), e.getMessage(name1), fn1.getSymbol(), e);
            }
        }, FNVK04, ctx);
    }

    @Override
    public Evaluator visitInvokeMethodExpression(InvokeMethodExpressionContext ctx) {
        var self1 = visit(ctx.expression(0));
        var method2 = ctx.G_IDENTIFIER();
        var name2 = method2.getText();
        var param3 = subList(ctx.expression(), 1).stream().map(this::visit).toList();
        return tryCatch(scope -> {
            var s1 = dereference(self1.evaluate(scope));
            var m2 = s1.getMethod(name2, param3.size());
            var p3 = param3.stream().map(p -> dereference(p.evaluate(scope))).toList();
            return m2.evaluate(s1, p3, scope);
        }, MNVK02, ctx);
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
            if(!(v1 instanceof GLeftValue l1)) throw failOnInvalidLeftValueIncrement(INCT01,
                getToken(ctx, G_INC).getSymbol());
            if(!(l1.getValue() instanceof ENumber n1)) throw failOnOperation(INCT02,
                OpIncrement, v1, getToken(ctx, G_INC));
            l1.setValue(increment(n1));
            return n1;
        }, INCT03, ctx);
    }

    private Evaluator handlePreIncrementExpression(ParserRuleContext ctx) {
        var lvalue1 = handleLeftValueUpdateExpression(ctx);
        return tryCatch(scope -> {
            var v1 = lvalue1.evaluate(scope);
            if(!(v1 instanceof GLeftValue l1)) throw failOnInvalidLeftValueIncrement(INCE01,
                getToken(ctx, G_INC).getSymbol());
            if(!(l1.getValue() instanceof ENumber n1)) throw failOnOperation(INCE02,
                OpIncrement, v1, getToken(ctx, G_INC));
            l1.setValue(n1 = increment(n1));
            return n1;
        }, INCE03, ctx);
    }

    private Evaluator handlePostDecrementExpression(ParserRuleContext ctx) {
        var lvalue1 = handleLeftValueUpdateExpression(ctx);
        return tryCatch(scope -> {
            var v1 = lvalue1.evaluate(scope);
            if(!(v1 instanceof GLeftValue l1)) throw failOnInvalidLeftValueDecrement(DECT01,
                getToken(ctx, G_DEC).getSymbol());
            if(!(l1.getValue() instanceof ENumber n1)) throw failOnOperation(DECT02,
                OpDecrement, v1, getToken(ctx, G_DEC));
            l1.setValue(decrement(n1));
            return n1;
        }, DECT03, ctx);
    }

    private Evaluator handlePreDecrementExpression(ParserRuleContext ctx) {
        var lvalue1 = handleLeftValueUpdateExpression(ctx);
        return tryCatch(scope -> {
            var v1 = lvalue1.evaluate(scope);
            if(!(v1 instanceof GLeftValue l1)) throw failOnInvalidLeftValueDecrement(DECE01,
                getToken(ctx, G_DEC).getSymbol());
            if(!(l1.getValue() instanceof ENumber n1)) throw failOnOperation(DECE02,
                OpDecrement, v1, getToken(ctx, G_DEC));
            l1.setValue(n1 = decrement(n1));
            return n1;
        }, DECE03, ctx);
    }

    @Override
    public Evaluator visitTryofExpression(TryofExpressionContext ctx) {
        var expression = visit(ctx.expression());
        return scope -> {
            try {
                var value = dereference(expression.evaluate(scope));
                return createTryofMonad(value, UNDEFINED);
            } catch(Exception e) {
                LogHelper.log(e, ctx.expression().getStart());
                return createTryofMonad(UNDEFINED, GString.from(e.getMessage()));
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
            throw new ScriptInitiatedException(formatForSchema(c1, m2,
                    ctx.G_THROW().getSymbol()));
        }, THRO02, ctx);
    }

    @Override
    public Evaluator visitTargetExpression(TargetExpressionContext ctx) {
        return tryCatch(scope -> {
            var v1 = scope.resolve(TARGET_HVAR);
            if(v1 == null) throw failOnTargetNotFound(ctx.G_TARGET().getSymbol());
            return v1;
        }, TRGT02, ctx);
    }

    @Override
    public Evaluator visitCallerExpression(CallerExpressionContext ctx) {
        return tryCatch(scope -> {
            var v1 = scope.resolve(CALLER_HVAR);
            if(v1 == null) throw failOnCallerNotFound(ctx.G_CALLER().getSymbol());
            return v1;
        }, CALR02, ctx);
    }

    @Override
    public Evaluator visitRangeBothExpression(RangeBothExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var expr2 = visit(ctx.expression(1), VOID_SUPPLIER);
        if(expr2 == VOID_SUPPLIER) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            if(!(v1 instanceof EInteger i1)) throw failOnOperation(RNGT01,
                OpRangeSetup, v1, ctx.G_RANGE());
            return GRange.from(i1, null);
        }, RNGT02, ctx);

        return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(!(v1 instanceof EInteger i1) || !(v2 instanceof EInteger i2))
                throw failOnOperation(RNGT03, OpRangeSetup, v1, v2, ctx.G_RANGE());
            return GRange.from(i1, i2);
        }, RNGT04, ctx);
    }

    @Override
    public Evaluator visitRangeEndExpression(RangeEndExpressionContext ctx) {
        var expr2 = visit(ctx.expression());
        return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(v2 instanceof EInteger i2)) throw failOnOperation(RNGT05,
                OpRangeSetup, v2, ctx.G_RANGE());
            return GRange.from(null, i2);
        }, RNGT06, ctx);
    }
}