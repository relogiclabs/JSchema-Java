package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.exception.ScriptInitiatedException;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.engine.ScriptTreeHelper;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GObject;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;

import java.util.List;

import static com.relogiclabs.jschema.internal.script.GBoolean.FALSE;
import static com.relogiclabs.jschema.internal.tree.ScriptFunction.CALLER_HVAR;
import static com.relogiclabs.jschema.internal.tree.ScriptFunction.TARGET_HVAR;
import static com.relogiclabs.jschema.internal.util.CommonHelper.nonNullFrom;
import static com.relogiclabs.jschema.message.ErrorCode.ACTL01;
import static com.relogiclabs.jschema.message.ErrorCode.ACTL02;
import static com.relogiclabs.jschema.message.ErrorCode.ACTL03;
import static com.relogiclabs.jschema.message.ErrorCode.EXPC01;
import static com.relogiclabs.jschema.message.ErrorCode.EXPC02;
import static com.relogiclabs.jschema.message.ErrorCode.EXPC03;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL01;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL02;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL03;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL04;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL05;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL06;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL07;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL08;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL09;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL10;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL11;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL12;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static com.relogiclabs.jschema.type.EValue.VOID;

public abstract class ScriptLibrary2 extends ScriptLibrary1 {
    static EValue printFunction(ScriptScope scope, List<EValue> arguments) {
        System.out.println(ScriptTreeHelper.stringify(arguments.get(0)));
        return VOID;
    }

    static EValue failFunction1(ScriptScope scope, List<EValue> arguments) {
        var caller = scope.resolve(CALLER_HVAR);
        fail(scope, new ScriptInitiatedException(formatForSchema(FAIL01,
            toString(arguments.get(0), Message_Id, FAIL02), (JNode) caller)));
        return FALSE;
    }

    static EValue failFunction2(ScriptScope scope, List<EValue> arguments) {
        var caller = scope.resolve(CALLER_HVAR);
        fail(scope, new ScriptInitiatedException(formatForSchema(
            toString(arguments.get(0), Code_Id, FAIL03),
            toString(arguments.get(1), Message_Id, FAIL04), (JNode) caller)));
        return FALSE;
    }

    static EValue failFunction4(ScriptScope scope, List<EValue> arguments) {
        var expected = cast(arguments.get(2), EObject.class, Expected_Id, FAIL05);
        var actual = cast(arguments.get(3), EObject.class, Actual_Id, FAIL06);
        fail(scope, new JsonSchemaException(new ErrorDetail(
            toString(arguments.get(0), Code_Id, FAIL07),
            toString(arguments.get(1), Message_Id, FAIL08)),
            new ExpectedDetail(getMember(expected, Node_Id, JNode.class, Expected_Id, FAIL09),
                getMember(expected, Message_Id, EString.class, Expected_Id, FAIL10).getValue()),
            new ActualDetail(getMember(actual, Node_Id, JNode.class, Actual_Id, FAIL11),
                getMember(actual, Message_Id, EString.class, Actual_Id, FAIL12).getValue())));
        return FALSE;
    }

    static EValue expectedFunction1(ScriptScope scope, List<EValue> arguments) {
        var result = new GObject(2);
        result.put(Node_Id, nonNullFrom(scope.resolve(CALLER_HVAR), VOID));
        result.put(Message_Id, cast(arguments.get(0), EString.class, Message_Id, EXPC01));
        return result;
    }

    static EValue expectedFunction2(ScriptScope scope, List<EValue> arguments) {
        var result = new GObject(2);
        result.put(Node_Id, cast(arguments.get(0), JNode.class, Node_Id, EXPC02));
        result.put(Message_Id, cast(arguments.get(1), EString.class, Message_Id, EXPC03));
        return result;
    }

    static EValue actualFunction1(ScriptScope scope, List<EValue> arguments) {
        var result = new GObject(2);
        result.put(Node_Id, nonNullFrom(scope.resolve(TARGET_HVAR), VOID));
        result.put(Message_Id, cast(arguments.get(0), EString.class, Message_Id, ACTL01));
        return result;
    }

    static EValue actualFunction2(ScriptScope scope, List<EValue> arguments) {
        var result = new GObject(2);
        result.put(Node_Id, cast(arguments.get(0), JNode.class, Node_Id, ACTL02));
        result.put(Message_Id, cast(arguments.get(1), EString.class, Message_Id, ACTL03));
        return result;
    }

    static EValue ticksFunction(ScriptScope scope, List<EValue> arguments) {
        return GInteger.from(System.currentTimeMillis());
    }
}