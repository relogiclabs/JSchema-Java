package com.relogiclabs.jschema.test.positive;

import com.relogiclabs.jschema.JsonAssert;
import org.junit.jupiter.api.Test;

public class OptionalTests {
    @Test
    public void When_OptionalInArray_ValidTrue() {
        var schema = "[#integer?]";
        var json = "[]";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_OptionalInObject_ValidTrue() {
        var schema =
            """
            {"key1":#string?}
            """;
        var json = "{}";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_OptionalWithMandatoryInArray_ValidTrue() {
        var schema = "[@range(1, 10) #number, @range(10, 100) #number?, #number?]";
        var json = "[8.5, 80]";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_OptionalWithFunctionInArray_ValidTrue() {
        var schema =
            """
            [
                @range(10, 11) #number,
                @negative #number?,
                @range(100, 500) #number?
            ]
            """;
        var json = "[10.5, -200]";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_OptionalWithFunctionsInObject_ValidTrue() {
        var schema =
            """
            {
                "key1": @positive #number,
                "key2": @length(6) #string?,
                "key3": @negative #number?
            }
            """;
        var json =
            """
            {
                "key1": 0.11,
                "key2": "value2"
            }
            """;
        JsonAssert.isValid(schema, json);
    }
}