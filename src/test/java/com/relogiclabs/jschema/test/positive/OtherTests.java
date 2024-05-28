package com.relogiclabs.jschema.test.positive;

import com.relogiclabs.jschema.JsonAssert;
import org.junit.jupiter.api.Test;

public class OtherTests {
    @Test
    public void When_DataTypeFloat_ValidTrue() {
        var schema = "#float";
        var json = "2.5";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypeDouble_ValidTrue() {
        var schema = "#double";
        var json = "2.5E1";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypeNull_ValidTrue() {
        var schema = "null #null";
        var json = "null";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypeUndefined_ValidTrue() {
        var schema = "!";
        var json = "1000";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_JsonAssertNonStaticIsValidMethodUsed_ValidTrue() {
        var schema =
            """
            %version: "October 09, 2010"
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions
            %pragma IgnoreUndefinedProperties: true
            %define $element: @range(1, 100) #integer
            %schema: {
                "key1": #integer*($element) #array,
                "key2": #array,
                "key3": @even #integer
            }
            """;
        var json1 =
            """
            {
                "key1": [1, 10, 100],
                "key2": [100, 1000, [10, 10000]],
                "key3": 10
            }
            """;
        var json2 =
            """
            {
                "key1": [10, 20, 50, 90, 100],
                "key2": ["test", 1000, [10.7, 10000]],
                "key3": 102
            }
            """;
        var jsonAssert = new JsonAssert(schema);
        jsonAssert.isValid(json1);
        jsonAssert.isValid(json2);
    }
}