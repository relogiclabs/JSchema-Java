package com.relogiclabs.jschema.function;

import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.internal.function.DateTimeAgent;
import com.relogiclabs.jschema.internal.time.DateTimeParser;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JDateTime;
import com.relogiclabs.jschema.node.JString;
import com.relogiclabs.jschema.node.JUndefined;
import com.relogiclabs.jschema.time.DateTimeType;

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

public abstract class CoreFunctions4 extends CoreFunctions3 {
    public boolean date(JString target, JString pattern) {
        return dateTime(target, pattern, DATE_TYPE);
    }

    public boolean time(JString target, JString pattern) {
        return dateTime(target, pattern, DateTimeType.TIME_TYPE);
    }

    private boolean dateTime(JString target, JString pattern, DateTimeType type) {
        return new DateTimeAgent(pattern.getValue(), type).parse(caller, target) != null;
    }

    public boolean before(JDateTime target, JString dateTime) {
        var refDateTime = getDateTime(target.getDateTimeParser(), dateTime);
        if(refDateTime == null) return false;
        if(target.getDateTime().compare(refDateTime.getDateTime()) < 0) return true;
        var type = target.getDateTime().getType();
        var code = type == DATE_TYPE ? BFORDT01 : BFORDT02;
        return fail(new FunctionValidationException(
            new ErrorDetail(code, "Target is not earlier than specified " + type),
            new ExpectedDetail(caller, "a " + type + " before " + dateTime),
            new ActualDetail(target, "target " + target + " is not earlier")
        ));
    }

    public boolean after(JDateTime target, JString dateTime) {
        var refDateTime = getDateTime(target.getDateTimeParser(), dateTime);
        if(refDateTime == null) return false;
        if(target.getDateTime().compare(refDateTime.getDateTime()) > 0) return true;
        var type = target.getDateTime().getType();
        var code = type == DATE_TYPE ? AFTRDT01 : AFTRDT02;
        return fail(new FunctionValidationException(
            new ErrorDetail(code, "Target is not later than specified " + type),
            new ExpectedDetail(caller, "a " + type + " after " + dateTime),
            new ActualDetail(target, "target " + target + " is not later")
        ));
    }

    public boolean range(JDateTime target, JString start, JString end) {
        var rStart = getDateTime(target.getDateTimeParser(), start);
        if(rStart == null) return false;
        var rEnd = getDateTime(target.getDateTimeParser(), end);
        if(rEnd == null) return false;
        if(target.getDateTime().compare(rStart.getDateTime()) < 0)
            return failOnStartDate(target, rStart, getErrorCode(target, RNDSTA01, RNDSTA02));
        if(target.getDateTime().compare(rEnd.getDateTime()) > 0)
            return failOnEndDate(target, rEnd, getErrorCode(target, RNDEND01, RNDEND02));
        return true;
    }

    private static String getErrorCode(JDateTime target, String date, String time) {
        return target.getDateTime().getType() == DATE_TYPE ? date : time;
    }

    private boolean failOnStartDate(JDateTime target, JDateTime start, String code) {
        var dateTime = target.getDateTime().getType();
        return fail(new FunctionValidationException(
            new ErrorDetail(code, "Target is earlier than start " + dateTime),
            new ExpectedDetail(caller, "a " + dateTime + " from or after " + start),
            new ActualDetail(target, "target " + target + " is before start")
        ));
    }

    private boolean failOnEndDate(JDateTime target, JDateTime end, String code) {
        var dateTime = target.getDateTime().getType();
        return fail(new FunctionValidationException(
            new ErrorDetail(code, "Target is later than end " + dateTime),
            new ExpectedDetail(caller, "a " + dateTime + " until or before " + end),
            new ActualDetail(target, "target " + target + " is after end")
        ));
    }

    public boolean range(JDateTime target, JUndefined start, JString end) {
        var rEnd = getDateTime(target.getDateTimeParser(), end);
        if(rEnd == null) return false;
        if (target.getDateTime().compare(rEnd.getDateTime()) <= 0) return true;
        return failOnEndDate(target, rEnd, getErrorCode(target, RNDEND03, RNDEND04));
    }

    public boolean range(JDateTime target, JString start, JUndefined end) {
        var rStart = getDateTime(target.getDateTimeParser(), start);
        if(rStart == null) return false;
        if (target.getDateTime().compare(rStart.getDateTime()) >= 0) return true;
        return failOnStartDate(target, rStart, getErrorCode(target, RNDSTA03, RNDSTA04));
    }

    public boolean start(JDateTime target, JString dateTime) {
        var refDateTime = getDateTime(target.getDateTimeParser(), dateTime);
        if(refDateTime == null) return false;
        if(target.getDateTime().compare(refDateTime.getDateTime()) < 0) {
            var type = target.getDateTime().getType();
            var code = type == DATE_TYPE ? STRTDT01 : STRTDT02;
            return fail(new FunctionValidationException(
                new ErrorDetail(code, "Target is earlier than start " + type),
                new ExpectedDetail(caller, "a " + type + " from or after " + dateTime),
                new ActualDetail(target, "target " + target + " is before start")
            ));
        }
        return true;
    }

    public boolean end(JDateTime target, JString dateTime) {
        var refDateTime = getDateTime(target.getDateTimeParser(), dateTime);
        if(refDateTime == null) return false;
        if(target.getDateTime().compare(refDateTime.getDateTime()) > 0) {
            var type = target.getDateTime().getType();
            var code = type == DATE_TYPE ? ENDFDT01 : ENDFDT02;
            return fail(new FunctionValidationException(
                new ErrorDetail(code, "Target is later than end " + type),
                new ExpectedDetail(caller, "a " + type + " before or until " + dateTime),
                new ActualDetail(target, "target " + target + " is after end")
            ));
        }
        return true;
    }

    private JDateTime getDateTime(DateTimeParser parser, JString dateTime) {
        if(dateTime.getDerived() instanceof JDateTime result
            && result.getDateTime().getType() == parser.getType()) return result;
        var jDateTime = new DateTimeAgent(parser).parse(caller, dateTime);
        if(jDateTime == null) return null;
        dateTime.setDerived(jDateTime.createNode(dateTime));
        return (JDateTime) dateTime.getDerived();
    }
}