package com.relogiclabs.jschema.test.extension;

import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.extension.ConstraintExtension;
import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JBoolean;
import com.relogiclabs.jschema.node.JInteger;
import com.relogiclabs.jschema.node.JNumber;
import com.relogiclabs.jschema.node.JReceiver;
import com.relogiclabs.jschema.node.JString;

import static com.relogiclabs.jschema.internal.util.StringHelper.join;
import static java.util.Arrays.stream;

// Functions for positive (valid) test cases
public class ConstraintExtension1 extends ConstraintExtension {
    public static final String EX_EVENFUNC01 = "EX_EVENFUNC01";
    public static final String EX_CONDFUNC01 = "EX_CONDFUNC01";
    public static final String EX_CONDFUNC02 = "EX_CONDFUNC02";
    public static final String EX_SUMEQUAL01 = "EX_SUMEQUAL01";
    public static final String EX_MINMAX01 = "EX_MINMAX01";

    @ConstraintFunction
    public boolean even(JNumber target) {
        // Precision loss is not considered here
        if(target.toDouble() % 2 == 0) return true;
        return fail(new FunctionValidationException(
            new ErrorDetail(EX_EVENFUNC01, "Target number is not even"),
            new ExpectedDetail(getInvoker(), "an even number"),
            new ActualDetail(target, "target " + target + " is odd")));
    }

    @ConstraintFunction
    public boolean canTest(JNumber target, JString str1, JBoolean bool1, JNumber... args) {
        System.out.println("Target: " + target);
        System.out.println("String Parameter: " + str1);
        System.out.println("Boolean Parameter: " + bool1);
        System.out.println("Params Numbers: " + join(stream(args), ",", "[", "]"));
        return true;
    }

    @ConstraintFunction
    public boolean condition(JInteger target, JReceiver receiver) {
        var threshold = receiver.getValueNode(JInteger.class).getValue();
        System.out.println("Received integer: " + threshold);
        if(threshold < target.getValue()) return true;
        return fail(new FunctionValidationException(
            new ErrorDetail(EX_CONDFUNC01, "Target number does not satisfy the condition"),
            new ExpectedDetail(getInvoker(), "a number > " + threshold + " of " + receiver),
            new ActualDetail(target, "found target " + target + " <= " + threshold)));
    }

    @ConstraintFunction
    public boolean conditionMany(JInteger target, JReceiver receiver) {
        var list = receiver.getValueNodes(JInteger.class);
        var values = join(list, ",", "[", "]");
        System.out.println("Target: " + target);
        System.out.println("Received integers: " + values);
        boolean result = list.stream().allMatch(i -> i.getValue() < target.getValue());
        if(result) return true;
        return fail(new FunctionValidationException(
            new ErrorDetail(EX_CONDFUNC02, "Target number does not satisfy the condition"),
            new ExpectedDetail(getInvoker(), "a number > any of " + values + " in " + receiver),
            new ActualDetail(target, "found target " + target + " <= some of " + values)));
    }

    @ConstraintFunction
    public FutureFunction sumEqual(JInteger target, JReceiver receiver) {
        // Capture the current value of the invoker
        var current = getInvoker();
        return () -> {
            var values = receiver.getValueNodes(JInteger.class);
            System.out.println("Target: " + target);
            long sum = values.stream().mapToLong(JInteger::getValue).sum();
            var expression = join(values, " + ") + " = " + sum;
            System.out.println("Received values: " + expression);
            if(sum == target.getValue()) return true;
            return fail(new FunctionValidationException(
                new ErrorDetail(EX_SUMEQUAL01, "Target number != sum of " + expression),
                new ExpectedDetail(current, "a number = sum of numbers " + sum),
                new ActualDetail(target, "found target " + target + " != " + sum)));
        };
    }

    @ConstraintFunction
    public FutureFunction minmax(JInteger target, JReceiver min, JReceiver max) {
        // Capture the current value of the invoker
        var current = getInvoker();
        return () -> {
            var intMin = min.getValueNode(JInteger.class).getValue();
            var intMax = max.getValueNode(JInteger.class).getValue();
            System.out.println("Target: " + target);
            System.out.println("Received min: " + intMin + ", max: " + intMax);
            if(target.getValue() >= intMin && target.getValue() <= intMax) return true;
            return fail(new FunctionValidationException(
                new ErrorDetail(EX_MINMAX01, "Target number is outside of range"),
                new ExpectedDetail(current, "a number in range [" + intMin + ", " + intMax + "]"),
                new ActualDetail(target, "found target " + target + " not in range")));
        };
    }
}