package com.relogiclabs.jschema.internal.util;

import com.relogiclabs.jschema.exception.DateTimeLexerException;
import com.relogiclabs.jschema.exception.JsonLexerException;
import com.relogiclabs.jschema.exception.SchemaLexerException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import static com.relogiclabs.jschema.message.ErrorCode.JSNLEX01;
import static com.relogiclabs.jschema.message.ErrorCode.LEXRDT01;
import static com.relogiclabs.jschema.message.ErrorCode.SCMLEX01;
import static com.relogiclabs.jschema.message.MessageFormatter.ERROR_POINTER;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public abstract class LexerErrorListener extends BaseErrorListener {
    public static final LexerErrorListener SCHEMA = new SchemaErrorListener();
    public static final LexerErrorListener JSON = new JsonErrorListener();
    public static final LexerErrorListener DATE_TIME = new DateTimeErrorListener();

    private static final String NOT_FOUND = "<NOT_FOUND>";

    protected abstract RuntimeException failOnSyntaxError(String message, Throwable cause);
    protected abstract String getMessageFormat();

    private static final class SchemaErrorListener extends LexerErrorListener {

        @Override
        protected RuntimeException failOnSyntaxError(String message, Throwable cause) {
            return new SchemaLexerException(SCMLEX01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Schema (Line %d:%d) [" + SCMLEX01 + "]: %s (Line: %s)";
        }
    }

    private static final class JsonErrorListener extends LexerErrorListener {

        @Override
        protected RuntimeException failOnSyntaxError(String message, Throwable cause) {
            return new JsonLexerException(JSNLEX01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Json (Line %d:%d) [" + JSNLEX01 + "]: %s (Line: %s)";
        }
    }

    private static final class DateTimeErrorListener extends LexerErrorListener {

        @Override
        protected RuntimeException failOnSyntaxError(String message, Throwable cause) {
            return new DateTimeLexerException(LEXRDT01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Invalid date-time pattern (%s, error on '%s')";
        }
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {
        if(this == DATE_TIME) {
            var note = ((Lexer) recognizer).getText();
            if(isEmpty(note)) note = getLine(recognizer, line);
            throw failOnSyntaxError(getMessageFormat().formatted(msg, note), e);
        }
        throw failOnSyntaxError(getMessageFormat().formatted(line, charPositionInLine,
            msg, getErrorLine(recognizer, line, charPositionInLine)), e);
    }

    private static String getErrorLine(Recognizer<?, ?> recognizer, int line, int charPositionInLine) {
        var stream = recognizer.getInputStream();
        if(stream == null) return NOT_FOUND;
        var textLine = stream.toString().lines().skip(line - 1).findFirst();
        if(textLine.isEmpty()) return NOT_FOUND;
        var textBuilder = new StringBuilder(textLine.get());
        if(textBuilder.length() < charPositionInLine) return NOT_FOUND;
        return textBuilder.insert(charPositionInLine, ERROR_POINTER).toString().trim();
    }

    private static String getLine(Recognizer<?, ?> recognizer, int line) {
        var stream = recognizer.getInputStream();
        if(stream == null) return NOT_FOUND;
        return stream.toString().lines().skip(line - 1).findFirst().orElse(NOT_FOUND).trim();
    }
}