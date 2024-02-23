package com.relogiclabs.jschema.test.positive;

import com.relogiclabs.jschema.JsonAssert;
import org.junit.jupiter.api.Test;

public class ScriptFunctionTests {
    @Test
    public void When_CheckConstraintOverloadingPrecedence_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "funcTest1": @funcTest #integer,
                "funcTest2": @funcTest(1) #integer,
                "funcTest3": @funcTest(3, 5) #integer,
                "funcTest4": @funcTest(2, 4, 6, 8) #integer
            }
            %script: {
                // Fixed parameter functions always take precedence over
                // variable parameter functions when arguments match both
                constraint funcTest(params...) {
                    if(!(target == 10 || target == 40)) return fail("Invalid: " + caller);
                    var n = size(params);
                    print("Params size: " + n);
                    if(!(n == 0 || n == 4)) return fail("Invalid: " + n);
                    foreach(var p in params) print("Received param: " + p);
                    return true;
                }
                
                constraint funcTest(param1) {
                    if(target != 20 || param1 != 1) return fail("Invalid: " + caller);
                    return true;
                }
                
                constraint funcTest(param1, param2) {
                    if(target != 30 || param1 != 3 || param2 != 5)
                        return fail("Invalid: " + caller);
                    return true;
                }
            }
            """;
        var json =
            """
            {
                "funcTest1": 10,
                "funcTest2": 20,
                "funcTest3": 30,
                "funcTest4": 40
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_SameConstraintWithSubroutineNoConflict_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest
            }
            %script: {
                constraint funcTest() {
                    return funcTest();
                }
                
                // Constraint functions are special functions and are not callable
                // from script thereby preventing any conflicts with subroutines
                // 'target' and 'caller' are available due to call initiated from schema
                subroutine funcTest() {
                    if(target != 10) return fail("Target not found");
                    if(stringify(caller) != "@funcTest") return fail("Caller not found");
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
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ConstraintOverloadingWithFixedParameters_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "funcTest1": @funcTest #integer,
                "funcTest2": @funcTest("test") #integer,
                "funcTest3": @funcTest(10, 20.5, "value") #integer
            }
            %script: {
                constraint funcTest() {
                    if(target != 10) fail("Invalid: " + target);
                    return true;
                }
                
                constraint funcTest(param1) {
                    if(target != 20) fail("Invalid: " + target);
                    return true;
                }
                
                constraint funcTest(param1, param2, param3) {
                    if(target != 30) fail("Invalid: " + target);
                    return true;
                }
            }
            """;
        var json =
            """
            {
                "funcTest1": 10,
                "funcTest2": 20,
                "funcTest3": 30
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_SubroutineVariadicCallWithVariableParameters_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "funcTest": @funcTest #integer
            }
            
            %script: {
                constraint funcTest() {
                    var r1 = funcVar();
                    if(r1 != 0) fail("Invalid: " + r1);
                    var r2 = funcVar(1, 2, 3, 4);
                    if(r2 != 4) fail("Invalid: " + r2);
                }
                
                subroutine funcVar(params...) {
                    return size(params);
                }
            }
            """;
        var json =
            """
            {
                "funcTest": 10
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_AlternativeSyntaxForDifferentFunctionTypes_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "funcTest1": @funcTest1 #integer,
                "funcTest2": @funcTest2 #integer,
                "funcTest3": @funcTest3 #integer,
                "funcTest4": @funcTest4 #integer,
                "funcTest5": @funcTest5 #integer,
                "funcTest6": @funcTest6 #integer
            }
            
            %script: {
                constraint funcTest1() {
                    print("funcTest1");
                    return funcTest7();
                }
                
                constraint function funcTest2() {
                    print("funcTest2");
                    return funcTest8();
                }
                
                future funcTest3() {
                    print("funcTest3");
                    return true;
                }
                
                future constraint funcTest4() {
                    print("funcTest4");
                    return true;
                }
                
                future function funcTest5() {
                    print("funcTest5");
                    return true;
                }
                
                future constraint function funcTest6() {
                    print("funcTest6");
                    return true;
                }
                
                subroutine funcTest7() {
                    print("funcTest7");
                    return true;
                }
                
                subroutine function funcTest8() {
                    print("funcTest8");
                    return true;
                }
            }
            """;
        var json =
            """
            {
                "funcTest1": 10,
                "funcTest2": 20,
                "funcTest3": 30,
                "funcTest4": 40,
                "funcTest5": 50,
                "funcTest6": 60
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_CheckScriptGlobalVariableState_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "funcTest1": @funcTest1 #integer,
                "funcTest2": @funcTest2 #integer
            }
            
            %script: {
                var test = 10, test2 = 5;
                constraint funcTest1() {
                    test = target;
                    test2 = 10;
                }
                
                constraint funcTest2() {
                    if(test != target - 10) return fail("Invalid: " + test);
                    if(test2 != 10) return fail("Invalid: " + test2);
                }
            }
            """;
        var json =
            """
            {
                "funcTest1": 20,
                "funcTest2": 30
            }
            """;
        JsonAssert.isValid(schema, json);
    }
}