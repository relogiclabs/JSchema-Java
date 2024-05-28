package com.relogiclabs.jschema.internal.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class JsonLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TRUE=1, FALSE=2, NULL=3, LBRACKET=4, RBRACKET=5, LBRACE=6, RBRACE=7, COMMA=8,
		COLON=9, STRING=10, INTEGER=11, FLOAT=12, DOUBLE=13, WHITE_SPACE=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"TRUE", "FALSE", "NULL", "LBRACKET", "RBRACKET", "LBRACE", "RBRACE",
			"COMMA", "COLON", "STRING", "ESCAPE", "UNICODE", "HEXDIGIT", "SAFE_CODEPOINT",
			"INTEGER", "FLOAT", "DOUBLE", "FRACTION", "INTDIGIT", "EXPONENT", "DIGIT",
			"WHITE_SPACE"
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


	public JsonLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "JsonLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u000e\u0092\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\t\u0005\tM\b\t\n\t\f\tP\t\t\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\n\u0003\nW\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0003"+
		"\u000ed\b\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u0010\u0001\u0010\u0003\u0010m\b\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0011\u0001\u0011\u0004\u0011s\b\u0011\u000b\u0011\f\u0011"+
		"t\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012z\b\u0012\n\u0012\f\u0012"+
		"}\t\u0012\u0003\u0012\u007f\b\u0012\u0001\u0013\u0001\u0013\u0003\u0013"+
		"\u0083\b\u0013\u0001\u0013\u0004\u0013\u0086\b\u0013\u000b\u0013\f\u0013"+
		"\u0087\u0001\u0014\u0001\u0014\u0001\u0015\u0004\u0015\u008d\b\u0015\u000b"+
		"\u0015\f\u0015\u008e\u0001\u0015\u0001\u0015\u0000\u0000\u0016\u0001\u0001"+
		"\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f"+
		"\b\u0011\t\u0013\n\u0015\u0000\u0017\u0000\u0019\u0000\u001b\u0000\u001d"+
		"\u000b\u001f\f!\r#\u0000%\u0000\'\u0000)\u0000+\u000e\u0001\u0000\b\b"+
		"\u0000\"\"//\\\\bbffnnrrtt\u0003\u000009AFaf\u0003\u0000\u0000\u001f\""+
		"\"\\\\\u0001\u000019\u0002\u0000EEee\u0002\u0000++--\u0001\u000009\u0003"+
		"\u0000\t\n\r\r  \u0094\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003"+
		"\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007"+
		"\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001"+
		"\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000"+
		"\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000"+
		"\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000"+
		"\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000"+
		"\u0001-\u0001\u0000\u0000\u0000\u00032\u0001\u0000\u0000\u0000\u00058"+
		"\u0001\u0000\u0000\u0000\u0007=\u0001\u0000\u0000\u0000\t?\u0001\u0000"+
		"\u0000\u0000\u000bA\u0001\u0000\u0000\u0000\rC\u0001\u0000\u0000\u0000"+
		"\u000fE\u0001\u0000\u0000\u0000\u0011G\u0001\u0000\u0000\u0000\u0013I"+
		"\u0001\u0000\u0000\u0000\u0015S\u0001\u0000\u0000\u0000\u0017X\u0001\u0000"+
		"\u0000\u0000\u0019^\u0001\u0000\u0000\u0000\u001b`\u0001\u0000\u0000\u0000"+
		"\u001dc\u0001\u0000\u0000\u0000\u001fg\u0001\u0000\u0000\u0000!j\u0001"+
		"\u0000\u0000\u0000#p\u0001\u0000\u0000\u0000%~\u0001\u0000\u0000\u0000"+
		"\'\u0080\u0001\u0000\u0000\u0000)\u0089\u0001\u0000\u0000\u0000+\u008c"+
		"\u0001\u0000\u0000\u0000-.\u0005t\u0000\u0000./\u0005r\u0000\u0000/0\u0005"+
		"u\u0000\u000001\u0005e\u0000\u00001\u0002\u0001\u0000\u0000\u000023\u0005"+
		"f\u0000\u000034\u0005a\u0000\u000045\u0005l\u0000\u000056\u0005s\u0000"+
		"\u000067\u0005e\u0000\u00007\u0004\u0001\u0000\u0000\u000089\u0005n\u0000"+
		"\u00009:\u0005u\u0000\u0000:;\u0005l\u0000\u0000;<\u0005l\u0000\u0000"+
		"<\u0006\u0001\u0000\u0000\u0000=>\u0005[\u0000\u0000>\b\u0001\u0000\u0000"+
		"\u0000?@\u0005]\u0000\u0000@\n\u0001\u0000\u0000\u0000AB\u0005{\u0000"+
		"\u0000B\f\u0001\u0000\u0000\u0000CD\u0005}\u0000\u0000D\u000e\u0001\u0000"+
		"\u0000\u0000EF\u0005,\u0000\u0000F\u0010\u0001\u0000\u0000\u0000GH\u0005"+
		":\u0000\u0000H\u0012\u0001\u0000\u0000\u0000IN\u0005\"\u0000\u0000JM\u0003"+
		"\u0015\n\u0000KM\u0003\u001b\r\u0000LJ\u0001\u0000\u0000\u0000LK\u0001"+
		"\u0000\u0000\u0000MP\u0001\u0000\u0000\u0000NL\u0001\u0000\u0000\u0000"+
		"NO\u0001\u0000\u0000\u0000OQ\u0001\u0000\u0000\u0000PN\u0001\u0000\u0000"+
		"\u0000QR\u0005\"\u0000\u0000R\u0014\u0001\u0000\u0000\u0000SV\u0005\\"+
		"\u0000\u0000TW\u0007\u0000\u0000\u0000UW\u0003\u0017\u000b\u0000VT\u0001"+
		"\u0000\u0000\u0000VU\u0001\u0000\u0000\u0000W\u0016\u0001\u0000\u0000"+
		"\u0000XY\u0005u\u0000\u0000YZ\u0003\u0019\f\u0000Z[\u0003\u0019\f\u0000"+
		"[\\\u0003\u0019\f\u0000\\]\u0003\u0019\f\u0000]\u0018\u0001\u0000\u0000"+
		"\u0000^_\u0007\u0001\u0000\u0000_\u001a\u0001\u0000\u0000\u0000`a\b\u0002"+
		"\u0000\u0000a\u001c\u0001\u0000\u0000\u0000bd\u0005-\u0000\u0000cb\u0001"+
		"\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000de\u0001\u0000\u0000\u0000"+
		"ef\u0003%\u0012\u0000f\u001e\u0001\u0000\u0000\u0000gh\u0003\u001d\u000e"+
		"\u0000hi\u0003#\u0011\u0000i \u0001\u0000\u0000\u0000jl\u0003\u001d\u000e"+
		"\u0000km\u0003#\u0011\u0000lk\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000"+
		"\u0000mn\u0001\u0000\u0000\u0000no\u0003\'\u0013\u0000o\"\u0001\u0000"+
		"\u0000\u0000pr\u0005.\u0000\u0000qs\u0003)\u0014\u0000rq\u0001\u0000\u0000"+
		"\u0000st\u0001\u0000\u0000\u0000tr\u0001\u0000\u0000\u0000tu\u0001\u0000"+
		"\u0000\u0000u$\u0001\u0000\u0000\u0000v\u007f\u00050\u0000\u0000w{\u0007"+
		"\u0003\u0000\u0000xz\u0003)\u0014\u0000yx\u0001\u0000\u0000\u0000z}\u0001"+
		"\u0000\u0000\u0000{y\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000\u0000"+
		"|\u007f\u0001\u0000\u0000\u0000}{\u0001\u0000\u0000\u0000~v\u0001\u0000"+
		"\u0000\u0000~w\u0001\u0000\u0000\u0000\u007f&\u0001\u0000\u0000\u0000"+
		"\u0080\u0082\u0007\u0004\u0000\u0000\u0081\u0083\u0007\u0005\u0000\u0000"+
		"\u0082\u0081\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000\u0000"+
		"\u0083\u0085\u0001\u0000\u0000\u0000\u0084\u0086\u0003)\u0014\u0000\u0085"+
		"\u0084\u0001\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087"+
		"\u0085\u0001\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000\u0000\u0088"+
		"(\u0001\u0000\u0000\u0000\u0089\u008a\u0007\u0006\u0000\u0000\u008a*\u0001"+
		"\u0000\u0000\u0000\u008b\u008d\u0007\u0007\u0000\u0000\u008c\u008b\u0001"+
		"\u0000\u0000\u0000\u008d\u008e\u0001\u0000\u0000\u0000\u008e\u008c\u0001"+
		"\u0000\u0000\u0000\u008e\u008f\u0001\u0000\u0000\u0000\u008f\u0090\u0001"+
		"\u0000\u0000\u0000\u0090\u0091\u0006\u0015\u0000\u0000\u0091,\u0001\u0000"+
		"\u0000\u0000\f\u0000LNVclt{~\u0082\u0087\u008e\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}