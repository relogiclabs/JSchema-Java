package com.relogiclabs.json.schema.function;

import com.relogiclabs.json.schema.exception.DateTimeLexerException;
import com.relogiclabs.json.schema.exception.InvalidDateTimeException;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.time.DateTimeParser;
import com.relogiclabs.json.schema.internal.time.DateTimeType;
import com.relogiclabs.json.schema.internal.time.JsonDateTime;
import com.relogiclabs.json.schema.message.ActualDetail;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.message.ExpectedDetail;
import com.relogiclabs.json.schema.types.JFunction;
import com.relogiclabs.json.schema.types.JString;

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
        try {
            if(parser == null) parser = new DateTimeParser(pattern, type);
            return parser.parse(dateTime.getValue());
        } catch(DateTimeLexerException ex) {
            function.failWith(new JsonSchemaException(
                    new ErrorDetail(ex.getCode(), ex.getMessage()),
                    new ExpectedDetail(function, "a valid ", type, " pattern"),
                    new ActualDetail(dateTime, "found ", pattern, " that is invalid"),
                    ex));
        } catch(InvalidDateTimeException ex) {
            function.failWith(new JsonSchemaException(
                    new ErrorDetail(ex.getCode(), ex.getMessage()),
                    new ExpectedDetail(function, "a valid ", type, " formatted as ", pattern),
                    new ActualDetail(dateTime, "found ", dateTime, " that is invalid or malformatted"),
                    ex));
        }
        return null;
    }
}