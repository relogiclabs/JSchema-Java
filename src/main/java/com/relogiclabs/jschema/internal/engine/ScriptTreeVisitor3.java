package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.internal.antlr.SchemaParser;
import com.relogiclabs.jschema.internal.script.GBoolean;
import com.relogiclabs.jschema.internal.script.GDouble;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GReference;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EString;

import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidLValueAssignment;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidLValueDecrement;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidLValueIncrement;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnOperation;
import static com.relogiclabs.jschema.internal.engine.ScriptOperation.ADDITION;
import static com.relogiclabs.jschema.internal.engine.ScriptOperation.COMPARISON;
import static com.relogiclabs.jschema.internal.engine.ScriptOperation.DECREMENT;
import static com.relogiclabs.jschema.internal.engine.ScriptOperation.DIVISION;
import static com.relogiclabs.jschema.internal.engine.ScriptOperation.INCREMENT;
import static com.relogiclabs.jschema.internal.engine.ScriptOperation.MULTIPLICATION;
import static com.relogiclabs.jschema.internal.engine.ScriptOperation.NEGATION;
import static com.relogiclabs.jschema.internal.engine.ScriptOperation.SUBTRACTION;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areEqual;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.decrement;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.increment;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.stringify;
import static com.relogiclabs.jschema.internal.script.GBoolean.FALSE;
import static com.relogiclabs.jschema.internal.script.GBoolean.TRUE;
import static com.relogiclabs.jschema.message.ErrorCode.ADDT01;
import static com.relogiclabs.jschema.message.ErrorCode.ADDT02;
import static com.relogiclabs.jschema.message.ErrorCode.ANDL01;
import static com.relogiclabs.jschema.message.ErrorCode.ASIN02;
import static com.relogiclabs.jschema.message.ErrorCode.DECR01;
import static com.relogiclabs.jschema.message.ErrorCode.DECR02;
import static com.relogiclabs.jschema.message.ErrorCode.DECR03;
import static com.relogiclabs.jschema.message.ErrorCode.DECR04;
import static com.relogiclabs.jschema.message.ErrorCode.DECR05;
import static com.relogiclabs.jschema.message.ErrorCode.DECR06;
import static com.relogiclabs.jschema.message.ErrorCode.DIVD01;
import static com.relogiclabs.jschema.message.ErrorCode.DIVD02;
import static com.relogiclabs.jschema.message.ErrorCode.EQUL01;
import static com.relogiclabs.jschema.message.ErrorCode.INCR01;
import static com.relogiclabs.jschema.message.ErrorCode.INCR02;
import static com.relogiclabs.jschema.message.ErrorCode.INCR03;
import static com.relogiclabs.jschema.message.ErrorCode.INCR04;
import static com.relogiclabs.jschema.message.ErrorCode.INCR05;
import static com.relogiclabs.jschema.message.ErrorCode.INCR06;
import static com.relogiclabs.jschema.message.ErrorCode.MULT01;
import static com.relogiclabs.jschema.message.ErrorCode.MULT02;
import static com.relogiclabs.jschema.message.ErrorCode.NEGT01;
import static com.relogiclabs.jschema.message.ErrorCode.NEGT02;
import static com.relogiclabs.jschema.message.ErrorCode.NEQL01;
import static com.relogiclabs.jschema.message.ErrorCode.NOTL01;
import static com.relogiclabs.jschema.message.ErrorCode.ORLG01;
import static com.relogiclabs.jschema.message.ErrorCode.RELA01;
import static com.relogiclabs.jschema.message.ErrorCode.RELA02;
import static com.relogiclabs.jschema.message.ErrorCode.RELA03;
import static com.relogiclabs.jschema.message.ErrorCode.RELA04;
import static com.relogiclabs.jschema.message.ErrorCode.RELA05;
import static com.relogiclabs.jschema.message.ErrorCode.RELA06;
import static com.relogiclabs.jschema.message.ErrorCode.RELA07;
import static com.relogiclabs.jschema.message.ErrorCode.RELA08;
import static com.relogiclabs.jschema.message.ErrorCode.SUBT01;
import static com.relogiclabs.jschema.message.ErrorCode.SUBT02;

public final class ScriptTreeVisitor3 extends ScriptTreeVisitor2 {
    public ScriptTreeVisitor3(RuntimeContext runtime) {
        super(runtime);
    }

