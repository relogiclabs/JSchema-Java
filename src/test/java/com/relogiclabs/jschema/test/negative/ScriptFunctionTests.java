package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.DataOwnerMismatchedException;
import com.relogiclabs.jschema.exception.DuplicateFunctionException;
import com.relogiclabs.jschema.exception.DuplicateParameterException;
import com.relogiclabs.jschema.exception.FunctionArgumentTypeException;
import com.relogiclabs.jschema.exception.FunctionNotFoundException;
import com.relogiclabs.jschema.exception.InvalidContextException;
import com.relogiclabs.jschema.exception.InvalidReturnTypeException;
import com.relogiclabs.jschema.exception.InvocationRuntimeException;
import com.relogiclabs.jschema.exception.MethodArgumentTypeException;
import com.relogiclabs.jschema.exception.MethodNotFoundException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.CALRSE01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCDEF01;
import static com.relogiclabs.jschema.message.ErrorCode.FNSARG01;
import static com.relogiclabs.jschema.message.ErrorCode.FNSDUP01;
import static com.relogiclabs.jschema.message.ErrorCode.FNSNVK01;
import static com.relogiclabs.jschema.message.ErrorCode.FNSNVK02;
import static com.relogiclabs.jschema.message.ErrorCode.MTHARG01;
import static com.relogiclabs.jschema.message.ErrorCode.MTHNVK01;
import static com.relogiclabs.jschema.message.ErrorCode.OWNRMS01;
import static com.relogiclabs.jschema.message.ErrorCode.PRMDUP01;
import static com.relogiclabs.jschema.message.ErrorCode.RETNSE02;
import static com.relogiclabs.jschema.message.ErrorCode.TRGTSE01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScriptFunctionTests {
    @Test
    public void When_ConstraintFunctionNotExists_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @notExists #integer
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
        assertEquals(FNCDEF01, exception.getCode());
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
                // constraint functions are available to schema context
                // subroutine functions are not available to schema context
                constraint funcTest() {
                    subroutineNotExists(target);
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
        var exception = assertThrows(FunctionNotFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNSNVK01, exception.getCode());
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
        var exception = assertThrows(InvocationRuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNSNVK02, exception.getCode());
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
        var exception = assertThrows(InvalidReturnTypeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RETNSE02, exception.getCode());
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
        var exception = assertThrows(DuplicateFunctionException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNSDUP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_BuiltinMethodCallOnWrongType_ExceptionThrown() {
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
        var exception = assertThrows(MethodNotFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(MTHNVK01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_BuiltinSubroutineCallWithWrongType_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest
            }
            %script: {
                constraint funcTest() {
                    var result = fail(target);
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
        var exception = assertThrows(FunctionArgumentTypeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNSARG01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_BuiltinMethodCallWithWrongType_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "methodTest": @methodTest
            }
            %script: {
                constraint methodTest() {
                    var result = target.find(-10);
                }
            }
            """;
        var json =
            """
            {
                "methodTest": "test string method"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(MethodArgumentTypeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(MTHARG01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongNativeDataPassToBuiltinSubroutine_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest
            }
            %script: {
                constraint funcTest() {
                    return fail("EX_ERR01", "Test Message",
                        actual("must be from expected function"),
                        expected("must be from actual function"));
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
        var exception = assertThrows(DataOwnerMismatchedException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(OWNRMS01, exception.getCode());
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
                    var result = target;
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
        var exception = assertThrows(InvalidContextException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(TRGTSE01, exception.getCode());
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
                    var result = caller;
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
        var exception = assertThrows(InvalidContextException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(CALRSE01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DuplicateConstraintDefinitionsConflict_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest(3)
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
                "funcTest": 2
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(DuplicateFunctionException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNSDUP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DuplicateConstraintDefinitionsWithFutureConflict_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest(3)
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
                "funcTest": 2
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(DuplicateFunctionException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNSDUP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DuplicateSubroutineDefinitionsConflict_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest(3)
            }
            %script: {
                constraint funcTest(param1) {
                    return funcTest(param1);
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
                "funcTest": 2
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(DuplicateFunctionException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNSDUP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_DuplicateParameterNamesConflict_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest(2, 3)
            }
            %script: {
                constraint funcTest(param1, param1) {
                    return true;
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
        var exception = assertThrows(DuplicateParameterException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(PRMDUP01, exception.getCode());
        exception.printStackTrace();
    }
}