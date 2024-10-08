package com.relogiclabs.jschema.test.positive;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import org.junit.jupiter.api.Test;

public class DataTypeTests {
    @Test
    public void When_DataTypeMultiple_ValidTrue() {
        var schema = " #string #integer #null ";
        var json = " 10 ";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_MultipleNestedDataTypeInObject_ValidTrue() {
        var schema =
            """
            {
                "key1": #date* #time* #array,
                "key2": #string* #integer* #null* #array,
                "key3": #integer* #float* #array
            }
            """;
        var json =
            """
            {
                "key1": ["2021-08-01", "2021-08-01T15:50:30.300Z"],
                "key2": ["test", null, 10],
                "key3": [10, 100, 200.5]
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_CheckMultiLevelNestedDataTypeInArray_ValidTrue() {
        var schema =
            """
            %define $inner: @range*(0, 100) #float*
            %schema: @length*(3) @length(3) #array*($inner) #array
            """;
        var json =
            """
            [
                [0.1, 1.2, 2.3],
                [10.5, 11.6, 12.7],
                [80.7, 90.8, 99.9]
            ]
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_MultipleDirectDataTypeInObject_ValidTrue() {
        var schema =
            """
            {
                "key1": #date #time,
                "key2": #array #null,
                "key3": #integer #float,
                "key4": #string #null,
                "key5": #number #string
            }
            """;
        var json1 =
            """
            {
                "key1": "2021-08-01",
                "key2": [10, 20, 30],
                "key3": 100,
                "key4": "test",
                "key5": 10.10
            }
            """;
        var json2 =
            """
            {
                "key1": "2021-08-01T15:50:30.300Z",
                "key2": null,
                "key3": 200.5,
                "key4": null,
                "key5": "10.10"
            }
            """;
        JsonSchema.isValid(schema, json1);
        JsonSchema.isValid(schema, json2);
        JsonAssert.isValid(schema, json1);
        JsonAssert.isValid(schema, json2);
    }

    @Test
    public void When_DataTypeMultipleInArray_ValidTrue() {
        var schema =
            """
            [#integer #null, #integer #null, #integer #boolean]
            """;
        var json =
            """
            [10, null, false]
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypeMultipleWithNestedFunctionInObject_ValidTrue() {
        var schema =
            """
            {
                "key1": @range*(1, 100) #array #null,
                "key2": @range*(1, 100) #array #null
            }
            """;
        var json =
            """
            {
                "key1": [10, 20, 30],
                "key2": null
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypeAnyInArray_ValidTrue() {
        var schema =
            """
            #any* #array
            """;
        var json =
            """
            [[], {}, null, false, "test", 0.5, 1E-10, 0]
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypePrimitiveInArray_ValidTrue() {
        var schema =
            """
            #primitive* #array
            """;
        var json =
            """
            ["test", 0, false, null]
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypeCompositeInArray_ValidTrue() {
        var schema =
            """
            #composite* #array
            """;
        var json =
            """
            [[], {}, [10, 20], {"key": 100}]
            """;
        JsonAssert.isValid(schema, json);
    }
}