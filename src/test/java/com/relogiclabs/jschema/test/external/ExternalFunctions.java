package com.relogiclabs.jschema.test.external;

import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.function.FunctionProvider;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.internal.function.IPAddress;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JBoolean;
import com.relogiclabs.jschema.node.JInteger;
import com.relogiclabs.jschema.node.JNumber;
import com.relogiclabs.jschema.node.JReceiver;
import com.relogiclabs.jschema.node.JString;

import java.util.Arrays;

import static com.relogiclabs.jschema.internal.util.StringHelper.join;

// Functions for positive (valid) test cases
public class ExternalFunctions extends FunctionProvider {
    public static final String EX_EVENFUNC01 = "EX_EVENFUNC01";
    public static final String EX_ERRACCESS01 = "EX_ERRACCESS01";
    public static final String EX_ERRORIP01 = "EX_ERRORIP01";
    public static final String EX_ERRORIP02 = "EX_ERRORIP02";
    public static final String EX_CONDFUNC01 = "EX_CONDFUNC01";
    public static final String EX_CONDFUNC02 = "EX_CONDFUNC02";
    public static final String EX_SUMEQUAL01 = "EX_SUMEQUAL01";
    public static final String EX_MINMAX01 = "EX_MINMAX01";

    private static final String INTERNAL_IP_PREFIX = "0.";

    public boolean even(JNumber target) {
        // Precision loss is not considered here
        if(target.toDouble() % 2 == 0) return true;
        return fail(new FunctionValidationException(
            new ErrorDetail(EX_EVENFUNC01, "Target number is not even"),
            new ExpectedDetail(caller, "an even number"),
            new ActualDetail(target, "target " + target + " is odd")));
    }

    public boolean canTest(JNumber target, JString str1, JBoolean bool1, JNumber... args) {
        System.out.println("Target: " + target);
        System.out.println("String Parameter: " + str1);
        System.out.println("Boolean Parameter: " + bool1);
        System.out.println("Params Numbers: " + join(Arrays.asList(args), ",", "[", "]"));
        return true;
    }

    public boolean checkDataAccess(JInteger target, JReceiver userRole) {
        var role = userRole.<JString>getValueNode().getValue();
        if(role.equals("user") && target.getValue() > 5)
            return fail(new FunctionValidationException(
                new ErrorDetail(EX_ERRACCESS01, "Data access incompatible with 'user' role"),
                new ExpectedDetail(caller, "an access at most 5 for 'user' role"),
                new ActualDetail(target, "found access " + target + " that is greater than 5")));
        return true;
    }

    public boolean checkIPAddress(JString target) {
        if(!IPAddress.isValidIPv4(target.getValue()))
            return fail(new FunctionValidationException(
                new ErrorDetail(EX_ERRORIP01, "Target IP address invalid"),
                new ExpectedDetail(caller, "a valid IP address"),
                new ActualDetail(target, "found invalid target " + target)));
        // Might have any IP restrictions depending on requirements
        if(target.getValue().startsWith(INTERNAL_IP_PREFIX))
            return fail(new FunctionValidationException(
                new ErrorDetail(EX_ERRORIP02, "Target IP address must not be in 0.0.0.0/8"),
                new ExpectedDetail(caller, "a valid IP address not in 0.0.0.0/8"),
                new ActualDetail(target, "found invalid target " + target)));
        return true;
    }

    public boolean condition(JInteger target, JReceiver receiver) {
        var threshold = receiver.<JInteger>getValueNode().getValue();
        System.out.println("Received integer: " + threshold);
        if(threshold < target.getValue()) return true;
        return fail(new FunctionValidationException(
            new ErrorDetail(EX_CONDFUNC01, "Target number does not satisfy the condition"),
            new ExpectedDetail(caller, "a number > " + threshold + " of " + receiver),
            new ActualDetail(target, "found target " + target + " <= " + threshold)));
    }

    public boolean conditionMany(JInteger target, JReceiver receiver) {
        var list = receiver.<JInteger>getValueNodes();
        var values = join(list, ",", "[", "]");
        System.out.println("Target: " + target);
        System.out.println("Received integers: " + values);
        boolean result = list.stream().allMatch(i -> i.getValue() < target.getValue());
        if(result) return true;
        return fail(new FunctionValidationException(
            new ErrorDetail(EX_CONDFUNC02, "Target number does not satisfy the condition"),
            new ExpectedDetail(caller, "a number > any of " + values + " in " + receiver),
            new ActualDetail(target, "found target " + target + " <= some of " + values)));
    }

    public FutureFunction sumEqual(JInteger target, JReceiver receiver) {
        // Capture the current value of the caller
        var current = caller;
        return () -> {
            var values = receiver.<JInteger>getValueNodes();
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

    public FutureFunction minmax(JInteger target, JReceiver min, JReceiver max) {
        // Capture the current value of the caller
        var current = caller;
        return () -> {
            var intMin = min.<JInteger>getValueNode().getValue();
            var intMax = max.<JInteger>getValueNode().getValue();
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