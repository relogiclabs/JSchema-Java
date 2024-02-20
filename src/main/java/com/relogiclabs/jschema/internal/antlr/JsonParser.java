package com.relogiclabs.jschema.internal.antlr;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class JsonParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TRUE=1, FALSE=2, NULL=3, LBRACKET=4, RBRACKET=5, LBRACE=6, RBRACE=7, COMMA=8,
		COLON=9, STRING=10, INTEGER=11, FLOAT=12, DOUBLE=13, WHITE_SPACE=14;
	public static final int
		RULE_json = 0, RULE_valueNode = 1, RULE_objectNode = 2, RULE_propertyNode = 3,
		RULE_arrayNode = 4, RULE_primitiveNode = 5;
	private static String[] makeRuleNames() {
		return new String[] {
			"json", "valueNode", "objectNode", "propertyNode", "arrayNode", "primitiveNode"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'true'", "'false'", "'null'", "'['", "']'", "'{'", "'}'", "','",
			"':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TRUE", "FALSE", "NULL", "LBRACKET", "RBRACKET", "LBRACE", "RBRACE",
			"COMMA", "COLON", "STRING", "INTEGER", "FLOAT", "DOUBLE", "WHITE_SPACE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "JsonParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JsonParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JsonContext extends ParserRuleContext {
		public ValueNodeContext valueNode() {
			return getRuleContext(ValueNodeContext.class,0);
		}
		public TerminalNode EOF() { return getToken(JsonParser.EOF, 0); }
		public JsonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_json; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitJson(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonContext json() throws RecognitionException {
		JsonContext _localctx = new JsonContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_json);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			valueNode();
			setState(13);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueNodeContext extends ParserRuleContext {
		public PrimitiveNodeContext primitiveNode() {
			return getRuleContext(PrimitiveNodeContext.class,0);
		}
		public ObjectNodeContext objectNode() {
			return getRuleContext(ObjectNodeContext.class,0);
		}
		public ArrayNodeContext arrayNode() {
			return getRuleContext(ArrayNodeContext.class,0);
		}
		public ValueNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueNode; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitValueNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueNodeContext valueNode() throws RecognitionException {
		ValueNodeContext _localctx = new ValueNodeContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_valueNode);
		try {
			setState(18);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case NULL:
			case STRING:
			case INTEGER:
			case FLOAT:
			case DOUBLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(15);
				primitiveNode();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(16);
				objectNode();
				}
				break;
			case LBRACKET:
				enterOuterAlt(_localctx, 3);
				{
				setState(17);
				arrayNode();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ObjectNodeContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(JsonParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(JsonParser.RBRACE, 0); }
		public List<PropertyNodeContext> propertyNode() {
			return getRuleContexts(PropertyNodeContext.class);
		}
		public PropertyNodeContext propertyNode(int i) {
			return getRuleContext(PropertyNodeContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JsonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JsonParser.COMMA, i);
		}
		public ObjectNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectNode; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitObjectNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectNodeContext objectNode() throws RecognitionException {
		ObjectNodeContext _localctx = new ObjectNodeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_objectNode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(20);
			match(LBRACE);
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING) {
				{
				setState(21);
				propertyNode();
				setState(26);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(22);
					match(COMMA);
					setState(23);
					propertyNode();
					}
					}
					setState(28);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(31);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyNodeContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(JsonParser.STRING, 0); }
		public TerminalNode COLON() { return getToken(JsonParser.COLON, 0); }
		public ValueNodeContext valueNode() {
			return getRuleContext(ValueNodeContext.class,0);
		}
		public PropertyNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyNode; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitPropertyNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyNodeContext propertyNode() throws RecognitionException {
		PropertyNodeContext _localctx = new PropertyNodeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_propertyNode);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(33);
			match(STRING);
			setState(34);
			match(COLON);
			setState(35);
			valueNode();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayNodeContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(JsonParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(JsonParser.RBRACKET, 0); }
		public List<ValueNodeContext> valueNode() {
			return getRuleContexts(ValueNodeContext.class);
		}
		public ValueNodeContext valueNode(int i) {
			return getRuleContext(ValueNodeContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(JsonParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(JsonParser.COMMA, i);
		}
		public ArrayNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayNode; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitArrayNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayNodeContext arrayNode() throws RecognitionException {
		ArrayNodeContext _localctx = new ArrayNodeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_arrayNode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			match(LBRACKET);
			setState(46);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 15454L) != 0)) {
				{
				setState(38);
				valueNode();
				setState(43);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(39);
					match(COMMA);
					setState(40);
					valueNode();
					}
					}
					setState(45);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(48);
			match(RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveNodeContext extends ParserRuleContext {
		public PrimitiveNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveNode; }

		public PrimitiveNodeContext() { }
		public void copyFrom(PrimitiveNodeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveDoubleContext extends PrimitiveNodeContext {
		public TerminalNode DOUBLE() { return getToken(JsonParser.DOUBLE, 0); }
		public PrimitiveDoubleContext(PrimitiveNodeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitPrimitiveDouble(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveFloatContext extends PrimitiveNodeContext {
		public TerminalNode FLOAT() { return getToken(JsonParser.FLOAT, 0); }
		public PrimitiveFloatContext(PrimitiveNodeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitPrimitiveFloat(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveNullContext extends PrimitiveNodeContext {
		public TerminalNode NULL() { return getToken(JsonParser.NULL, 0); }
		public PrimitiveNullContext(PrimitiveNodeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitPrimitiveNull(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveTrueContext extends PrimitiveNodeContext {
		public TerminalNode TRUE() { return getToken(JsonParser.TRUE, 0); }
		public PrimitiveTrueContext(PrimitiveNodeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitPrimitiveTrue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveFalseContext extends PrimitiveNodeContext {
		public TerminalNode FALSE() { return getToken(JsonParser.FALSE, 0); }
		public PrimitiveFalseContext(PrimitiveNodeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitPrimitiveFalse(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveStringContext extends PrimitiveNodeContext {
		public TerminalNode STRING() { return getToken(JsonParser.STRING, 0); }
		public PrimitiveStringContext(PrimitiveNodeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitPrimitiveString(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveIntegerContext extends PrimitiveNodeContext {
		public TerminalNode INTEGER() { return getToken(JsonParser.INTEGER, 0); }
		public PrimitiveIntegerContext(PrimitiveNodeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JsonParserVisitor ) return ((JsonParserVisitor<? extends T>)visitor).visitPrimitiveInteger(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveNodeContext primitiveNode() throws RecognitionException {
		PrimitiveNodeContext _localctx = new PrimitiveNodeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_primitiveNode);
		try {
			setState(57);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
				_localctx = new PrimitiveTrueContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(50);
				match(TRUE);
				}
				break;
			case FALSE:
				_localctx = new PrimitiveFalseContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(51);
				match(FALSE);
				}
				break;
			case STRING:
				_localctx = new PrimitiveStringContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(52);
				match(STRING);
				}
				break;
			case INTEGER:
				_localctx = new PrimitiveIntegerContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(53);
				match(INTEGER);
				}
				break;
			case FLOAT:
				_localctx = new PrimitiveFloatContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(54);
				match(FLOAT);
				}
				break;
			case DOUBLE:
				_localctx = new PrimitiveDoubleContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(55);
				match(DOUBLE);
				}
				break;
			case NULL:
				_localctx = new PrimitiveNullContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(56);
				match(NULL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u000e<\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0003\u0001\u0013\b\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u0002\u0019\b\u0002\n\u0002\f\u0002\u001c\t\u0002"+
		"\u0003\u0002\u001e\b\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0005\u0004*\b\u0004\n\u0004\f\u0004-\t\u0004\u0003\u0004/\b\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0003\u0005:\b\u0005\u0001\u0005\u0000"+
		"\u0000\u0006\u0000\u0002\u0004\u0006\b\n\u0000\u0000A\u0000\f\u0001\u0000"+
		"\u0000\u0000\u0002\u0012\u0001\u0000\u0000\u0000\u0004\u0014\u0001\u0000"+
		"\u0000\u0000\u0006!\u0001\u0000\u0000\u0000\b%\u0001\u0000\u0000\u0000"+
		"\n9\u0001\u0000\u0000\u0000\f\r\u0003\u0002\u0001\u0000\r\u000e\u0005"+
		"\u0000\u0000\u0001\u000e\u0001\u0001\u0000\u0000\u0000\u000f\u0013\u0003"+
		"\n\u0005\u0000\u0010\u0013\u0003\u0004\u0002\u0000\u0011\u0013\u0003\b"+
		"\u0004\u0000\u0012\u000f\u0001\u0000\u0000\u0000\u0012\u0010\u0001\u0000"+
		"\u0000\u0000\u0012\u0011\u0001\u0000\u0000\u0000\u0013\u0003\u0001\u0000"+
		"\u0000\u0000\u0014\u001d\u0005\u0006\u0000\u0000\u0015\u001a\u0003\u0006"+
		"\u0003\u0000\u0016\u0017\u0005\b\u0000\u0000\u0017\u0019\u0003\u0006\u0003"+
		"\u0000\u0018\u0016\u0001\u0000\u0000\u0000\u0019\u001c\u0001\u0000\u0000"+
		"\u0000\u001a\u0018\u0001\u0000\u0000\u0000\u001a\u001b\u0001\u0000\u0000"+
		"\u0000\u001b\u001e\u0001\u0000\u0000\u0000\u001c\u001a\u0001\u0000\u0000"+
		"\u0000\u001d\u0015\u0001\u0000\u0000\u0000\u001d\u001e\u0001\u0000\u0000"+
		"\u0000\u001e\u001f\u0001\u0000\u0000\u0000\u001f \u0005\u0007\u0000\u0000"+
		" \u0005\u0001\u0000\u0000\u0000!\"\u0005\n\u0000\u0000\"#\u0005\t\u0000"+
		"\u0000#$\u0003\u0002\u0001\u0000$\u0007\u0001\u0000\u0000\u0000%.\u0005"+
		"\u0004\u0000\u0000&+\u0003\u0002\u0001\u0000\'(\u0005\b\u0000\u0000(*"+
		"\u0003\u0002\u0001\u0000)\'\u0001\u0000\u0000\u0000*-\u0001\u0000\u0000"+
		"\u0000+)\u0001\u0000\u0000\u0000+,\u0001\u0000\u0000\u0000,/\u0001\u0000"+
		"\u0000\u0000-+\u0001\u0000\u0000\u0000.&\u0001\u0000\u0000\u0000./\u0001"+
		"\u0000\u0000\u0000/0\u0001\u0000\u0000\u000001\u0005\u0005\u0000\u0000"+
		"1\t\u0001\u0000\u0000\u00002:\u0005\u0001\u0000\u00003:\u0005\u0002\u0000"+
		"\u00004:\u0005\n\u0000\u00005:\u0005\u000b\u0000\u00006:\u0005\f\u0000"+
		"\u00007:\u0005\r\u0000\u00008:\u0005\u0003\u0000\u000092\u0001\u0000\u0000"+
		"\u000093\u0001\u0000\u0000\u000094\u0001\u0000\u0000\u000095\u0001\u0000"+
		"\u0000\u000096\u0001\u0000\u0000\u000097\u0001\u0000\u0000\u000098\u0001"+
		"\u0000\u0000\u0000:\u000b\u0001\u0000\u0000\u0000\u0006\u0012\u001a\u001d"+
		"+.9";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}