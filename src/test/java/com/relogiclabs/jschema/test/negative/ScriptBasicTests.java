package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.DuplicateVariableException;
import com.relogiclabs.jschema.exception.ScriptIteratorException;
import com.relogiclabs.jschema.exception.SystemOperationException;
import com.relogiclabs.jschema.exception.VariableNotFoundException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.ITERSE01;
import static com.relogiclabs.jschema.message.ErrorCode.OPDIVD02;
import static com.relogiclabs.jschema.message.ErrorCode.VARDUP01;
import static com.relogiclabs.jschema.message.ErrorCode.VARRES01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScriptBasicTests {
    @Test
    public void When_DuplicateVariableDeclaration_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "variableTest": @variableTest #integer
            }
            %script: {
                constraint variableTest() {
                    var test = 10;
                    if(true) {
                        // Variable shadowing in inner scope
                        var test = 30;
                        if(test != 30) throw("Invalid: " + test);
                    }
                    if(test != 10) throw("Invalid: " + test);
                    // Variable 'test' already defined in this scope
                    var test = 20;
                }
            }
            """;
        var json =
            """
            {
                "variableTest": 2
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DuplicateVariableException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(VARDUP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_VariableDeclarationNotFound_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "variableTest": @variableTest #integer
            }
            %script: {
                constraint variableTest() {
                    var test = 10;
                    print(test2); //test2 is not declared
                }
            }
            """;
        var json =
            """
            {
                "variableTest": 2
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(VariableNotFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(VARRES01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ForeachWithNonIterableValue_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "iteratorTest": @iteratorTest #integer
            }
            %script: {
                constraint iteratorTest() {
                    foreach(var i in target) print(i);
                }
            }
            """;
        var json =
            """
            {
                "iteratorTest": 2
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptIteratorException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ITERSE01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongIndexAndRangeOutOfBounds_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "stringTest": @stringTest #string,
                "arrayTest": @arrayTest #array
            }
            %script: {
                constraint stringTest() {
                    var result1 = tryof(target[100]);
                    if(!result1.error.find("[STRIDX01]")) throw("Invalid: " + target);
                    var result2 = tryof(target[-1]);
                    if(!result2.error.find("[STRIDX02]")) throw("Invalid: " + target);
                    var result3 = tryof(target[100..]);
                    if(!result3.error.find("[STRRNG01]")) throw("Invalid: " + target);
                    var result4 = tryof(target[..100]);
                    if(!result4.error.find("[STRRNG02]")) throw("Invalid: " + target);
                    var result5 = tryof(target[8..6]);
                    if(!result5.error.find("[STRRNG03]")) throw("Invalid: " + target);
                }

                constraint arrayTest() {
                    var result1 = tryof(target[10]);
                    if(!result1.error.find("[ARRIDX01]")) throw("Invalid: " + target);
                    var result2 = tryof(target[-1]);
                    if(!result2.error.find("[ARRIDX02]")) throw("Invalid: " + target);
                    var result3 = tryof(target[10..]);
                    if(!result3.error.find("[ARRRNG01]")) throw("Invalid: " + target);
                    var result4 = tryof(target[..10]);
                    if(!result4.error.find("[ARRRNG02]")) throw("Invalid: " + target);
                    var result5 = tryof(target[-2..-4]);
                    if(!result5.error.find("[ARRRNG03]")) throw("Invalid: " + target);
                    var array = [0, 1];
                    // only assign at the end of array to add
                    // use fill method for array of specific size
                    var result6 = tryof(array[10] = 10);
                    if(!result6.error.find("[ARNSRT02]")) throw("Invalid: " + array);
                }
            }
            """;
        var json =
            """
            {
                "stringTest": "This is a test",
                "arrayTest": [1, 2, 3, 4, 5, 6]
            }
            """;
        //JsonSchema.isValid(schema, json);
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_WrongTypeForDifferentOperations_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "operationTest1": @unaryOperation #array,
                "operationTest2": @binaryOperation #object,
                "operationTest3": @comparisonOperation #string
            }
            %script: {
                constraint unaryOperation() {
                    var result1 = tryof(+target);
                    if(!result1.error.find("[OPPLUS01]")) throw("Invalid: " + target);
                    var result2 = tryof(-target);
                    if(!result2.error.find("[OPMINS01]")) throw("Invalid: " + target);
                }

                constraint binaryOperation() {
                    var result1 = tryof(target + 10);
                    if(!result1.error.find("[OPADDT01]")) throw("Invalid: " + target);
                    var result2 = tryof(target - 10);
                    if(!result2.error.find("[OPSUBT01]")) throw("Invalid: " + target);
                    var result3 = tryof(target * 10);
                    if(!result3.error.find("[OPMULT01]")) throw("Invalid: " + target);
                    var result4 = tryof(target / 10);
                    if(!result4.error.find("[OPDIVD01]")) throw("Invalid: " + target);
                    var result5 = tryof(target % 10);
                    if(!result5.error.find("[OPMODU01]")) throw("Invalid: " + target);
                }

                constraint comparisonOperation() {
                    var result1 = tryof(target > 10);
                    if(!result1.error.find("[CMPSGT01]")) throw("Invalid: " + target);
                    var result2 = tryof(target >= 10);
                    if(!result2.error.find("[CMPSGE01]")) throw("Invalid: " + target);
                    var result3 = tryof(target < 10);
                    if(!result3.error.find("[CMPSLT01]")) throw("Invalid: " + target);
                    var result4 = tryof(target <= 10);
                    if(!result4.error.find("[CMPSLE01]")) throw("Invalid: " + target);
                }
            }
            """;
        var json =
            """
            {
                "operationTest1": [10, 20],
                "operationTest2": {"k1": 10},
                "operationTest3": "test"
            }
            """;
        //JsonSchema.isValid(schema, json);
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_WrongLeftValueForIncrementDecrement_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "incDecTest1": @incDecTest #object,
                "incDecTest2": @incDecTest #array
            }
            %script: {
                constraint incDecTest() {
                    var t = target;
                    var result1 = tryof(t++);
                    if(!result1.error.find("[INCPST02]")) throw("Invalid: " + target);
                    var result2 = tryof(++t);
                    if(!result2.error.find("[INCPRE02]")) throw("Invalid: " + target);
                    var result3 = tryof(t--);
                    if(!result3.error.find("[DECPST02]")) throw("Invalid: " + target);
                    var result4 = tryof(--t);
                    if(!result4.error.find("[DECPRE02]")) throw("Invalid: " + target);
                }
            }
            """;
        var json =
            """
            {
                "incDecTest1": {},
                "incDecTest2": []
            }
            """;
        //JsonSchema.isValid(schema, json);
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ReadonlyLeftValueForUpdateOperations_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "lvalueTest1": @lvalueTest1 #array,
                "lvalueTest2": @lvalueTest2 #object,
                "lvalueTest3": @lvalueTest3 #string
            }
            %script: {
                constraint lvalueTest1() {
                    var result1 = tryof(target[2] = 10);
                    if(!result1.error.find("[ROARRY01]")) throw("Invalid: " + target);
                    var result2 = tryof(target[0] = 10);
                    if(!result2.error.find("[ROARRY01]")) throw("Invalid: " + target);
                }

                constraint lvalueTest2() {
                    var result1 = tryof(target.test = 10);
                    if(!result1.error.find("[ROOBJT01]")) throw("Invalid: " + target);
                    var result2 = tryof(target.k1 = 10);
                    if(!result2.error.find("[ROOBJT01]")) throw("Invalid: " + target);
                }

                constraint lvalueTest3() {
                    // String values are designed to be immutable
                    var result1 = tryof(target[1] = "f");
                    if(!result1.error.find("[STRASN01]")) throw("Invalid: " + target);
                    var result2 = tryof(target[11] = "f");
                    if(!result2.error.find("[STRASN01]")) throw("Invalid: " + target);
                }
            }
            """;
        var json =
            """
            {
                "lvalueTest1": [100, 200],
                "lvalueTest2": {"k1": 100},
                "lvalueTest3": "test string"
            }
            """;
        //JsonSchema.isValid(schema, json);
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_WrongLeftValueForDifferentOperations_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "lvalueTest1": @lvalueTest1 #array,
                "lvalueTest2": @lvalueTest2 #object,
                "lvalueTest3": @lvalueTest3 #integer,
                "lvalueTest4": @lvalueTest4 #any
            }
            %script: {
                constraint lvalueTest1() {
                    var result1 = tryof(target.test);
                    if(!result1.error.find("[OPPRTY01]")) throw("Invalid: " + target);
                    var result2 = tryof(target["test"]);
                    if(!result2.error.find("[REDBKT01]")) throw("Invalid: " + target);
                }

                constraint lvalueTest2() {
                    var result1 = tryof(target[0]);
                    if(!result1.error.find("[REDIDX01]")) throw("Invalid: " + target);
                    var result2 = tryof(target[0..]);
                    if(!result2.error.find("[REDRNG01]")) throw("Invalid: " + target);
                }

                constraint lvalueTest3() {
                    var result1 = tryof(target.test);
                    if(!result1.error.find("[OPPRTY01]")) throw("Invalid: " + target);
                    var result2 = tryof(target[0]);
                    if(!result2.error.find("[REDIDX01]")) throw("Invalid: " + target);
                    var result3 = tryof(target[0..]);
                    if(!result3.error.find("[REDRNG01]")) throw("Invalid: " + target);
                    var result4 = tryof(target[0] = 0);
                    if(!result4.error.find("[IDXASN01]")) throw("Invalid: " + target);
                    var result5 = tryof(target[0..] = 10);
                    if(!result5.error.find("[RNGASN01]")) throw("Invalid: " + target);
                }

                constraint lvalueTest4() {
                    var string = "Strings are designed to be immutable";
                    var result1 = tryof(string[1] = "f");
                    if(!result1.error.find("[STRASN01]")) throw("Invalid: " + string);
                    var array = [10, 20, 30, 40];
                    // an array range update is not supported
                    var result2 = tryof(array[1..3]++);
                    if(!result2.error.find("[ARRUPD01]")) throw("Invalid: " + array);
                    var result3 = tryof(array[1..3] = "test");
                    if(!result3.error.find("[ARRASN01]")) throw("Invalid: " + array);
                }
            }
            """;
        var json =
            """
            {
                "lvalueTest1": [10, 20],
                "lvalueTest2": { "k1": 10 },
                "lvalueTest3": 10,
                "lvalueTest4": ""
            }
            """;
        //JsonSchema.isValid(schema, json);
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_InvalidRangeForWrongBoundaryValueType_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "testRangeSetup": @testRangeSetup #string
            }
            %script: {
                constraint testRangeSetup() {
                    var result1 = tryof(target..);
                    if(!result1.error.find("[OPRNGT01]")) throw("Invalid: " + target);
                    var result2 = tryof(5..target);
                    if(!result2.error.find("[OPRNGT03]")) throw("Invalid: " + target);
                    var result3 = tryof(..target);
                    if(!result3.error.find("[OPRNGT05]")) throw("Invalid: " + target);
                }
            }
            """;
        var json =
            """
            {
                "testRangeSetup": "This is a string"
            }
            """;
        //JsonSchema.isValid(schema, json);
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_RuntimeSystemOperationException_ExceptionThrown() {
        // Exception like OutOfMemoryError / DivideByZero etc.
        var schema =
            """
            %schema:
            {
                "exceptionTest": @exceptionTest #integer
            }
            %script: {
                constraint exceptionTest() {
                    var result = target / 0;
                }
            }
            """;
        var json =
            """
            {
                "exceptionTest": 10
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(SystemOperationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(OPDIVD02, exception.getCode());
        exception.printStackTrace();
    }
}