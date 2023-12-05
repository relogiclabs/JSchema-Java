package com.relogiclabs.json.schema.internal.antlr;

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
public class SchemaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TITLE=1, VERSION=2, INCLUDE=3, PRAGMA=4, DEFINE=5, SCHEMA=6, TRUE=7, FALSE=8,
		NULL=9, COLON=10, COMMA=11, STAR=12, LBRACE=13, RBRACE=14, LBRACKET=15,
		RBRACKET=16, LPAREN=17, RPAREN=18, OPTIONAL=19, UNDEFINED=20, IDENTIFIER=21,
		ALIAS=22, DATATYPE=23, FUNCTION=24, RECEIVER=25, STRING=26, INTEGER=27,
		FLOAT=28, DOUBLE=29, MULTILINE_COMMENT=30, LINE_COMMENT=31, WHITE_SPACE=32,
		COLON1=33, VERSION_NUMBER1=34, WHITE_SPACE1=35, MULTILINE_COMMENT1=36,
		LINE_COMMENT1=37;
	public static final int
		RULE_schema = 0, RULE_schemaBase = 1, RULE_title = 2, RULE_version = 3,
		RULE_include = 4, RULE_pragma = 5, RULE_define = 6, RULE_alias = 7, RULE_validatorMain = 8,
		RULE_validator = 9, RULE_value = 10, RULE_receiver = 11, RULE_object = 12,
		RULE_property = 13, RULE_array = 14, RULE_datatype = 15, RULE_function = 16,
		RULE_argument = 17, RULE_primitive = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"schema", "schemaBase", "title", "version", "include", "pragma", "define",
			"alias", "validatorMain", "validator", "value", "receiver", "object",
			"property", "array", "datatype", "function", "argument", "primitive"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'%title'", "'%version'", "'%include'", "'%pragma'", "'%define'",
			"'%schema'", "'true'", "'false'", "'null'", null, "','", "'*'", "'{'",
			"'}'", "'['", "']'", "'('", "')'", "'?'", "'!'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TITLE", "VERSION", "INCLUDE", "PRAGMA", "DEFINE", "SCHEMA", "TRUE",
			"FALSE", "NULL", "COLON", "COMMA", "STAR", "LBRACE", "RBRACE", "LBRACKET",
			"RBRACKET", "LPAREN", "RPAREN", "OPTIONAL", "UNDEFINED", "IDENTIFIER",
			"ALIAS", "DATATYPE", "FUNCTION", "RECEIVER", "STRING", "INTEGER", "FLOAT",
			"DOUBLE", "MULTILINE_COMMENT", "LINE_COMMENT", "WHITE_SPACE", "COLON1",
			"VERSION_NUMBER1", "WHITE_SPACE1", "MULTILINE_COMMENT1", "LINE_COMMENT1"
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
	public String getGrammarFileName() { return "SchemaParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SchemaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SchemaContext extends ParserRuleContext {
		public SchemaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schema; }

		public SchemaContext() { }
		public void copyFrom(SchemaContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CoreSchemaContext extends SchemaContext {
		public ValidatorContext validator() {
			return getRuleContext(ValidatorContext.class,0);
		}
		public TerminalNode EOF() { return getToken(SchemaParser.EOF, 0); }
		public CoreSchemaContext(SchemaContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitCoreSchema(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AggregateSchemaContext extends SchemaContext {
		public SchemaBaseContext schemaBase() {
			return getRuleContext(SchemaBaseContext.class,0);
		}
		public TerminalNode EOF() { return getToken(SchemaParser.EOF, 0); }
		public TitleContext title() {
			return getRuleContext(TitleContext.class,0);
		}
		public VersionContext version() {
			return getRuleContext(VersionContext.class,0);
		}
		public List<IncludeContext> include() {
			return getRuleContexts(IncludeContext.class);
		}
		public IncludeContext include(int i) {
			return getRuleContext(IncludeContext.class,i);
		}
		public List<PragmaContext> pragma() {
			return getRuleContexts(PragmaContext.class);
		}
		public PragmaContext pragma(int i) {
			return getRuleContext(PragmaContext.class,i);
		}
		public List<DefineContext> define() {
			return getRuleContexts(DefineContext.class);
		}
		public DefineContext define(int i) {
			return getRuleContext(DefineContext.class,i);
		}
		public AggregateSchemaContext(SchemaContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitAggregateSchema(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaContext schema() throws RecognitionException {
		SchemaContext _localctx = new SchemaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_schema);
		int _la;
		try {
			setState(74);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TITLE:
			case VERSION:
			case INCLUDE:
			case PRAGMA:
			case DEFINE:
			case SCHEMA:
				_localctx = new AggregateSchemaContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(39);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==TITLE) {
					{
					setState(38);
					title();
					}
				}

				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VERSION) {
					{
					setState(41);
					version();
					}
				}

				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==INCLUDE) {
					{
					{
					setState(44);
					include();
					}
					}
					setState(49);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PRAGMA) {
					{
					{
					setState(50);
					pragma();
					}
					}
					setState(55);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(59);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==DEFINE) {
					{
					{
					setState(56);
					define();
					}
					}
					setState(61);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(62);
				schemaBase();
				setState(66);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==DEFINE) {
					{
					{
					setState(63);
					define();
					}
					}
					setState(68);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(69);
				match(EOF);
				}
				break;
			case TRUE:
			case FALSE:
			case NULL:
			case LBRACE:
			case LBRACKET:
			case UNDEFINED:
			case ALIAS:
			case DATATYPE:
			case FUNCTION:
			case STRING:
			case INTEGER:
			case FLOAT:
			case DOUBLE:
				_localctx = new CoreSchemaContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				validator();
				setState(72);
				match(EOF);
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
	public static class SchemaBaseContext extends ParserRuleContext {
		public TerminalNode SCHEMA() { return getToken(SchemaParser.SCHEMA, 0); }
		public TerminalNode COLON() { return getToken(SchemaParser.COLON, 0); }
		public ValidatorContext validator() {
			return getRuleContext(ValidatorContext.class,0);
		}
		public SchemaBaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaBase; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitSchemaBase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaBaseContext schemaBase() throws RecognitionException {
		SchemaBaseContext _localctx = new SchemaBaseContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_schemaBase);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			match(SCHEMA);
			setState(77);
			match(COLON);
			setState(78);
			validator();
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
	public static class TitleContext extends ParserRuleContext {
		public TerminalNode TITLE() { return getToken(SchemaParser.TITLE, 0); }
		public TerminalNode COLON() { return getToken(SchemaParser.COLON, 0); }
		public TerminalNode STRING() { return getToken(SchemaParser.STRING, 0); }
		public TitleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_title; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitTitle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TitleContext title() throws RecognitionException {
		TitleContext _localctx = new TitleContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_title);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			match(TITLE);
			setState(81);
			match(COLON);
			setState(82);
			match(STRING);
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
	public static class VersionContext extends ParserRuleContext {
		public TerminalNode VERSION() { return getToken(SchemaParser.VERSION, 0); }
		public TerminalNode COLON1() { return getToken(SchemaParser.COLON1, 0); }
		public TerminalNode VERSION_NUMBER1() { return getToken(SchemaParser.VERSION_NUMBER1, 0); }
		public VersionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_version; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitVersion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VersionContext version() throws RecognitionException {
		VersionContext _localctx = new VersionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_version);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(VERSION);
			setState(85);
			match(COLON1);
			setState(86);
			match(VERSION_NUMBER1);
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
	public static class IncludeContext extends ParserRuleContext {
		public TerminalNode INCLUDE() { return getToken(SchemaParser.INCLUDE, 0); }
		public TerminalNode COLON() { return getToken(SchemaParser.COLON, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(SchemaParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(SchemaParser.IDENTIFIER, i);
		}
		public TerminalNode COMMA() { return getToken(SchemaParser.COMMA, 0); }
		public IncludeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_include; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitInclude(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IncludeContext include() throws RecognitionException {
		IncludeContext _localctx = new IncludeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_include);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(INCLUDE);
			setState(89);
			match(COLON);
			setState(90);
			match(IDENTIFIER);
			setState(93);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(91);
				match(COMMA);
				setState(92);
				match(IDENTIFIER);
				}
			}

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
	public static class PragmaContext extends ParserRuleContext {
		public TerminalNode PRAGMA() { return getToken(SchemaParser.PRAGMA, 0); }
		public TerminalNode IDENTIFIER() { return getToken(SchemaParser.IDENTIFIER, 0); }
		public TerminalNode COLON() { return getToken(SchemaParser.COLON, 0); }
		public PrimitiveContext primitive() {
			return getRuleContext(PrimitiveContext.class,0);
		}
		public PragmaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pragma; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitPragma(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PragmaContext pragma() throws RecognitionException {
		PragmaContext _localctx = new PragmaContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_pragma);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(PRAGMA);
			setState(96);
			match(IDENTIFIER);
			setState(97);
			match(COLON);
			setState(98);
			primitive();
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
	public static class DefineContext extends ParserRuleContext {
		public TerminalNode DEFINE() { return getToken(SchemaParser.DEFINE, 0); }
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public TerminalNode COLON() { return getToken(SchemaParser.COLON, 0); }
		public ValidatorMainContext validatorMain() {
			return getRuleContext(ValidatorMainContext.class,0);
		}
		public DefineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_define; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitDefine(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefineContext define() throws RecognitionException {
		DefineContext _localctx = new DefineContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_define);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(DEFINE);
			setState(101);
			alias();
			setState(102);
			match(COLON);
			setState(103);
			validatorMain();
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
	public static class AliasContext extends ParserRuleContext {
		public TerminalNode ALIAS() { return getToken(SchemaParser.ALIAS, 0); }
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			match(ALIAS);
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
	public static class ValidatorMainContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public List<FunctionContext> function() {
			return getRuleContexts(FunctionContext.class);
		}
		public FunctionContext function(int i) {
			return getRuleContext(FunctionContext.class,i);
		}
		public List<DatatypeContext> datatype() {
			return getRuleContexts(DatatypeContext.class);
		}
		public DatatypeContext datatype(int i) {
			return getRuleContext(DatatypeContext.class,i);
		}
		public List<ReceiverContext> receiver() {
			return getRuleContexts(ReceiverContext.class);
		}
		public ReceiverContext receiver(int i) {
			return getRuleContext(ReceiverContext.class,i);
		}
		public TerminalNode OPTIONAL() { return getToken(SchemaParser.OPTIONAL, 0); }
		public ValidatorMainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_validatorMain; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitValidatorMain(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidatorMainContext validatorMain() throws RecognitionException {
		ValidatorMainContext _localctx = new ValidatorMainContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_validatorMain);
		int _la;
		try {
			setState(163);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case NULL:
			case LBRACE:
			case LBRACKET:
			case UNDEFINED:
			case STRING:
			case INTEGER:
			case FLOAT:
			case DOUBLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(107);
				value();
				setState(111);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==FUNCTION) {
					{
					{
					setState(108);
					function();
					}
					}
					setState(113);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(117);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==DATATYPE) {
					{
					{
					setState(114);
					datatype();
					}
					}
					setState(119);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(123);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==RECEIVER) {
					{
					{
					setState(120);
					receiver();
					}
					}
					setState(125);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(127);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OPTIONAL) {
					{
					setState(126);
					match(OPTIONAL);
					}
				}

				}
				break;
			case FUNCTION:
				enterOuterAlt(_localctx, 2);
				{
				setState(130);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(129);
					function();
					}
					}
					setState(132);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==FUNCTION );
				setState(137);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==DATATYPE) {
					{
					{
					setState(134);
					datatype();
					}
					}
					setState(139);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(143);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==RECEIVER) {
					{
					{
					setState(140);
					receiver();
					}
					}
					setState(145);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(147);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OPTIONAL) {
					{
					setState(146);
					match(OPTIONAL);
					}
				}

				}
				break;
			case DATATYPE:
				enterOuterAlt(_localctx, 3);
				{
				setState(150);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(149);
					datatype();
					}
					}
					setState(152);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==DATATYPE );
				setState(157);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==RECEIVER) {
					{
					{
					setState(154);
					receiver();
					}
					}
					setState(159);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(161);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==OPTIONAL) {
					{
					setState(160);
					match(OPTIONAL);
					}
				}

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
	public static class ValidatorContext extends ParserRuleContext {
		public ValidatorMainContext validatorMain() {
			return getRuleContext(ValidatorMainContext.class,0);
		}
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public ValidatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_validator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitValidator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValidatorContext validator() throws RecognitionException {
		ValidatorContext _localctx = new ValidatorContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_validator);
		try {
			setState(167);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case NULL:
			case LBRACE:
			case LBRACKET:
			case UNDEFINED:
			case DATATYPE:
			case FUNCTION:
			case STRING:
			case INTEGER:
			case FLOAT:
			case DOUBLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(165);
				validatorMain();
				}
				break;
			case ALIAS:
				enterOuterAlt(_localctx, 2);
				{
				setState(166);
				alias();
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
	public static class ValueContext extends ParserRuleContext {
		public PrimitiveContext primitive() {
			return getRuleContext(PrimitiveContext.class,0);
		}
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_value);
		try {
			setState(172);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case NULL:
			case UNDEFINED:
			case STRING:
			case INTEGER:
			case FLOAT:
			case DOUBLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(169);
				primitive();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(170);
				object();
				}
				break;
			case LBRACKET:
				enterOuterAlt(_localctx, 3);
				{
				setState(171);
				array();
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
	public static class ReceiverContext extends ParserRuleContext {
		public TerminalNode RECEIVER() { return getToken(SchemaParser.RECEIVER, 0); }
		public ReceiverContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_receiver; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitReceiver(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReceiverContext receiver() throws RecognitionException {
		ReceiverContext _localctx = new ReceiverContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_receiver);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			match(RECEIVER);
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
	public static class ObjectContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(SchemaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(SchemaParser.RBRACE, 0); }
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SchemaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SchemaParser.COMMA, i);
		}
		public ObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_object; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitObject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectContext object() throws RecognitionException {
		ObjectContext _localctx = new ObjectContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_object);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			match(LBRACE);
			setState(185);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING) {
				{
				setState(177);
				property();
				setState(182);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(178);
					match(COMMA);
					setState(179);
					property();
					}
					}
					setState(184);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(187);
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
	public static class PropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(SchemaParser.STRING, 0); }
		public TerminalNode COLON() { return getToken(SchemaParser.COLON, 0); }
		public ValidatorContext validator() {
			return getRuleContext(ValidatorContext.class,0);
		}
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_property);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			match(STRING);
			setState(190);
			match(COLON);
			setState(191);
			validator();
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
	public static class ArrayContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(SchemaParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(SchemaParser.RBRACKET, 0); }
		public List<ValidatorContext> validator() {
			return getRuleContexts(ValidatorContext.class);
		}
		public ValidatorContext validator(int i) {
			return getRuleContext(ValidatorContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SchemaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SchemaParser.COMMA, i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			match(LBRACKET);
			setState(202);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1037083520L) != 0)) {
				{
				setState(194);
				validator();
				setState(199);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(195);
					match(COMMA);
					setState(196);
					validator();
					}
					}
					setState(201);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(204);
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
	public static class DatatypeContext extends ParserRuleContext {
		public TerminalNode DATATYPE() { return getToken(SchemaParser.DATATYPE, 0); }
		public TerminalNode STAR() { return getToken(SchemaParser.STAR, 0); }
		public TerminalNode LPAREN() { return getToken(SchemaParser.LPAREN, 0); }
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(SchemaParser.RPAREN, 0); }
		public DatatypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_datatype; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitDatatype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatatypeContext datatype() throws RecognitionException {
		DatatypeContext _localctx = new DatatypeContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_datatype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			match(DATATYPE);
			setState(208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(207);
				match(STAR);
				}
			}

			setState(214);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(210);
				match(LPAREN);
				setState(211);
				alias();
				setState(212);
				match(RPAREN);
				}
			}

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
	public static class FunctionContext extends ParserRuleContext {
		public TerminalNode FUNCTION() { return getToken(SchemaParser.FUNCTION, 0); }
		public TerminalNode STAR() { return getToken(SchemaParser.STAR, 0); }
		public TerminalNode LPAREN() { return getToken(SchemaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(SchemaParser.RPAREN, 0); }
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SchemaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SchemaParser.COMMA, i);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			match(FUNCTION);
			setState(218);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STAR) {
				{
				setState(217);
				match(STAR);
				}
			}

			setState(232);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(220);
				match(LPAREN);
				setState(229);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1041277824L) != 0)) {
					{
					setState(221);
					argument();
					setState(226);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(222);
						match(COMMA);
						setState(223);
						argument();
						}
						}
						setState(228);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(231);
				match(RPAREN);
				}
			}

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
	public static class ArgumentContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public ReceiverContext receiver() {
			return getRuleContext(ReceiverContext.class,0);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_argument);
		try {
			setState(236);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case NULL:
			case LBRACE:
			case LBRACKET:
			case UNDEFINED:
			case STRING:
			case INTEGER:
			case FLOAT:
			case DOUBLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(234);
				value();
				}
				break;
			case RECEIVER:
				enterOuterAlt(_localctx, 2);
				{
				setState(235);
				receiver();
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
	public static class PrimitiveContext extends ParserRuleContext {
		public PrimitiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitive; }

		public PrimitiveContext() { }
		public void copyFrom(PrimitiveContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveDoubleContext extends PrimitiveContext {
		public TerminalNode DOUBLE() { return getToken(SchemaParser.DOUBLE, 0); }
		public PrimitiveDoubleContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitPrimitiveDouble(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveFloatContext extends PrimitiveContext {
		public TerminalNode FLOAT() { return getToken(SchemaParser.FLOAT, 0); }
		public PrimitiveFloatContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitPrimitiveFloat(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveNullContext extends PrimitiveContext {
		public TerminalNode NULL() { return getToken(SchemaParser.NULL, 0); }
		public PrimitiveNullContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitPrimitiveNull(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveUndefinedContext extends PrimitiveContext {
		public TerminalNode UNDEFINED() { return getToken(SchemaParser.UNDEFINED, 0); }
		public PrimitiveUndefinedContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitPrimitiveUndefined(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveTrueContext extends PrimitiveContext {
		public TerminalNode TRUE() { return getToken(SchemaParser.TRUE, 0); }
		public PrimitiveTrueContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitPrimitiveTrue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveFalseContext extends PrimitiveContext {
		public TerminalNode FALSE() { return getToken(SchemaParser.FALSE, 0); }
		public PrimitiveFalseContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitPrimitiveFalse(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveStringContext extends PrimitiveContext {
		public TerminalNode STRING() { return getToken(SchemaParser.STRING, 0); }
		public PrimitiveStringContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitPrimitiveString(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveIntegerContext extends PrimitiveContext {
		public TerminalNode INTEGER() { return getToken(SchemaParser.INTEGER, 0); }
		public PrimitiveIntegerContext(PrimitiveContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SchemaParserVisitor ) return ((SchemaParserVisitor<? extends T>)visitor).visitPrimitiveInteger(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveContext primitive() throws RecognitionException {
		PrimitiveContext _localctx = new PrimitiveContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_primitive);
		try {
			setState(246);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
				_localctx = new PrimitiveTrueContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(238);
				match(TRUE);
				}
				break;
			case FALSE:
				_localctx = new PrimitiveFalseContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(239);
				match(FALSE);
				}
				break;
			case STRING:
				_localctx = new PrimitiveStringContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(240);
				match(STRING);
				}
				break;
			case INTEGER:
				_localctx = new PrimitiveIntegerContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(241);
				match(INTEGER);
				}
				break;
			case FLOAT:
				_localctx = new PrimitiveFloatContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(242);
				match(FLOAT);
				}
				break;
			case DOUBLE:
				_localctx = new PrimitiveDoubleContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(243);
				match(DOUBLE);
				}
				break;
			case NULL:
				_localctx = new PrimitiveNullContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(244);
				match(NULL);
				}
				break;
			case UNDEFINED:
				_localctx = new PrimitiveUndefinedContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(245);
				match(UNDEFINED);
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
		"\u0004\u0001%\u00f9\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0001\u0000\u0003\u0000(\b\u0000\u0001\u0000\u0003\u0000+\b\u0000\u0001"+
		"\u0000\u0005\u0000.\b\u0000\n\u0000\f\u00001\t\u0000\u0001\u0000\u0005"+
		"\u00004\b\u0000\n\u0000\f\u00007\t\u0000\u0001\u0000\u0005\u0000:\b\u0000"+
		"\n\u0000\f\u0000=\t\u0000\u0001\u0000\u0001\u0000\u0005\u0000A\b\u0000"+
		"\n\u0000\f\u0000D\t\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0003\u0000K\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0003\u0004^\b\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0005"+
		"\bn\b\b\n\b\f\bq\t\b\u0001\b\u0005\bt\b\b\n\b\f\bw\t\b\u0001\b\u0005\b"+
		"z\b\b\n\b\f\b}\t\b\u0001\b\u0003\b\u0080\b\b\u0001\b\u0004\b\u0083\b\b"+
		"\u000b\b\f\b\u0084\u0001\b\u0005\b\u0088\b\b\n\b\f\b\u008b\t\b\u0001\b"+
		"\u0005\b\u008e\b\b\n\b\f\b\u0091\t\b\u0001\b\u0003\b\u0094\b\b\u0001\b"+
		"\u0004\b\u0097\b\b\u000b\b\f\b\u0098\u0001\b\u0005\b\u009c\b\b\n\b\f\b"+
		"\u009f\t\b\u0001\b\u0003\b\u00a2\b\b\u0003\b\u00a4\b\b\u0001\t\u0001\t"+
		"\u0003\t\u00a8\b\t\u0001\n\u0001\n\u0001\n\u0003\n\u00ad\b\n\u0001\u000b"+
		"\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u00b5\b\f\n\f\f\f"+
		"\u00b8\t\f\u0003\f\u00ba\b\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000e\u00c6\b"+
		"\u000e\n\u000e\f\u000e\u00c9\t\u000e\u0003\u000e\u00cb\b\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000f\u0001\u000f\u0003\u000f\u00d1\b\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u00d7\b\u000f\u0001\u0010"+
		"\u0001\u0010\u0003\u0010\u00db\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0005\u0010\u00e1\b\u0010\n\u0010\f\u0010\u00e4\t\u0010\u0003"+
		"\u0010\u00e6\b\u0010\u0001\u0010\u0003\u0010\u00e9\b\u0010\u0001\u0011"+
		"\u0001\u0011\u0003\u0011\u00ed\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012"+
		"\u00f7\b\u0012\u0001\u0012\u0000\u0000\u0013\u0000\u0002\u0004\u0006\b"+
		"\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$\u0000\u0000"+
		"\u010f\u0000J\u0001\u0000\u0000\u0000\u0002L\u0001\u0000\u0000\u0000\u0004"+
		"P\u0001\u0000\u0000\u0000\u0006T\u0001\u0000\u0000\u0000\bX\u0001\u0000"+
		"\u0000\u0000\n_\u0001\u0000\u0000\u0000\fd\u0001\u0000\u0000\u0000\u000e"+
		"i\u0001\u0000\u0000\u0000\u0010\u00a3\u0001\u0000\u0000\u0000\u0012\u00a7"+
		"\u0001\u0000\u0000\u0000\u0014\u00ac\u0001\u0000\u0000\u0000\u0016\u00ae"+
		"\u0001\u0000\u0000\u0000\u0018\u00b0\u0001\u0000\u0000\u0000\u001a\u00bd"+
		"\u0001\u0000\u0000\u0000\u001c\u00c1\u0001\u0000\u0000\u0000\u001e\u00ce"+
		"\u0001\u0000\u0000\u0000 \u00d8\u0001\u0000\u0000\u0000\"\u00ec\u0001"+
		"\u0000\u0000\u0000$\u00f6\u0001\u0000\u0000\u0000&(\u0003\u0004\u0002"+
		"\u0000\'&\u0001\u0000\u0000\u0000\'(\u0001\u0000\u0000\u0000(*\u0001\u0000"+
		"\u0000\u0000)+\u0003\u0006\u0003\u0000*)\u0001\u0000\u0000\u0000*+\u0001"+
		"\u0000\u0000\u0000+/\u0001\u0000\u0000\u0000,.\u0003\b\u0004\u0000-,\u0001"+
		"\u0000\u0000\u0000.1\u0001\u0000\u0000\u0000/-\u0001\u0000\u0000\u0000"+
		"/0\u0001\u0000\u0000\u000005\u0001\u0000\u0000\u00001/\u0001\u0000\u0000"+
		"\u000024\u0003\n\u0005\u000032\u0001\u0000\u0000\u000047\u0001\u0000\u0000"+
		"\u000053\u0001\u0000\u0000\u000056\u0001\u0000\u0000\u00006;\u0001\u0000"+
		"\u0000\u000075\u0001\u0000\u0000\u00008:\u0003\f\u0006\u000098\u0001\u0000"+
		"\u0000\u0000:=\u0001\u0000\u0000\u0000;9\u0001\u0000\u0000\u0000;<\u0001"+
		"\u0000\u0000\u0000<>\u0001\u0000\u0000\u0000=;\u0001\u0000\u0000\u0000"+
		">B\u0003\u0002\u0001\u0000?A\u0003\f\u0006\u0000@?\u0001\u0000\u0000\u0000"+
		"AD\u0001\u0000\u0000\u0000B@\u0001\u0000\u0000\u0000BC\u0001\u0000\u0000"+
		"\u0000CE\u0001\u0000\u0000\u0000DB\u0001\u0000\u0000\u0000EF\u0005\u0000"+
		"\u0000\u0001FK\u0001\u0000\u0000\u0000GH\u0003\u0012\t\u0000HI\u0005\u0000"+
		"\u0000\u0001IK\u0001\u0000\u0000\u0000J\'\u0001\u0000\u0000\u0000JG\u0001"+
		"\u0000\u0000\u0000K\u0001\u0001\u0000\u0000\u0000LM\u0005\u0006\u0000"+
		"\u0000MN\u0005\n\u0000\u0000NO\u0003\u0012\t\u0000O\u0003\u0001\u0000"+
		"\u0000\u0000PQ\u0005\u0001\u0000\u0000QR\u0005\n\u0000\u0000RS\u0005\u001a"+
		"\u0000\u0000S\u0005\u0001\u0000\u0000\u0000TU\u0005\u0002\u0000\u0000"+
		"UV\u0005!\u0000\u0000VW\u0005\"\u0000\u0000W\u0007\u0001\u0000\u0000\u0000"+
		"XY\u0005\u0003\u0000\u0000YZ\u0005\n\u0000\u0000Z]\u0005\u0015\u0000\u0000"+
		"[\\\u0005\u000b\u0000\u0000\\^\u0005\u0015\u0000\u0000][\u0001\u0000\u0000"+
		"\u0000]^\u0001\u0000\u0000\u0000^\t\u0001\u0000\u0000\u0000_`\u0005\u0004"+
		"\u0000\u0000`a\u0005\u0015\u0000\u0000ab\u0005\n\u0000\u0000bc\u0003$"+
		"\u0012\u0000c\u000b\u0001\u0000\u0000\u0000de\u0005\u0005\u0000\u0000"+
		"ef\u0003\u000e\u0007\u0000fg\u0005\n\u0000\u0000gh\u0003\u0010\b\u0000"+
		"h\r\u0001\u0000\u0000\u0000ij\u0005\u0016\u0000\u0000j\u000f\u0001\u0000"+
		"\u0000\u0000ko\u0003\u0014\n\u0000ln\u0003 \u0010\u0000ml\u0001\u0000"+
		"\u0000\u0000nq\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000op\u0001"+
		"\u0000\u0000\u0000pu\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000"+
		"rt\u0003\u001e\u000f\u0000sr\u0001\u0000\u0000\u0000tw\u0001\u0000\u0000"+
		"\u0000us\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000v{\u0001\u0000"+
		"\u0000\u0000wu\u0001\u0000\u0000\u0000xz\u0003\u0016\u000b\u0000yx\u0001"+
		"\u0000\u0000\u0000z}\u0001\u0000\u0000\u0000{y\u0001\u0000\u0000\u0000"+
		"{|\u0001\u0000\u0000\u0000|\u007f\u0001\u0000\u0000\u0000}{\u0001\u0000"+
		"\u0000\u0000~\u0080\u0005\u0013\u0000\u0000\u007f~\u0001\u0000\u0000\u0000"+
		"\u007f\u0080\u0001\u0000\u0000\u0000\u0080\u00a4\u0001\u0000\u0000\u0000"+
		"\u0081\u0083\u0003 \u0010\u0000\u0082\u0081\u0001\u0000\u0000\u0000\u0083"+
		"\u0084\u0001\u0000\u0000\u0000\u0084\u0082\u0001\u0000\u0000\u0000\u0084"+
		"\u0085\u0001\u0000\u0000\u0000\u0085\u0089\u0001\u0000\u0000\u0000\u0086"+
		"\u0088\u0003\u001e\u000f\u0000\u0087\u0086\u0001\u0000\u0000\u0000\u0088"+
		"\u008b\u0001\u0000\u0000\u0000\u0089\u0087\u0001\u0000\u0000\u0000\u0089"+
		"\u008a\u0001\u0000\u0000\u0000\u008a\u008f\u0001\u0000\u0000\u0000\u008b"+
		"\u0089\u0001\u0000\u0000\u0000\u008c\u008e\u0003\u0016\u000b\u0000\u008d"+
		"\u008c\u0001\u0000\u0000\u0000\u008e\u0091\u0001\u0000\u0000\u0000\u008f"+
		"\u008d\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000\u0000\u0000\u0090"+
		"\u0093\u0001\u0000\u0000\u0000\u0091\u008f\u0001\u0000\u0000\u0000\u0092"+
		"\u0094\u0005\u0013\u0000\u0000\u0093\u0092\u0001\u0000\u0000\u0000\u0093"+
		"\u0094\u0001\u0000\u0000\u0000\u0094\u00a4\u0001\u0000\u0000\u0000\u0095"+
		"\u0097\u0003\u001e\u000f\u0000\u0096\u0095\u0001\u0000\u0000\u0000\u0097"+
		"\u0098\u0001\u0000\u0000\u0000\u0098\u0096\u0001\u0000\u0000\u0000\u0098"+
		"\u0099\u0001\u0000\u0000\u0000\u0099\u009d\u0001\u0000\u0000\u0000\u009a"+
		"\u009c\u0003\u0016\u000b\u0000\u009b\u009a\u0001\u0000\u0000\u0000\u009c"+
		"\u009f\u0001\u0000\u0000\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009d"+
		"\u009e\u0001\u0000\u0000\u0000\u009e\u00a1\u0001\u0000\u0000\u0000\u009f"+
		"\u009d\u0001\u0000\u0000\u0000\u00a0\u00a2\u0005\u0013\u0000\u0000\u00a1"+
		"\u00a0\u0001\u0000\u0000\u0000\u00a1\u00a2\u0001\u0000\u0000\u0000\u00a2"+
		"\u00a4\u0001\u0000\u0000\u0000\u00a3k\u0001\u0000\u0000\u0000\u00a3\u0082"+
		"\u0001\u0000\u0000\u0000\u00a3\u0096\u0001\u0000\u0000\u0000\u00a4\u0011"+
		"\u0001\u0000\u0000\u0000\u00a5\u00a8\u0003\u0010\b\u0000\u00a6\u00a8\u0003"+
		"\u000e\u0007\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a7\u00a6\u0001"+
		"\u0000\u0000\u0000\u00a8\u0013\u0001\u0000\u0000\u0000\u00a9\u00ad\u0003"+
		"$\u0012\u0000\u00aa\u00ad\u0003\u0018\f\u0000\u00ab\u00ad\u0003\u001c"+
		"\u000e\u0000\u00ac\u00a9\u0001\u0000\u0000\u0000\u00ac\u00aa\u0001\u0000"+
		"\u0000\u0000\u00ac\u00ab\u0001\u0000\u0000\u0000\u00ad\u0015\u0001\u0000"+
		"\u0000\u0000\u00ae\u00af\u0005\u0019\u0000\u0000\u00af\u0017\u0001\u0000"+
		"\u0000\u0000\u00b0\u00b9\u0005\r\u0000\u0000\u00b1\u00b6\u0003\u001a\r"+
		"\u0000\u00b2\u00b3\u0005\u000b\u0000\u0000\u00b3\u00b5\u0003\u001a\r\u0000"+
		"\u00b4\u00b2\u0001\u0000\u0000\u0000\u00b5\u00b8\u0001\u0000\u0000\u0000"+
		"\u00b6\u00b4\u0001\u0000\u0000\u0000\u00b6\u00b7\u0001\u0000\u0000\u0000"+
		"\u00b7\u00ba\u0001\u0000\u0000\u0000\u00b8\u00b6\u0001\u0000\u0000\u0000"+
		"\u00b9\u00b1\u0001\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000\u0000"+
		"\u00ba\u00bb\u0001\u0000\u0000\u0000\u00bb\u00bc\u0005\u000e\u0000\u0000"+
		"\u00bc\u0019\u0001\u0000\u0000\u0000\u00bd\u00be\u0005\u001a\u0000\u0000"+
		"\u00be\u00bf\u0005\n\u0000\u0000\u00bf\u00c0\u0003\u0012\t\u0000\u00c0"+
		"\u001b\u0001\u0000\u0000\u0000\u00c1\u00ca\u0005\u000f\u0000\u0000\u00c2"+
		"\u00c7\u0003\u0012\t\u0000\u00c3\u00c4\u0005\u000b\u0000\u0000\u00c4\u00c6"+
		"\u0003\u0012\t\u0000\u00c5\u00c3\u0001\u0000\u0000\u0000\u00c6\u00c9\u0001"+
		"\u0000\u0000\u0000\u00c7\u00c5\u0001\u0000\u0000\u0000\u00c7\u00c8\u0001"+
		"\u0000\u0000\u0000\u00c8\u00cb\u0001\u0000\u0000\u0000\u00c9\u00c7\u0001"+
		"\u0000\u0000\u0000\u00ca\u00c2\u0001\u0000\u0000\u0000\u00ca\u00cb\u0001"+
		"\u0000\u0000\u0000\u00cb\u00cc\u0001\u0000\u0000\u0000\u00cc\u00cd\u0005"+
		"\u0010\u0000\u0000\u00cd\u001d\u0001\u0000\u0000\u0000\u00ce\u00d0\u0005"+
		"\u0017\u0000\u0000\u00cf\u00d1\u0005\f\u0000\u0000\u00d0\u00cf\u0001\u0000"+
		"\u0000\u0000\u00d0\u00d1\u0001\u0000\u0000\u0000\u00d1\u00d6\u0001\u0000"+
		"\u0000\u0000\u00d2\u00d3\u0005\u0011\u0000\u0000\u00d3\u00d4\u0003\u000e"+
		"\u0007\u0000\u00d4\u00d5\u0005\u0012\u0000\u0000\u00d5\u00d7\u0001\u0000"+
		"\u0000\u0000\u00d6\u00d2\u0001\u0000\u0000\u0000\u00d6\u00d7\u0001\u0000"+
		"\u0000\u0000\u00d7\u001f\u0001\u0000\u0000\u0000\u00d8\u00da\u0005\u0018"+
		"\u0000\u0000\u00d9\u00db\u0005\f\u0000\u0000\u00da\u00d9\u0001\u0000\u0000"+
		"\u0000\u00da\u00db\u0001\u0000\u0000\u0000\u00db\u00e8\u0001\u0000\u0000"+
		"\u0000\u00dc\u00e5\u0005\u0011\u0000\u0000\u00dd\u00e2\u0003\"\u0011\u0000"+
		"\u00de\u00df\u0005\u000b\u0000\u0000\u00df\u00e1\u0003\"\u0011\u0000\u00e0"+
		"\u00de\u0001\u0000\u0000\u0000\u00e1\u00e4\u0001\u0000\u0000\u0000\u00e2"+
		"\u00e0\u0001\u0000\u0000\u0000\u00e2\u00e3\u0001\u0000\u0000\u0000\u00e3"+
		"\u00e6\u0001\u0000\u0000\u0000\u00e4\u00e2\u0001\u0000\u0000\u0000\u00e5"+
		"\u00dd\u0001\u0000\u0000\u0000\u00e5\u00e6\u0001\u0000\u0000\u0000\u00e6"+
		"\u00e7\u0001\u0000\u0000\u0000\u00e7\u00e9\u0005\u0012\u0000\u0000\u00e8"+
		"\u00dc\u0001\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000\u00e9"+
		"!\u0001\u0000\u0000\u0000\u00ea\u00ed\u0003\u0014\n\u0000\u00eb\u00ed"+
		"\u0003\u0016\u000b\u0000\u00ec\u00ea\u0001\u0000\u0000\u0000\u00ec\u00eb"+
		"\u0001\u0000\u0000\u0000\u00ed#\u0001\u0000\u0000\u0000\u00ee\u00f7\u0005"+
		"\u0007\u0000\u0000\u00ef\u00f7\u0005\b\u0000\u0000\u00f0\u00f7\u0005\u001a"+
		"\u0000\u0000\u00f1\u00f7\u0005\u001b\u0000\u0000\u00f2\u00f7\u0005\u001c"+
		"\u0000\u0000\u00f3\u00f7\u0005\u001d\u0000\u0000\u00f4\u00f7\u0005\t\u0000"+
		"\u0000\u00f5\u00f7\u0005\u0014\u0000\u0000\u00f6\u00ee\u0001\u0000\u0000"+
		"\u0000\u00f6\u00ef\u0001\u0000\u0000\u0000\u00f6\u00f0\u0001\u0000\u0000"+
		"\u0000\u00f6\u00f1\u0001\u0000\u0000\u0000\u00f6\u00f2\u0001\u0000\u0000"+
		"\u0000\u00f6\u00f3\u0001\u0000\u0000\u0000\u00f6\u00f4\u0001\u0000\u0000"+
		"\u0000\u00f6\u00f5\u0001\u0000\u0000\u0000\u00f7%\u0001\u0000\u0000\u0000"+
		"\"\'*/5;BJ]ou{\u007f\u0084\u0089\u008f\u0093\u0098\u009d\u00a1\u00a3\u00a7"+
		"\u00ac\u00b6\u00b9\u00c7\u00ca\u00d0\u00d6\u00da\u00e2\u00e5\u00e8\u00ec"+
		"\u00f6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}