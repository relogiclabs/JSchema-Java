package com.relogiclabs.json.schema.internal.util;

import com.relogiclabs.json.schema.exception.CommonException;
import com.relogiclabs.json.schema.exception.JsonParserException;
import com.relogiclabs.json.schema.exception.SchemaParserException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import static com.relogiclabs.json.schema.message.ErrorCode.JPRS01;
import static com.relogiclabs.json.schema.message.ErrorCode.SPRS01;

public abstract class ParserErrorListener extends BaseErrorListener {
    public static final ParserErrorListener SCHEMA = new SchemaErrorListener();
    public static final ParserErrorListener JSON = new JsonErrorListener();

    protected abstract CommonException createException(String message, Throwable cause);
    protected abstract String getMessageFormat();

    private static final class SchemaErrorListener extends ParserErrorListener {
        @Override
        protected CommonException createException(String message, Throwable cause) {
            return new SchemaParserException(SPRS01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Schema (Line %d:%d) [" + SPRS01 + "]: %s (error on '%s')";
        }
    }

    private static final class JsonErrorListener extends ParserErrorListener {
        @Override
        protected CommonException createException(String message, Throwable cause) {
            return new JsonParserException(JPRS01, message, cause);
        }

        @Override
        protected String getMessageFormat() {
            return "Json (Line %d:%d) [" + JPRS01 + "]: %s (error on '%s')";
        }
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {
        DebugUtilities.print(recognizer);
        var message = getMessageFormat().formatted(line, charPositionInLine, msg, offendingSymbol);
        throw createException(message, e);
    }
}