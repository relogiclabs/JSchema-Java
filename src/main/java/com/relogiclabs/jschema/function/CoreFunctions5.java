package com.relogiclabs.jschema.function;

import com.relogiclabs.jschema.exception.FunctionArgumentValueException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.internal.function.IPAddress;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JInteger;
import com.relogiclabs.jschema.node.JString;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.relogiclabs.jschema.internal.function.IPAddress.IPV4_VERSION;
import static com.relogiclabs.jschema.internal.function.IPAddress.IPV6_VERSION;
import static com.relogiclabs.jschema.message.ErrorCode.IPV4CF01;
import static com.relogiclabs.jschema.message.ErrorCode.IPV6CF01;
import static com.relogiclabs.jschema.message.ErrorCode.IPVACF01;
import static com.relogiclabs.jschema.message.ErrorCode.IPVACF02;
import static com.relogiclabs.jschema.message.ErrorCode.IPVACF03;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public final class CoreFunctions5 extends CoreFunctions4 {
    public boolean ipv4(JString target) {
        // Based on RFC 790 and RFC 1918
        if(IPAddress.isValidIPv4(target.getValue())) return true;
        return fail(new FunctionValidationException(
            new ErrorDetail(IPV4CF01, "Invalid target IPv4 address"),
            new ExpectedDetail(caller, "a valid IPv4 address"),
            new ActualDetail(target, "found malformed target " + target)));
    }

    public boolean ipv6(JString target) {
        // Based on RFC 2373 (including widely used format)
        // Also support IPv4 mapped IPv6 address
        if(IPAddress.isValidIPv6(target.getValue())) return true;
        return fail(new FunctionValidationException(
            new ErrorDetail(IPV6CF01, "Invalid target IPv6 address"),
            new ExpectedDetail(caller, "a valid IPv6 address"),
            new ActualDetail(target, "found malformed target " + target)));
    }

    public boolean ipv(JString target, JInteger... versions) {
        if(versions.length == 0) return fail(new FunctionArgumentValueException(
            formatForSchema(IPVACF01, "Version number(s) are missing", caller)));
        for(var version : versions) {
            if(version.getValue() == IPV4_VERSION) {
                if(IPAddress.isValidIPv4(target.getValue())) return true;
            } else if(version.getValue() == IPV6_VERSION) {
                if(IPAddress.isValidIPv6(target.getValue())) return true;
            } else return fail(new FunctionArgumentValueException(formatForSchema(IPVACF02,
                "Invalid version number " + version, caller)));
        }
        var list = Arrays.stream(versions).map(JInteger::toString)
            .collect(Collectors.joining(" or "));
        return fail(new FunctionValidationException(
            new ErrorDetail(IPVACF03, "Invalid target IP address"),
            new ExpectedDetail(caller, "a valid IPv(" + list + ") address"),
            new ActualDetail(target, "found malformed target " + target)));
    }
}