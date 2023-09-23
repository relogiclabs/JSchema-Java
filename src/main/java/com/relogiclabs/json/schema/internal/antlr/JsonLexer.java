package com.relogiclabs.json.schema.internal.antlr;

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

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class JsonLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.0", RuntimeMetaData.VERSION); }

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
			"COMMA", "COLON", "STRING", "ESCAPE", "UNICODE", "HEXDIGIT", "SAFECODEPOINT",
			"INTEGER", "FLOAT", "DOUBLE", "INTDIGIT", "EXPONENT", "DIGIT", "WHITE_SPACE"
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
		"\u0004\u0000\u000e\u0093\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0005"+
		"\tK\b\t\n\t\f\tN\t\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0003\nU"+
		"\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0003\u000eb\b\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0004\u000f"+
		"i\b\u000f\u000b\u000f\f\u000fj\u0001\u0010\u0001\u0010\u0001\u0010\u0004"+
		"\u0010p\b\u0010\u000b\u0010\f\u0010q\u0003\u0010t\b\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011{\b\u0011"+
		"\n\u0011\f\u0011~\t\u0011\u0003\u0011\u0080\b\u0011\u0001\u0012\u0001"+
		"\u0012\u0003\u0012\u0084\b\u0012\u0001\u0012\u0004\u0012\u0087\b\u0012"+
		"\u000b\u0012\f\u0012\u0088\u0001\u0013\u0001\u0013\u0001\u0014\u0004\u0014"+
		"\u008e\b\u0014\u000b\u0014\f\u0014\u008f\u0001\u0014\u0001\u0014\u0000"+
		"\u0000\u0015\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b"+
		"\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u0000\u0017\u0000\u0019\u0000"+
		"\u001b\u0000\u001d\u000b\u001f\f!\r#\u0000%\u0000\'\u0000)\u000e\u0001"+
		"\u0000\b\b\u0000\"\"//\\\\bbffnnrrtt\u0003\u000009AFaf\u0003\u0000\u0000"+
		"\u001f\"\"\\\\\u0001\u000019\u0002\u0000EEee\u0002\u0000++--\u0001\u0000"+
		"09\u0003\u0000\t\n\r\r  \u0097\u0000\u0001\u0001\u0000\u0000\u0000\u0000"+
		"\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000"+
		"\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b"+
		"\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001"+
		"\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001"+
		"\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001"+
		"\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000"+
		"\u0000\u0001+\u0001\u0000\u0000\u0000\u00030\u0001\u0000\u0000\u0000\u0005"+
		"6\u0001\u0000\u0000\u0000\u0007;\u0001\u0000\u0000\u0000\t=\u0001\u0000"+
		"\u0000\u0000\u000b?\u0001\u0000\u0000\u0000\rA\u0001\u0000\u0000\u0000"+
		"\u000fC\u0001\u0000\u0000\u0000\u0011E\u0001\u0000\u0000\u0000\u0013G"+
		"\u0001\u0000\u0000\u0000\u0015Q\u0001\u0000\u0000\u0000\u0017V\u0001\u0000"+
		"\u0000\u0000\u0019\\\u0001\u0000\u0000\u0000\u001b^\u0001\u0000\u0000"+
		"\u0000\u001da\u0001\u0000\u0000\u0000\u001fe\u0001\u0000\u0000\u0000!"+
		"l\u0001\u0000\u0000\u0000#\u007f\u0001\u0000\u0000\u0000%\u0081\u0001"+
		"\u0000\u0000\u0000\'\u008a\u0001\u0000\u0000\u0000)\u008d\u0001\u0000"+
		"\u0000\u0000+,\u0005t\u0000\u0000,-\u0005r\u0000\u0000-.\u0005u\u0000"+
		"\u0000./\u0005e\u0000\u0000/\u0002\u0001\u0000\u0000\u000001\u0005f\u0000"+
		"\u000012\u0005a\u0000\u000023\u0005l\u0000\u000034\u0005s\u0000\u0000"+
		"45\u0005e\u0000\u00005\u0004\u0001\u0000\u0000\u000067\u0005n\u0000\u0000"+
		"78\u0005u\u0000\u000089\u0005l\u0000\u00009:\u0005l\u0000\u0000:\u0006"+
		"\u0001\u0000\u0000\u0000;<\u0005[\u0000\u0000<\b\u0001\u0000\u0000\u0000"+
		"=>\u0005]\u0000\u0000>\n\u0001\u0000\u0000\u0000?@\u0005{\u0000\u0000"+
		"@\f\u0001\u0000\u0000\u0000AB\u0005}\u0000\u0000B\u000e\u0001\u0000\u0000"+
		"\u0000CD\u0005,\u0000\u0000D\u0010\u0001\u0000\u0000\u0000EF\u0005:\u0000"+
		"\u0000F\u0012\u0001\u0000\u0000\u0000GL\u0005\"\u0000\u0000HK\u0003\u0015"+
		"\n\u0000IK\u0003\u001b\r\u0000JH\u0001\u0000\u0000\u0000JI\u0001\u0000"+
		"\u0000\u0000KN\u0001\u0000\u0000\u0000LJ\u0001\u0000\u0000\u0000LM\u0001"+
		"\u0000\u0000\u0000MO\u0001\u0000\u0000\u0000NL\u0001\u0000\u0000\u0000"+
		"OP\u0005\"\u0000\u0000P\u0014\u0001\u0000\u0000\u0000QT\u0005\\\u0000"+
		"\u0000RU\u0007\u0000\u0000\u0000SU\u0003\u0017\u000b\u0000TR\u0001\u0000"+
		"\u0000\u0000TS\u0001\u0000\u0000\u0000U\u0016\u0001\u0000\u0000\u0000"+
		"VW\u0005u\u0000\u0000WX\u0003\u0019\f\u0000XY\u0003\u0019\f\u0000YZ\u0003"+
		"\u0019\f\u0000Z[\u0003\u0019\f\u0000[\u0018\u0001\u0000\u0000\u0000\\"+
		"]\u0007\u0001\u0000\u0000]\u001a\u0001\u0000\u0000\u0000^_\b\u0002\u0000"+
		"\u0000_\u001c\u0001\u0000\u0000\u0000`b\u0005-\u0000\u0000a`\u0001\u0000"+
		"\u0000\u0000ab\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000cd\u0003"+
		"#\u0011\u0000d\u001e\u0001\u0000\u0000\u0000ef\u0003\u001d\u000e\u0000"+
		"fh\u0005.\u0000\u0000gi\u0003\'\u0013\u0000hg\u0001\u0000\u0000\u0000"+
		"ij\u0001\u0000\u0000\u0000jh\u0001\u0000\u0000\u0000jk\u0001\u0000\u0000"+
		"\u0000k \u0001\u0000\u0000\u0000ls\u0003\u001d\u000e\u0000mo\u0005.\u0000"+
		"\u0000np\u0003\'\u0013\u0000on\u0001\u0000\u0000\u0000pq\u0001\u0000\u0000"+
		"\u0000qo\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000\u0000rt\u0001\u0000"+
		"\u0000\u0000sm\u0001\u0000\u0000\u0000st\u0001\u0000\u0000\u0000tu\u0001"+
		"\u0000\u0000\u0000uv\u0003%\u0012\u0000v\"\u0001\u0000\u0000\u0000w\u0080"+
		"\u00050\u0000\u0000x|\u0007\u0003\u0000\u0000y{\u0003\'\u0013\u0000zy"+
		"\u0001\u0000\u0000\u0000{~\u0001\u0000\u0000\u0000|z\u0001\u0000\u0000"+
		"\u0000|}\u0001\u0000\u0000\u0000}\u0080\u0001\u0000\u0000\u0000~|\u0001"+
		"\u0000\u0000\u0000\u007fw\u0001\u0000\u0000\u0000\u007fx\u0001\u0000\u0000"+
		"\u0000\u0080$\u0001\u0000\u0000\u0000\u0081\u0083\u0007\u0004\u0000\u0000"+
		"\u0082\u0084\u0007\u0005\u0000\u0000\u0083\u0082\u0001\u0000\u0000\u0000"+
		"\u0083\u0084\u0001\u0000\u0000\u0000\u0084\u0086\u0001\u0000\u0000\u0000"+
		"\u0085\u0087\u0003\'\u0013\u0000\u0086\u0085\u0001\u0000\u0000\u0000\u0087"+
		"\u0088\u0001\u0000\u0000\u0000\u0088\u0086\u0001\u0000\u0000\u0000\u0088"+
		"\u0089\u0001\u0000\u0000\u0000\u0089&\u0001\u0000\u0000\u0000\u008a\u008b"+
		"\u0007\u0006\u0000\u0000\u008b(\u0001\u0000\u0000\u0000\u008c\u008e\u0007"+
		"\u0007\u0000\u0000\u008d\u008c\u0001\u0000\u0000\u0000\u008e\u008f\u0001"+
		"\u0000\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000\u008f\u0090\u0001"+
		"\u0000\u0000\u0000\u0090\u0091\u0001\u0000\u0000\u0000\u0091\u0092\u0006"+
		"\u0014\u0000\u0000\u0092*\u0001\u0000\u0000\u0000\r\u0000JLTajqs|\u007f"+
		"\u0083\u0088\u008f\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}