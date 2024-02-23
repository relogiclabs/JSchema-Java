package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.exception.ScriptArgumentException;
import com.relogiclabs.jschema.exception.ScriptInitiatedException;
import com.relogiclabs.jschema.internal.engine.ScopeContext;
import com.relogiclabs.jschema.internal.engine.ScriptTreeHelper;
import com.relogiclabs.jschema.internal.script.GArray;
import com.relogiclabs.jschema.internal.script.GDouble;
import com.relogiclabs.jschema.internal.script.GInteger;
import com.relogiclabs.jschema.internal.script.GObject;
import com.relogiclabs.jschema.internal.script.GString;
import com.relogiclabs.jschema.message.ActualDetail;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.message.ExpectedDetail;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EDouble;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.ENull;
import com.relogiclabs.jschema.type.ENumber;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EUndefined;
import com.relogiclabs.jschema.type.EValue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areEqual;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.dereference;
import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.stringify;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.ACTUAL_FN1;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.ACTUAL_FN2;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.ACTUAL_ID;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.ARGS_IDV;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.CALLER_HVAR;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.CEIL_FN1;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.CODE_ID;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.COPY_FN1;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.EXPECTED_FN1;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.EXPECTED_FN2;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.EXPECTED_ID;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.FAIL_FN1;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.FAIL_FN2;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.FAIL_FN4;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.FILL_FN2;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.FIND_FN2;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.FIND_FN3;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.FLOOR_FN1;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.FROM_ID;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.GROUP_ID;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.ITEM_ID;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.LOG_FN1;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.MESSAGE_ID;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.MOD_FN2;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.NODE_ID;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.POW_FN2;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.PRINT_FNV;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.REGULAR_FN1;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.SIZE_FN1;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.SIZE_ID;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.STRINGIFY_FN1;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.TARGET_HVAR;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.TICKS_FN0;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.TYPE_FN1;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.VALUE1_ID;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.VALUE2_ID;
import static com.relogiclabs.jschema.internal.library.ScriptConstant.VALUE_ID;
import static com.relogiclabs.jschema.internal.script.GBoolean.FALSE;
import static com.relogiclabs.jschema.internal.script.GBoolean.TRUE;
import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.ACTL01;
import static com.relogiclabs.jschema.message.ErrorCode.ACTL02;
import static com.relogiclabs.jschema.message.ErrorCode.ACTL03;
import static com.relogiclabs.jschema.message.ErrorCode.CEIL01;
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
import static com.relogiclabs.jschema.message.ErrorCode.FILL01;
import static com.relogiclabs.jschema.message.ErrorCode.FIND01;
import static com.relogiclabs.jschema.message.ErrorCode.FIND02;
import static com.relogiclabs.jschema.message.ErrorCode.FIND03;
import static com.relogiclabs.jschema.message.ErrorCode.FIND04;
import static com.relogiclabs.jschema.message.ErrorCode.FIND05;
import static com.relogiclabs.jschema.message.ErrorCode.FIND06;
import static com.relogiclabs.jschema.message.ErrorCode.FLOR01;
import static com.relogiclabs.jschema.message.ErrorCode.LOGA01;
import static com.relogiclabs.jschema.message.ErrorCode.MODU01;
import static com.relogiclabs.jschema.message.ErrorCode.MODU02;
import static com.relogiclabs.jschema.message.ErrorCode.POWR01;
import static com.relogiclabs.jschema.message.ErrorCode.POWR02;
import static com.relogiclabs.jschema.message.ErrorCode.SIZE01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static com.relogiclabs.jschema.message.OutlineFormatter.createOutline;
import static com.relogiclabs.jschema.type.EValue.VOID;

public class ScriptLibrary {
    private static final ScriptLibrary LIBRARY = new ScriptLibrary();
    private final Map<String, EValue> symbols;

    private ScriptLibrary() {
        symbols = new HashMap<>(30);
        scriptPrintFunction();
        scriptTypeFunction();
        scriptSizeFunction();
        scriptStringifyFunction();
        scriptFindFunction1();
        scriptFindFunction2();
        scriptRegularFunction();
        scriptFailFunction1();
        scriptFailFunction2();
        scriptFailFunction3();
        scriptExpectedFunction1();
        scriptExpectedFunction2();
        scriptActualFunction1();
        scriptActualFunction2();
        scriptCopyFunction();
        scriptFillFunction();
        scriptCeilFunction();
        scriptFloorFunction();
        scriptModFunction();
        scriptPowFunction();
        scriptLogFunction();
        scriptTicksFunction();
    }

