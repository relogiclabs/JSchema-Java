package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScriptLiteralTests {
    @Test
    public void When_CheckUndefinedFromSchemaWithWrongJson_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "value1": @checkRange(10, !) #integer,
                "value2": @checkRange(!, 1000) #integer
            }
            %script: {
                future constraint checkRange(min, max) {
                    if(min != undefined && target < min) return fail(
                        "EX_RANGEERR01", "Target number is less than minimum",
                        expected("a number in range " + [min, max]),
                        actual("found " + target + " that is out of range"));
                    if(max != undefined && target > max) return fail(
                        "EX_RANGEERR02", "Target number is greater than maximum",
                        expected("a number in range " + [min, max]),
                        actual("found " + target + " that is out of range"));
                }
            }
            """;
        var json =
            """
            {
                "value1": 9,
                "value2": 1100
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals("EX_RANGEERR01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_CheckNullFromSchemaWithWrongJson_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "value1": @checkRange(10, null) #integer,
                "value2": @checkRange(null, 10000) #integer
            }
            %script: {
                future constraint checkRange(min, max) {
                    if(min != null && target < min) return fail(
                        "EX_RANGEERR01", "Target number is less than minimum",
                        expected("a number in range " + [min, max]),
                        actual("found " + target + " that is out of range"));
                    if(max != null && target > max) return fail(
                        "EX_RANGEERR02", "Target number is greater than maximum",
                        expected("a number in range " + [min, max]),
                        actual("found " + target + " that is out of range"));
                }
            }
            """;
        var json =
            """
            {
                "value1": 5,
                "value2": 12200
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals("EX_RANGEERR01", exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_CheckNullOrUndefinedFromSchemaWithWrongJson_ExceptionThrown() {
        var schema =
            """
            %schema:
            {
                "value1": @checkRange(200, 1000) #integer,
                "value2": @checkRange(200, 1000) #integer,
                "value3": @checkRange(20, !) #integer,
                "value4": @checkRange(null, 1000) #integer
            }
            %script: {
                future constraint checkRange(min, max) {
                    if(min && target < min) return fail(
                        "EX_RANGEERR01", "Target number is less than minimum",
                        expected("a number in range " + [min, max]),
                        actual("found " + target + " that is out of range"));
                    if(max && target > max) return fail(
                        "EX_RANGEERR02", "Target number is greater than maximum",
                        expected("a number in range " + [min, max]),
                        actual("found " + target + " that is out of range"));
                }
            }
            """;
        var json =
            """
            {
                "value1": -100,
                "value2": 1001,
                "value3": 19,
                "value4": 12202
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals("EX_RANGEERR01", exception.getCode());
        exception.printStackTrace();
    }
}