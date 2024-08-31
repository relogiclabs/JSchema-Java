package com.relogiclabs.jschema.internal.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SchemaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SchemaParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code CompleteSchema}
	 * labeled alternative in {@link SchemaParser#schema}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompleteSchema(SchemaParser.CompleteSchemaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ShortSchema}
	 * labeled alternative in {@link SchemaParser#schema}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShortSchema(SchemaParser.ShortSchemaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#schemaCoreNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaCoreNode(SchemaParser.SchemaCoreNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#titleNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTitleNode(SchemaParser.TitleNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#versionNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersionNode(SchemaParser.VersionNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#importNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportNode(SchemaParser.ImportNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#pragmaNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPragmaNode(SchemaParser.PragmaNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#defineNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefineNode(SchemaParser.DefineNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#validatorNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidatorNode(SchemaParser.ValidatorNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#validatorMainNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidatorMainNode(SchemaParser.ValidatorMainNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#aliasNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAliasNode(SchemaParser.AliasNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#valueNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueNode(SchemaParser.ValueNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#receiverNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReceiverNode(SchemaParser.ReceiverNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#objectNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectNode(SchemaParser.ObjectNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#propertyNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyNode(SchemaParser.PropertyNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#arrayNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayNode(SchemaParser.ArrayNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#datatypeNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatypeNode(SchemaParser.DatatypeNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#functionNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionNode(SchemaParser.FunctionNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#argumentNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentNode(SchemaParser.ArgumentNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueNode}
	 * labeled alternative in {@link SchemaParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueNode(SchemaParser.TrueNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseNode}
	 * labeled alternative in {@link SchemaParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseNode(SchemaParser.FalseNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringNode}
	 * labeled alternative in {@link SchemaParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringNode(SchemaParser.StringNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerNode}
	 * labeled alternative in {@link SchemaParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerNode(SchemaParser.IntegerNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FloatNode}
	 * labeled alternative in {@link SchemaParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatNode(SchemaParser.FloatNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DoubleNode}
	 * labeled alternative in {@link SchemaParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleNode(SchemaParser.DoubleNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullNode}
	 * labeled alternative in {@link SchemaParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullNode(SchemaParser.NullNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UndefinedNode}
	 * labeled alternative in {@link SchemaParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndefinedNode(SchemaParser.UndefinedNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#scriptNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScriptNode(SchemaParser.ScriptNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#globalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalStatement(SchemaParser.GlobalStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(SchemaParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#functionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(SchemaParser.FunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#varStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarStatement(SchemaParser.VarStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(SchemaParser.VarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#expressionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(SchemaParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(SchemaParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(SchemaParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(SchemaParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(SchemaParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#foreachStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeachStatement(SchemaParser.ForeachStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(SchemaParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(SchemaParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(SchemaParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CallerExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallerExpression(SchemaParser.CallerExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalAndExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndExpression(SchemaParser.LogicalAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignmentBracketExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentBracketExpression(SchemaParser.AssignmentBracketExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalOrExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOrExpression(SchemaParser.LogicalOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TryofExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryofExpression(SchemaParser.TryofExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignmentAugExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentAugExpression(SchemaParser.AssignmentAugExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryMinusExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinusExpression(SchemaParser.UnaryMinusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryPlusExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryPlusExpression(SchemaParser.UnaryPlusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualityExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(SchemaParser.EqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultiplicativeExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(SchemaParser.MultiplicativeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PreIncDecExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreIncDecExpression(SchemaParser.PreIncDecExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RangeEndExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeEndExpression(SchemaParser.RangeEndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpression(SchemaParser.ParenthesizedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AdditiveExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(SchemaParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RelationalExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpression(SchemaParser.RelationalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MemberBracketExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberBracketExpression(SchemaParser.MemberBracketExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PostIncDecExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostIncDecExpression(SchemaParser.PostIncDecExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpression(SchemaParser.LiteralExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MemberDotExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberDotExpression(SchemaParser.MemberDotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TargetExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetExpression(SchemaParser.TargetExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalNotExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalNotExpression(SchemaParser.LogicalNotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VariableExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableExpression(SchemaParser.VariableExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ThrowExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowExpression(SchemaParser.ThrowExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InvokeFunctionExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvokeFunctionExpression(SchemaParser.InvokeFunctionExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignmentIdExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentIdExpression(SchemaParser.AssignmentIdExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InvokeMethodExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvokeMethodExpression(SchemaParser.InvokeMethodExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignmentDotExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentDotExpression(SchemaParser.AssignmentDotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RangeBothExpression}
	 * labeled alternative in {@link SchemaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeBothExpression(SchemaParser.RangeBothExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueLiteral}
	 * labeled alternative in {@link SchemaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueLiteral(SchemaParser.TrueLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseLiteral}
	 * labeled alternative in {@link SchemaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseLiteral(SchemaParser.FalseLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link SchemaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(SchemaParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DoubleLiteral}
	 * labeled alternative in {@link SchemaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleLiteral(SchemaParser.DoubleLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link SchemaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(SchemaParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayLiteral}
	 * labeled alternative in {@link SchemaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLiteral(SchemaParser.ArrayLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ObjectLiteral}
	 * labeled alternative in {@link SchemaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectLiteral(SchemaParser.ObjectLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullLiteral}
	 * labeled alternative in {@link SchemaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(SchemaParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UndefinedLiteral}
	 * labeled alternative in {@link SchemaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndefinedLiteral(SchemaParser.UndefinedLiteralContext ctx);
}