    public static EValue resolveStatic(String name) {
        var value = LIBRARY.symbols.get(name);
        return value == null ? VOID : value;
    }

    private void scriptPrintFunction() {
        NHandler handler = (scope, arguments) -> {
            System.out.println(arguments.stream().map(ScriptTreeHelper::stringify)
                    .collect(Collectors.joining(" ")));
            return VOID;
        };
        var function = new NFunction(handler, MESSAGE_ID, ARGS_IDV);
        symbols.put(PRINT_FNV, function);
    }

    private void scriptTypeFunction() {
        NHandler handler = (scope, arguments)
                -> GString.of(arguments.get(0).getType().getName());
        var function = new NFunction(handler, VALUE_ID);
        symbols.put(TYPE_FN1, function);
    }

    private void scriptSizeFunction() {
        NHandler handler = (scope, arguments) -> {
            var value = arguments.get(0);
            if(value instanceof EArray a) return GInteger.of(a.size());
            if(value instanceof EObject o) return GInteger.of(o.size());
            if(value instanceof EString s) return GInteger.of(s.length());
            throw failOnInvalidArgumentType(SIZE01, VALUE_ID, value);
        };
        var function = new NFunction(handler, VALUE_ID);
        symbols.put(SIZE_FN1, function);
    }

    private void scriptStringifyFunction() {
        NHandler handler = (scope, arguments)
                -> GString.of(stringify(arguments.get(0)));
        var function = new NFunction(handler, VALUE_ID);
        symbols.put(STRINGIFY_FN1, function);
    }

    private void scriptFindFunction1() {
        NHandler handler = (scope, arguments) -> {
            var group = arguments.get(0);
            var item = arguments.get(1);
            var rt = scope.getRuntime();
            if(group instanceof EArray g) {
                var s  = g.size();
                for(int i = 0; i < s; i++) if(areEqual(g.get(i), item, rt)) return GInteger.of(i);
                return GInteger.of(-1);
            } else if(group instanceof EString g) {
                return GInteger.of(g.getValue().indexOf(getString(item, ITEM_ID, FIND01)));
            } else throw failOnInvalidArgumentType(FIND02, GROUP_ID, group);
        };
        var function = new NFunction(handler, GROUP_ID, ITEM_ID);
        symbols.put(FIND_FN2, function);
    }

    private void scriptFindFunction2() {
        NHandler handler = (scope, arguments) -> {
            var group = arguments.get(0);
            var item = arguments.get(1);
            var from = arguments.get(2);
            var rt = scope.getRuntime();
            if(group instanceof EArray g) {
                var s  = g.size();
                for(int i = (int) getInteger(from, FROM_ID, FIND03); i < s; i++)
                    if(areEqual(g.get(i), item, rt)) return GInteger.of(i);
                return GInteger.of(-1);
            } else if(group instanceof EString g) {
                return GInteger.of(g.getValue().indexOf(getString(item, ITEM_ID, FIND04),
                        (int) getInteger(from, FROM_ID, FIND05)));
            } else throw failOnInvalidArgumentType(FIND06, GROUP_ID, group);
        };
        var function = new NFunction(handler, GROUP_ID, ITEM_ID, FROM_ID);
        symbols.put(FIND_FN3, function);
    }

    private void scriptRegularFunction() {
        NHandler handler = (scope, arguments) -> {
            var value = arguments.get(0);
            if(value instanceof ENull) return FALSE;
            if(value instanceof EUndefined) return FALSE;
            if(value == VOID) return FALSE;
            return TRUE;
        };
        var function = new NFunction(handler, VALUE_ID);
        symbols.put(REGULAR_FN1, function);
    }

    private void scriptFailFunction1() {
        NHandler handler = (scope, arguments) -> {
            var caller = scope.resolve(CALLER_HVAR);
            if(caller == VOID) caller = null;
            fail(scope, new ScriptInitiatedException(formatForSchema(FAIL01,
                    getString(arguments.get(0), MESSAGE_ID, FAIL02), (JNode) caller)));
            return FALSE;
        };
        var function = new NFunction(handler, MESSAGE_ID);
        symbols.put(FAIL_FN1, function);
    }

