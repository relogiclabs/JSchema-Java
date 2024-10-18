package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JArray;
import com.relogiclabs.jschema.node.JNumber;
import com.relogiclabs.jschema.node.JObject;
import com.relogiclabs.jschema.node.JString;
import com.relogiclabs.jschema.type.EBoolean;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EUndefined;

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

public abstract class SchemaFunctions2 extends SchemaFunctions1 {
    @ConstraintFunction("enum")
    public boolean enum1(JString target, EString... items) {
        var list = Arrays.asList(items);
        if(!list.contains(target)) return fail(new FunctionValidationException(
            new ErrorDetail(ENMSTR01, "Target string is not in enum listing"),
            new ExpectedDetail(invoker, "a string in listing " + joinWith(list, ", ", "[", "]")),
            new ActualDetail(target, "target " + target + " is not in listing")));
        return true;
    }

    @ConstraintFunction("enum")
    public boolean enum1(JNumber target, ENumber... items) {
        var list = Arrays.asList(items);
        if(!list.contains(target)) return fail(new FunctionValidationException(
            new ErrorDetail(ENMNUM01, "Target number is not in enum listing"),
            new ExpectedDetail(invoker, "a number in listing " + joinWith(list, ", ", "[", "]")),
            new ActualDetail(target, "target " + target + " is not in listing")));
        return true;
    }

    @ConstraintFunction({"minimum", "min"})
    public boolean minimum(JNumber target, ENumber minimum) {
        if(target.compareTo(minimum) < 0) return fail(new FunctionValidationException(
            new ErrorDetail(MINICF01, "Target number must not be less than minimum"),
            new ExpectedDetail(invoker, "a number greater than or equal to " + minimum),
            new ActualDetail(target, "target " + target + " is less than " + minimum)));
        return true;
    }

    @ConstraintFunction({"minimum", "min"})
    public boolean minimum(JNumber target, ENumber minimum, EBoolean exclusive) {
        var relationTo = exclusive.getValue() ? "greater than " : "greater than or equal to ";
        var compareToMin = target.compareTo(minimum);
        if(compareToMin < 0) return fail(new FunctionValidationException(
            new ErrorDetail(MINICF02, "Target number must not be less than minimum"),
            new ExpectedDetail(invoker, "a number " + relationTo + minimum),
            new ActualDetail(target, "target " + target + " is less than " + minimum)));
        if(exclusive.getValue() && compareToMin == 0)
            return fail(new FunctionValidationException(
                new ErrorDetail(MINICF03, "Target number must be greater than minimum"),
                new ExpectedDetail(invoker, "a number " + relationTo + minimum),
                new ActualDetail(target, "target " + target + " is equal to " + minimum)));
        return true;
    }

    @ConstraintFunction({"maximum", "max"})
    public boolean maximum(JNumber target, ENumber maximum) {
        if(target.compareTo(maximum) > 0) return fail(new FunctionValidationException(
            new ErrorDetail(MAXICF01, "Target number must not be greater than maximum"),
            new ExpectedDetail(invoker, "a number less than or equal " + maximum),
            new ActualDetail(target, "target " + target + " is greater than " + maximum)));
        return true;
    }

    @ConstraintFunction({"maximum", "max"})
    public boolean maximum(JNumber target, ENumber maximum, EBoolean exclusive) {
        var relationTo = exclusive.getValue() ? "less than " : "less than or equal to ";
        var compareToMax = target.compareTo(maximum);
        if(compareToMax > 0) return fail(new FunctionValidationException(
            new ErrorDetail(MAXICF02, "Target number must not be greater than maximum"),
            new ExpectedDetail(invoker, "a number " + relationTo + maximum),
            new ActualDetail(target, "target " + target + " is greater than " + maximum)));
        if(exclusive.getValue() && compareToMax == 0)
            return fail(new FunctionValidationException(
                new ErrorDetail(MAXICF03, "Target number must be less than maximum"),
                new ExpectedDetail(invoker, "a number " + relationTo + maximum),
                new ActualDetail(target, "target " + target + " is equal to " + maximum)));
        return true;
    }

