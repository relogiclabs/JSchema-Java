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
public class DateTimeLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ERA=1, YEAR_NUM4=2, YEAR_NUM2=3, MONTH_NAME=4, MONTH_SHORT_NAME=5, MONTH_NUM2=6,
		MONTH_NUM=7, WEEKDAY_NAME=8, WEEKDAY_SHORT_NAME=9, DAY_NUM2=10, DAY_NUM=11,
		AM_PM=12, HOUR_NUM2=13, HOUR_NUM=14, MINUTE_NUM2=15, MINUTE_NUM=16, SECOND_NUM2=17,
		SECOND_NUM=18, FRACTION_NUM06=19, FRACTION_NUM05=20, FRACTION_NUM04=21,
		FRACTION_NUM03=22, FRACTION_NUM02=23, FRACTION_NUM01=24, FRACTION_NUM=25,
		UTC_OFFSET_TIME2=26, UTC_OFFSET_TIME1=27, UTC_OFFSET_HOUR=28, SYMBOL=29,
		WHITESPACE=30, TEXT=31;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"ERA", "YEAR_NUM4", "YEAR_NUM2", "MONTH_NAME", "MONTH_SHORT_NAME", "MONTH_NUM2",
			"MONTH_NUM", "WEEKDAY_NAME", "WEEKDAY_SHORT_NAME", "DAY_NUM2", "DAY_NUM",
			"AM_PM", "HOUR_NUM2", "HOUR_NUM", "MINUTE_NUM2", "MINUTE_NUM", "SECOND_NUM2",
			"SECOND_NUM", "FRACTION_NUM06", "FRACTION_NUM05", "FRACTION_NUM04", "FRACTION_NUM03",
			"FRACTION_NUM02", "FRACTION_NUM01", "FRACTION_NUM", "UTC_OFFSET_TIME2",
			"UTC_OFFSET_TIME1", "UTC_OFFSET_HOUR", "SYMBOL", "WHITESPACE", "TEXT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'G'", "'YYYY'", "'YY'", "'MMMM'", "'MMM'", "'MM'", "'M'", "'DDDD'",
			"'DDD'", "'DD'", "'D'", "'t'", "'hh'", "'h'", "'mm'", "'m'", "'ss'",
			"'s'", "'ffffff'", "'fffff'", "'ffff'", "'fff'", "'ff'", "'f'", "'F'",
			"'ZZZ'", "'ZZ'", "'Z'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ERA", "YEAR_NUM4", "YEAR_NUM2", "MONTH_NAME", "MONTH_SHORT_NAME",
			"MONTH_NUM2", "MONTH_NUM", "WEEKDAY_NAME", "WEEKDAY_SHORT_NAME", "DAY_NUM2",
			"DAY_NUM", "AM_PM", "HOUR_NUM2", "HOUR_NUM", "MINUTE_NUM2", "MINUTE_NUM",
			"SECOND_NUM2", "SECOND_NUM", "FRACTION_NUM06", "FRACTION_NUM05", "FRACTION_NUM04",
			"FRACTION_NUM03", "FRACTION_NUM02", "FRACTION_NUM01", "FRACTION_NUM",
			"UTC_OFFSET_TIME2", "UTC_OFFSET_TIME1", "UTC_OFFSET_HOUR", "SYMBOL",
			"WHITESPACE", "TEXT"
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


	public DateTimeLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DateTimeLexer.g4"; }

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
		"\u0004\u0000\u001f\u00b1\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"+
		"\u0002\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d"+
		"\u0002\u001e\u0007\u001e\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017"+
		"\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b"+
		"\u0001\u001c\u0004\u001c\u009e\b\u001c\u000b\u001c\f\u001c\u009f\u0001"+
		"\u001d\u0004\u001d\u00a3\b\u001d\u000b\u001d\f\u001d\u00a4\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0005\u001e\u00ab\b\u001e\n\u001e"+
		"\f\u001e\u00ae\t\u001e\u0001\u001e\u0001\u001e\u0000\u0000\u001f\u0001"+
		"\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007"+
		"\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d"+
		"\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017/"+
		"\u00181\u00193\u001a5\u001b7\u001c9\u001d;\u001e=\u001f\u0001\u0000\u0003"+
		"\u0004\u0000!/:@[`{~\u0003\u0000\t\n\r\r  \u0001\u0000\'\'\u00b4\u0000"+
		"\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000"+
		"\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000"+
		"\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r"+
		"\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011"+
		"\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015"+
		"\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019"+
		"\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d"+
		"\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001"+
		"\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000"+
		"\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000"+
		"\u0000+\u0001\u0000\u0000\u0000\u0000-\u0001\u0000\u0000\u0000\u0000/"+
		"\u0001\u0000\u0000\u0000\u00001\u0001\u0000\u0000\u0000\u00003\u0001\u0000"+
		"\u0000\u0000\u00005\u0001\u0000\u0000\u0000\u00007\u0001\u0000\u0000\u0000"+
		"\u00009\u0001\u0000\u0000\u0000\u0000;\u0001\u0000\u0000\u0000\u0000="+
		"\u0001\u0000\u0000\u0000\u0001?\u0001\u0000\u0000\u0000\u0003A\u0001\u0000"+
		"\u0000\u0000\u0005F\u0001\u0000\u0000\u0000\u0007I\u0001\u0000\u0000\u0000"+
		"\tN\u0001\u0000\u0000\u0000\u000bR\u0001\u0000\u0000\u0000\rU\u0001\u0000"+
		"\u0000\u0000\u000fW\u0001\u0000\u0000\u0000\u0011\\\u0001\u0000\u0000"+
		"\u0000\u0013`\u0001\u0000\u0000\u0000\u0015c\u0001\u0000\u0000\u0000\u0017"+
		"e\u0001\u0000\u0000\u0000\u0019g\u0001\u0000\u0000\u0000\u001bj\u0001"+
		"\u0000\u0000\u0000\u001dl\u0001\u0000\u0000\u0000\u001fo\u0001\u0000\u0000"+
		"\u0000!q\u0001\u0000\u0000\u0000#t\u0001\u0000\u0000\u0000%v\u0001\u0000"+
		"\u0000\u0000\'}\u0001\u0000\u0000\u0000)\u0083\u0001\u0000\u0000\u0000"+
		"+\u0088\u0001\u0000\u0000\u0000-\u008c\u0001\u0000\u0000\u0000/\u008f"+
		"\u0001\u0000\u0000\u00001\u0091\u0001\u0000\u0000\u00003\u0093\u0001\u0000"+
		"\u0000\u00005\u0097\u0001\u0000\u0000\u00007\u009a\u0001\u0000\u0000\u0000"+
		"9\u009d\u0001\u0000\u0000\u0000;\u00a2\u0001\u0000\u0000\u0000=\u00a6"+
		"\u0001\u0000\u0000\u0000?@\u0005G\u0000\u0000@\u0002\u0001\u0000\u0000"+
		"\u0000AB\u0005Y\u0000\u0000BC\u0005Y\u0000\u0000CD\u0005Y\u0000\u0000"+
		"DE\u0005Y\u0000\u0000E\u0004\u0001\u0000\u0000\u0000FG\u0005Y\u0000\u0000"+
		"GH\u0005Y\u0000\u0000H\u0006\u0001\u0000\u0000\u0000IJ\u0005M\u0000\u0000"+
		"JK\u0005M\u0000\u0000KL\u0005M\u0000\u0000LM\u0005M\u0000\u0000M\b\u0001"+
		"\u0000\u0000\u0000NO\u0005M\u0000\u0000OP\u0005M\u0000\u0000PQ\u0005M"+
		"\u0000\u0000Q\n\u0001\u0000\u0000\u0000RS\u0005M\u0000\u0000ST\u0005M"+
		"\u0000\u0000T\f\u0001\u0000\u0000\u0000UV\u0005M\u0000\u0000V\u000e\u0001"+
		"\u0000\u0000\u0000WX\u0005D\u0000\u0000XY\u0005D\u0000\u0000YZ\u0005D"+
		"\u0000\u0000Z[\u0005D\u0000\u0000[\u0010\u0001\u0000\u0000\u0000\\]\u0005"+
		"D\u0000\u0000]^\u0005D\u0000\u0000^_\u0005D\u0000\u0000_\u0012\u0001\u0000"+
		"\u0000\u0000`a\u0005D\u0000\u0000ab\u0005D\u0000\u0000b\u0014\u0001\u0000"+
		"\u0000\u0000cd\u0005D\u0000\u0000d\u0016\u0001\u0000\u0000\u0000ef\u0005"+
		"t\u0000\u0000f\u0018\u0001\u0000\u0000\u0000gh\u0005h\u0000\u0000hi\u0005"+
		"h\u0000\u0000i\u001a\u0001\u0000\u0000\u0000jk\u0005h\u0000\u0000k\u001c"+
		"\u0001\u0000\u0000\u0000lm\u0005m\u0000\u0000mn\u0005m\u0000\u0000n\u001e"+
		"\u0001\u0000\u0000\u0000op\u0005m\u0000\u0000p \u0001\u0000\u0000\u0000"+
		"qr\u0005s\u0000\u0000rs\u0005s\u0000\u0000s\"\u0001\u0000\u0000\u0000"+
		"tu\u0005s\u0000\u0000u$\u0001\u0000\u0000\u0000vw\u0005f\u0000\u0000w"+
		"x\u0005f\u0000\u0000xy\u0005f\u0000\u0000yz\u0005f\u0000\u0000z{\u0005"+
		"f\u0000\u0000{|\u0005f\u0000\u0000|&\u0001\u0000\u0000\u0000}~\u0005f"+
		"\u0000\u0000~\u007f\u0005f\u0000\u0000\u007f\u0080\u0005f\u0000\u0000"+
		"\u0080\u0081\u0005f\u0000\u0000\u0081\u0082\u0005f\u0000\u0000\u0082("+
		"\u0001\u0000\u0000\u0000\u0083\u0084\u0005f\u0000\u0000\u0084\u0085\u0005"+
		"f\u0000\u0000\u0085\u0086\u0005f\u0000\u0000\u0086\u0087\u0005f\u0000"+
		"\u0000\u0087*\u0001\u0000\u0000\u0000\u0088\u0089\u0005f\u0000\u0000\u0089"+
		"\u008a\u0005f\u0000\u0000\u008a\u008b\u0005f\u0000\u0000\u008b,\u0001"+
		"\u0000\u0000\u0000\u008c\u008d\u0005f\u0000\u0000\u008d\u008e\u0005f\u0000"+
		"\u0000\u008e.\u0001\u0000\u0000\u0000\u008f\u0090\u0005f\u0000\u0000\u0090"+
		"0\u0001\u0000\u0000\u0000\u0091\u0092\u0005F\u0000\u0000\u00922\u0001"+
		"\u0000\u0000\u0000\u0093\u0094\u0005Z\u0000\u0000\u0094\u0095\u0005Z\u0000"+
		"\u0000\u0095\u0096\u0005Z\u0000\u0000\u00964\u0001\u0000\u0000\u0000\u0097"+
		"\u0098\u0005Z\u0000\u0000\u0098\u0099\u0005Z\u0000\u0000\u00996\u0001"+
		"\u0000\u0000\u0000\u009a\u009b\u0005Z\u0000\u0000\u009b8\u0001\u0000\u0000"+
		"\u0000\u009c\u009e\u0007\u0000\u0000\u0000\u009d\u009c\u0001\u0000\u0000"+
		"\u0000\u009e\u009f\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000\u0000"+
		"\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0:\u0001\u0000\u0000\u0000"+
		"\u00a1\u00a3\u0007\u0001\u0000\u0000\u00a2\u00a1\u0001\u0000\u0000\u0000"+
		"\u00a3\u00a4\u0001\u0000\u0000\u0000\u00a4\u00a2\u0001\u0000\u0000\u0000"+
		"\u00a4\u00a5\u0001\u0000\u0000\u0000\u00a5<\u0001\u0000\u0000\u0000\u00a6"+
		"\u00ac\u0005\'\u0000\u0000\u00a7\u00ab\b\u0002\u0000\u0000\u00a8\u00a9"+
		"\u0005\'\u0000\u0000\u00a9\u00ab\u0005\'\u0000\u0000\u00aa\u00a7\u0001"+
		"\u0000\u0000\u0000\u00aa\u00a8\u0001\u0000\u0000\u0000\u00ab\u00ae\u0001"+
		"\u0000\u0000\u0000\u00ac\u00aa\u0001\u0000\u0000\u0000\u00ac\u00ad\u0001"+
		"\u0000\u0000\u0000\u00ad\u00af\u0001\u0000\u0000\u0000\u00ae\u00ac\u0001"+
		"\u0000\u0000\u0000\u00af\u00b0\u0005\'\u0000\u0000\u00b0>\u0001\u0000"+
		"\u0000\u0000\u0005\u0000\u009f\u00a4\u00aa\u00ac\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}