package com.relogiclabs.json.schema.positive;

import com.relogiclabs.json.schema.JsonAssert;
import org.junit.jupiter.api.Test;

public class BooleanTests {

    @Test
    public void When_DataTypeBoolean_ValidTrue() {
        var schema = "#boolean";
        var json = "true";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypeBooleanInObject_ValidTrue() {
        var schema =
            """
            {
                "key1": #boolean,
                "key2": #boolean,
                "key3": #boolean
            }
            """;
        var json =
            """
            {
                "key1": true,
                "key2": false,
                "key3": true
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypeBooleanInArray_ValidTrue() {
        var schema =
            """
            [#boolean, #boolean, #boolean]
            """;
        var json =
            """
            [true, true, false]
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_NestedDataTypeBooleanInArray_ValidTrue() {
        var schema =
            """
            #boolean*
            """;
        var json =
            """
            [false, true, false]
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_NestedDataTypeBooleanInObject_ValidTrue() {
        var schema =
            """
            #boolean*
            """;
        var json =
            """
            {
                "key1": true,
                "key2": true,
                "key3": false
            }
            """;
        JsonAssert.isValid(schema, json);
    }
}