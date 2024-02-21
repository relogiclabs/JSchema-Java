package com.relogiclabs.jschema.internal.util;

import com.relogiclabs.jschema.exception.CommonException;
import com.relogiclabs.jschema.exception.DateTimeLexerException;
import com.relogiclabs.jschema.exception.JsonLexerException;
import com.relogiclabs.jschema.exception.SchemaLexerException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import static com.relogiclabs.jschema.message.ErrorCode.DLEX01;
import static com.relogiclabs.jschema.message.ErrorCode.JLEX01;
import static com.relogiclabs.jschema.message.ErrorCode.SLEX01;

public abstract class LexerErrorListener extends BaseErrorListener {

    public static final LexerErrorListener SCHEMA = new SchemaErrorListener();
    public static final LexerErrorListener JSON = new JsonErrorListener();
    public static final LexerErrorListener DATE_TIME = new DateTimeErrorListener();

    protected abstract CommonException failOnSyntaxError(String message, Throwable cause);
    protected abstract String getMessageFormat();

    private static final class SchemaErrorListener extends LexerErrorListener {

        @Override
        protected CommonException failOnSyntaxError(String message, Throwable cause) {
            return new SchemaLexerException(SLEX01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Schema (Line %d:%d) [" + SLEX01 + "]: %s (Line: %s)";
        }
    }

    private static final class JsonErrorListener extends LexerErrorListener {

        @Override
        protected CommonException failOnSyntaxError(String message, Throwable cause) {
            return new JsonLexerException(JLEX01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Json (Line %d:%d) [" + JLEX01 + "]: %s (Line: %s)";
        }
    }

    private static final class DateTimeErrorListener extends LexerErrorListener {

        @Override
        protected CommonException failOnSyntaxError(String message, Throwable cause) {
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
        if(this == DATE_TIME) {
            throw failOnSyntaxError(getMessageFormat().formatted(msg, lexer.getText()), e);
        } else {
            var errorLine = new StringBuilder(recognizer.getInputStream().toString()
                    .split("\\r?\\n")[line - 1]).insert(charPositionInLine, "<|>").toString().trim();
            throw failOnSyntaxError(getMessageFormat().formatted(line, charPositionInLine,
                    msg, errorLine), e);
        }
    }
}