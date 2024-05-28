package com.relogiclabs.jschema.test.positive;

import com.relogiclabs.jschema.JsonAssert;
import org.junit.jupiter.api.Test;

public class StringTests {
    @Test
    public void When_DataTypeString_ValidTrue() {
        var schema = "#string";
        var json = "\"value\"";
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypeWithUnicodeString_ValidTrue() {
        var schema = "#string";
        var json =
            """
            "\\u0985\\u0986\\r\\n\\t"
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypeStringInObject_ValidTrue() {
        var schema =
            """
            {
                "key1": #string,
                "key2": #string,
                "key3": #string
            }
            """;
        var json =
            """
            {
                "key1": "",
                "key2": "val2",
                "key3": "value3"
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_DataTypeStringInArray_ValidTrue() {
        var schema =
            """
            [#string, #string, #string]
            """;
        var json =
            """
            ["value1", "Lorem ipsum dolor sit amet",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit"]
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_NestedDataTypeStringInArray_ValidTrue() {
        var schema =
            """
            #string*
            """;
        var json =
            """
            ["value1", "value2", "value3"]
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_NestedDataTypeStringInObject_ValidTrue() {
        var schema =
            """
            #string*
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

    @Test
    public void When_NestedLengthWithStringInObject_ValidTrue() {
        var schema =
            """
            @length*(5, 80) #string*
            """;
        var json =
            """
            {
                "key1": "value1",
                "key2": "Lorem ipsum dolor sit amet",
                "key3": "Lorem ipsum dolor sit amet, consectetur adipiscing elit"
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_NestedLengthWithUndefinedStringInObject_ValidTrue() {
        var schema =
            """
            @length*(20, !) #string*
            """;
        var json =
            """
            {
                "key1": "Lorem ipsum dolor sit amet",
                "key2": "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                "key3": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_NestedLengthWithUndefinedStringInArray_ValidTrue() {
        var schema =
            """
            @length*(!, 10) #string*
            """;
        var json =
            """
            ["value1", "value11", "value111"]
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_RegexWithStringInObject_ValidTrue() {
        var schema =
            """
            { "key1": @regex("[A-Za-z0-9]+@gmail\\.com") }
            """.replace("\\", "\\\\");
        var json =
            """
            {
                "key1": "example@gmail.com"
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_EnumWithStringInObject_ValidTrue() {
        var schema =
            """
            {
                "key1": #string,
                "key2": @enum("val1", "val2") #string
            }
            """;
        var json =
            """
            {
                "key1": "",
                "key2": "val2"
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_EmailWithStringInObject_ValidTrue() {
        var schema =
            """
            {
                "key1": @email #string,
                "key2": @email #string
            }
            """;
        var json =
            """
            {
                "key1": "email.test@example.com",
                "key2": "Email_Test@example.org"
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_UrlWithStringInObject_ValidTrue() {
        var schema =
            """
            {
                "key1": @url #string,
                "key2": @url #string,
                "key3": @url("ftps") #string
            }
            """;
        var json =
            """
            {
                "key1": "https://www.google.com/",
                "key2": "https://www.microsoft.com/en-us/",
                "key3": "ftps://subdomain.example.com/test#section?query=string"
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_PhoneWithStringInObject_ValidTrue() {
        var schema =
            """
            {
                "key1": @phone #string,
                "key2": @phone #string,
                "key3": @phone #string
            }
            """;
        var json =
            """
            {
                "key1": "01737048177",
                "key2": "+8801737048177",
                "key3": "008801737048177"
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_NonEmptyStringInObject_ValidTrue() {
        var schema =
            """
            {
                "key1": @nonempty #string,
                "key2": @nonempty #string
            }
            """;
        var json =
            """
            {
                "key1": "A",
                "key2": "This is a test string"
            }
            """;
        JsonAssert.isValid(schema, json);
    }
}