    private void scriptFailFunction2() {
        NHandler handler = (scope, arguments) -> {
            var caller = scope.resolve(CALLER_HVAR);
            if(caller == VOID) caller = null;
            var code = getString(arguments.get(0), CODE_ID, FAIL03);
            fail(scope, new ScriptInitiatedException(formatForSchema(code,
                    getString(arguments.get(1), MESSAGE_ID, FAIL04), (JNode) caller)));
            return FALSE;
        };
        var function = new NFunction(handler, CODE_ID, MESSAGE_ID);
        symbols.put(FAIL_FN2, function);
    }

    private void scriptFailFunction3() {
        NHandler handler = (scope, arguments) -> {
            var code = getString(arguments.get(0), CODE_ID, FAIL05);
            var message = getString(arguments.get(1), MESSAGE_ID, FAIL06);
            var expected = castObject(arguments.get(2), EXPECTED_ID, FAIL07);
            var actual = castObject(arguments.get(3), ACTUAL_ID, FAIL08);
            fail(scope, new JsonSchemaException(new ErrorDetail(code, message),
                    new ExpectedDetail(getNode(expected, NODE_ID, EXPECTED_ID, FAIL09),
                            getString(expected, MESSAGE_ID, EXPECTED_ID, FAIL10)),
                    new ActualDetail(getNode(actual, NODE_ID, ACTUAL_ID, FAIL11),
                            getString(actual, MESSAGE_ID, ACTUAL_ID, FAIL12))));
            return FALSE;
        };
        var function = new NFunction(handler, CODE_ID, MESSAGE_ID, EXPECTED_ID, ACTUAL_ID);
        symbols.put(FAIL_FN4, function);
    }

    private static JNode getNode(EObject object, String key, String parameter, String code) {
        var value = dereference(object.get(key));
        if(value == VOID || !(value instanceof JNode node))
            throw failOnInvalidArgumentValue(code, parameter, object);
        return node;
    }

    private static String getString(EObject object, String key, String parameter, String code) {
        var value = dereference(object.get(key));
        if(value == VOID || !(value instanceof EString string))
            throw failOnInvalidArgumentValue(code, parameter, object);
        return string.getValue();
    }

    private void scriptExpectedFunction1() {
        NHandler handler = (scope, arguments) -> {
            var object = new GObject(2);
            object.set(NODE_ID, scope.resolve(CALLER_HVAR));
            object.set(MESSAGE_ID, castString(arguments.get(0), MESSAGE_ID, EXPC01));
            return object;
        };
        var function = new NFunction(handler, MESSAGE_ID);
        symbols.put(EXPECTED_FN1, function);
    }

    private void scriptExpectedFunction2() {
        NHandler handler = (scope, arguments) -> {
            var object = new GObject(2);
            object.set(NODE_ID, getNode(arguments.get(0), NODE_ID, EXPC02));
            object.set(MESSAGE_ID, castString(arguments.get(1), MESSAGE_ID, EXPC03));
            return object;
        };
        var function = new NFunction(handler, NODE_ID, MESSAGE_ID);
        symbols.put(EXPECTED_FN2, function);
    }

    private void scriptActualFunction1() {
        NHandler handler = (scope, arguments) -> {
            var object = new GObject(2);
            object.set(NODE_ID, scope.resolve(TARGET_HVAR));
            object.set(MESSAGE_ID, castString(arguments.get(0), MESSAGE_ID, ACTL01));
            return object;
        };
        var function = new NFunction(handler, MESSAGE_ID);
        symbols.put(ACTUAL_FN1, function);
    }

    private void scriptActualFunction2() {
        NHandler handler = (scope, arguments) -> {
            var object = new GObject(2);
            object.set(NODE_ID, getNode(arguments.get(0), NODE_ID, ACTL02));
            object.set(MESSAGE_ID, castString(arguments.get(1), MESSAGE_ID, ACTL03));
            return object;
        };
        var function = new NFunction(handler, NODE_ID, MESSAGE_ID);
        symbols.put(ACTUAL_FN2, function);
    }

    private void scriptCopyFunction() {
        NHandler handler = (scope, arguments) -> {
            var value = arguments.get(0);
            if(value instanceof EArray a) return new GArray(a.elements());
            if(value instanceof EObject o) return new GObject(o);
            if(value instanceof EString s) return GString.of(s.getValue());
            if(value instanceof EInteger i) return GInteger.of(i.getValue());
            if(value instanceof EDouble d) return GDouble.of(d.getValue());
            return value;
        };
        var function = new NFunction(handler, VALUE_ID);
        symbols.put(COPY_FN1, function);
    }

