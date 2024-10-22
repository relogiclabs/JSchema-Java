package com.relogiclabs.jschema.test.extension;

import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.extension.ConstraintFunction;
import com.relogiclabs.jschema.extension.GeneralExtension;
import com.relogiclabs.jschema.function.SchemaFunctions;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JInteger;
import com.relogiclabs.jschema.node.JReceiver;
import com.relogiclabs.jschema.node.JString;

// Functions for positive (valid) test cases
public class GeneralExtension1 extends GeneralExtension {
    public static final String EX_ERRACCESS01 = "EX_ERRACCESS01";
    public static final String EX_ERRORIP01 = "EX_ERRORIP01";

    private static final String INTERNAL_IP_PREFIX = "0.";

    @ConstraintFunction
    public boolean checkDataAccess(JInteger target, JReceiver role) {
        var roleValue = role.getValueNode(JString.class).getValue();
        if(roleValue.equals("user") && target.getValue() > 5)
            return fail(new FunctionValidationException(
                new ErrorDetail(EX_ERRACCESS01, "Data access incompatible with 'user' role"),
                new ExpectedDetail(getInvoker(), "an access at most 5 for 'user' role"),
                new ActualDetail(target, "found access " + target + " that is greater than 5")));
        return true;
    }

    @ConstraintFunction
    public boolean checkIPAddress(JString target) {
        var functions = SchemaFunctions.getInstance(this);
        if(!functions.ipv4(target)) return false;
        // Below is an example of different IP restrictions
        if(target.getValue().startsWith(INTERNAL_IP_PREFIX))
            return fail(new FunctionValidationException(
                new ErrorDetail(EX_ERRORIP01, "Target IP address must not be in 0.0.0.0/8"),
                new ExpectedDetail(getInvoker(), "a valid IP address not in 0.0.0.0/8"),
                new ActualDetail(target, "found invalid target " + target)));
        return true;
    }
}