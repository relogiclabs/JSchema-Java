package com.relogiclabs.json.schema.internal.antlr;
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
	 * Visit a parse tree produced by the {@code AggregateSchema}
	 * labeled alternative in {@link SchemaParser#schema}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregateSchema(SchemaParser.AggregateSchemaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CoreSchema}
	 * labeled alternative in {@link SchemaParser#schema}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoreSchema(SchemaParser.CoreSchemaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#schemaBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaBase(SchemaParser.SchemaBaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#title}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTitle(SchemaParser.TitleContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersion(SchemaParser.VersionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#include}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclude(SchemaParser.IncludeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#pragma}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPragma(SchemaParser.PragmaContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#define}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefine(SchemaParser.DefineContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#aliasName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAliasName(SchemaParser.AliasNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#validatorMain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidatorMain(SchemaParser.ValidatorMainContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#validator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidator(SchemaParser.ValidatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(SchemaParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject(SchemaParser.ObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty(SchemaParser.PropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(SchemaParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#datatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatype(SchemaParser.DatatypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SchemaParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(SchemaParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveTrue}
	 * labeled alternative in {@link SchemaParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveTrue(SchemaParser.PrimitiveTrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveFalse}
	 * labeled alternative in {@link SchemaParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveFalse(SchemaParser.PrimitiveFalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveString}
	 * labeled alternative in {@link SchemaParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveString(SchemaParser.PrimitiveStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveInteger}
	 * labeled alternative in {@link SchemaParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveInteger(SchemaParser.PrimitiveIntegerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveFloat}
	 * labeled alternative in {@link SchemaParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveFloat(SchemaParser.PrimitiveFloatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveDouble}
	 * labeled alternative in {@link SchemaParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveDouble(SchemaParser.PrimitiveDoubleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveNull}
	 * labeled alternative in {@link SchemaParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveNull(SchemaParser.PrimitiveNullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimitiveUndefined}
	 * labeled alternative in {@link SchemaParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveUndefined(SchemaParser.PrimitiveUndefinedContext ctx);
}