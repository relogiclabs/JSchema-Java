package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.exception.DateTimeLexerException;
import com.relogiclabs.jschema.exception.InvalidDateTimeException;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.internal.time.DateTimeParser;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JFunction;
import com.relogiclabs.jschema.node.JString;
import com.relogiclabs.jschema.time.DateTimeType;
import com.relogiclabs.jschema.time.JsonDateTime;

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

    public JsonDateTime parse(JFunction function, JString dateTime) {
        var exceptions = function.getRuntime().getExceptions();
        try {
            if(parser == null) parser = new DateTimeParser(pattern, type);
            return parser.parse(dateTime.getValue());
        } catch(DateTimeLexerException ex) {
            exceptions.fail(new JsonSchemaException(
                    new ErrorDetail(ex.getCode(), ex.getMessage()),
                    new ExpectedDetail(function, "a valid ", type, " pattern"),
                    new ActualDetail(dateTime, "found ", pattern, " that is invalid"),
                    ex));
        } catch(InvalidDateTimeException ex) {
            exceptions.fail(new JsonSchemaException(
                    new ErrorDetail(ex.getCode(), ex.getMessage()),
                    new ExpectedDetail(function, "a valid ", type, " formatted as ", pattern),
                    new ActualDetail(dateTime, "found ", dateTime, " that is invalid or malformatted"),
                    ex));
        }
        return null;
    }
}