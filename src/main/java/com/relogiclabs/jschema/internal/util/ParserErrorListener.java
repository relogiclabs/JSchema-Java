package com.relogiclabs.jschema.internal.util;

import com.relogiclabs.jschema.exception.CommonException;
import com.relogiclabs.jschema.exception.JsonParserException;
import com.relogiclabs.jschema.exception.SchemaParserException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import static com.relogiclabs.jschema.message.ErrorCode.JPRS01;
import static com.relogiclabs.jschema.message.ErrorCode.SPRS01;

public abstract class ParserErrorListener extends BaseErrorListener {
    public static final ParserErrorListener SCHEMA = new SchemaErrorListener();
    public static final ParserErrorListener JSON = new JsonErrorListener();

    protected abstract CommonException failOnSyntaxError(String message, Throwable cause);
    protected abstract String getMessageFormat();

    private static final class SchemaErrorListener extends ParserErrorListener {
        @Override
        protected CommonException failOnSyntaxError(String message, Throwable cause) {
            return new SchemaParserException(SPRS01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Schema (Line %d:%d) [" + SPRS01 + "]: %s (Line: %s)";
        }
    }

    private static final class JsonErrorListener extends ParserErrorListener {
        @Override
        protected CommonException failOnSyntaxError(String message, Throwable cause) {
            return new JsonParserException(JPRS01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Json (Line %d:%d) [" + JPRS01 + "]: %s (Line: %s)";
        }
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {
        LogHelper.debug(recognizer);
        var errorLine = new StringBuilder(((CommonTokenStream) recognizer.getInputStream())
                .getTokenSource().getInputStream().toString().split("\\r?\\n")[line - 1])
                .insert(charPositionInLine, "<|>").toString().trim();
        throw failOnSyntaxError(getMessageFormat().formatted(line, charPositionInLine,
                msg, errorLine), e);
    }
}