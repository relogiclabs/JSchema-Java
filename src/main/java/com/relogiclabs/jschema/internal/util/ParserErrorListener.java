package com.relogiclabs.jschema.internal.util;

import com.relogiclabs.jschema.exception.JsonParserException;
import com.relogiclabs.jschema.exception.SchemaParserException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import static com.relogiclabs.jschema.message.ErrorCode.JSNPRS01;
import static com.relogiclabs.jschema.message.ErrorCode.SCMPRS01;
import static com.relogiclabs.jschema.message.MessageFormatter.ERROR_POINTER;

public abstract class ParserErrorListener extends BaseErrorListener {
    public static final ParserErrorListener SCHEMA = new SchemaErrorListener();
    public static final ParserErrorListener JSON = new JsonErrorListener();

    private static final String NOT_FOUND = "<NOT_FOUND>";

    protected abstract RuntimeException failOnSyntaxError(String message, Throwable cause);
    protected abstract String getMessageFormat();

    private static final class SchemaErrorListener extends ParserErrorListener {
        @Override
        protected RuntimeException failOnSyntaxError(String message, Throwable cause) {
            return new SchemaParserException(SCMPRS01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Schema (Line %d:%d) [" + SCMPRS01 + "]: %s (Line: %s)";
        }
    }

    private static final class JsonErrorListener extends ParserErrorListener {
        @Override
        protected RuntimeException failOnSyntaxError(String message, Throwable cause) {
            return new JsonParserException(JSNPRS01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Json (Line %d:%d) [" + JSNPRS01 + "]: %s (Line: %s)";
        }
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {
        LogHelper.debug(recognizer);
        throw failOnSyntaxError(getMessageFormat().formatted(line, charPositionInLine,
            msg, getErrorLine(recognizer, line, charPositionInLine)), e);
    }

    private static String getErrorLine(Recognizer<?, ?> recognizer, int line, int charPositionInLine) {
        var tokenStream = (CommonTokenStream) recognizer.getInputStream();
        if(tokenStream == null) return NOT_FOUND;
        var inputStream = tokenStream.getTokenSource().getInputStream();
        if(inputStream == null) return NOT_FOUND;
        var textLine = inputStream.toString().lines().skip(line - 1).findFirst();
        if(textLine.isEmpty()) return NOT_FOUND;
        var textBuilder = new StringBuilder(textLine.get());
        if(textBuilder.length() < charPositionInLine) return NOT_FOUND;
        return textBuilder.insert(charPositionInLine, ERROR_POINTER).toString().trim();
    }
}