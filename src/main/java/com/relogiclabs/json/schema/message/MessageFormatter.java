package com.relogiclabs.json.schema.message;

import com.relogiclabs.json.schema.tree.Context;
import com.relogiclabs.json.schema.tree.Location;
import lombok.Getter;
import lombok.Setter;

import static org.apache.commons.lang3.StringUtils.capitalize;

@Getter
public abstract class MessageFormatter {
    private static final String NEWLINE = System.lineSeparator();

    private static final String SCHEMA_BASE_EXCEPTION = "Schema Input [%s]: %s";
    private static final String JSON_BASE_EXCEPTION = "Json Input [%s]: %s";
    private static final String SCHEMA_PARSE_EXCEPTION = "Schema (Line: %s) [%s]: %s";
    private static final String JSON_PARSE_EXCEPTION = "Json (Line: %s) [%s]: %s";

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

    @Setter
    private int outlineLength = 200;

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

    public static ErrorDetail formatForSchema(String code, String message, Context context) {
        return formatForSchema(code, message, context != null? context.getLocation() : null);
    }

    public static ErrorDetail formatForSchema(String code, String message, Location location) {
        return location == null
                ? createDetail(code, SCHEMA_BASE_EXCEPTION, message)
                : createDetail(code, SCHEMA_PARSE_EXCEPTION, message, location);
    }

    public static ErrorDetail formatForJson(String code, String message, Context context) {
        return formatForJson(code, message, context != null? context.getLocation() : null);
    }

    public static ErrorDetail formatForJson(String code, String message, Location location) {
        return location == null
                ? createDetail(code, JSON_BASE_EXCEPTION, message)
                : createDetail(code, JSON_PARSE_EXCEPTION, message, location);
    }

    private static ErrorDetail createDetail(String code, String format, String message) {
        return new ErrorDetail(code, format.formatted(code, message));
    }

    private static ErrorDetail createDetail(String code, String format, String message,
                                            Location location) {
        return new ErrorDetail(code, format.formatted(location, code, message));
    }
}