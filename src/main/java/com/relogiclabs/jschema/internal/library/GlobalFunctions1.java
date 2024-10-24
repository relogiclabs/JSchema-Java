package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.exception.InvalidContextException;
import com.relogiclabs.jschema.extension.ScriptFunction;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.NObject;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.stringify;
import static com.relogiclabs.jschema.internal.loader.ScriptSchemaFunction.CALLER_HVAR;
import static com.relogiclabs.jschema.internal.loader.ScriptSchemaFunction.TARGET_HVAR;
import static com.relogiclabs.jschema.internal.script.DataOwner.ACTUAL_FUNCTION;
import static com.relogiclabs.jschema.internal.script.DataOwner.EXPECTED_FUNCTION;
import static com.relogiclabs.jschema.internal.script.GBoolean.FALSE;
import static com.relogiclabs.jschema.message.ErrorCode.CALRIF01;
import static com.relogiclabs.jschema.message.ErrorCode.FAILDF01;
import static com.relogiclabs.jschema.message.ErrorCode.TRGTIF01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForBoth;
import static com.relogiclabs.jschema.type.EValue.VOID;

public class GlobalFunctions1 extends AbstractFunctions {
    private static final String MESSAGE_KEY = "message";
    private static final String NODE_KEY = "node";

    @ScriptFunction
    public EValue print(EValue value) {
        System.out.println(stringify(value));
        return VOID;
    }

    @ScriptFunction
    public EValue printError(EValue value) {
        System.err.println(stringify(value));
        return VOID;
    }

    @ScriptFunction
    public EValue ticks() {
        return GInteger.from(System.currentTimeMillis());
    }

    @ScriptFunction
    public EValue fail(EString message) {
        var caller = (JNode) resolve(CALLER_HVAR);
        var target = (JNode) resolve(TARGET_HVAR);
        fail(new FunctionValidationException(formatForBoth(FAILDF01,
            message.getValue(), caller, target), caller, target));
        return FALSE;
    }

    @ScriptFunction
    public EValue fail(EString code, EString message) {
        var caller = (JNode) resolve(CALLER_HVAR);
        var target = (JNode) resolve(TARGET_HVAR);
        fail(new FunctionValidationException(formatForBoth(code.getValue(),
            message.getValue(), caller, target), caller, target));
        return FALSE;
    }

    @ScriptFunction
    public EValue fail(EString code, EString message, NObject expected, NObject actual) {
        expected.requireOwner(EXPECTED_FUNCTION);
        actual.requireOwner(ACTUAL_FUNCTION);
        var expectedNode = (JNode) expected.get(NODE_KEY);
        var actualNode = (JNode) actual.get(NODE_KEY);
        var expectedMessage = (EString) expected.get(MESSAGE_KEY);
        var actualMessage = (EString) actual.get(MESSAGE_KEY);
        fail(new FunctionValidationException(
            new ErrorDetail(code.getValue(), message.getValue()),
            new ExpectedDetail(expectedNode, expectedMessage.getValue()),
            new ActualDetail(actualNode, actualMessage.getValue())));
        return FALSE;
    }

    @ScriptFunction
    public EValue expected(EString message) {
        var result = new NObject(2, EXPECTED_FUNCTION);
        var caller = resolve(CALLER_HVAR);
        if(caller == null) throw new InvalidContextException(CALRIF01, "Caller not found");
        result.put(NODE_KEY, caller);
        result.put(MESSAGE_KEY, message);
        return result;
    }

    @ScriptFunction
    public EValue expected(JNode node, EString message) {
        var result = new NObject(2, EXPECTED_FUNCTION);
        result.put(NODE_KEY, node);
        result.put(MESSAGE_KEY, message);
        return result;
    }

    @ScriptFunction
    public EValue actual(EString message) {
        var result = new NObject(2, ACTUAL_FUNCTION);
        var target = resolve(TARGET_HVAR);
        if(target == null) throw new InvalidContextException(TRGTIF01, "Target not found");
        result.put(NODE_KEY, target);
        result.put(MESSAGE_KEY, message);
        return result;
    }

    @ScriptFunction
    public EValue actual(JNode node, EString message) {
        var result = new NObject(2, ACTUAL_FUNCTION);
        result.put(NODE_KEY, node);
        result.put(MESSAGE_KEY, message);
        return result;
    }
}