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
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnIdentifierNotFound;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnInvalidLeftValueAssignment;
import static com.relogiclabs.jschema.internal.engine.ScriptErrorHelper.failOnOperation;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areEqual;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.stringify;
import static com.relogiclabs.jschema.internal.script.GBoolean.FALSE;
import static com.relogiclabs.jschema.internal.script.GBoolean.TRUE;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.getLast;
import static com.relogiclabs.jschema.message.ErrorCode.ADDN01;
import static com.relogiclabs.jschema.message.ErrorCode.ADDN02;
import static com.relogiclabs.jschema.message.ErrorCode.ADDN03;
import static com.relogiclabs.jschema.message.ErrorCode.ADDT01;
import static com.relogiclabs.jschema.message.ErrorCode.ADDT02;
import static com.relogiclabs.jschema.message.ErrorCode.ANDL01;
import static com.relogiclabs.jschema.message.ErrorCode.ASIN01;
import static com.relogiclabs.jschema.message.ErrorCode.ASIN02;
import static com.relogiclabs.jschema.message.ErrorCode.ASIN03;
import static com.relogiclabs.jschema.message.ErrorCode.ASIN04;
import static com.relogiclabs.jschema.message.ErrorCode.ASIN05;
import static com.relogiclabs.jschema.message.ErrorCode.DIVD01;
import static com.relogiclabs.jschema.message.ErrorCode.DIVD02;
import static com.relogiclabs.jschema.message.ErrorCode.DIVN01;
import static com.relogiclabs.jschema.message.ErrorCode.DIVN02;
import static com.relogiclabs.jschema.message.ErrorCode.DIVN03;
import static com.relogiclabs.jschema.message.ErrorCode.EQUL01;
import static com.relogiclabs.jschema.message.ErrorCode.IDEN03;
import static com.relogiclabs.jschema.message.ErrorCode.MINS01;
import static com.relogiclabs.jschema.message.ErrorCode.MINS02;
import static com.relogiclabs.jschema.message.ErrorCode.MODN01;
import static com.relogiclabs.jschema.message.ErrorCode.MODN02;
import static com.relogiclabs.jschema.message.ErrorCode.MODN03;
import static com.relogiclabs.jschema.message.ErrorCode.MODU01;
import static com.relogiclabs.jschema.message.ErrorCode.MODU02;
import static com.relogiclabs.jschema.message.ErrorCode.MULN01;
import static com.relogiclabs.jschema.message.ErrorCode.MULN02;
import static com.relogiclabs.jschema.message.ErrorCode.MULN03;
import static com.relogiclabs.jschema.message.ErrorCode.MULT01;
import static com.relogiclabs.jschema.message.ErrorCode.MULT02;
import static com.relogiclabs.jschema.message.ErrorCode.NEQL01;
import static com.relogiclabs.jschema.message.ErrorCode.NOTL01;
import static com.relogiclabs.jschema.message.ErrorCode.ORLG01;
import static com.relogiclabs.jschema.message.ErrorCode.PLUS01;
import static com.relogiclabs.jschema.message.ErrorCode.PLUS02;
import static com.relogiclabs.jschema.message.ErrorCode.RELA01;
import static com.relogiclabs.jschema.message.ErrorCode.RELA02;
import static com.relogiclabs.jschema.message.ErrorCode.RELA03;
import static com.relogiclabs.jschema.message.ErrorCode.RELA04;
import static com.relogiclabs.jschema.message.ErrorCode.RELA05;
import static com.relogiclabs.jschema.message.ErrorCode.RELA06;
import static com.relogiclabs.jschema.message.ErrorCode.RELA07;
import static com.relogiclabs.jschema.message.ErrorCode.RELA08;
import static com.relogiclabs.jschema.message.ErrorCode.SUBN01;
import static com.relogiclabs.jschema.message.ErrorCode.SUBN02;
import static com.relogiclabs.jschema.message.ErrorCode.SUBN03;
import static com.relogiclabs.jschema.message.ErrorCode.SUBT01;
import static com.relogiclabs.jschema.message.ErrorCode.SUBT02;

