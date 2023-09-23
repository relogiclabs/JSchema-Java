package com.relogiclabs.json.schema.internal.antlr;
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
	 * Visit a parse tree produced by {@link JsonParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(JsonParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParser#object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject(JsonParser.ObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(JsonParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link JsonParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(JsonParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveTrue}
	 * labeled alternative in {@link JsonParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveTrue(JsonParser.PrimitiveTrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveFalse}
	 * labeled alternative in {@link JsonParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveFalse(JsonParser.PrimitiveFalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveString}
	 * labeled alternative in {@link JsonParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveString(JsonParser.PrimitiveStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveInteger}
	 * labeled alternative in {@link JsonParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveInteger(JsonParser.PrimitiveIntegerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveFloat}
	 * labeled alternative in {@link JsonParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveFloat(JsonParser.PrimitiveFloatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveDouble}
	 * labeled alternative in {@link JsonParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveDouble(JsonParser.PrimitiveDoubleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveNull}
	 * labeled alternative in {@link JsonParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveNull(JsonParser.PrimitiveNullContext ctx);
}