    private void scriptFillFunction() {
        NHandler handler = (scope, arguments) -> GArray.filledFrom(
                arguments.get(0), (int) getInteger(arguments.get(1), SIZE_ID, FILL01));
        var function = new NFunction(handler, VALUE_ID, SIZE_ID);
        symbols.put(FILL_FN2, function);
    }

    private void scriptCeilFunction() {
        NHandler handler = (scope, arguments) -> GInteger.of((long) Math.ceil(
                getNumber(arguments.get(0), VALUE_ID, CEIL01)));
        var function = new NFunction(handler, VALUE_ID);
        symbols.put(CEIL_FN1, function);
    }

    private void scriptFloorFunction() {
        NHandler handler = (scope, arguments) -> GInteger.of((long) Math.floor(
                getNumber(arguments.get(0), VALUE_ID, FLOR01)));
        var function = new NFunction(handler, VALUE_ID);
        symbols.put(FLOOR_FN1, function);
    }

    private void scriptModFunction() {
        NHandler handler = (scope, arguments) -> {
            var val1 = arguments.get(0);
            var val2 = arguments.get(1);
            var num1 = getNumber(val1, VALUE1_ID, MODU01);
            var num2 = getNumber(val2, VALUE2_ID, MODU02);
            if(val1 instanceof EInteger i1 && val2 instanceof EInteger i2)
                return GInteger.of(i1.getValue() % i2.getValue());
            return GDouble.of(num1 % num2);
        };
        var function = new NFunction(handler, VALUE1_ID, VALUE2_ID);
        symbols.put(MOD_FN2, function);
    }

    private void scriptPowFunction() {
        NHandler handler = (scope, arguments) -> GDouble.of(Math.pow(
                getNumber(arguments.get(0), VALUE1_ID, POWR01),
                getNumber(arguments.get(1), VALUE2_ID, POWR02)
        ));
        var function = new NFunction(handler, VALUE1_ID, VALUE2_ID);
        symbols.put(POW_FN2, function);
    }

    private void scriptLogFunction() {
        NHandler handler = (scope, arguments) -> GDouble.of(Math.log(
                getNumber(arguments.get(0), VALUE_ID, LOGA01)));
        var function = new NFunction(handler, VALUE_ID);
        symbols.put(LOG_FN1, function);
    }

    private void scriptTicksFunction() {
        NHandler handler = (scope, arguments) -> GInteger.of(System.nanoTime());
        var function = new NFunction(handler);
        symbols.put(TICKS_FN0, function);
    }

    private static long getInteger(EValue value, String parameter, String code) {
        if(!(value instanceof EInteger v)) throw failOnInvalidArgumentType(code, parameter, value);
        return v.getValue();
    }

    private static double getNumber(EValue value, String parameter, String code) {
        if(!(value instanceof ENumber v)) throw failOnInvalidArgumentType(code, parameter, value);
        return v.toDouble();
    }

    private static String getString(EValue value, String parameter, String code) {
        if(!(value instanceof EString v)) throw failOnInvalidArgumentType(code, parameter, value);
        return v.getValue();
    }

    private static JNode getNode(EValue value, String parameter, String code) {
        if(!(value instanceof JNode n)) throw failOnInvalidArgumentType(code, parameter, value);
        return n;
    }

    private static EString castString(EValue value, String parameter, String code) {
        if(!(value instanceof EString v)) throw failOnInvalidArgumentType(code, parameter, value);
        return v;
    }

    private static EObject castObject(EValue value, String parameter, String code) {
        if(!(value instanceof EObject v)) throw failOnInvalidArgumentType(code, parameter, value);
        return v;
    }

    private static ScriptArgumentException failOnInvalidArgumentType(String code,
                String parameter, EValue value) {
        return new ScriptArgumentException(code, concat("Invalid argument type ",
                value.getType(), " for parameter '", parameter, "' of function '%s'"));
    }

    private static ScriptArgumentException failOnInvalidArgumentValue(String code,
                String parameter, EValue value) {
        return new ScriptArgumentException(code, concat("Invalid argument value ",
                createOutline(value), " for parameter '", parameter,
                "' of function '%s'"));
    }

    private static void fail(ScopeContext scope, RuntimeException exception) {
        scope.getRuntime().getExceptions().fail(exception);
    }
}