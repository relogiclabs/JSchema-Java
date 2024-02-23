package com.relogiclabs.jschema.test.external;

import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.function.FunctionProvider;
import com.relogiclabs.jschema.function.FutureFunction;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JBoolean;
import com.relogiclabs.jschema.node.JInteger;
import com.relogiclabs.jschema.node.JNumber;
import com.relogiclabs.jschema.node.JReceiver;
import com.relogiclabs.jschema.node.JString;
import com.relogiclabs.jschema.tree.RuntimeContext;

import java.util.Arrays;

import static com.relogiclabs.jschema.internal.util.StringHelper.join;

// Functions for positive (valid) test cases
public class ExternalFunctions extends FunctionProvider {
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
        if(!result) return fail(new JsonSchemaException(
                new ErrorDetail(EVENFUNC01, "Number is not even"),
                new ExpectedDetail(caller, "even number"),
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
        if(role.equals("user") && target.getValue() > 5) return fail(new JsonSchemaException(
                new ErrorDetail(ERRACCESS01, "Data access incompatible with 'user' role"),
                new ExpectedDetail(caller, "an access at most 5 for 'user' role"),
                new ActualDetail(target, "found access ", target, " which is greater than 5")));
        return true;
    }

    public boolean condition(JInteger target, JReceiver receiver) {
        long threshold = receiver.<JInteger>getValueNode().getValue();
        System.out.println("Received integer: " + threshold);
        boolean result = threshold < target.getValue();
        if(!result) return fail(new JsonSchemaException(
                new ErrorDetail(CONDFUNC01, "Number does not satisfy the condition"),
                new ExpectedDetail(caller, "a number > ", threshold, " of '", receiver.getName(), "'"),
                new ActualDetail(target, "found number ", target, " <= ", threshold)));
        return result;
    }

    public boolean conditionAll(JInteger target, JReceiver receiver) {
        var list = receiver.<JInteger>getValueNodes();
        var values = join(list, ",", "[", "]");
        System.out.println("Target: " + target);
        System.out.println("Received integers: " + values);
        boolean result = list.stream().allMatch(i -> i.getValue() < target.getValue());
        if(!result) return fail(new JsonSchemaException(
                new ErrorDetail(CONDFUNC02, "Number does not satisfy the condition"),
                new ExpectedDetail(caller, "a number > any of ", values, " of '", receiver.getName(), "'"),
                new ActualDetail(target, "found number ", target, " <= some of ", values)));
        return true;
    }

    public FutureFunction sumEqual(JInteger target, JReceiver receiver) {
        // Capture the current value of the caller
        var current = caller;
        return () -> {
            var values = receiver.<JInteger>getValueNodes();
            var expression = join(values, "+");
            System.out.println("Target: " + target);
            System.out.println("Received values: " + expression);
            long result = values.stream().mapToLong(JInteger::getValue).sum();
            if(result != target.getValue())
                return fail(new JsonSchemaException(
                        new ErrorDetail(SUMEQUAL01, "Number != sum of ", expression, " = ", result),
                        new ExpectedDetail(current, "a number = sum of numbers ", result),
                        new ActualDetail(target, "found number ", target, " != ", result)));
            return true;
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
            boolean result = target.getValue() >= intMin && target.getValue() <= intMax;
            if(!result) return fail(new JsonSchemaException(
                    new ErrorDetail(MINMAX01, "Number is outside of range"),
                    new ExpectedDetail(current, "a number in range [", intMin, ", ", intMax, "]"),
                    new ActualDetail(target, "found number ", target, " not in range")));
            return true;
        };
    }
}