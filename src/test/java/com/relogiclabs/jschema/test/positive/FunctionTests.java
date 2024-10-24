package com.relogiclabs.jschema.test.positive;

import com.relogiclabs.jschema.JsonAssert;
import org.junit.jupiter.api.Test;

public class FunctionTests {
    @Test
    public void When_ImportedFunctionExecute_ValidTrue() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.ConstraintExtension1
            %schema: @even #integer
            """;
        var json = "10";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ImportedVarArgsFunctionExecute_ValidTrue() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.ConstraintExtension1
            %schema: @canTest("test", true, 1, 2, 3) #integer
            """;
        var json = "10";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ImportedFunctionWithoutDataType_ValidTrue() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.ConstraintExtension1
            %schema: @even
            """;
        var json = "10";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ImportedFunctionInObject_ValidTrue() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.ConstraintExtension1
            %schema:
            {
                "key1": @even #integer,
                "key2": @even #integer
            }
            """;
        var json =
            """
            {
                "key1": 10,
                "key2": 12
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ImportedFunctionInArray_ValidTrue() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.ConstraintExtension1
            %schema: [
                @even #integer,
                @even #integer
            ]
            """;
        var json = "[100, 200]";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ImportedDifferentFunctionsAndMethods_ValidTrue() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.GeneralExtension2
            %schema: {
                "key1": @checkPrime #integer,
                "key2": @scriptTest1 #integer,
                "key3": @scriptTest2 #object,
                "key4": @scriptTest3("integer") #integer,
                "key5": @scriptTest3("object") #object
            }
            %script: {
                constraint scriptTest1() {
                    var max = maxNum(8, 92, 30, 78, target);
                    if(max != 110) throw("Invalid: " + target);
                    if(!target.isEvenCheck()) throw("Invalid: " + target);
                    if((target - 1).isEvenCheck()) throw("Invalid: " + target);
                }

                constraint scriptTest2() {
                    if(target.isEmptyCheck()) throw("Invalid: " + target);
                    var array = [];
                    if(!array.isEmptyCheck()) throw("Invalid: " + array);
                }

                constraint scriptTest3(name) {
                    if(target.getTypeName() != name) throw("Invalid: " + target);
                }
            }
            """;
        var json =
            """
            {
                "key1": 131,
                "key2": 110,
                "key3": {"key": 10},
                "key4": 100,
                "key5": {"key": 100}
            }
            """;
        JsonAssert.isValid(schema, json);
    }
}