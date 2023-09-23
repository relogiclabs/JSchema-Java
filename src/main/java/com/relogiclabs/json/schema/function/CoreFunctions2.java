package com.relogiclabs.json.schema.function;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.message.ActualDetail;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.message.ExpectedDetail;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.types.JArray;
import com.relogiclabs.json.schema.types.JNumber;
import com.relogiclabs.json.schema.types.JObject;
import com.relogiclabs.json.schema.types.JString;
import com.relogiclabs.json.schema.types.JUndefined;

import java.util.Arrays;

import static com.relogiclabs.json.schema.internal.util.StringHelper.toUnitedString;
import static com.relogiclabs.json.schema.message.ErrorCode.ENUM01;
import static com.relogiclabs.json.schema.message.ErrorCode.ENUM02;
import static com.relogiclabs.json.schema.message.ErrorCode.NEGI01;
import static com.relogiclabs.json.schema.message.ErrorCode.NEMT01;
import static com.relogiclabs.json.schema.message.ErrorCode.NEMT02;
import static com.relogiclabs.json.schema.message.ErrorCode.NEMT03;
import static com.relogiclabs.json.schema.message.ErrorCode.POSI01;
import static com.relogiclabs.json.schema.message.ErrorCode.RANG01;
import static com.relogiclabs.json.schema.message.ErrorCode.RANG02;
import static com.relogiclabs.json.schema.message.ErrorCode.RANG03;
import static com.relogiclabs.json.schema.message.ErrorCode.RANG04;

public class CoreFunctions2 extends CoreFunctions1 {
    public CoreFunctions2(RuntimeContext runtime) {
        super(runtime);
    }

    public boolean _enum(JString target, JString... items) {
        var list = Arrays.asList(items);
        if(!list.contains(target)) return failWith(new JsonSchemaException(
                new ErrorDetail(ENUM01, "String is not in enum list"),
                new ExpectedDetail(function, "string in list ", toUnitedString(list, ", ", "[", "]")),
                new ActualDetail(target, "string ", target, " is not found in list")));
        return true;
    }

    public boolean _enum(JNumber target, JNumber... items) {
        var list = Arrays.asList(items);
        if(!list.contains(target)) return failWith(new JsonSchemaException(
                new ErrorDetail(ENUM02, "Number is not in enum list"),
                new ExpectedDetail(function, "number in list ", toUnitedString(list, ", ", "[", "]")),
                new ActualDetail(target, "number ", target, " is not found in list")));
        return true;
    }

    public boolean positive(JNumber target) {
        if(target.compare(0) <= 0) return failWith(new JsonSchemaException(
                new ErrorDetail(POSI01, "Number is not positive"),
                new ExpectedDetail(function, "a positive number"),
                new ActualDetail(target, "number ", target, " is less than or equal to zero")));
        return true;
    }

    public boolean negative(JNumber target) {
        if(target.compare(0) >= 0) return failWith(new JsonSchemaException(
                new ErrorDetail(NEGI01, "Number is not negative"),
                new ExpectedDetail(function, "a negative number"),
                new ActualDetail(target, "number ", target, " is greater than or equal to zero")));
        return true;
    }

    public boolean range(JNumber target, JNumber minimum, JNumber maximum) {
        if(target.compare(minimum) < 0) return failWith(new JsonSchemaException(
                new ErrorDetail(RANG01, "Number is outside of range"),
                new ExpectedDetail(function, "number in range [", minimum, ", ", maximum, "]"),
                new ActualDetail(target, "number ", target, " is less than ", minimum)));
        if(target.compare(maximum) > 0) return failWith(new JsonSchemaException(
                new ErrorDetail(RANG02, "Number is outside of range"),
                new ExpectedDetail(function, "number in range [", minimum, ", ", maximum, "]"),
                new ActualDetail(target, "number ", target, " is greater than ", maximum)));
        return true;
    }

    public boolean range(JNumber target, JNumber minimum, JUndefined undefined) {
        if(target.compare(minimum) < 0) return failWith(new JsonSchemaException(
                new ErrorDetail(RANG03, "Number is outside of range"),
                new ExpectedDetail(function, "number in range [", minimum, ", ", undefined, "]"),
                new ActualDetail(target, "number ", target, " is less than ", minimum)));
        return true;
    }

    public boolean range(JNumber target, JUndefined undefined, JNumber maximum) {
        if(target.compare(maximum) > 0) return failWith(new JsonSchemaException(
                new ErrorDetail(RANG04, "Number is outside of range"),
                new ExpectedDetail(function, "number in range [", undefined, ", ", maximum, "]"),
                new ActualDetail(target, "number ", target, " is greater than ", maximum)));
        return true;
    }

    public boolean nonempty(JString target) {
        var length = target.getValue().length();
        if(length <= 0) return failWith(new JsonSchemaException(
                new ErrorDetail(NEMT01, "String is empty"),
                new ExpectedDetail(function, "Non empty string"),
                new ActualDetail(target, "found empty string")));
        return true;
    }

    public boolean nonempty(JArray target) {
        var length = target.getElements().size();
        if(length <= 0) return failWith(new JsonSchemaException(
                new ErrorDetail(NEMT02, "Array is empty"),
                new ExpectedDetail(function, "Non empty array"),
                new ActualDetail(target, "found empty array")));
        return true;
    }

    public boolean nonempty(JObject target) {
        var length = target.getProperties().size();
        if(length <= 0) return failWith(new JsonSchemaException(
                new ErrorDetail(NEMT03, "Object is empty"),
                new ExpectedDetail(function, "Non empty object"),
                new ActualDetail(target, "found empty object")));
        return true;
    }
}