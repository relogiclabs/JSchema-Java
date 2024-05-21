package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.tree.Context;
import lombok.Getter;

import static com.relogiclabs.jschema.internal.util.CommonHelper.nonNullFrom;

@Getter
public class JsonSchemaException extends ScriptRuntimeException {
    private final ErrorDetail error;
    private final ExpectedDetail expected;
    private final ActualDetail actual;

    public JsonSchemaException(ErrorDetail error, ExpectedDetail expected, ActualDetail actual) {
        this(error, expected, actual, null);
    }

    public JsonSchemaException(ErrorDetail error, ExpectedDetail expected,
                               ActualDetail actual, Throwable cause) {
        super(error.getCode(), formatMessage(error, expected, actual), cause);
        this.error = error;
        this.expected = expected;
        this.actual = actual;
    }

    private static String formatMessage(ErrorDetail error, ExpectedDetail expected,
                                        ActualDetail actual) {
        Context context = nonNullFrom(expected.getContext(), actual.getContext());
        return context.getRuntime().getMessageFormatter().format(error, expected, actual);
    }
}