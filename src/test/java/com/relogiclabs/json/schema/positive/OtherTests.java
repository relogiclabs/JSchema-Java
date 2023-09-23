package com.relogiclabs.json.schema.positive;

import com.relogiclabs.json.schema.JsonAssert;
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
    public void When_NonStaticValidMethod_ValidTrue() {
        var schema =
            """
            {
                "key1": #array,
                "key2": #array
            }
            """;
        var json =
            """
            {
                "key1": [1, 10, 100],
                "key2": [100, 1000, [10, 10000]]
            }
            """;
        var jsonAssert = new JsonAssert(schema);
        jsonAssert.isValid(json);
        jsonAssert.isValid(json);
    }
}