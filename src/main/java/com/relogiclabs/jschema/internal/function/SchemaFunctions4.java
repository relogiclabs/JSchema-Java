package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.internal.time.DateTimeParser;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JDateTime;
import com.relogiclabs.jschema.node.JString;
import com.relogiclabs.jschema.time.DateTimeType;
import com.relogiclabs.jschema.time.JsonDateTime;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EUndefined;

import static com.relogiclabs.jschema.internal.util.ReflectionHelper.getDerived;
import static com.relogiclabs.jschema.message.ErrorCode.AFTRDT01;
import static com.relogiclabs.jschema.message.ErrorCode.AFTRDT02;
import static com.relogiclabs.jschema.message.ErrorCode.BFORDT01;
import static com.relogiclabs.jschema.message.ErrorCode.BFORDT02;
import static com.relogiclabs.jschema.message.ErrorCode.ENDFDT01;
import static com.relogiclabs.jschema.message.ErrorCode.ENDFDT02;
import static com.relogiclabs.jschema.message.ErrorCode.RNDEND01;
import static com.relogiclabs.jschema.message.ErrorCode.RNDEND02;
import static com.relogiclabs.jschema.message.ErrorCode.RNDEND03;
import static com.relogiclabs.jschema.message.ErrorCode.RNDEND04;
import static com.relogiclabs.jschema.message.ErrorCode.RNDSTA01;
import static com.relogiclabs.jschema.message.ErrorCode.RNDSTA02;
import static com.relogiclabs.jschema.message.ErrorCode.RNDSTA03;
import static com.relogiclabs.jschema.message.ErrorCode.RNDSTA04;
import static com.relogiclabs.jschema.message.ErrorCode.STRTDT01;
import static com.relogiclabs.jschema.message.ErrorCode.STRTDT02;
import static com.relogiclabs.jschema.time.DateTimeType.DATE_TYPE;
import static com.relogiclabs.jschema.time.DateTimeType.TIME_TYPE;

public abstract class SchemaFunctions4 extends SchemaFunctions3 {
    @ConstraintFunction
    public boolean date(JString target, EString pattern) {
        return dateTime(target, pattern, DATE_TYPE);
    }

    @ConstraintFunction
    public boolean time(JString target, EString pattern) {
        return dateTime(target, pattern, TIME_TYPE);
    }

    private boolean dateTime(JString target, EString pattern, DateTimeType type) {
        return new DateTimeAgent(pattern.getValue(), type).parse(invoker, target) != null;
    }

    @ConstraintFunction
    public boolean before(JDateTime target, EString dateTime) {
        var refDateTime = getDateTime(target.getDateTimeParser(), dateTime);
        if(refDateTime == null) return false;
        if(target.getDateTime().compareTo(refDateTime) < 0) return true;
        var type = target.getDateTime().getType();
        var code = type == DATE_TYPE ? BFORDT01 : BFORDT02;
        return fail(new FunctionValidationException(
            new ErrorDetail(code, "Target is not earlier than specified " + type),
            new ExpectedDetail(invoker, "a " + type + " before " + dateTime),
            new ActualDetail(target, "target " + target + " is not earlier")
        ));
    }

    @ConstraintFunction
    public boolean after(JDateTime target, EString dateTime) {
        var refDateTime = getDateTime(target.getDateTimeParser(), dateTime);
        if(refDateTime == null) return false;
        if(target.getDateTime().compareTo(refDateTime) > 0) return true;
        var type = target.getDateTime().getType();
        var code = type == DATE_TYPE ? AFTRDT01 : AFTRDT02;
        return fail(new FunctionValidationException(
            new ErrorDetail(code, "Target is not later than specified " + type),
            new ExpectedDetail(invoker, "a " + type + " after " + dateTime),
            new ActualDetail(target, "target " + target + " is not later")
        ));
    }

