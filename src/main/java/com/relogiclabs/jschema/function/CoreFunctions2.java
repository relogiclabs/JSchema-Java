package com.relogiclabs.jschema.function;

import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JArray;
import com.relogiclabs.jschema.node.JBoolean;
import com.relogiclabs.jschema.node.JNumber;
import com.relogiclabs.jschema.node.JObject;
import com.relogiclabs.jschema.node.JString;
import com.relogiclabs.jschema.node.JUndefined;

import java.util.Arrays;

import static com.relogiclabs.jschema.internal.util.StringHelper.joinWith;
import static com.relogiclabs.jschema.message.ErrorCode.EMPTCF01;
import static com.relogiclabs.jschema.message.ErrorCode.EMPTCF02;
import static com.relogiclabs.jschema.message.ErrorCode.EMPTCF03;
import static com.relogiclabs.jschema.message.ErrorCode.ENMNUM01;
import static com.relogiclabs.jschema.message.ErrorCode.ENMSTR01;
import static com.relogiclabs.jschema.message.ErrorCode.MAXICF01;
import static com.relogiclabs.jschema.message.ErrorCode.MAXICF02;
import static com.relogiclabs.jschema.message.ErrorCode.MAXICF03;
import static com.relogiclabs.jschema.message.ErrorCode.MINICF01;
import static com.relogiclabs.jschema.message.ErrorCode.MINICF02;
import static com.relogiclabs.jschema.message.ErrorCode.MINICF03;
import static com.relogiclabs.jschema.message.ErrorCode.NEGICF01;
import static com.relogiclabs.jschema.message.ErrorCode.NEGICF02;
import static com.relogiclabs.jschema.message.ErrorCode.POSICF01;
import static com.relogiclabs.jschema.message.ErrorCode.POSICF02;
import static com.relogiclabs.jschema.message.ErrorCode.RNGNUM01;
import static com.relogiclabs.jschema.message.ErrorCode.RNGNUM02;
import static com.relogiclabs.jschema.message.ErrorCode.RNGNUM03;
import static com.relogiclabs.jschema.message.ErrorCode.RNGNUM04;

public abstract class CoreFunctions2 extends CoreFunctions1 {
    // enum is a keyword in Java and _ will be escaped
    public boolean _enum(JString target, JString... items) {
        var list = Arrays.asList(items);
        if(!list.contains(target)) return fail(new FunctionValidationException(
            new ErrorDetail(ENMSTR01, "Target string is not in enum listing"),
            new ExpectedDetail(caller, "a string in listing " + joinWith(list, ", ", "[", "]")),
            new ActualDetail(target, "target " + target + " is not in listing")));
        return true;
    }

    public boolean _enum(JNumber target, JNumber... items) {
        var list = Arrays.asList(items);
        if(!list.contains(target)) return fail(new FunctionValidationException(
            new ErrorDetail(ENMNUM01, "Target number is not in enum listing"),
            new ExpectedDetail(caller, "a number in listing " + joinWith(list, ", ", "[", "]")),
            new ActualDetail(target, "target " + target + " is not in listing")));
        return true;
    }

    public boolean minimum(JNumber target, JNumber minimum) {
        if(target.compare(minimum) < 0) return fail(new FunctionValidationException(
            new ErrorDetail(MINICF01, "Target number must not be less than minimum"),
            new ExpectedDetail(caller, "a number greater than or equal to " + minimum),
            new ActualDetail(target, "target " + target + " is less than " + minimum)));
        return true;
    }

    public boolean minimum(JNumber target, JNumber minimum, JBoolean exclusive) {
        var relationTo = exclusive.getValue() ? "greater than " : "greater than or equal to ";
        if(target.compare(minimum) < 0) return fail(new FunctionValidationException(
            new ErrorDetail(MINICF02, "Target number must not be less than minimum"),
            new ExpectedDetail(caller, "a number " + relationTo + minimum),
            new ActualDetail(target, "target " + target + " is less than " + minimum)));
        if(exclusive.getValue() && target.compare(minimum) == 0)
            return fail(new FunctionValidationException(
                new ErrorDetail(MINICF03, "Target number must be greater than minimum"),
                new ExpectedDetail(caller, "a number " + relationTo + minimum),
                new ActualDetail(target, "target " + target + " is equal to " + minimum)));
        return true;
    }

    public boolean maximum(JNumber target, JNumber maximum) {
        if(target.compare(maximum) > 0) return fail(new FunctionValidationException(
            new ErrorDetail(MAXICF01, "Target number must not be greater than maximum"),
            new ExpectedDetail(caller, "a number less than or equal " + maximum),
            new ActualDetail(target, "target " + target + " is greater than " + maximum)));
        return true;
    }

