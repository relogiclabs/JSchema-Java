package com.relogiclabs.jschema.message;

import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.tree.Context;
import com.relogiclabs.jschema.tree.Location;
import lombok.Getter;
import org.antlr.v4.runtime.Token;

import static org.apache.commons.lang3.StringUtils.capitalize;

@Getter
public abstract class MessageFormatter {
    private static final String NEWLINE = System.lineSeparator();

    private static final String SCHEMA_BASIC_FORMAT = "Schema Input [%s]: %s";
    private static final String JSON_BASIC_FORMAT = "Json Input [%s]: %s";
    private static final String SCHEMA_DETAIL_FORMAT = "Schema (Line: %s) [%s]: %s";
    private static final String JSON_DETAIL_FORMAT = "Json (Line: %s) [%s]: %s";

    public static final MessageFormatter SCHEMA_VALIDATION = new ValidationFormatter(
            "Schema (Line: %s) Json (Line: %s) [%s]: %s.",
            " %s is expected",
            " but %s.");

    public static final MessageFormatter SCHEMA_ASSERTION = new AssertionFormatter(
            "%s: %s" + NEWLINE,
            "Expected (Schema Line: %s): %s" + NEWLINE,
            "Actual (Json Line: %s): %s" + NEWLINE);

    public static final MessageFormatter JSON_ASSERTION = new AssertionFormatter(
            "%s: %s" + NEWLINE,
            "Expected (Json Line: %s): %s" + NEWLINE,
            "Actual (Json Line: %s): %s" + NEWLINE);

    private final String summary;
    private final String expected;
    private final String actual;

    protected MessageFormatter(String summary, String expected, String actual) {
        this.summary = summary;
        this.expected = expected;
        this.actual = actual;
    }

    public abstract String format(ErrorDetail error, ExpectedDetail expected, ActualDetail actual);

    private static final class ValidationFormatter extends MessageFormatter {
        private ValidationFormatter(String summary, String expected, String actual) {
            super(summary, expected, actual);
        }

        @Override
        public String format(ErrorDetail error, ExpectedDetail expected, ActualDetail actual) {
            return  getSummary().formatted(expected.getLocation(), actual.getLocation(),
                            error.getCode(), error.getMessage()) +
                    getExpected().formatted(capitalize(expected.getMessage())) +
                    getActual().formatted(actual.getMessage());
        }
    }

    private static final class AssertionFormatter extends MessageFormatter {
        public AssertionFormatter(String summary, String expected, String actual) {
            super(summary, expected, actual);
        }

        @Override
        public String format(ErrorDetail error, ExpectedDetail expected, ActualDetail actual) {
            return  getSummary().formatted(error.getCode(), error.getMessage()) +
                    getExpected().formatted(expected.getLocation(), expected.getMessage()) +
                    getActual().formatted(actual.getLocation(), actual.getMessage());
        }
    }

    public static ErrorDetail formatForSchema(String code, String message, JNode node) {
        return formatForSchema(code, message, node != null? node.getContext().getLocation() : null);
    }

    public static ErrorDetail formatForSchema(String code, String message, Context context) {
        return formatForSchema(code, message, context != null? context.getLocation() : null);
    }

    public static ErrorDetail formatForSchema(String code, String message, Location location) {
        return location == null
                ? createError(code, SCHEMA_BASIC_FORMAT, message)
                : createError(code, SCHEMA_DETAIL_FORMAT, message, location);
    }

    public static ErrorDetail formatForSchema(String code, String message, Token token) {
        return token == null
                ? createError(code, SCHEMA_BASIC_FORMAT, message)
                : createError(code, SCHEMA_DETAIL_FORMAT, message, token);
    }

    public static ErrorDetail formatForJson(String code, String message, JNode node) {
        return formatForJson(code, message, node != null? node.getContext().getLocation() : null);
    }

    public static ErrorDetail formatForJson(String code, String message, Context context) {
        return formatForJson(code, message, context != null? context.getLocation() : null);
    }

    public static ErrorDetail formatForJson(String code, String message, Location location) {
        return location == null
                ? createError(code, JSON_BASIC_FORMAT, message)
                : createError(code, JSON_DETAIL_FORMAT, message, location);
    }

    private static ErrorDetail createError(String code, String format, String message) {
        return new ErrorDetail(code, format.formatted(code, message));
    }

    private static ErrorDetail createError(String code, String format, String message,
                                            Location location) {
        return new ErrorDetail(code, format.formatted(location, code, message));
    }

    private static ErrorDetail createError(String code, String format, String message,
                                            Token token) {
        return new ErrorDetail(code, format.formatted(token.getLine() + ":"
                + token.getCharPositionInLine(), code, message));
    }
}