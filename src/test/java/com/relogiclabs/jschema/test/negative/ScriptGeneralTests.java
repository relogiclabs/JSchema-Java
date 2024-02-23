package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.ScriptInitiatedException;
import com.relogiclabs.jschema.exception.ScriptRuntimeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScriptGeneralTests {
    @Test
    public void When_WrongValuesConditionUnsatisfiedV1_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "role": #string &role,
                "dataAccess": @checkAccess(&role) #integer
            }
            %script: {
                future constraint checkAccess(role) {
                    if(role[0] == "user" && target > 5) return fail(
                        "ERRACCESS01", "Data access incompatible with 'user' role",
                        // 'caller' is the default node added automatically
                        expected("an access at most 5 for 'user' role"),
                        // 'target' is the default node added automatically
                        actual("found access " + target + " which is greater than 5"));
                }
            }
            """;
        var json =
            """
            {
                "role": "user",
                "dataAccess": 6
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals("ERRACCESS01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongValuesConditionUnsatisfiedV2_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "role": #string &role,
                "dataAccess": @checkAccess(&role) #integer
            }
            %script: {
                future constraint checkAccess(role) {
                    if(role[0] == "user" && target > 5) return fail(
                        "ERRACCESS01", "Data access incompatible with 'user' role",
                        // Pass any node explicitly to the expected function
                        expected(caller, "an access at most 5 for 'user' role"),
                        // Pass any node explicitly to the actual function
                        actual(target, "found access " + target + " which is greater than 5"));
                }
            }
            """;
        var json =
            """
            {
                "role": "user",
                "dataAccess": 6
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals("ERRACCESS01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongValuesConditionUnsatisfiedV3_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "role": #string &role,
                "dataAccess": @checkAccess(&role) #integer
            }
            %script: {
                constraint checkAccess(role) {
                    if(role[0] == "user" && target > 5) return fail(
                        "ERRACCESS01", "Data access incompatible with 'user' role",
                        // Create an expected object explicitly without any function
                        { node: caller, message: "an access at most 5 for 'user' role" },
                        // Create an actual object explicitly without any function
                        { node: target, message: "found access " + target
                                + " which is greater than 5" });
                }
            }
            """;
        var json =
            """
            {
                "role": "user",
                "dataAccess": 7
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals("ERRACCESS01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongValuesConditionUnsatisfiedV4_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "role": #string &role,
                "dataAccess": @checkAccess(&role) #integer
            }
            %script: {
                future constraint checkAccess(role) {
                    // Fail with simple message and a code
                    if(role[0] == "user" && target > 5) return fail(
                        "ERRACCESS01", "Data access incompatible with 'user' role");
                }
            }
            """;
        var json =
            """
            {
                "role": "user",
                "dataAccess": 6
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals("ERRACCESS01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongValuesConditionUnsatisfiedV5_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "role": #string &role,
                "dataAccess": @checkAccess(&role) #integer
            }
            %script: {
                future constraint checkAccess(role) {
                    // Fail with just a message
                    if(role[0] == "user" && target > 5) return fail(
                        "Data access incompatible with 'user' role");
                }
            }
            """;
        var json =
            """
            {
                "role": "user",
                "dataAccess": 6
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptRuntimeException.class,
                () -> JsonAssert.isValid(schema, json));
        // FAIL01 is the default code if no code provided
        assertEquals("FAIL01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ThrowExceptionFromScriptV1_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "throwTest": @throwTest #array
            }
            %script: {
                future constraint throwTest() {
                    if(type(target) != "#array") return fail("Invalid: " + target);
                    if(find(target, 45.5) < 0) return fail("Invalid: " + target);
                    throw("ERROR01", "Throw exception from script");
                }
            }
            """;
        var json =
            """
            {
                "throwTest": [10, 20, 30, 45.5]
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptInitiatedException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals("ERROR01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ThrowExceptionFromScriptV2_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "throwTest": @throwTest #array
            }
            %script: {
                future constraint throwTest() {
                    var c1 = copy(target[0]);
                    var c2 = copy(target[3]);
                    if(c1 != 10) return fail("Invalid: " + c1);
                    if(c2 != 45.5) return fail("Invalid: " + c2);
                    throw("Throw exception from script");
                }
            }
            """;
        var json =
            """
            {
                "throwTest": [10, 20, 30, 45.5]
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptInitiatedException.class,
                () -> JsonAssert.isValid(schema, json));
        // THRO01 is the default code if no code provided
        assertEquals("THRO01", exception.getCode());
        exception.printStackTrace();
    }
}