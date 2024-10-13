package com.relogiclabs.jschema.internal.function;

import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JArray;
import com.relogiclabs.jschema.node.JObject;
import com.relogiclabs.jschema.node.JString;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.EUndefined;

import static com.relogiclabs.jschema.message.ErrorCode.LENARR01;
import static com.relogiclabs.jschema.message.ErrorCode.LENARR02;
import static com.relogiclabs.jschema.message.ErrorCode.LENARR03;
import static com.relogiclabs.jschema.message.ErrorCode.LENARR04;
import static com.relogiclabs.jschema.message.ErrorCode.LENARR05;
import static com.relogiclabs.jschema.message.ErrorCode.LENOBJ01;
import static com.relogiclabs.jschema.message.ErrorCode.LENOBJ02;
import static com.relogiclabs.jschema.message.ErrorCode.LENOBJ03;
import static com.relogiclabs.jschema.message.ErrorCode.LENOBJ04;
import static com.relogiclabs.jschema.message.ErrorCode.LENOBJ05;
import static com.relogiclabs.jschema.message.ErrorCode.LENSTR01;
import static com.relogiclabs.jschema.message.ErrorCode.LENSTR02;
import static com.relogiclabs.jschema.message.ErrorCode.LENSTR03;
import static com.relogiclabs.jschema.message.ErrorCode.LENSTR04;
import static com.relogiclabs.jschema.message.ErrorCode.LENSTR05;

public abstract class SchemaFunctions1 extends FunctionSupport {
    @ConstraintFunction
    public boolean length(JString target, EInteger length) {
        var tLength = target.length();
        if(tLength != length.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENSTR01, "Target string length is invalid"),
            new ExpectedDetail(invoker, "a string of length " + length),
            new ActualDetail(target, "found length " + tLength + " for target "
                + getOutline(target))));
        return true;
    }

    @ConstraintFunction
    public boolean length(JArray target, EInteger length) {
        var tLength = target.size();
        if(tLength != length.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENARR01, "Target array length is invalid"),
            new ExpectedDetail(invoker, "an array of length " + length),
            new ActualDetail(target, "found length " + tLength + " for target "
                + getOutline(target))));
        return true;
    }

    @ConstraintFunction
    public boolean length(JObject target, EInteger length) {
        var tLength = target.size();
        if(tLength != length.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENOBJ01, "Target object size or length is invalid"),
            new ExpectedDetail(invoker, "an object of length " + length),
            new ActualDetail(target, "found length " + tLength + " for target "
                + getOutline(target))));
        return true;
    }

    @ConstraintFunction
    public boolean length(JString target, EInteger minimum, EInteger maximum) {
        var length = target.length();
        if(length < minimum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENSTR02, "Target string length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + minimum + ", " + maximum + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is less than " + minimum)));
        if(length > maximum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENSTR03, "Target string length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + minimum + ", " + maximum + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is greater than " + maximum)));
        return true;
    }

    @ConstraintFunction
    public boolean length(JString target, EInteger minimum, EUndefined undefined) {
        var length = target.length();
        if(length < minimum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENSTR04, "Target string length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + minimum + ", " + undefined + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is less than " + minimum)));
        return true;
    }

    @ConstraintFunction
    public boolean length(JString target, EUndefined undefined, EInteger maximum) {
        var length = target.length();
        if(length > maximum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENSTR05, "Target string length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + undefined + ", " + maximum + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is greater than " + maximum)));
        return true;
    }

    @ConstraintFunction
    public boolean length(JArray target, EInteger minimum, EInteger maximum) {
        var length = target.size();
        if(length < minimum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENARR02, "Target array length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + minimum + ", " + maximum + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is less than " + minimum)));
        if(length > maximum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENARR03, "Target array length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + minimum + ", " + maximum + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is greater than " + maximum)));
        return true;
    }

    @ConstraintFunction
    public boolean length(JArray target, EInteger minimum, EUndefined undefined) {
        var length = target.size();
        if(length < minimum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENARR04, "Target array length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + minimum + ", " + undefined + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is less than " + minimum)));
        return true;
    }

    @ConstraintFunction
    public boolean length(JArray target, EUndefined undefined, EInteger maximum) {
        var length = target.size();
        if(length > maximum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENARR05, "Target array length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + undefined + ", " + maximum + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is greater than " + maximum)));
        return true;
    }

    @ConstraintFunction
    public boolean length(JObject target, EInteger minimum, EInteger maximum) {
        var length = target.size();
        if(length < minimum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENOBJ02, "Target object size or length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + minimum + ", " + maximum + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is less than " + minimum)));
        if(length > maximum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENOBJ03, "Target object size or length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + minimum + ", " + maximum + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is greater than " + maximum)));
        return true;
    }

    @ConstraintFunction
    public boolean length(JObject target, EInteger minimum, EUndefined undefined) {
        var length = target.size();
        if(length < minimum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENOBJ04, "Target object size or length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + minimum + ", " + undefined + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is less than " + minimum)));
        return true;
    }

    @ConstraintFunction
    public boolean length(JObject target, EUndefined undefined, EInteger maximum) {
        var length = target.size();
        if(length > maximum.getValue()) return fail(new FunctionValidationException(
            new ErrorDetail(LENOBJ05, "Target object size or length is outside of range"),
            new ExpectedDetail(invoker, "a length in range [" + undefined + ", " + maximum + "]"),
            new ActualDetail(target, "length " + length + " of target " + getOutline(target)
                + " is greater than " + maximum)));
        return true;
    }
}