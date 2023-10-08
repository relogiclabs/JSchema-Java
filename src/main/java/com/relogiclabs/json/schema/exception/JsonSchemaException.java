package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ActualDetail;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.message.ExpectedDetail;
import com.relogiclabs.json.schema.tree.Context;
import lombok.Getter;

import static com.relogiclabs.json.schema.internal.util.MiscellaneousHelper.nonNull;

@Getter
public class JsonSchemaException extends CommonException {

    private final ExpectedDetail expected;
    private final ActualDetail actual;

    public JsonSchemaException(ErrorDetail error, ExpectedDetail expected, ActualDetail actual) {
        super(error.getCode(), format(error, expected, actual));
        this.expected = expected;
        this.actual = actual;
    }

    public JsonSchemaException(ErrorDetail error, ExpectedDetail expected, ActualDetail actual,
                               Throwable cause) {
        super(error.getCode(), format(error, expected, actual), cause);
        this.expected = expected;
        this.actual = actual;
    }

    private static String format(ErrorDetail error, ExpectedDetail expected, ActualDetail actual) {
        Context context = nonNull(expected.getContext(), actual.getContext());
        return context.getRuntime().getMessageFormatter().format(error, expected, actual);
    }
}