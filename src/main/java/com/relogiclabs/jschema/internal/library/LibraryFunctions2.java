package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.exception.InvalidArgumentException;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GObject;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

import static com.relogiclabs.jschema.internal.library.LibraryHelper.asString;
import static com.relogiclabs.jschema.internal.library.LibraryHelper.cast;
import static com.relogiclabs.jschema.internal.library.LibraryHelper.castMember;
import static com.relogiclabs.jschema.internal.script.GBoolean.FALSE;
import static com.relogiclabs.jschema.internal.tree.ScriptFunction.CALLER_HVAR;
import static com.relogiclabs.jschema.internal.tree.ScriptFunction.TARGET_HVAR;
import static com.relogiclabs.jschema.internal.util.CommonHelper.nonNullFrom;
import static com.relogiclabs.jschema.message.ErrorCode.ACTLSF01;
import static com.relogiclabs.jschema.message.ErrorCode.ACTLSF02;
import static com.relogiclabs.jschema.message.ErrorCode.ACTLSF03;
import static com.relogiclabs.jschema.message.ErrorCode.EXPTSF01;
import static com.relogiclabs.jschema.message.ErrorCode.EXPTSF02;
import static com.relogiclabs.jschema.message.ErrorCode.EXPTSF03;
import static com.relogiclabs.jschema.message.ErrorCode.FAILBS01;
import static com.relogiclabs.jschema.message.ErrorCode.FAILBS02;
import static com.relogiclabs.jschema.message.ErrorCode.FAILBS03;
import static com.relogiclabs.jschema.message.ErrorCode.FAILDF01;
import static com.relogiclabs.jschema.message.ErrorCode.FAILEX01;
import static com.relogiclabs.jschema.message.ErrorCode.FAILEX02;
import static com.relogiclabs.jschema.message.ErrorCode.FAILEX03;
import static com.relogiclabs.jschema.message.ErrorCode.FAILEX04;
import static com.relogiclabs.jschema.message.ErrorCode.FAILMB01;
import static com.relogiclabs.jschema.message.ErrorCode.FAILMB02;
import static com.relogiclabs.jschema.message.ErrorCode.FAILMB03;
import static com.relogiclabs.jschema.message.ErrorCode.FAILMB04;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForBoth;
import static com.relogiclabs.jschema.type.EValue.VOID;

public abstract class LibraryFunctions2 extends LibraryFunctions1 {
    static EValue failFunction1(ScriptScope scope, List<EValue> arguments) {
        var caller = (JNode) scope.resolve(CALLER_HVAR);
        var target = (JNode) scope.resolve(TARGET_HVAR);
        String message;
        try {
            message = asString(arguments.get(0));
        } catch(InvalidArgumentException e) {
            e.setContext(FAILBS01, Message_Id);
            throw e.failWithFunctionException();
        }
        fail(scope, new FunctionValidationException(formatForBoth(FAILDF01,
            message, caller, target), caller, target));
        return FALSE;
    }

    static EValue failFunction2(ScriptScope scope, List<EValue> arguments) {
        var caller = (JNode) scope.resolve(CALLER_HVAR);
        var target = (JNode) scope.resolve(TARGET_HVAR);
        String code, message;
        try {
            code = asString(arguments.get(0));
        } catch(InvalidArgumentException e) {
            e.setContext(FAILBS02, Code_Id);
            throw e.failWithFunctionException();
        }
        try {
            message = asString(arguments.get(1));
        } catch(InvalidArgumentException e) {
            e.setContext(FAILBS03, Message_Id);
            throw e.failWithFunctionException();
        }
        fail(scope, new FunctionValidationException(formatForBoth(code,
            message, caller, target), caller, target));
        return FALSE;
    }

