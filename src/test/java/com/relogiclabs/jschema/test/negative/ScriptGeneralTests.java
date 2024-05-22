package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.exception.ScriptInitiatedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScriptGeneralTests {
    @Test
    public void When_WrongValueConditionUnsatisfiedV1_ExceptionThrown() {
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
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals("ERRACCESS01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongValueConditionUnsatisfiedV2_ExceptionThrown() {
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
                "dataAccess": 7
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals("ERRACCESS01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongValueConditionUnsatisfiedV3_ExceptionThrown() {
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
                "dataAccess": 8
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals("ERRACCESS01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongValueConditionUnsatisfiedV4_ExceptionThrown() {
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
                    if(role == "user" && target > 5) return fail(
                        "ERRACCESS01", "Data access incompatible with 'user' role");
                }
            }
            """;
        var json =
            """
            {
                "role": "user",
                "dataAccess": 9
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptInitiatedException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals("ERRACCESS01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongValueConditionUnsatisfiedV5_ExceptionThrown() {
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
                    if(role == "user" && target > 5) return fail(
                        "Data access incompatible with 'user' role");
                }
            }
            """;
        var json =
            """
            {
                "role": "user",
                "dataAccess": 11
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(ScriptInitiatedException.class,
            () -> JsonAssert.isValid(schema, json));
        // FAIL01 is the default code if no code provided
        assertEquals("FAIL01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ThrowFromScriptWithCode_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "throwTest": @throwTest #array
            }
            %script: {
                future constraint throwTest() {
                    if(target.type() != "#array") return fail("Invalid: " + target);
                    if(!target.find(45.5)) return fail("Invalid: " + target);
                    if(target[1] == 20) throw("ERROR01", "Throw test with code");
                    return fail("NOTTHRO01", "Exception not thrown");
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
    public void When_ThrowFromScriptWithoutCode_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "throwTest": @throwTest #array
            }
            %script: {
                future constraint throwTest() {
                    var c1 = target[0].copy();
                    var c2 = target[3].copy();
                    if(c1 != 10) return fail("Invalid: " + target);
                    if(c2 != 45.5) return fail("Invalid: " + target);
                    if(target[2] == 30) throw("Throw test without code");
                    return fail("NOTTHRO01", "Exception not thrown");
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