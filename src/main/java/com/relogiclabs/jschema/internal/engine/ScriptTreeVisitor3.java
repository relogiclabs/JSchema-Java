package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.internal.antlr.SchemaParser.AdditiveExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.AssignmentAugExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.AssignmentBracketExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.AssignmentDotExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.AssignmentIdExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.EqualityExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.LogicalAndExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.LogicalNotExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.LogicalOrExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.MultiplicativeExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.ParenthesizedExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.RelationalExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.UnaryMinusExpressionContext;
import com.relogiclabs.jschema.internal.antlr.SchemaParser.UnaryPlusExpressionContext;
import com.relogiclabs.jschema.internal.script.GBoolean;
import com.relogiclabs.jschema.internal.script.GDouble;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GLeftValue;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.tree.RuntimeContext;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;

import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpAddition;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpAdditionAssignment;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpComparison;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpDivision;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpDivisionAssignment;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpModulus;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpModulusAssignment;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpMultiplication;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpMultiplicationAssignment;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpPropertyAssignment;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpSubtraction;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpSubtractionAssignment;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpUnaryMinus;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.OpUnaryPlus;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidLeftValueAssignment;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnOperation;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnVariableNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areEqual;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.stringify;
import static com.relogiclabs.jschema.internal.script.GBoolean.FALSE;
import static com.relogiclabs.jschema.internal.script.GBoolean.TRUE;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.getLast;
import static com.relogiclabs.jschema.message.ErrorCode.ADDASN01;
import static com.relogiclabs.jschema.message.ErrorCode.ADDASN02;
import static com.relogiclabs.jschema.message.ErrorCode.ADDASN03;
import static com.relogiclabs.jschema.message.ErrorCode.ASNBKT01;
import static com.relogiclabs.jschema.message.ErrorCode.ASNDOT01;
import static com.relogiclabs.jschema.message.ErrorCode.ASNDOT02;
import static com.relogiclabs.jschema.message.ErrorCode.ASNVAR01;
import static com.relogiclabs.jschema.message.ErrorCode.ASNVAR02;
import static com.relogiclabs.jschema.message.ErrorCode.ASNVAR03;
import static com.relogiclabs.jschema.message.ErrorCode.CMPSGE01;
import static com.relogiclabs.jschema.message.ErrorCode.CMPSGE02;
import static com.relogiclabs.jschema.message.ErrorCode.CMPSGT01;
import static com.relogiclabs.jschema.message.ErrorCode.CMPSGT02;
import static com.relogiclabs.jschema.message.ErrorCode.CMPSLE01;
import static com.relogiclabs.jschema.message.ErrorCode.CMPSLE02;
import static com.relogiclabs.jschema.message.ErrorCode.CMPSLT01;
import static com.relogiclabs.jschema.message.ErrorCode.CMPSLT02;
import static com.relogiclabs.jschema.message.ErrorCode.DIVASN01;
import static com.relogiclabs.jschema.message.ErrorCode.DIVASN02;
import static com.relogiclabs.jschema.message.ErrorCode.DIVASN03;
import static com.relogiclabs.jschema.message.ErrorCode.MODASN01;
import static com.relogiclabs.jschema.message.ErrorCode.MODASN02;
import static com.relogiclabs.jschema.message.ErrorCode.MODASN03;
import static com.relogiclabs.jschema.message.ErrorCode.MULASN01;
import static com.relogiclabs.jschema.message.ErrorCode.MULASN02;
import static com.relogiclabs.jschema.message.ErrorCode.MULASN03;
import static com.relogiclabs.jschema.message.ErrorCode.OPADDT01;
import static com.relogiclabs.jschema.message.ErrorCode.OPADDT02;
import static com.relogiclabs.jschema.message.ErrorCode.OPANDL01;
import static com.relogiclabs.jschema.message.ErrorCode.OPDIVD01;
import static com.relogiclabs.jschema.message.ErrorCode.OPDIVD02;
import static com.relogiclabs.jschema.message.ErrorCode.OPEQUL01;
import static com.relogiclabs.jschema.message.ErrorCode.OPMINS01;
import static com.relogiclabs.jschema.message.ErrorCode.OPMINS02;
import static com.relogiclabs.jschema.message.ErrorCode.OPMODU01;
import static com.relogiclabs.jschema.message.ErrorCode.OPMODU02;
import static com.relogiclabs.jschema.message.ErrorCode.OPMULT01;
import static com.relogiclabs.jschema.message.ErrorCode.OPMULT02;
import static com.relogiclabs.jschema.message.ErrorCode.OPNEQL01;
import static com.relogiclabs.jschema.message.ErrorCode.OPNOTL01;
import static com.relogiclabs.jschema.message.ErrorCode.OPORLG01;
import static com.relogiclabs.jschema.message.ErrorCode.OPPLUS01;
import static com.relogiclabs.jschema.message.ErrorCode.OPPLUS02;
import static com.relogiclabs.jschema.message.ErrorCode.OPSUBT01;
import static com.relogiclabs.jschema.message.ErrorCode.OPSUBT02;
import static com.relogiclabs.jschema.message.ErrorCode.SUBASN01;
import static com.relogiclabs.jschema.message.ErrorCode.SUBASN02;
import static com.relogiclabs.jschema.message.ErrorCode.SUBASN03;