    static EValue failFunction4(ScriptScope scope, List<EValue> arguments) {
        EObject expected, actual;
        try {
            expected = cast(arguments.get(2), EObject.class);
        } catch(InvalidArgumentException e) {
            e.setContext(FAILEX01, Expected_Id);
            throw e.failWithFunctionException();
        }
        try {
            actual = cast(arguments.get(3), EObject.class);
        } catch(InvalidArgumentException e) {
            e.setContext(FAILEX02, Actual_Id);
            throw e.failWithFunctionException();
        }

        String code, message;
        try {
            code = asString(arguments.get(0));
        } catch(InvalidArgumentException e) {
            e.setContext(FAILEX03, Code_Id);
            throw e.failWithFunctionException();
        }
        try {
            message = asString(arguments.get(1));
        } catch(InvalidArgumentException e) {
            e.setContext(FAILEX04, Message_Id);
            throw e.failWithFunctionException();
        }

        JNode expectedNode, actualNode;
        String expectedMessage, actualMessage;
        try {
            expectedNode = castMember(expected, Node_Id, JNode.class);
        } catch(InvalidArgumentException e) {
            e.setContext(FAILMB01, Expected_Id);
            throw e.failWithFunctionException();
        }
        try {
            expectedMessage = castMember(expected, Message_Id, EString.class).getValue();
        } catch(InvalidArgumentException e) {
            e.setContext(FAILMB02, Expected_Id);
            throw e.failWithFunctionException();
        }

        try {
            actualNode = castMember(actual, Node_Id, JNode.class);
        } catch(InvalidArgumentException e) {
            e.setContext(FAILMB03, Actual_Id);
            throw e.failWithFunctionException();
        }
        try {
            actualMessage = castMember(actual, Message_Id, EString.class).getValue();
        } catch(InvalidArgumentException e) {
            e.setContext(FAILMB04, Actual_Id);
            throw e.failWithFunctionException();
        }
        fail(scope, new FunctionValidationException(
            new ErrorDetail(code, message),
            new ExpectedDetail(expectedNode, expectedMessage),
            new ActualDetail(actualNode, actualMessage)));
        return FALSE;
    }

    static EValue expectedFunction1(ScriptScope scope, List<EValue> arguments) {
        var result = new GObject(2);
        EString message;
        try {
            message = cast(arguments.get(0), EString.class);
        } catch(InvalidArgumentException e) {
            e.setContext(EXPTSF01, Message_Id);
            throw e.failWithFunctionException();
        }
        result.put(Node_Id, nonNullFrom(scope.resolve(CALLER_HVAR), VOID));
        result.put(Message_Id, message);
        return result;
    }

    static EValue expectedFunction2(ScriptScope scope, List<EValue> arguments) {
        var result = new GObject(2);
        JNode node;
        EString message;
        try {
            node = cast(arguments.get(0), JNode.class);
        } catch(InvalidArgumentException e) {
            e.setContext(EXPTSF02, Node_Id);
            throw e.failWithFunctionException();
        }
        try {
            message = cast(arguments.get(1), EString.class);
        } catch(InvalidArgumentException e) {
            e.setContext(EXPTSF03, Message_Id);
            throw e.failWithFunctionException();
        }
        result.put(Node_Id, node);
        result.put(Message_Id, message);
        return result;
    }

    static EValue actualFunction1(ScriptScope scope, List<EValue> arguments) {
        var result = new GObject(2);
        EString message;
        try {
            message = cast(arguments.get(0), EString.class);
        } catch(InvalidArgumentException e) {
            e.setContext(ACTLSF01, Message_Id);
            throw e.failWithFunctionException();
        }
        result.put(Node_Id, nonNullFrom(scope.resolve(TARGET_HVAR), VOID));
        result.put(Message_Id, message);
        return result;
    }

    static EValue actualFunction2(ScriptScope scope, List<EValue> arguments) {
        var result = new GObject(2);
        JNode node;
        EString message;
        try {
            node = cast(arguments.get(0), JNode.class);
        } catch(InvalidArgumentException e) {
            e.setContext(ACTLSF02, Node_Id);
            throw e.failWithFunctionException();
        }
        try {
            message = cast(arguments.get(1), EString.class);
        } catch(InvalidArgumentException e) {
            e.setContext(ACTLSF03, Message_Id);
            throw e.failWithFunctionException();
        }
        result.put(Node_Id, node);
        result.put(Message_Id, message);
        return result;
    }
}