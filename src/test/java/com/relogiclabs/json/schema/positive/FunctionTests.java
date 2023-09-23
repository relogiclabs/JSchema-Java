package com.relogiclabs.json.schema.positive;

import com.relogiclabs.json.schema.JsonAssert;
import org.junit.jupiter.api.Test;

public class FunctionTests {
    @Test
    public void When_ExternalFunctionExecute_ValidTrue() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.positive.ExternalFunctions
            %schema: @even #integer
            """;
        var json = "10";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ExternalFunctionExecute2_ValidTrue() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.positive.ExternalFunctions
            %schema: @canTest("test", true, 1, 2, 3) #integer
            """;
        var json = "10";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ExternalFunctionWithoutDataType_ValidTrue() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.positive.ExternalFunctions
            %schema: @even
            """;
        var json = "10";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_ExternalFunctionInObject_ValidTrue() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.positive.ExternalFunctions
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
    public void When_ExternalFunctionInArray_ValidTrue() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.positive.ExternalFunctions
            %schema: [
                @even #integer,
                @even #integer
            ]
            """;
        var json = "[100, 200]";
        JsonAssert.isValid(schema, json);
    }
}