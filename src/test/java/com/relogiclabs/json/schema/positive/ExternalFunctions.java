package com.relogiclabs.json.schema.positive;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.function.FunctionBase;
import com.relogiclabs.json.schema.function.FutureValidator;
import com.relogiclabs.json.schema.message.ActualDetail;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.message.ExpectedDetail;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.types.JBoolean;
import com.relogiclabs.json.schema.types.JInteger;
import com.relogiclabs.json.schema.types.JNumber;
import com.relogiclabs.json.schema.types.JReceiver;
import com.relogiclabs.json.schema.types.JString;

import java.util.Arrays;

import static com.relogiclabs.json.schema.internal.util.StringHelper.join;

public class ExternalFunctions extends FunctionBase {
    public static final String EVENFUNC01 = "EVENFUNC01";
    public static final String ERRACCESS01 = "ERRACCESS01";
    public static final String CONDFUNC01 = "CONDFUNC01";
    public static final String CONDFUNC02 = "CONDFUNC02";
    public static final String SUMEQUAL01 = "SUMEQUAL01";
    public static final String MINMAX01 = "MINMAX01";

    public ExternalFunctions(RuntimeContext runtime) {
        super(runtime);
    }

    public boolean even(JNumber target) {
        boolean result = (target.toDouble() % 2 == 0);
        if(!result) failWith(new JsonSchemaException(
                new ErrorDetail(EVENFUNC01, "Number is not even"),
                new ExpectedDetail(target, "even number"),
                new ActualDetail(target, "number ", target, " is odd")));
        return true;
    }

    public boolean canTest(JNumber target, JString str1, JBoolean bool1, JNumber... args) {
        System.out.println("Target: " + target);
        System.out.println("String Parameter: " + str1);
        System.out.println("Boolean Parameter: " + bool1);
        System.out.println("Params Numbers: " + join(Arrays.asList(args), ",", "[", "]"));
        return true;
    }

    public boolean checkAccess(JInteger target, JReceiver userRole) {
        String role = userRole.<JString>getValueNode().getValue();
        if(role.equals("user") && target.getValue() > 5) return failWith(new JsonSchemaException(
                new ErrorDetail(ERRACCESS01, "Data access incompatible with 'use' role"),
                new ExpectedDetail(target, "an access at most 5 for 'user' role"),
                new ActualDetail(target, "found access ", target, " which is greater than 5")));
        return true;
    }

    public boolean condition(JInteger target, JReceiver receiver) {
        long threshold = receiver.<JInteger>getValueNode().getValue();
        System.out.println("Received integer: " + threshold);
        boolean result = threshold < target.getValue();
        if(!result) return failWith(new JsonSchemaException(
                new ErrorDetail(CONDFUNC01, "Number does not satisfy the condition"),
                new ExpectedDetail(target, "a number > ", threshold, " of '", receiver.getName(), "'"),
                new ActualDetail(target, "found number ", target, " <= ", threshold)));
        return result;
    }

    public boolean conditionAll(JInteger target, JReceiver receiver) {
        var list = receiver.<JInteger>getValueNodes();
        var values = join(list, ",", "[", "]");
        System.out.println("Target: " + target);
        System.out.println("Received integers: " + values);
        boolean result = list.stream().allMatch(i -> i.getValue() < target.getValue());
        if(!result) return failWith(new JsonSchemaException(
                new ErrorDetail(CONDFUNC02, "Number does not satisfy the condition"),
                new ExpectedDetail(target, "a number > any of ", values, " of '", receiver.getName(), "'"),
                new ActualDetail(target, "found number ", target, " <= some of ", values)));
        return true;
    }

    public FutureValidator sumEqual(JInteger target, JReceiver receiver) {
        return () -> {
            var values = receiver.<JInteger>getValueNodes();
            var expression = join(values, "+");
            System.out.println("Target: " + target);
            System.out.println("Received values: " + expression);
            long result = values.stream().mapToLong(JInteger::getValue).sum();
            if(result != target.getValue())
                return failWith(new JsonSchemaException(
                        new ErrorDetail(SUMEQUAL01, "Number != sum of ", expression, " = ", result),
                        new ExpectedDetail(target, "a number = sum of numbers ", result),
                        new ActualDetail(target, "found number ", target, " != ", result)));
            return true;
        };
    }

    public FutureValidator minmax(JInteger target, JReceiver min, JReceiver max) {
        return () -> {
            var intMin = min.<JInteger>getValueNode().getValue();
            var intMax = max.<JInteger>getValueNode().getValue();
            System.out.println("Target: " + target);
            System.out.println("Received min: " + intMin + ", max: " + intMax);
            boolean result = target.getValue() >= intMin && target.getValue() <= intMax;
            if(!result) return failWith(new JsonSchemaException(
                    new ErrorDetail(MINMAX01, "Number is outside of range"),
                    new ExpectedDetail(target, "a number in range [", intMin, ", ", intMax, "]"),
                    new ActualDetail(target, "found number ", target, " not in range")));
            return true;
        };
    }
}