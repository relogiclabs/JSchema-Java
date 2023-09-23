package com.relogiclabs.json.schema.internal.util;

import com.relogiclabs.json.schema.exception.CommonException;
import com.relogiclabs.json.schema.exception.DateTimeLexerException;
import com.relogiclabs.json.schema.exception.JsonLexerException;
import com.relogiclabs.json.schema.exception.SchemaLexerException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import static com.relogiclabs.json.schema.message.ErrorCode.DLEX01;
import static com.relogiclabs.json.schema.message.ErrorCode.JLEX01;
import static com.relogiclabs.json.schema.message.ErrorCode.SLEX01;

public abstract class LexerErrorListener extends BaseErrorListener {

    public static final LexerErrorListener SCHEMA = new SchemaErrorListener();
    public static final LexerErrorListener JSON = new JsonErrorListener();
    public static final LexerErrorListener DATE_TIME = new DateTimeErrorListener();

    protected abstract CommonException createException(String message, Throwable cause);
    protected abstract String getMessageFormat();

    private static class SchemaErrorListener extends LexerErrorListener {

        @Override
        protected CommonException createException(String message, Throwable cause) {
            return new SchemaLexerException(SLEX01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Schema (Line %d:%d) [" + SLEX01 + "]: %s (error on '%s')";
        }
    }

    private static class JsonErrorListener extends LexerErrorListener {

        @Override
        protected CommonException createException(String message, Throwable cause) {
            return new JsonLexerException(JLEX01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Json (Line %d:%d) [" + JLEX01 + "]: %s (error on '%s')";
        }
    }

    private static class DateTimeErrorListener extends LexerErrorListener {

        @Override
        protected CommonException createException(String message, Throwable cause) {
            return new DateTimeLexerException(DLEX01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Invalid date-time pattern (%s, error on '%s')";
        }
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {
        var lexer = (Lexer) recognizer;
        var message = this == DATE_TIME ?
                getMessageFormat().formatted(msg, lexer.getText()) :
                getMessageFormat().formatted(line, charPositionInLine, msg, lexer.getText());
        throw createException(message, e);
    }
}