package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.exception.DateTimeLexerException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.exception.InvalidDateTimeException;
import com.relogiclabs.jschema.internal.time.DateTimeParser;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JString;
import com.relogiclabs.jschema.time.DateTimeType;
import com.relogiclabs.jschema.time.JsonDateTime;
import com.relogiclabs.jschema.type.EString;

import static com.relogiclabs.jschema.internal.util.StringHelper.quote;

public class DateTimeAgent {
    private final String pattern;
    private final DateTimeType type;
    private DateTimeParser parser;

    public DateTimeAgent(String pattern, DateTimeType type) {
        this.pattern = pattern;
        this.type = type;
    }

    public DateTimeAgent(DateTimeParser parser) {
        this.pattern = parser.getPattern();
        this.type = parser.getType();
        this.parser = parser;
    }

    public JsonDateTime parse(JNode invoker, EString dateTime) {
        try {
            if(parser == null) parser = new DateTimeParser(pattern, type);
            return parser.parse(dateTime.getValue());
        } catch(DateTimeLexerException ex) {
            if(!(dateTime instanceof JString node)) fail(invoker, ex);
            else fail(invoker, new FunctionValidationException(
                new ErrorDetail(ex.getCode(), ex.getMessage()),
                new ExpectedDetail(invoker, "a valid " + type + " pattern"),
                new ActualDetail(node, "found " + quote(pattern) + " that is invalid"), ex));
        } catch(InvalidDateTimeException ex) {
            if(!(dateTime instanceof JString node)) fail(invoker, ex);
            else fail(invoker, new FunctionValidationException(
                new ErrorDetail(ex.getCode(), ex.getMessage()),
                new ExpectedDetail(invoker, "a valid " + type + " formatted as " + quote(pattern)),
                new ActualDetail(node, "target " + dateTime + " is invalid or malformed"),
                ex));
        }
        return null;
    }

    private static void fail(JNode invoker, RuntimeException exception) {
        invoker.getRuntime().getExceptions().fail(exception);
    }
}