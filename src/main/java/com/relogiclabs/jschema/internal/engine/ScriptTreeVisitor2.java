package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.exception.ScriptArgumentException;
import com.relogiclabs.jschema.exception.ScriptInitiatedException;
import com.relogiclabs.jschema.exception.ScriptInvocationException;
import com.relogiclabs.jschema.internal.antlr.SchemaParser;
import com.relogiclabs.jschema.internal.script.GArray;
import com.relogiclabs.jschema.internal.script.GControl;
import com.relogiclabs.jschema.internal.script.GRange;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.internal.script.RFunction;
import com.relogiclabs.jschema.internal.util.LogHelper;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;

import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnCallerNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnFunctionNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnIdentifierNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnIndexOutOfBounds;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidRangeIndex;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnOperation;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnPropertyNotExist;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnRuntime;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnTargetNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptOperation.INDEX;
import static com.relogiclabs.jschema.internal.engine.ScriptOperation.PROPERTY;
import static com.relogiclabs.jschema.internal.engine.ScriptOperation.RANGE;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.createTryofMonad;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.stringify;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.CALLER_HVAR;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.TARGET_HVAR;
import static com.relogiclabs.jschema.message.ErrorCode.CALR02;
import static com.relogiclabs.jschema.message.ErrorCode.INDX06;
import static com.relogiclabs.jschema.message.ErrorCode.INDX07;
import static com.relogiclabs.jschema.message.ErrorCode.INDX08;
import static com.relogiclabs.jschema.message.ErrorCode.INVK01;
import static com.relogiclabs.jschema.message.ErrorCode.PRPS01;
import static com.relogiclabs.jschema.message.ErrorCode.PRPS03;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS01;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS02;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS03;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS10;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS11;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS12;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS13;
import static com.relogiclabs.jschema.message.ErrorCode.THRO01;
import static com.relogiclabs.jschema.message.ErrorCode.THRO02;
import static com.relogiclabs.jschema.message.ErrorCode.TRGT02;
import static com.relogiclabs.jschema.message.ErrorCode.VARD04;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static com.relogiclabs.jschema.type.EUndefined.UNDEFINED;
import static com.relogiclabs.jschema.type.EValue.VOID;

public abstract class ScriptTreeVisitor2 extends ScriptTreeVisitor1 {
    private static final Evaluator CODE_SUPPLIER = s -> GString.of(THRO01);

    public ScriptTreeVisitor2(RuntimeContext runtime) {
        super(runtime);
    }