    @ConstraintFunction
    public boolean positive(JNumber target) {
        if(target.compareTo(0) <= 0) return fail(new FunctionValidationException(
            new ErrorDetail(POSICF01, "Target number must be positive"),
            new ExpectedDetail(invoker, "a positive number"),
            new ActualDetail(target, "target " + target + " is less than or equal to zero")));
        return true;
    }

    @ConstraintFunction
    public boolean negative(JNumber target) {
        if(target.compareTo(0) >= 0) return fail(new FunctionValidationException(
            new ErrorDetail(NEGICF01, "Target number must be negative"),
            new ExpectedDetail(invoker, "a negative number"),
            new ActualDetail(target, "target " + target + " is greater than or equal to zero")));
        return true;
    }

    @ConstraintFunction
    public boolean positive(JNumber target, ENumber reference) {
        if(target.compareTo(reference) < 0) return fail(new FunctionValidationException(
            new ErrorDetail(POSICF02, "Target number must be positive from reference"),
            new ExpectedDetail(invoker, "a positive number from " + reference),
            new ActualDetail(target, "target " + target + " is less than reference")));
        return true;
    }

    @ConstraintFunction
    public boolean negative(JNumber target, ENumber reference) {
        if(target.compareTo(reference) > 0) return fail(new FunctionValidationException(
            new ErrorDetail(NEGICF02, "Target number must be negative from reference"),
            new ExpectedDetail(invoker, "a negative number from " + reference),
            new ActualDetail(target, "target " + target + " is greater than reference")));
        return true;
    }

    @ConstraintFunction
    public boolean range(JNumber target, ENumber minimum, ENumber maximum) {
        if(target.compareTo(minimum) < 0) return fail(new FunctionValidationException(
            new ErrorDetail(RNGNUM01, "Target number is outside of range"),
            new ExpectedDetail(invoker, "a number in range [" + minimum + ", " + maximum + "]"),
            new ActualDetail(target, "target " + target + " is less than " + minimum)));
        if(target.compareTo(maximum) > 0) return fail(new FunctionValidationException(
            new ErrorDetail(RNGNUM02, "Target number is outside of range"),
            new ExpectedDetail(invoker, "a number in range [" + minimum + ", " + maximum + "]"),
            new ActualDetail(target, "target " + target + " is greater than " + maximum)));
        return true;
    }

    @ConstraintFunction
    public boolean range(JNumber target, ENumber minimum, EUndefined undefined) {
        if(target.compareTo(minimum) < 0) return fail(new FunctionValidationException(
            new ErrorDetail(RNGNUM03, "Target number is outside of range"),
            new ExpectedDetail(invoker, "a number in range [" + minimum + ", " + undefined + "]"),
            new ActualDetail(target, "target " + target + " is less than " + minimum)));
        return true;
    }

    @ConstraintFunction
    public boolean range(JNumber target, EUndefined undefined, ENumber maximum) {
        if(target.compareTo(maximum) > 0) return fail(new FunctionValidationException(
            new ErrorDetail(RNGNUM04, "Target number is outside of range"),
            new ExpectedDetail(invoker, "a number in range [" + undefined + ", " + maximum + "]"),
            new ActualDetail(target, "target " + target + " is greater than " + maximum)));
        return true;
    }

    @ConstraintFunction({"nonempty", "notEmpty"})
    public boolean nonempty(JString target) {
        var length = target.length();
        if(length <= 0) return fail(new FunctionValidationException(
            new ErrorDetail(EMPTCF01, "Target string must not be empty"),
            new ExpectedDetail(invoker, "a non-empty string"),
            new ActualDetail(target, "found empty target string")));
        return true;
    }

    @ConstraintFunction({"nonempty", "notEmpty"})
    public boolean nonempty(JArray target) {
        var length = target.size();
        if(length <= 0) return fail(new FunctionValidationException(
            new ErrorDetail(EMPTCF02, "Target array must not be empty"),
            new ExpectedDetail(invoker, "a non-empty array"),
            new ActualDetail(target, "found empty target array")));
        return true;
    }

    @ConstraintFunction({"nonempty", "notEmpty"})
    public boolean nonempty(JObject target) {
        var length = target.size();
        if(length <= 0) return fail(new FunctionValidationException(
            new ErrorDetail(EMPTCF03, "Target object must not be empty"),
            new ExpectedDetail(invoker, "a non-empty object"),
            new ActualDetail(target, "found empty target object")));
        return true;
    }
}