public final class ScriptTreeVisitor3 extends ScriptTreeVisitor2 {
    public ScriptTreeVisitor3(RuntimeContext runtime) {
        super(runtime);
    }

    @Override
    public Evaluator visitUnaryPlusExpression(UnaryPlusExpressionContext ctx) {
        var expr1 = visit(ctx.expression());
        return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            if(!(v1 instanceof ENumber)) throw failOnOperation(PLUS01,
                OpUnaryPlus, v1, ctx.G_PLUS());
            return v1;
        }, PLUS02, ctx);
    }

    @Override
    public Evaluator visitUnaryMinusExpression(UnaryMinusExpressionContext ctx) {
        var expr1 = visit(ctx.expression());
        return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            if(v1 instanceof EInteger i1) return GInteger.from(-i1.getValue());
            if(v1 instanceof ENumber n1) return GDouble.from(-n1.toDouble());
            throw failOnOperation(MINS01, OpUnaryMinus, v1, ctx.G_MINUS());
        }, MINS02, ctx);
    }

    @Override
    public Evaluator visitLogicalNotExpression(LogicalNotExpressionContext ctx) {
        var expr1 = visit(ctx.expression());
        return tryCatch(scope -> expr1.evaluate(scope).toBoolean()
            ? FALSE : TRUE, NOTL01, ctx);
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
            throw failOnOperation(MULT01, OpMultiplication, v1, v2, ctx.G_MUL());
        }, MULT02, ctx);

        if(ctx.G_DIV() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.from(i1.getValue() / i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.from(n1.toDouble() / n2.toDouble());
            throw failOnOperation(DIVD01, OpDivision, v1, v2, ctx.G_DIV());
        }, DIVD02, ctx);

        if(ctx.G_MOD() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.from(i1.getValue() % i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.from(n1.toDouble() % n2.toDouble());
            throw failOnOperation(MODU01, OpModulus, v1, v2, ctx.G_MOD());
        }, MODU02, ctx);

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
            throw failOnOperation(ADDT01, OpAddition, v1, v2, ctx.G_PLUS());
        }, ADDT02, ctx);

        if(ctx.G_MINUS() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(v1 instanceof EInteger i1 && v2 instanceof EInteger i2)
                return GInteger.from(i1.getValue() - i2.getValue());
            if(v1 instanceof ENumber n1 && v2 instanceof ENumber n2)
                return GDouble.from(n1.toDouble() - n2.toDouble());
            throw failOnOperation(SUBT01, OpSubtraction, v1, v2, ctx.G_MINUS());
        }, SUBT02, ctx);

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
                throw failOnOperation(RELA01, OpComparison, v1, v2, ctx.G_GT());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return FALSE;
            return d1 > d2 ? TRUE : FALSE;
        }, RELA05, ctx);

        if(ctx.G_GE() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(!(v1 instanceof ENumber n1) || !(v2 instanceof ENumber n2))
                throw failOnOperation(RELA02, OpComparison, v1, v2, ctx.G_GE());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return TRUE;
            return d1 > d2 ? TRUE : FALSE;
        }, RELA06, ctx);

        if(ctx.G_LT() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(!(v1 instanceof ENumber n1) || !(v2 instanceof ENumber n2))
                throw failOnOperation(RELA03, OpComparison, v1, v2, ctx.G_LT());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return FALSE;
            return d1 < d2 ? TRUE : FALSE;
        }, RELA07, ctx);

        if(ctx.G_LE() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            if(!(v1 instanceof ENumber n1) || !(v2 instanceof ENumber n2))
                throw failOnOperation(RELA04, OpComparison, v1, v2, ctx.G_LE());
            double d1 = n1.toDouble(), d2 = n2.toDouble();
            if(runtime.areEqual(d1, d2)) return TRUE;
            return d1 < d2 ? TRUE : FALSE;
        }, RELA08, ctx);

        throw new IllegalStateException("Invalid parser state");
    }

    @Override
    public Evaluator visitEqualityExpression(EqualityExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var expr2 = visit(ctx.expression(1));
        if(ctx.G_EQ() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            return areEqual(v1, v2, runtime) ? TRUE : FALSE;
        }, EQUL01, ctx);

        if(ctx.G_NE() != null) return tryCatch(scope -> {
            var v1 = dereference(expr1.evaluate(scope));
            var v2 = dereference(expr2.evaluate(scope));
            return areEqual(v1, v2, runtime) ? FALSE : TRUE;
        }, NEQL01, ctx);

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
        }, ANDL01, ctx);
    }

    @Override
    public Evaluator visitLogicalOrExpression(LogicalOrExpressionContext ctx) {
        var expr1 = visit(ctx.expression(0));
        var expr2 = visit(ctx.expression(1));
        return tryCatch(scope -> {
            var v1 = expr1.evaluate(scope);
            if(v1.toBoolean()) return v1;
            return expr2.evaluate(scope);
        }, ORLG01, ctx);
    }

    @Override
    public Evaluator visitAssignmentAugExpression(AssignmentAugExpressionContext ctx) {
        var lvalue1 = handleLeftValueUpdateExpression(ctx);
        var expr2 = visit(getLast(ctx.expression()));
        if(ctx.G_ADD_ASSIGN() != null) return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(lvalue1.evaluate(scope) instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(ADDN01, ctx.getStart());
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
            throw failOnOperation(ADDN02, OpAdditionAssignment, v1, v2, ctx.G_ADD_ASSIGN());
        }, ADDN03, ctx);

        if(ctx.G_SUB_ASSIGN() != null) return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(lvalue1.evaluate(scope) instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(SUBN01, ctx.getStart());
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
            throw failOnOperation(SUBN02, OpSubtractionAssignment, v1, v2, ctx.G_SUB_ASSIGN());
        }, SUBN03, ctx);

        if(ctx.G_MUL_ASSIGN() != null) return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(lvalue1.evaluate(scope) instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(MULN01, ctx.getStart());
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
            throw failOnOperation(MULN02, OpMultiplicationAssignment, v1, v2, ctx.G_MUL_ASSIGN());
        }, MULN03, ctx);

        if(ctx.G_DIV_ASSIGN() != null) return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(lvalue1.evaluate(scope) instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(DIVN01, ctx.getStart());
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
            throw failOnOperation(DIVN02, OpDivisionAssignment, v1, v2, ctx.G_DIV_ASSIGN());
        }, DIVN03, ctx);

        if(ctx.G_MOD_ASSIGN() != null) return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            if(!(lvalue1.evaluate(scope) instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(MODN01, ctx.getStart());
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
            throw failOnOperation(MODN02, OpModulusAssignment, v1, v2, ctx.G_MOD_ASSIGN());
        }, MODN03, ctx);

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
        }, ASIN01, ctx);
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
            throw failOnOperation(ASIN02, OpPropertyAssignment, v1, ctx.G_DOT());
        }, ASIN03, ctx);
    }

    @Override
    public Evaluator visitAssignmentIdExpression(AssignmentIdExpressionContext ctx) {
        var id1 = ctx.G_IDENTIFIER();
        var expr2 = visit(ctx.expression());
        var ids1 = id1.getText();
        return tryCatch(scope -> {
            var v2 = dereference(expr2.evaluate(scope));
            var v1 = scope.resolve(ids1);
            if(v1 == null) throw failOnIdentifierNotFound(IDEN03, id1.getSymbol());
            if(!(v1 instanceof GLeftValue l1))
                throw failOnInvalidLeftValueAssignment(ASIN04, id1.getSymbol());
            l1.setValue(v2);
            return v2;
        }, ASIN05, ctx);
    }

    @Override
    public Evaluator visitParenthesizedExpression(ParenthesizedExpressionContext ctx) {
        return visit(ctx.expression());
    }
}