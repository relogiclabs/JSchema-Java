package com.relogiclabs.json.schema.function;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.message.ActualDetail;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.message.ExpectedDetail;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.types.JArray;
import com.relogiclabs.json.schema.types.JInteger;
import com.relogiclabs.json.schema.types.JObject;
import com.relogiclabs.json.schema.types.JString;
import com.relogiclabs.json.schema.types.JUndefined;

import static com.relogiclabs.json.schema.message.ErrorCode.ALEN01;
import static com.relogiclabs.json.schema.message.ErrorCode.ALEN02;
import static com.relogiclabs.json.schema.message.ErrorCode.ALEN03;
import static com.relogiclabs.json.schema.message.ErrorCode.ALEN04;
import static com.relogiclabs.json.schema.message.ErrorCode.ALEN05;
import static com.relogiclabs.json.schema.message.ErrorCode.OLEN01;
import static com.relogiclabs.json.schema.message.ErrorCode.OLEN02;
import static com.relogiclabs.json.schema.message.ErrorCode.OLEN03;
import static com.relogiclabs.json.schema.message.ErrorCode.OLEN04;
import static com.relogiclabs.json.schema.message.ErrorCode.OLEN05;
import static com.relogiclabs.json.schema.message.ErrorCode.SLEN01;
import static com.relogiclabs.json.schema.message.ErrorCode.SLEN02;
import static com.relogiclabs.json.schema.message.ErrorCode.SLEN03;
import static com.relogiclabs.json.schema.message.ErrorCode.SLEN04;
import static com.relogiclabs.json.schema.message.ErrorCode.SLEN05;

public class CoreFunctions1 extends FunctionBase {
    public CoreFunctions1(RuntimeContext runtime) {
        super(runtime);
    }

    public boolean length(JString target, JInteger length) {
        var rLength = target.getValue().length();
        if(rLength != length.getValue()) return failWith(new JsonSchemaException(
                new ErrorDetail(SLEN01, "Invalid length of string ", target),
                new ExpectedDetail(function, "a string of length ", length),
                new ActualDetail(target, "found ", rLength, " which does not match")));
        return true;
    }

    public boolean length(JArray target, JInteger length) {
        var rLength = target.getElements().size();
        if(rLength != length.getValue()) return failWith(new JsonSchemaException(
                new ErrorDetail(ALEN01, "Invalid length of array ", target.getOutline()),
                new ExpectedDetail(function, "an array of length ", length),
                new ActualDetail(target, "found ", rLength, " which does not match")));
        return true;
    }

    public boolean length(JObject target, JInteger length) {
        var rLength = target.getProperties().size();
        if(rLength != length.getValue()) return failWith(new JsonSchemaException(
                new ErrorDetail(OLEN01, "Invalid size or length of object ", target.getOutline()),
                new ExpectedDetail(function, "an object of length ", length),
                new ActualDetail(target, "found ", rLength, " which does not match")));
        return true;
    }

    public boolean length(JString target, JInteger minimum, JInteger maximum) {
        var length = target.getValue().length();
        if(length < minimum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(SLEN02,
                    "String ", target.getOutline(), " length is outside of range"),
                    new ExpectedDetail(function, "length in range [", minimum, ", ", maximum, "]"),
                    new ActualDetail(target, "found ", length, " that is less than ", minimum)));
        if(length > maximum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(SLEN03,
                    "String ", target.getOutline(), " length is outside of range"),
                    new ExpectedDetail(function, "length in range [", minimum, ", ", maximum, "]"),
                    new ActualDetail(target, "found ", length, " that is greater than ", maximum)));
        return true;
    }

    public boolean length(JString target, JInteger minimum, JUndefined undefined) {
        var length = target.getValue().length();
        if(length < minimum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(SLEN04,
                    "String ", target.getOutline(), " length is outside of range"),
                    new ExpectedDetail(function, "length in range [", minimum, ", ", undefined, "]"),
                    new ActualDetail(target, "found ", length, " that is less than ", minimum)));
        return true;
    }

    public boolean length(JString target, JUndefined undefined, JInteger maximum) {
        var length = target.getValue().length();
        if(length > maximum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(SLEN05,
                    "String ", target.getOutline(), " length is outside of range"),
                    new ExpectedDetail(function, "length in range [", undefined, ", ", maximum, "]"),
                    new ActualDetail(target, "found ", length, " that is greater than ", maximum)));
        return true;
    }

    public boolean length(JArray target, JInteger minimum, JInteger maximum) {
        var length = target.getElements().size();
        if(length < minimum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(ALEN02,
                    "Array ", target.getOutline(), " length is outside of range"),
                    new ExpectedDetail(function, "length in range [", minimum, ", ", maximum, "]"),
                    new ActualDetail(target, "found ", length, " that is less than ", minimum)));
        if(length > maximum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(ALEN03,
                    "Array ", target.getOutline(), " length is outside of range"),
                    new ExpectedDetail(function, "length in range [", minimum, ", ", maximum, "]"),
                    new ActualDetail(target, "found ", length, " that is greater than ", maximum)));
        return true;
    }

    public boolean length(JArray target, JInteger minimum, JUndefined undefined) {
        var length = target.getElements().size();
        if(length < minimum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(ALEN04,
                    "Array ", target.getOutline(), " length is outside of range"),
                    new ExpectedDetail(function, "length in range [", minimum, ", ", undefined, "]"),
                    new ActualDetail(target, "found ", length, " that is less than ", minimum)));
        return true;
    }

    public boolean length(JArray target, JUndefined undefined, JInteger maximum) {
        var length = target.getElements().size();
        if(length > maximum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(ALEN05,
                    "Array ", target.getOutline(), " length is outside of range"),
                    new ExpectedDetail(function, "length in range [", undefined, ", ", maximum, "]"),
                    new ActualDetail(target, "found ", length, " that is greater than ", maximum)));
        return true;
    }

    public boolean length(JObject target, JInteger minimum, JInteger maximum) {
        var length = target.getProperties().size();
        if(length < minimum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(OLEN02,
                    "Object ", target.getOutline(), " size or length is outside of range"),
                    new ExpectedDetail(function, "length in range [", minimum, ", ", maximum, "]"),
                    new ActualDetail(target, "found ", length, " that is less than ", minimum)));
        if(length > maximum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(OLEN03,
                    "Object ", target.getOutline(), " size or length is outside of range"),
                    new ExpectedDetail(function, "length in range [", minimum, ", ", maximum, "]"),
                    new ActualDetail(target, "found ", length, " that is greater than ", maximum)));
        return true;
    }

    public boolean length(JObject target, JInteger minimum, JUndefined undefined) {
        var length = target.getProperties().size();
        if(length < minimum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(OLEN04,
                    "Object ", target.getOutline(), " size or length is outside of range"),
                    new ExpectedDetail(function, "length in range [", minimum, ", ", undefined, "]"),
                    new ActualDetail(target, "found ", length, " that is less than ", minimum)));
        return true;
    }

    public boolean length(JObject target, JUndefined undefined, JInteger maximum) {
        var length = target.getProperties().size();
        if(length > maximum.getValue())
            return failWith(new JsonSchemaException(new ErrorDetail(OLEN05,
                    "Object ", target.getOutline(), " size or length is outside of range"),
                    new ExpectedDetail(function, "length in range [", undefined, ", ", maximum, "]"),
                    new ActualDetail(target, "found ", length, " that is greater than ", maximum)));
        return true;
    }
}