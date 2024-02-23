package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.FunctionNotFoundException;
import com.relogiclabs.jschema.exception.ScriptRuntimeException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.CALR01;
import static com.relogiclabs.jschema.message.ErrorCode.FAIL09;
import static com.relogiclabs.jschema.message.ErrorCode.FIND02;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC05;
import static com.relogiclabs.jschema.message.ErrorCode.FUNS02;
import static com.relogiclabs.jschema.message.ErrorCode.FUNS03;
import static com.relogiclabs.jschema.message.ErrorCode.FUNS04;
import static com.relogiclabs.jschema.message.ErrorCode.FUNS05;
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
        assertEquals(FUNS04, exception.getCode());
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
        assertEquals(FUNS05, exception.getCode());
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
    public void When_MultipleVariadicSubroutine_ExceptionThrown() {
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
                
                // Regardless required params only one variadic subroutine
                // can be overloaded with the same name but any number of
                // fixed params subroutine can be overload with the same name
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
        assertEquals(FUNS03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidArgumentTypeWithNativeSubroutine_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest
            }
            %script: {
                constraint funcTest() {
                    if(!regular(target)) return fail("Invalid: " + target);
                    var result = find(target, 10);
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
        assertEquals(FIND02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidArgumentValueWithNativeSubroutine_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest
            }
            %script: {
                constraint funcTest() {
                    fail("ERR01", "Test Message", { node: null }, {});
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
                "funcTest": @funcTest
            }
            %script: {
                // target is available inside subroutine when call initiated from schema
                subroutine subroutineTest() {
                    var test = target;
                    return 10;
                }
                var test = subroutineTest();
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
        assertEquals(TRGT01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ScriptInitiatedCallNotFoundCallerNode_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest
            }
            %script: {
                // caller is available inside subroutine when call initiated from schema
                subroutine subroutineTest() {
                    var test = caller;
                    return 10;
                }
                var test = subroutineTest();
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
        assertEquals(FUNS02, exception.getCode());
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
        assertEquals(FUNS02, exception.getCode());
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
        assertEquals(FUNS03, exception.getCode());
        exception.printStackTrace();
    }
}