public final class ScriptTreeVisitor3 extends ScriptTreeVisitor2 {
    public ScriptTreeVisitor3(RuntimeContext runtime) {
        super(runtime);
    }

    @Override
    public Evaluator visitUnaryPlusExpression(UnaryPlusExpressionContext ctx) {
        var expr1 = visit(ctx.expression());
        return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            if(!(v1 instanceof ENumber)) throw failOnOperation(OPPLUS01,
                OpUnaryPlus, v1, ctx.G_PLUS());
            return v1;
        }, OPPLUS02, ctx);
    }

    @Override
    public Evaluator visitUnaryMinusExpression(UnaryMinusExpressionContext ctx) {
        var expr1 = visit(ctx.expression());
        return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            if(v1 instanceof EInteger i1) return GInteger.from(-i1.getValue());
            if(v1 instanceof ENumber n1) return GDouble.from(-n1.toDouble());
            throw failOnOperation(OPMINS01, OpUnaryMinus, v1, ctx.G_MINUS());
        }, OPMINS02, ctx);
    }

    @Override
    public Evaluator visitLogicalNotExpression(LogicalNotExpressionContext ctx) {
        var expr1 = visit(ctx.expression());
        return tryCatch(scope -> expr1.evaluate(scope).toBoolean()
            ? FALSE : TRUE, OPNOTL01, ctx);
    }

    @Override
    public Evaluator visitMultiplicativeExpression(MultiplicativeExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var expr2 = visit(ctx.expression(1));
        if(ctx.G_MUL() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.from(i1.getValue() * i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.from(n1.toDouble() * n2.toDouble());
            throw failOnOperation(OPMULT01, OpMultiplication, v1, v2, ctx.G_MUL());
        }, OPMULT02, ctx);

        if(ctx.G_DIV() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.from(i1.getValue() / i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.from(n1.toDouble() / n2.toDouble());
            throw failOnOperation(OPDIVD01, OpDivision, v1, v2, ctx.G_DIV());
        }, OPDIVD02, ctx);

        if(ctx.G_MOD() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.from(i1.getValue() % i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.from(n1.toDouble() % n2.toDouble());
            throw failOnOperation(OPMODU01, OpModulus, v1, v2, ctx.G_MOD());
        }, OPMODU02, ctx);

        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public Evaluator visitAdditiveExpression(AdditiveExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var expr2 = visit(ctx.expression(1));
        if(ctx.G_PLUS() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.from(i1.getValue() + i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.from(n1.toDouble() + n2.toDouble());
            if(v1 instanceof EString || v2 instanceof EString)
                return GString.from(stringify(v1) + stringify(v2));
            throw failOnOperation(OPADDT01, OpAddition, v1, v2, ctx.G_PLUS());
        }, OPADDT02, ctx);

        if(ctx.G_MINUS() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.from(i1.getValue() - i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.from(n1.toDouble() - n2.toDouble());
            throw failOnOperation(OPSUBT01, OpSubtraction, v1, v2, ctx.G_MINUS());
        }, OPSUBT02, ctx);

        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public Evaluator visitRelationalExpression(RelationalExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var expr2 = visit(ctx.expression(1));
        if(ctx.G_GT() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(!(v1 instanceof ENumber n1) || !(v2 instanceof ENumber n2))
                throw failOnOperation(CMPSGT01, OpComparison, v1, v2, ctx.G_GT());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return FALSE;
            return d1 > d2 ? TRUE : FALSE;
        }, CMPSGT02, ctx);

        if(ctx.G_GE() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(!(v1 instanceof ENumber n1) || !(v2 instanceof ENumber n2))
                throw failOnOperation(CMPSGE01, OpComparison, v1, v2, ctx.G_GE());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return TRUE;
            return d1 > d2 ? TRUE : FALSE;
        }, CMPSGE02, ctx);

        if(ctx.G_LT() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(!(v1 instanceof ENumber n1) || !(v2 instanceof ENumber n2))
                throw failOnOperation(CMPSLT01, OpComparison, v1, v2, ctx.G_LT());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return FALSE;
            return d1 < d2 ? TRUE : FALSE;
        }, CMPSLT02, ctx);

        if(ctx.G_LE() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(!(v1 instanceof ENumber n1) || !(v2 instanceof ENumber n2))
                throw failOnOperation(CMPSLE01, OpComparison, v1, v2, ctx.G_LE());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return TRUE;
            return d1 < d2 ? TRUE : FALSE;
        }, CMPSLE02, ctx);

        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public Evaluator visitEqualityExpression(EqualityExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var expr2 = visit(ctx.expression(1));
        if(ctx.G_EQ() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            return GBoolean.from(areEqual(v1, v2, runtime));
        }, OPEQUL01, ctx);

        if(ctx.G_NE() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            return GBoolean.from(!areEqual(v1, v2, runtime));
        }, OPNEQL01, ctx);

        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public Evaluator visitLogicalAndExpression(LogicalAndExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var expr2 = visit(ctx.expression(1));
        return tryCatch(scope -> {
            var v1 = expr1.evaluate(scope);
            if(!v1.toBoolean()) return v1;
            return expr2.evaluate(scope);
        }, OPANDL01, ctx);
    }

    @Override
    public Evaluator visitLogicalOrExpression(LogicalOrExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var expr2 = visit(ctx.expression(1));
        return tryCatch(scope -> {
            var v1 = expr1.evaluate(scope);
            if(v1.toBoolean()) return v1;
            return expr2.evaluate(scope);
        }, OPORLG01, ctx);
    }

    @Override
    public Evaluator visitAssignmentAugExpression(AssignmentAugExpressionContext ctx) {
        var lvalue1 = handleLeftValueUpdateExpression(ctx);
        var expr2 = visit(getLast(ctx.expression()));
        if(ctx.G_ADD_ASSIGN() != null) return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(lvalue1.evaluate(scope) instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(ADDASN01, ctx.getStart());
            var v1 = l1.getValue();
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2) {
                var v3 = GInteger.from(i1.getValue() + i2.getValue());
                l1.setValue(v3);
                return v3;
            }
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2) {
                var v3 = GDouble.from(n1.toDouble() + n2.toDouble());
                l1.setValue(v3);
                return v3;
            }
            if(v1 instanceof EString || v2 instanceof EString) {
                var v3 = GString.from(stringify(v1) + stringify(v2));
                l1.setValue(v3);
                return v3;
            }
            throw failOnOperation(ADDASN02, OpAdditionAssignment, v1, v2, ctx.G_ADD_ASSIGN());
        }, ADDASN03, ctx);

        if(ctx.G_SUB_ASSIGN() != null) return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(lvalue1.evaluate(scope) instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(SUBASN01, ctx.getStart());
            var v1 = l1.getValue();
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2) {
                var v3 = GInteger.from(i1.getValue() - i2.getValue());
                l1.setValue(v3);
                return v3;
            }
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2) {
                var v3 = GDouble.from(n1.toDouble() - n2.toDouble());
                l1.setValue(v3);
                return v3;
            }
            throw failOnOperation(SUBASN02, OpSubtractionAssignment, v1, v2, ctx.G_SUB_ASSIGN());
        }, SUBASN03, ctx);

        if(ctx.G_MUL_ASSIGN() != null) return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(lvalue1.evaluate(scope) instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(MULASN01, ctx.getStart());
            var v1 = l1.getValue();
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2) {
                var v3 = GInteger.from(i1.getValue() * i2.getValue());
                l1.setValue(v3);
                return v3;
            }
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2) {
                var v3 = GDouble.from(n1.toDouble() * n2.toDouble());
                l1.setValue(v3);
                return v3;
            }
            throw failOnOperation(MULASN02, OpMultiplicationAssignment, v1, v2, ctx.G_MUL_ASSIGN());
        }, MULASN03, ctx);

        if(ctx.G_DIV_ASSIGN() != null) return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(lvalue1.evaluate(scope) instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(DIVASN01, ctx.getStart());
            var v1 = l1.getValue();
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2) {
                var v3 = GInteger.from(i1.getValue() / i2.getValue());
                l1.setValue(v3);
                return v3;
            }
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2) {
                var v3 = GDouble.from(n1.toDouble() / n2.toDouble());
                l1.setValue(v3);
                return v3;
            }
            throw failOnOperation(DIVASN02, OpDivisionAssignment, v1, v2, ctx.G_DIV_ASSIGN());
        }, DIVASN03, ctx);

        if(ctx.G_MOD_ASSIGN() != null) return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(lvalue1.evaluate(scope) instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(MODASN01, ctx.getStart());
            var v1 = l1.getValue();
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2) {
                var v3 = GInteger.from(i1.getValue() % i2.getValue());
                l1.setValue(v3);
                return v3;
            }
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2) {
                var v3 = GDouble.from(n1.toDouble() % n2.toDouble());
                l1.setValue(v3);
                return v3;
            }
            throw failOnOperation(MODASN02, OpModulusAssignment, v1, v2, ctx.G_MOD_ASSIGN());
        }, MODASN03, ctx);

        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public Evaluator visitAssignmentBracketExpression(AssignmentBracketExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var expr2 = visit(ctx.expression(1));
        var expr3 = visit(ctx.expression(2));
        return tryCatch(scope -> {
            var v3 = dereference(expr3.evaluate(scope));
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(v1 instanceof EArray a1 && v2 instanceof EInteger i2) {
                a1.set((int) i2.getValue(), v3);
                return v3;
            }
            if(v1 instanceof EObject o1 && v2 instanceof EString s2) {
                o1.set(s2.getValue(), v3);
                return v3;
            }
            throwBracketUpdateException(v1, v2, ctx);
            throw new IllegalStateException("Invalid runtime state");
        }, ASNBKT01, ctx);
    }

    @Override
    public Evaluator visitAssignmentDotExpression(AssignmentDotExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var id2 = ctx.G_IDENTIFIER();
        var expr3 = visit(ctx.expression(1));
        var ids2 = id2.getText();
        return tryCatch(scope -> {
            var v3 = dereference(expr3.evaluate(scope));
            var v1 = dereference(expr1.evaluate(scope));
            if(v1 instanceof EObject o1) {
                o1.set(ids2, v3);
                return v3;
            }
            throw failOnOperation(ASNDOT01, OpPropertyAssignment, v1, ctx.G_DOT());
        }, ASNDOT02, ctx);
    }

    @Override
    public Evaluator visitAssignmentIdExpression(AssignmentIdExpressionContext ctx) {
        var id1 = ctx.G_IDENTIFIER();
        var expr2 = visit(ctx.expression());
        var key1 = id1.getText();
        return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            var v1 = scope.resolve(key1);
            if(v1 == null) throw failOnVariableNotFound(ASNVAR01, id1.getSymbol());
            if(!(v1 instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(ASNVAR02, id1.getSymbol());
            l1.setValue(v2);
            return v2;
        }, ASNVAR03, ctx);
    }

    @Override
    public Evaluator visitParenthesizedExpression(ParenthesizedExpressionContext ctx) {
        return visit(ctx.expression());
    }
}