    @Override
    public Evaluator visitDotExpression(SchemaParser.DotExpressionContext ctx) {
        var e1 = visit(ctx.refExpression());
        var s2 = ctx.G_IDENTIFIER().getText();
        return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            if(!(v1 instanceof EObject o1)) throw failOnOperation(PRPS01, PROPERTY, v1, ctx.G_DOT());
            var v2 = o1.get(s2);
            if(v2 == null) throw failOnPropertyNotExist(o1, s2, ctx.G_IDENTIFIER().getSymbol());
            return v2;
        }, PRPS03, ctx);
    }

    @Override
    public Evaluator visitIndexExpression(SchemaParser.IndexExpressionContext ctx) {
        var e1 = visit(ctx.refExpression());
        var e2 = visit(ctx.expression());
        return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            if(v1 instanceof EArray a1 && v2 instanceof EInteger i2) {
                try {
                    return a1.get((int) i2.getValue());
                } catch(IndexOutOfBoundsException e) {
                    throw failOnIndexOutOfBounds(a1, i2, ctx.expression().getStart(), e);
                }
            }
            if(v1 instanceof EArray a1 && v2 instanceof GRange r2) {
                try {
                    return GArray.from(a1, r2);
                } catch(IndexOutOfBoundsException | IllegalArgumentException e) {
                    throw failOnInvalidRangeIndex(a1, r2, ctx.expression().getStart(), e);
                }
            }
            if(v1 instanceof EObject o1 && v2 instanceof EString s2) {
                var v3 = o1.get(s2.getValue());
                if(v3 == null) throw failOnPropertyNotExist(o1, s2.getValue(),
                        ctx.expression().getStart());
                return v3;
            }
            if(v1 instanceof EString s1 && v2 instanceof EInteger i2) {
                try {
                    return GString.of(s1.getValue().charAt((int) i2.getValue()));
                } catch(IndexOutOfBoundsException e) {
                    throw failOnIndexOutOfBounds(s1, i2, ctx.expression().getStart(), e);
                }
            }
            if(v1 instanceof EString s1 && v2 instanceof GRange r2) {
                try {
                    return GString.from(s1, r2);
                } catch(IndexOutOfBoundsException | IllegalArgumentException e) {
                    throw failOnInvalidRangeIndex(s1, r2, ctx.expression().getStart(), e);
                }
            }
            if(v2 instanceof EInteger) throw failOnOperation(INDX06, INDEX, v1, ctx.G_LBRACKET());
            if(v2 instanceof GRange) throw failOnOperation(RNGS10, RANGE, v1, ctx.G_LBRACKET());
            throw failOnOperation(INDX07, INDEX, v1, v2, ctx.G_LBRACKET());
        }, INDX08, ctx);
    }

    @Override
    public Evaluator visitInvokeExpression(SchemaParser.InvokeExpressionContext ctx) {
        var e1 = ctx.G_IDENTIFIER();
        var e2 = ctx.expression().stream().map(this::visit).toList();
        var s1 = e1.getText();
        var n1 = s1 + "#" + e2.size();
        var n2 = s1.concat("#...");
        return tryCatch(scope -> {
            try {
                var v1 = scope.resolve(n1);
                if(v1 == VOID) v1 = scope.resolve(n2);
                if(!(v1 instanceof RFunction f1)) throw failOnFunctionNotFound(s1,
                        e2.size(), e1.getSymbol());
                var v2 = e2.stream().map(a -> dereference(a.evaluate(scope))).toList();
                var v3 = f1.invoke(f1.bind(scope, v2), v2);
                if(v3 instanceof GControl ctrl) return ctrl.toFunction();
                return v3;
            } catch(ScriptInvocationException e) {
                throw failOnRuntime(e.getCode(), e.getMessage(s1), e.getToken(e1.getSymbol()), e);
            } catch(ScriptArgumentException e) {
                throw failOnRuntime(e.getCode(), e.getMessage(s1), e1.getSymbol(), e);
            }
        }, INVK01, ctx);
    }

    @Override
    public Evaluator visitTryofExpression(SchemaParser.TryofExpressionContext ctx) {
        var e1 = visit(ctx.expression());
        return scope -> {
            try {
                var value = dereference(e1.evaluate(scope));
                return createTryofMonad(value, UNDEFINED);
            } catch(Exception e) {
                LogHelper.log(e, ctx.expression().getStart());
                return createTryofMonad(UNDEFINED, GString.of(e.getMessage()));
            }
        };
    }

    @Override
    public Evaluator visitThrowExpression(SchemaParser.ThrowExpressionContext ctx) {
        var hasCode = ctx.G_COMMA() != null;
        var e1 = hasCode ? visit(ctx.expression(0)) : CODE_SUPPLIER;
        var e2 = hasCode ? visit(ctx.expression(1)) : visit(ctx.expression(0));
        return tryCatch(scope -> {
            var s1 = stringify(dereference(e1.evaluate(scope)));
            var s2 = stringify(dereference(e2.evaluate(scope)));
            throw new ScriptInitiatedException(formatForSchema(s1, s2,
                    ctx.G_THROW().getSymbol()));
        }, THRO02, ctx);
    }

    @Override
    public Evaluator visitTargetExpression(SchemaParser.TargetExpressionContext ctx) {
        return tryCatch(scope -> {
            var v1 = scope.resolve(TARGET_HVAR);
            if(v1 == VOID) throw failOnTargetNotFound(ctx.G_TARGET().getSymbol());
            return v1;
        }, TRGT02, ctx);
    }

    @Override
    public Evaluator visitCallerExpression(SchemaParser.CallerExpressionContext ctx) {
        return tryCatch(scope -> {
            var v1 = scope.resolve(CALLER_HVAR);
            if(v1 == VOID) throw failOnCallerNotFound(ctx.G_CALLER().getSymbol());
            return v1;
        }, CALR02, ctx);
    }

    @Override
    public Evaluator visitIdentifierExpression(SchemaParser.IdentifierExpressionContext ctx) {
        var e1 = ctx.G_IDENTIFIER();
        var s1 = e1.getText();
        return tryCatch(scope -> {
            var v1 = scope.resolve(s1);
            if(v1 == VOID) throw failOnIdentifierNotFound(e1.getSymbol());
            return v1;
        }, VARD04, ctx);
    }

    @Override
    public Evaluator visitRangeBothExpression(SchemaParser.RangeBothExpressionContext ctx) {
        var e1 = visit(ctx.expression(0));
        var e2 = visit(ctx.expression(1));
        if(e2 == null) return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            if(!(v1 instanceof EInteger i1)) throw failOnOperation(RNGS01,
                    RANGE, v1, ctx.G_RANGE());
            return GRange.of(i1, null);
        }, RNGS11, ctx);

        return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            if(!(v1 instanceof EInteger i1) || !(v2 instanceof EInteger i2))
                throw failOnOperation(RNGS02, RANGE, v1, v2, ctx.G_RANGE());
            return GRange.of(i1, i2);
        }, RNGS12, ctx);
    }

    @Override
    public Evaluator visitRangeEndExpression(SchemaParser.RangeEndExpressionContext ctx) {
        var e2 = visit(ctx.expression());
        return tryCatch(scope -> {
            var v2 = dereference(e2.evaluate(scope));
            if(!(v2 instanceof EInteger i2)) throw failOnOperation(RNGS03, RANGE, v2, ctx.G_RANGE());
            return GRange.of(null, i2);
        }, RNGS13, ctx);
    }
}