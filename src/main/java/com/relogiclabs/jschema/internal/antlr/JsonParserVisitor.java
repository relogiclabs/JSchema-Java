package com.relogiclabs.jschema.internal.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JsonParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JsonParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JsonParser#json}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson(JsonParser.JsonContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParser#valueNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueNode(JsonParser.ValueNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParser#objectNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectNode(JsonParser.ObjectNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParser#propertyNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyNode(JsonParser.PropertyNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParser#arrayNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayNode(JsonParser.ArrayNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueNode}
	 * labeled alternative in {@link JsonParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueNode(JsonParser.TrueNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseNode}
	 * labeled alternative in {@link JsonParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseNode(JsonParser.FalseNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringNode}
	 * labeled alternative in {@link JsonParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringNode(JsonParser.StringNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerNode}
	 * labeled alternative in {@link JsonParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerNode(JsonParser.IntegerNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FloatNode}
	 * labeled alternative in {@link JsonParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatNode(JsonParser.FloatNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DoubleNode}
	 * labeled alternative in {@link JsonParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleNode(JsonParser.DoubleNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullNode}
	 * labeled alternative in {@link JsonParser#primitiveNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullNode(JsonParser.NullNodeContext ctx);
}