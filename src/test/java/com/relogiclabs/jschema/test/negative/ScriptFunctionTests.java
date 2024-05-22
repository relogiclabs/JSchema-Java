package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.FunctionNotFoundException;
import com.relogiclabs.jschema.exception.ScriptRuntimeException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.CALR01;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL09;
import static com.relogiclabs.jschema.message.ErrorCode.FNVK01;
import static com.relogiclabs.jschema.message.ErrorCode.FNVK02;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC05;
import static com.relogiclabs.jschema.message.ErrorCode.FUND01;
import static com.relogiclabs.jschema.message.ErrorCode.FUND02;
import static com.relogiclabs.jschema.message.ErrorCode.MNVK01;
import static com.relogiclabs.jschema.message.ErrorCode.RETN01;
import static com.relogiclabs.jschema.message.ErrorCode.TRGT01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScriptFunctionTests {
    @Test
    public void When_ConstraintFunctionNotExists_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest #integer
            }
            """;
        var json =
            """
            {
                "funcTest": 2
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionNotFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FUNC05, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_SubroutineFunctionNotExists_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest #integer
            }

            %script: {
                constraint funcTest() {
                    subroutineFunction(target);
                }
            }
            """;
        var json =
            """
            {
                "funcTest": 2
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNVK01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_SubroutineVariadicCallWithoutRequiredArguments_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest #integer
            }

            %script: {
                constraint funcTest() {
                    testFunction(10);
                }

                subroutine function testFunction(p1, p2, p3...) {
                    return false;
                }
            }
            """;
        var json =
            """
            {
                "funcTest": 2
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNVK02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidReturnTypeOfConstraint_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest #integer
            }
            %script: {
                // constraint functions are available to schema context
                // subroutine functions are not available to schema context
                constraint function funcTest() {
                    return 10;
                }
            }
            """;
        var json =
            """
            {
                "funcTest": 2
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RETN01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_MultipleVariadicSubroutineWithSameName_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest #integer
            }
            %script: {
                constraint funcTest() {
                    return true;
                }

                // Currently regardless of required params only one variadic
                // subroutine can be overloaded with the same name but any number
                // of fixed params subroutine can be overload with the same name
                subroutine testFunction(p1, p2, p3...) {
                    return false;
                }

                subroutine testFunction(p1, p2...) {
                    return false;
                }
            }
            """;
        var json =
            """
            {
                "funcTest": 2
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FUND02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidBuiltinMethodCallWithWrongType_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest
            }
            %script: {
                constraint funcTest() {
                    // type can be checked using the type method
                    if(!target) return fail("Invalid: " + target);
                    var result = target.find(10);
                }
            }
            """;
        var json =
            """
            {
                "funcTest": 2
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(MNVK01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidArgumentValueWithBuiltinSubroutine_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest
            }
            %script: {
                constraint funcTest() {
                    return fail("ERR01", "Test Message", { node: null }, {});
                }
            }
            """;
        var json =
            """
            {
                "funcTest": 2
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FAIL09, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ScriptInitiatedCallNotFoundTargetNode_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "targetTest": #integer
            }
            %script: {
                // target is only available inside subroutine from call stack
                // when call initiated from schema but not from script
                subroutine targetTest() {
                    var test = target;
                    return 5;
                }
                var test = targetTest();
            }
            """;
        var json =
            """
            {
                "targetTest": 2
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(TRGT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ScriptInitiatedCallNotFoundCallerNode_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "callerTest": #integer
            }
            %script: {
                // caller is only available inside subroutine from call stack
                // when call initiated from schema but not from script
                subroutine callerTest() {
                    var test = caller;
                    return 5;
                }
                var test = callerTest();
            }
            """;
        var json =
            """
            {
                "callerTest": 2
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(CALR01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DuplicateConstraintDefinitionsConflict_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest(5)
            }
            %script: {
                constraint funcTest(param1) {
                    return true;
                }

                constraint funcTest(param1) {
                    return true;
                }
            }
            """;
        var json =
            """
            {
                "funcTest": 10
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FUND01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DuplicateConstraintDefinitionsWithFutureConflict_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest(5)
            }
            %script: {
                constraint funcTest(param1) {
                    return true;
                }

                // future functions are also constraint functions
                future constraint funcTest(param1) {
                    return true;
                }
            }
            """;
        var json =
            """
            {
                "funcTest": 10
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FUND01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DuplicateSubroutineDefinitionsConflict_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest(5)
            }
            %script: {
                constraint funcTest(param1) {
                    return true;
                }

                // Constraint functions are special functions and are not callable
                // from script thus preventing any conflicts with subroutines
                subroutine funcTest(param1) {
                    return true;
                }

                subroutine funcTest(param1) {
                    return true;
                }
            }
            """;
        var json =
            """
            {
                "funcTest": 10
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FUND02, exception.getCode());
        exception.printStackTrace();
    }
}