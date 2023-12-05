package com.relogiclabs.json.schema.function;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.time.DateTimeParser;
import com.relogiclabs.json.schema.internal.time.DateTimeType;
import com.relogiclabs.json.schema.message.ActualDetail;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.message.ExpectedDetail;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.types.JDateTime;
import com.relogiclabs.json.schema.types.JString;
import com.relogiclabs.json.schema.types.JUndefined;

import static com.relogiclabs.json.schema.internal.time.DateTimeType.DATE_TYPE;
import static com.relogiclabs.json.schema.message.ErrorCode.AFTR01;
import static com.relogiclabs.json.schema.message.ErrorCode.AFTR02;
import static com.relogiclabs.json.schema.message.ErrorCode.BFOR01;
import static com.relogiclabs.json.schema.message.ErrorCode.BFOR02;
import static com.relogiclabs.json.schema.message.ErrorCode.DRNG01;
import static com.relogiclabs.json.schema.message.ErrorCode.DRNG02;
import static com.relogiclabs.json.schema.message.ErrorCode.DRNG03;
import static com.relogiclabs.json.schema.message.ErrorCode.DRNG04;
import static com.relogiclabs.json.schema.message.ErrorCode.DRNG05;
import static com.relogiclabs.json.schema.message.ErrorCode.DRNG06;
import static com.relogiclabs.json.schema.message.ErrorCode.DRNG07;
import static com.relogiclabs.json.schema.message.ErrorCode.DRNG08;
import static com.relogiclabs.json.schema.message.ErrorCode.ENDE01;
import static com.relogiclabs.json.schema.message.ErrorCode.ENDE02;
import static com.relogiclabs.json.schema.message.ErrorCode.STRT01;
import static com.relogiclabs.json.schema.message.ErrorCode.STRT02;

public class CoreFunctions4 extends CoreFunctions3 {
    public CoreFunctions4(RuntimeContext runtime) {
        super(runtime);
    }

    public boolean date(JString target, JString pattern) {
        return dateTime(target, pattern, DATE_TYPE);
    }

    public boolean time(JString target, JString pattern) {
        return dateTime(target, pattern, DateTimeType.TIME_TYPE);
    }

    private boolean dateTime(JString target, JString pattern, DateTimeType type) {
        return new DateTimeAgent(pattern.getValue(), type).parse(function, target) != null;
    }

    public boolean before(JDateTime target, JString reference) {
        var dateTime = getDateTime(target.getDateTimeParser(), reference);
        if(dateTime == null) return false;
        if(target.getDateTime().compare(dateTime.getDateTime()) < 0) return true;
        var type = target.getDateTime().getType();
        var code = type == DATE_TYPE ? BFOR01 : BFOR02;
        return failWith(new JsonSchemaException(
                new ErrorDetail(code, type, " is not earlier than specified"),
                new ExpectedDetail(reference, "a ", type, " before ", reference),
                new ActualDetail(target, "found ", target, " which is not inside limit")
        ));
    }

    public boolean after(JDateTime target, JString reference) {
        var dateTime = getDateTime(target.getDateTimeParser(), reference);
        if(dateTime == null) return false;
        if(target.getDateTime().compare(dateTime.getDateTime()) > 0) return true;
        var type = target.getDateTime().getType();
        var code = type == DATE_TYPE ? AFTR01 : AFTR02;
        return failWith(new JsonSchemaException(
                new ErrorDetail(code, type, " is not later than specified"),
                new ExpectedDetail(reference, "a ", type, " after ", reference),
                new ActualDetail(target, "found ", target, " which is not inside limit")
        ));
    }

    public boolean range(JDateTime target, JString start, JString end) {
        var rStart = getDateTime(target.getDateTimeParser(), start);
        if(rStart == null) return false;
        var rEnd = getDateTime(target.getDateTimeParser(), end);
        if(rEnd == null) return false;
        boolean result = true;
        result &= isValidStart(target, rStart, DRNG01, DRNG02);
        result &= isValidEnd(target, rEnd, DRNG03, DRNG04);
        return result;
    }

    public boolean range(JDateTime target, JUndefined start, JString end) {
        var rEnd = getDateTime(target.getDateTimeParser(), end);
        if(rEnd == null) return false;
        return isValidEnd(target, rEnd, DRNG05, DRNG06);
    }

    public boolean range(JDateTime target, JString start, JUndefined end) {
        var rStart = getDateTime(target.getDateTimeParser(), start);
        if(rStart == null) return false;
        return isValidStart(target, rStart, DRNG07, DRNG08);
    }

    private boolean isValidStart(JDateTime target, JDateTime start, String codeDate, String codeTime) {
        if(target.getDateTime().compare(start.getDateTime()) < 0) {
            var type = target.getDateTime().getType();
            var code = type == DATE_TYPE ? codeDate : codeTime;
            return failWith(new JsonSchemaException(
                    new ErrorDetail(code, type, " is earlier than start ", type),
                    new ExpectedDetail(start, "a ", type, " from or after ", start),
                    new ActualDetail(target, "found ", target, " which is before start ", type)
            ));
        }
        return true;
    }

    private boolean isValidEnd(JDateTime target, JDateTime end, String codeDate, String codeTime) {
        if(target.getDateTime().compare(end.getDateTime()) > 0) {
            var type = target.getDateTime().getType();
            var code = type == DATE_TYPE ? codeDate : codeTime;
            return failWith(new JsonSchemaException(
                    new ErrorDetail(code, type, " is later than end ", type),
                    new ExpectedDetail(end, "a ", type, " until or before ", end),
                    new ActualDetail(target, "found ", target, " which is after end ", type)
            ));
        }
        return true;
    }

    public boolean start(JDateTime target, JString reference) {
        var dateTime = getDateTime(target.getDateTimeParser(), reference);
        if(dateTime == null) return false;
        if(target.getDateTime().compare(dateTime.getDateTime()) < 0) {
            var type = target.getDateTime().getType();
            var code = type == DATE_TYPE ? STRT01 : STRT02;
            return failWith(new JsonSchemaException(
                    new ErrorDetail(code, type, " is earlier than specified"),
                    new ExpectedDetail(dateTime, "a ", type, " from or after ", dateTime),
                    new ActualDetail(target, "found ", target, " which is before limit")
            ));
        }
        return true;
    }

    public boolean end(JDateTime target, JString reference) {
        var dateTime = getDateTime(target.getDateTimeParser(), reference);
        if(dateTime == null) return false;
        if(target.getDateTime().compare(dateTime.getDateTime()) > 0) {
            var type = target.getDateTime().getType();
            var code = type == DATE_TYPE ? ENDE01 : ENDE02;
            return failWith(new JsonSchemaException(
                    new ErrorDetail(code, type, " is later than specified"),
                    new ExpectedDetail(dateTime, "a ", type, " until or before ", dateTime),
                    new ActualDetail(target, "found ", target, " which is after limit")
            ));
        }
        return true;
    }

    private JDateTime getDateTime(DateTimeParser parser, JString dateTime) {
        if(dateTime.getDerived() instanceof JDateTime result
            && result.getDateTime().getType() == parser.getType()) return result;
        var jDateTime = new DateTimeAgent(parser).parse(function, dateTime);
        if(jDateTime == null) return null;
        dateTime.setDerived(jDateTime.create(dateTime));
        return (JDateTime) dateTime.getDerived();
    }
}