    public boolean maximum(JNumber target, JNumber maximum, JBoolean exclusive) {
        var relationTo = exclusive.getValue() ? "less than " : "less than or equal to ";
        if(target.compare(maximum) > 0) return fail(new FunctionValidationException(
            new ErrorDetail(MAXICF02, "Target number must not be greater than maximum"),
            new ExpectedDetail(caller, "a number " + relationTo + maximum),
            new ActualDetail(target, "target " + target + " is greater than " + maximum)));
        if(exclusive.getValue() && target.compare(maximum) == 0)
            return fail(new FunctionValidationException(
                new ErrorDetail(MAXICF03, "Target number must be less than maximum"),
                new ExpectedDetail(caller, "a number " + relationTo + maximum),
                new ActualDetail(target, "target " + target + " is equal to " + maximum)));
        return true;
    }

    public boolean positive(JNumber target) {
        if(target.compare(0) <= 0) return fail(new FunctionValidationException(
            new ErrorDetail(POSICF01, "Target number must be positive"),
            new ExpectedDetail(caller, "a positive number"),
            new ActualDetail(target, "target " + target + " is less than or equal to zero")));
        return true;
    }

    public boolean negative(JNumber target) {
        if(target.compare(0) >= 0) return fail(new FunctionValidationException(
            new ErrorDetail(NEGICF01, "Target number must be negative"),
            new ExpectedDetail(caller, "a negative number"),
            new ActualDetail(target, "target " + target + " is greater than or equal to zero")));
        return true;
    }

    public boolean positive(JNumber target, JNumber reference) {
        if(target.compare(reference) < 0) return fail(new FunctionValidationException(
            new ErrorDetail(POSICF02, "Target number must be positive from reference"),
            new ExpectedDetail(caller, "a positive number from " + reference),
            new ActualDetail(target, "target " + target + " is less than reference")));
        return true;
    }

    public boolean negative(JNumber target, JNumber reference) {
        if(target.compare(reference) > 0) return fail(new FunctionValidationException(
            new ErrorDetail(NEGICF02, "Target number must be negative from reference"),
            new ExpectedDetail(caller, "a negative number from " + reference),
            new ActualDetail(target, "target " + target + " is greater than reference")));
        return true;
    }

    public boolean range(JNumber target, JNumber minimum, JNumber maximum) {
        if(target.compare(minimum) < 0) return fail(new FunctionValidationException(
            new ErrorDetail(RNGNUM01, "Target number is outside of range"),
            new ExpectedDetail(caller, "a number in range [" + minimum + ", " + maximum + "]"),
            new ActualDetail(target, "target " + target + " is less than " + minimum)));
        if(target.compare(maximum) > 0) return fail(new FunctionValidationException(
            new ErrorDetail(RNGNUM02, "Target number is outside of range"),
            new ExpectedDetail(caller, "a number in range [" + minimum + ", " + maximum + "]"),
            new ActualDetail(target, "target " + target + " is greater than " + maximum)));
        return true;
    }

    public boolean range(JNumber target, JNumber minimum, JUndefined undefined) {
        if(target.compare(minimum) < 0) return fail(new FunctionValidationException(
            new ErrorDetail(RNGNUM03, "Target number is outside of range"),
            new ExpectedDetail(caller, "a number in range [" + minimum + ", " + undefined + "]"),
            new ActualDetail(target, "target " + target + " is less than " + minimum)));
        return true;
    }

    public boolean range(JNumber target, JUndefined undefined, JNumber maximum) {
        if(target.compare(maximum) > 0) return fail(new FunctionValidationException(
            new ErrorDetail(RNGNUM04, "Target number is outside of range"),
            new ExpectedDetail(caller, "a number in range [" + undefined + ", " + maximum + "]"),
            new ActualDetail(target, "target " + target + " is greater than " + maximum)));
        return true;
    }

    public boolean nonempty(JString target) {
        var length = target.length();
        if(length <= 0) return fail(new FunctionValidationException(
            new ErrorDetail(EMPTCF01, "Target string must not be empty"),
            new ExpectedDetail(caller, "a non-empty string"),
            new ActualDetail(target, "found empty target string")));
        return true;
    }

    public boolean nonempty(JArray target) {
        var length = target.getElements().size();
        if(length <= 0) return fail(new FunctionValidationException(
            new ErrorDetail(EMPTCF02, "Target array must not be empty"),
            new ExpectedDetail(caller, "a non-empty array"),
            new ActualDetail(target, "found empty target array")));
        return true;
    }

    public boolean nonempty(JObject target) {
        var length = target.getProperties().size();
        if(length <= 0) return fail(new FunctionValidationException(
            new ErrorDetail(EMPTCF03, "Target object must not be empty"),
            new ExpectedDetail(caller, "a non-empty object"),
            new ActualDetail(target, "found empty target object")));
        return true;
    }
}