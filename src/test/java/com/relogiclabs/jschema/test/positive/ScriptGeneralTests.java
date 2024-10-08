package com.relogiclabs.jschema.test.positive;

import com.relogiclabs.jschema.JsonAssert;
import org.junit.jupiter.api.Test;

public class ScriptGeneralTests {
    @Test
    public void When_ConditionSatisfiedWithValueV1_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "role": #string &role,
                "dataAccess": @checkAccess(&role) #integer
            }
            %script: {
                future constraint checkAccess(role) {
                    if(role == "user" && target > 5) return fail(
                        "EX_ERRACCESS01", "Data access incompatible with 'user' role",
                        // 'caller' is the default node added automatically
                        expected("an access at most 5 for 'user' role"),
                        // 'target' is the default node added automatically
                        actual("found access " + target + " that is greater than 5"));
                }
            }
            """;
        var json =
            """
            {
                "role": "user",
                "dataAccess": 5
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ConditionSatisfiedWithValueV2_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "role": #string &role,
                "dataAccess": @checkAccess(&role) #integer
            }
            %script: {
                future checkAccess(role) {
                    if(role == "user" && target > 5) return fail(
                        "EX_ERRACCESS01", "Data access incompatible with 'user' role",
                        // Pass any node explicitly to the expected function
                        expected(caller, "an access at most 5 for 'user' role"),
                        // Pass any node explicitly to the actual function
                        actual(target, "found access " + target + " that is greater than 5"));
                }
            }
            """;
        var json =
            """
            {
                "role": "user",
                "dataAccess": 3
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ConditionSatisfiedWithValueV3_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "role": #string &role,
                "dataAccess": @checkAccess(&role) #integer
            }
            %script: {
                constraint checkAccess(role) {
                    if(role == "user" && target > 5) return fail(
                        "EX_ERRACCESS01", "Data access incompatible with 'user' role",
                        // Create an expected object explicitly without any function
                        { node: caller, message: "an access at most 5 for 'user' role" },
                        // Create an actual object explicitly without any function
                        { node: target, message: "found access " + target
                                + " that is greater than 5" });
                }
            }
            """;
        var json =
            """
            {
                "role": "user",
                "dataAccess": 2
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_SumOfValuesFromSingleReceivedArray_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "array": #array &array,
                "result": @sumTest(&array) #integer
            }
            %script: {
                future sumTest(array) {
                    // Auto-unpacking unwraps the single array received
                    var sum = 0;
                    foreach(var e in array) sum += e;
                    if(sum != target) return fail("Invalid: " + target);
                }
            }
            """;
        var json =
            """
            {
                "array": [10, 20, 30, 40, 50, 60, 70],
                "result": 280
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_SumOfReceivedMultiValuesFromReceiver_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "v1": #integer #float &values,
                "v2": #integer #float &values,
                "v3": #integer #float &values,
                "v4": #integer #float &values,
                "v5": #integer #float &values,
                "result": @sumTest(&values) #float
            }
            %script: {
                future constraint function sumTest(values) {
                    // No auto-unpacking for multiple received in '&values'
                    var sum = 0;
                    foreach(var e in values) sum += e;
                    if(sum != target) return fail("Invalid: " + target);
                }
            }
            """;
        var json =
            """
            {
                "v1": 10.5,
                "v2": 16,
                "v3": 90.3,
                "v4": 130.1,
                "v5": 50,
                "result": 296.9
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ArithmeticOnTargetAndReceivedNumber_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "value1": #float &value1,
                "value2": @arithmeticTest(&value1)
            }
            %script: {
                constraint arithmeticTest(number) {
                    if(target / 2 - 43.78 + 88.56 != number * 2 + 78.9 - 21.07)
                        return fail("Invalid: " + target);
                }
            }
            """;
        var json =
            """
            {
                "value1": 25.4,
                "value2": 127.7
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_TargetRangeDependsOnOtherValues_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "min": #integer &min,
                "value": @checkRange(&min, &max) #integer,
                "max": #integer &max
            }
            %script: {
                future constraint checkRange(min, max) {
                    if(target < min || target > max) return fail(
                        "EX_RANGEERR01", "Target number is out of range",
                        expected("a number in range " + [min, max]),
                        actual("found " + target + " that is out of range"));
                }
            }
            """;
        var json =
            """
            {
                "min": 100,
                "value": 180,
                "max": 200
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_FindByIterationOnObjectProperties_ValidTrue() {
        var schema =
            """
            %schema:
            {
                "key1": #string,
                "key2": #string,
                "key3": #string
            } @checkObject

            %script: {
                constraint checkObject() {
                    if(target.type() != "#object") return fail("Invalid: " + target);
                    var hasKey = false;
                    foreach(var k in target) {
                        if(k == "key2") hasKey = true;
                        //print("Value of", k, ":", target[k]);
                    }
                    if(!hasKey) return fail("Invalid: " + target);
                    print("key2: " + target.key2);
                }
            }
            """;
        var json =
            """
            {
                "key1": "value1",
                "key2": "value2",
                "key3": "value3"
            }
            """;
        JsonAssert.isValid(schema, json);
    }
}