    @ConstraintFunction
    public boolean range(JDateTime target, EString start, EString end) {
        var rStart = getDateTime(target.getDateTimeParser(), start);
        if(rStart == null) return false;
        var rEnd = getDateTime(target.getDateTimeParser(), end);
        if(rEnd == null) return false;
        if(target.getDateTime().compareTo(rStart) < 0)
            return failOnStartDate(target, start, getErrorCode(target, RNDSTA01, RNDSTA02));
        if(target.getDateTime().compareTo(rEnd) > 0)
            return failOnEndDate(target, end, getErrorCode(target, RNDEND01, RNDEND02));
        return true;
    }

    private static String getErrorCode(JDateTime target, String date, String time) {
        return target.getDateTime().getType() == DATE_TYPE ? date : time;
    }

    private boolean failOnStartDate(JDateTime target, EString start, String code) {
        var dateTime = target.getDateTime().getType();
        return fail(new FunctionValidationException(
            new ErrorDetail(code, "Target is earlier than start " + dateTime),
            new ExpectedDetail(invoker, "a " + dateTime + " from or after " + start),
            new ActualDetail(target, "target " + target + " is before start")
        ));
    }

    private boolean failOnEndDate(JDateTime target, EString end, String code) {
        var dateTime = target.getDateTime().getType();
        return fail(new FunctionValidationException(
            new ErrorDetail(code, "Target is later than end " + dateTime),
            new ExpectedDetail(invoker, "a " + dateTime + " until or before " + end),
            new ActualDetail(target, "target " + target + " is after end")
        ));
    }

    @ConstraintFunction
    public boolean range(JDateTime target, EUndefined start, EString end) {
        var rEnd = getDateTime(target.getDateTimeParser(), end);
        if(rEnd == null) return false;
        if (target.getDateTime().compareTo(rEnd) <= 0) return true;
        return failOnEndDate(target, end, getErrorCode(target, RNDEND03, RNDEND04));
    }

    @ConstraintFunction
    public boolean range(JDateTime target, EString start, EUndefined end) {
        var rStart = getDateTime(target.getDateTimeParser(), start);
        if(rStart == null) return false;
        if (target.getDateTime().compareTo(rStart) >= 0) return true;
        return failOnStartDate(target, start, getErrorCode(target, RNDSTA03, RNDSTA04));
    }

    @ConstraintFunction
    public boolean start(JDateTime target, EString dateTime) {
        var refDateTime = getDateTime(target.getDateTimeParser(), dateTime);
        if(refDateTime == null) return false;
        if(target.getDateTime().compareTo(refDateTime) >= 0) return true;
        var type = target.getDateTime().getType();
        var code = type == DATE_TYPE ? STRTDT01 : STRTDT02;
        return fail(new FunctionValidationException(
            new ErrorDetail(code, "Target is earlier than start " + type),
            new ExpectedDetail(invoker, "a " + type + " from or after " + dateTime),
            new ActualDetail(target, "target " + target + " is before start")
        ));
    }

    @ConstraintFunction
    public boolean end(JDateTime target, EString dateTime) {
        var refDateTime = getDateTime(target.getDateTimeParser(), dateTime);
        if(refDateTime == null) return false;
        if(target.getDateTime().compareTo(refDateTime) <= 0) return true;
        var type = target.getDateTime().getType();
        var code = type == DATE_TYPE ? ENDFDT01 : ENDFDT02;
        return fail(new FunctionValidationException(
            new ErrorDetail(code, "Target is later than end " + type),
            new ExpectedDetail(invoker, "a " + type + " before or until " + dateTime),
            new ActualDetail(target, "target " + target + " is after end")
        ));
    }

    private JsonDateTime getDateTime(DateTimeParser parser, EString dateTime) {
        if(getDerived(dateTime) instanceof JDateTime result
            && result.getDateTime().getType() == parser.getType()) return result.getDateTime();
        var jDateTime = new DateTimeAgent(parser).parse(invoker, dateTime);
        if(jDateTime == null) return null;
        if(dateTime instanceof JString string) string.setDerived(jDateTime.createNode(string));
        return jDateTime;
    }
}