    @Override
    public Evaluator visitUnaryMinusExpression(SchemaParser.UnaryMinusExpressionContext ctx) {
        var e1 = visit(ctx.expression());
        return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            if(v1 instanceof EInteger i1) return GInteger.of(-i1.getValue());
            if(v1 instanceof ENumber n1) return GDouble.of(-n1.toDouble());
            throw failOnOperation(NEGT01, NEGATION, v1, ctx.G_MINUS());
        }, NEGT02, ctx);
    }

    @Override
    public Evaluator visitLogicalNotExpression(SchemaParser.LogicalNotExpressionContext ctx) {
        var e1 = visit(ctx.expression());
        return tryCatch(scope -> e1.evaluate(scope).toBoolean() ? FALSE : TRUE,
                NOTL01, ctx);
    }

    @Override
    public Evaluator visitPostIncrementExpression(SchemaParser.PostIncrementExpressionContext ctx) {
        var e1 = visit(ctx.refExpression());
        return tryCatch(scope -> {
            var v1 = e1.evaluate(scope);
            if(!(v1 instanceof GReference r1)) throw failOnInvalidLValueIncrement(INCR01,
                    ctx.refExpression().getStart());
            if(!(r1.getValue() instanceof ENumber n1)) throw failOnOperation(INCR02,
                    INCREMENT, v1, ctx.G_INC());
            r1.setValue(increment(n1));
            return n1;
        }, INCR05, ctx);
    }

    @Override
    public Evaluator visitPreIncrementExpression(SchemaParser.PreIncrementExpressionContext ctx) {
        var e1 = visit(ctx.refExpression());
        return tryCatch(scope -> {
            var v1 = e1.evaluate(scope);
            if(!(v1 instanceof GReference r1)) throw failOnInvalidLValueIncrement(INCR03,
                    ctx.refExpression().getStart());
            if(!(r1.getValue() instanceof ENumber n1)) throw failOnOperation(INCR04,
                    INCREMENT, v1, ctx.G_INC());
            r1.setValue(n1 = increment(n1));
            return n1;
        }, INCR06, ctx);
    }

    @Override
    public Evaluator visitPostDecrementExpression(SchemaParser.PostDecrementExpressionContext ctx) {
        var e1 = visit(ctx.refExpression());
        return tryCatch(scope -> {
            var v1 = e1.evaluate(scope);
            if(!(v1 instanceof GReference r1)) throw failOnInvalidLValueDecrement(DECR01,
                    ctx.refExpression().getStart());
            if(!(r1.getValue() instanceof ENumber n1)) throw failOnOperation(DECR02,
                    DECREMENT, v1, ctx.G_DEC());
            r1.setValue(decrement(n1));
            return n1;
        }, DECR05, ctx);
    }

    @Override
    public Evaluator visitPreDecrementExpression(SchemaParser.PreDecrementExpressionContext ctx) {
        var e1 = visit(ctx.refExpression());
        return tryCatch(scope -> {
            var v1 = e1.evaluate(scope);
            if(!(v1 instanceof GReference r1)) throw failOnInvalidLValueDecrement(DECR03,
                    ctx.refExpression().getStart());
            if(!(r1.getValue() instanceof ENumber n1)) throw failOnOperation(DECR04,
                    DECREMENT, v1, ctx.G_DEC());
            r1.setValue(n1 = decrement(n1));
            return n1;
        }, DECR06, ctx);
    }

    @Override
    public Evaluator visitMultiplicativeExpression(SchemaParser.MultiplicativeExpressionContext ctx) {
        var e1 = visit(ctx.expression(0));
        var e2 = visit(ctx.expression(1));
        if(ctx.G_MUL() != null) return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.of(i1.getValue() * i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.of(n1.toDouble() * n2.toDouble());
            throw failOnOperation(MULT01, MULTIPLICATION, v1, v2, ctx.G_MUL());
        }, MULT02, ctx);

        if(ctx.G_DIV() != null) return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.of(i1.getValue() / i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.of(n1.toDouble() / n2.toDouble());
            throw failOnOperation(DIVD01, DIVISION, v1, v2, ctx.G_DIV());
        }, DIVD02, ctx);

        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public Evaluator visitAdditiveExpression(SchemaParser.AdditiveExpressionContext ctx) {
        var e1 = visit(ctx.expression(0));
        var e2 = visit(ctx.expression(1));
        if(ctx.G_PLUS() != null) return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.of(i1.getValue() + i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.of(n1.toDouble() + n2.toDouble());
            if(v1 instanceof EString || v2 instanceof EString)
                return GString.of(stringify(v1) + stringify(v2));
            throw failOnOperation(ADDT01, ADDITION, v1, v2, ctx.G_PLUS());
        }, ADDT02, ctx);

        if(ctx.G_MINUS() != null) return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.of(i1.getValue() - i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.of(n1.toDouble() - n2.toDouble());
            throw failOnOperation(SUBT01, SUBTRACTION, v1, v2, ctx.G_MINUS());
        }, SUBT02, ctx);

        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public Evaluator visitRelationalExpression(SchemaParser.RelationalExpressionContext ctx) {
        var e1 = visit(ctx.expression(0));
        var e2 = visit(ctx.expression(1));
        if(ctx.G_GT() != null) return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            if(!(v1 instanceof ENumber n1) || !(v2 instanceof ENumber n2))
                throw failOnOperation(RELA01, COMPARISON, v1, v2, ctx.G_GT());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return FALSE;
            return d1 > d2 ? TRUE : FALSE;
        }, RELA05, ctx);

        if(ctx.G_GE() != null) return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            if(!(v1 instanceof ENumber n1) || !(v2 instanceof ENumber n2))
                throw failOnOperation(RELA02, COMPARISON, v1, v2, ctx.G_GE());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return TRUE;
            return d1 > d2 ? TRUE : FALSE;
        }, RELA06, ctx);

        if(ctx.G_LT() != null) return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            if(!(v1 instanceof ENumber n1) || !(v2 instanceof ENumber n2))
                throw failOnOperation(RELA03, COMPARISON, v1, v2, ctx.G_LT());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return FALSE;
            return d1 < d2 ? TRUE : FALSE;
        }, RELA07, ctx);

        if(ctx.G_LE() != null) return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            if(!(v1 instanceof ENumber n1) || !(v2 instanceof ENumber n2))
                throw failOnOperation(RELA04, COMPARISON, v1, v2, ctx.G_LE());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return TRUE;
            return d1 < d2 ? TRUE : FALSE;
        }, RELA08, ctx);

        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public Evaluator visitEqualityExpression(SchemaParser.EqualityExpressionContext ctx) {
        var e1 = visit(ctx.expression(0));
        var e2 = visit(ctx.expression(1));
        if(ctx.G_EQ() != null) return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            return GBoolean.of(areEqual(v1, v2, runtime));
        }, EQUL01, ctx);

        if(ctx.G_NE() != null) return tryCatch(scope -> {
            var v1 = dereference(e1.evaluate(scope));
            var v2 = dereference(e2.evaluate(scope));
            return GBoolean.of(!areEqual(v1, v2, runtime));
        }, NEQL01, ctx);

        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public Evaluator visitLogicalAndExpression(SchemaParser.LogicalAndExpressionContext ctx) {
        var e1 = visit(ctx.expression(0));
        var e2 = visit(ctx.expression(1));
        return tryCatch(scope -> {
            var v1 = e1.evaluate(scope);
            if(!v1.toBoolean()) return v1;
            return e2.evaluate(scope);
        }, ANDL01, ctx);
    }

    @Override
    public Evaluator visitLogicalOrExpression(SchemaParser.LogicalOrExpressionContext ctx) {
        var e1 = visit(ctx.expression(0));
        var e2 = visit(ctx.expression(1));
        return tryCatch(scope -> {
            var v1 = e1.evaluate(scope);
            if(v1.toBoolean()) return v1;
            return e2.evaluate(scope);
        }, ORLG01, ctx);
    }

    @Override
    public Evaluator visitAssignmentExpression(SchemaParser.AssignmentExpressionContext ctx) {
        var e1 = visit(ctx.refExpression());
        var e2 = visit(ctx.expression());
        return tryCatch(scope -> {
            var v2 = e2.evaluate(scope);
            var v1 = e1.evaluate(scope);
            if(!(v1 instanceof GReference r1)) throw failOnInvalidLValueAssignment(
                    ctx.refExpression().getStart());
            r1.setValue(v2);
            return v2;
        }, ASIN02, ctx);
    }

    @Override
    public Evaluator visitParenthesizedExpression(SchemaParser.ParenthesizedExpressionContext ctx) {
        return visit(ctx.expression());
    }
}