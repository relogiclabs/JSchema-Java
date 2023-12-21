package com.relogiclabs.json.schema.function;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.message.ActualDetail;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.message.ExpectedDetail;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.type.JArray;
import com.relogiclabs.json.schema.type.JBoolean;
import com.relogiclabs.json.schema.type.JNumber;
import com.relogiclabs.json.schema.type.JObject;
import com.relogiclabs.json.schema.type.JString;
import com.relogiclabs.json.schema.type.JUndefined;

import java.util.Arrays;
import java.util.function.Supplier;

import static com.relogiclabs.json.schema.internal.util.StringHelper.joinWith;
import static com.relogiclabs.json.schema.message.ErrorCode.ENUM01;
import static com.relogiclabs.json.schema.message.ErrorCode.ENUM02;
import static com.relogiclabs.json.schema.message.ErrorCode.MAXI01;
import static com.relogiclabs.json.schema.message.ErrorCode.MAXI02;
import static com.relogiclabs.json.schema.message.ErrorCode.MAXI03;
import static com.relogiclabs.json.schema.message.ErrorCode.MINI01;
import static com.relogiclabs.json.schema.message.ErrorCode.MINI02;
import static com.relogiclabs.json.schema.message.ErrorCode.MINI03;
import static com.relogiclabs.json.schema.message.ErrorCode.NEGI01;
import static com.relogiclabs.json.schema.message.ErrorCode.NEGI02;
import static com.relogiclabs.json.schema.message.ErrorCode.NEMT01;
import static com.relogiclabs.json.schema.message.ErrorCode.NEMT02;
import static com.relogiclabs.json.schema.message.ErrorCode.NEMT03;
import static com.relogiclabs.json.schema.message.ErrorCode.POSI01;
import static com.relogiclabs.json.schema.message.ErrorCode.POSI02;
import static com.relogiclabs.json.schema.message.ErrorCode.RANG01;
import static com.relogiclabs.json.schema.message.ErrorCode.RANG02;
import static com.relogiclabs.json.schema.message.ErrorCode.RANG03;
import static com.relogiclabs.json.schema.message.ErrorCode.RANG04;

public class CoreFunctions2 extends CoreFunctions1 {
    public CoreFunctions2(RuntimeContext runtime) {
        super(runtime);
    }

    // enum is a keyword in Java but _ will be escaped
    public boolean _enum(JString target, JString... items) {
        var list = Arrays.asList(items);
        if(!list.contains(target)) return failWith(new JsonSchemaException(
                new ErrorDetail(ENUM01, "String is not in enum list"),
                new ExpectedDetail(function, "string in list ", joinWith(list, ", ", "[", "]")),
                new ActualDetail(target, "string ", target, " is not found in list")));
        return true;
    }

    public boolean _enum(JNumber target, JNumber... items) {
        var list = Arrays.asList(items);
        if(!list.contains(target)) return failWith(new JsonSchemaException(
                new ErrorDetail(ENUM02, "Number is not in enum list"),
                new ExpectedDetail(function, "number in list ", joinWith(list, ", ", "[", "]")),
                new ActualDetail(target, "number ", target, " is not found in list")));
        return true;
    }

    public boolean minimum(JNumber target, JNumber minimum) {
        if(target.compare(minimum) < 0)
            return failWith(new JsonSchemaException(
                    new ErrorDetail(MINI01, "Number is less than provided minimum"),
                    new ExpectedDetail(function, "a number greater than or equal to ", minimum),
                    new ActualDetail(target, "number ", target, " is less than ", minimum)));
        return true;
    }

    public boolean minimum(JNumber target, JNumber minimum, JBoolean exclusive) {
        Supplier<String> relationTo = () -> exclusive.getValue()
                ? "greater than"
                : "greater than or equal to";

        if(target.compare(minimum) < 0)
            return failWith(new JsonSchemaException(
                    new ErrorDetail(MINI02, "Number is less than provided minimum"),
                    new ExpectedDetail(function, "a number ", relationTo.get(), " ", minimum),
                    new ActualDetail(target, "number ", target, " is less than ", minimum)));
        if(exclusive.getValue() && target.compare(minimum) == 0)
            return failWith(new JsonSchemaException(
                    new ErrorDetail(MINI03, "Number is equal to provided minimum"),
                    new ExpectedDetail(function, "a number ", relationTo.get(), " ", minimum),
                    new ActualDetail(target, "number ", target, " is equal to ", minimum)));
        return true;
    }

    public boolean maximum(JNumber target, JNumber maximum) {
        if(target.compare(maximum) > 0)
            return failWith(new JsonSchemaException(
                    new ErrorDetail(MAXI01, "Number is greater than provided maximum"),
                    new ExpectedDetail(function, "a number less than or equal ", maximum),
                    new ActualDetail(target, "number ", target, " is greater than ", maximum)));
        return true;
    }

    public boolean maximum(JNumber target, JNumber maximum, JBoolean exclusive) {
        Supplier<String> relationTo = () -> exclusive.getValue()
                ? "less than"
                : "less than or equal to";

        if(target.compare(maximum) > 0)
            return failWith(new JsonSchemaException(
                    new ErrorDetail(MAXI02, "Number is greater than provided maximum"),
                    new ExpectedDetail(function, "a number ", relationTo.get(), " ", maximum),
                    new ActualDetail(target, "number ", target, " is greater than ", maximum)));
        if(exclusive.getValue() && target.compare(maximum) == 0)
            return failWith(new JsonSchemaException(
                    new ErrorDetail(MAXI03, "Number is equal to provided maximum"),
                    new ExpectedDetail(function, "a number ", relationTo.get(), " ", maximum),
                    new ActualDetail(target, "number ", target, " is equal to ", maximum)));
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

    public boolean positive(JNumber target, JNumber reference) {
        if(target.compare(reference) < 0) return failWith(new JsonSchemaException(
                new ErrorDetail(POSI02, "Number is not positive from reference"),
                new ExpectedDetail(function, "a positive number from ", reference),
                new ActualDetail(target, "number ", target, " is less than reference")));
        return true;
    }

    public boolean negative(JNumber target, JNumber reference) {
        if(target.compare(reference) > 0) return failWith(new JsonSchemaException(
                new ErrorDetail(NEGI02, "Number is not negative from reference"),
                new ExpectedDetail(function, "a negative number from ", reference),
                new ActualDetail(target, "number ", target, " is greater than reference")));
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