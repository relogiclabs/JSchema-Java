package com.relogiclabs.json.schema.negative;

import com.relogiclabs.json.schema.JsonAssert;
import com.relogiclabs.json.schema.JsonSchema;
import com.relogiclabs.json.schema.exception.JsonLexerException;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.exception.SchemaLexerException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.json.schema.message.ErrorCode.DTYP04;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP06;
import static com.relogiclabs.json.schema.message.ErrorCode.EMAL01;
import static com.relogiclabs.json.schema.message.ErrorCode.ENUM01;
import static com.relogiclabs.json.schema.message.ErrorCode.JLEX01;
import static com.relogiclabs.json.schema.message.ErrorCode.NEMT01;
import static com.relogiclabs.json.schema.message.ErrorCode.PHON01;
import static com.relogiclabs.json.schema.message.ErrorCode.REGX01;
import static com.relogiclabs.json.schema.message.ErrorCode.SLEN01;
import static com.relogiclabs.json.schema.message.ErrorCode.SLEN03;
import static com.relogiclabs.json.schema.message.ErrorCode.SLEN04;
import static com.relogiclabs.json.schema.message.ErrorCode.SLEN05;
import static com.relogiclabs.json.schema.message.ErrorCode.SLEX01;
import static com.relogiclabs.json.schema.message.ErrorCode.URLA01;
import static com.relogiclabs.json.schema.message.ErrorCode.URLA04;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringTests {
    @Test
    public void When_JsonNotString_ExceptionThrown() {
        var schema = "#string";
        var json = "10";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidUnicodeStringInSchema_ExceptionThrown() {
        var schema = "\"\\uX0485\\uY486\\r\\n\\t\" #string";
        var json = "\"\\u0485\\u0486\\r\\n\\t\"";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(SchemaLexerException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(SLEX01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidUnicodeStringInJson_ExceptionThrown() {
        var schema = "\"\\u0485\\u0486\\r\\n\\t\" #string";
        var json = "\"\\uX0485\\uY486\\r\\n\\t\"";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonLexerException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(JLEX01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotStringInObject_ExceptionThrown() {
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
                "key2": 10,
                "key3": null
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotStringInArray_ExceptionThrown() {
        var schema =
            """
            [#string, #string, #string]
            """;
        var json =
            """
            ["value1", null,
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit"]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedJsonNotStringInArray_ExceptionThrown() {
        var schema =
            """
            #string*
            """;
        var json =
            """
            ["value1", 10, "value3"]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedJsonNotStringInObject_ExceptionThrown() {
        var schema =
            """
            #string*
            """;
        var json =
            """
            {
                "key1": "value1",
                "key2": null,
                "key3": 100.5
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYP06, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_WrongLengthWithStringInObject_ExceptionThrown() {
        var schema =
            """
            @length*(5) #string*
            """;
        var json =
            """
            {
                "key1": "12345",
                "key2": "1234",
                "key3": "123456"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(SLEN01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedWrongLengthWithStringInObject_ExceptionThrown() {
        var schema =
            """
            @length*(5, 15) #string*
            """;
        var json =
            """
            {
                "key1": "value1",
                "key2": "Lorem ipsum dolor sit amet",
                "key3": "Lorem ipsum dolor sit amet, consectetur adipiscing elit"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(SLEN03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedWrongLengthWithUndefinedStringInObject_ExceptionThrown() {
        var schema =
            """
            @length*(100, !) #string*
            """;
        var json =
            """
            {
                "key1": "Lorem ipsum dolor sit amet",
                "key2": "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                "key3": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(SLEN04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NestedWrongLengthWithUndefinedStringInArray_ExceptionThrown() {
        var schema =
            """
            @length*(!, 10) #string*
            """;
        var json =
            """
            ["Lorem ipsum dolor sit amet", "value11", "value111"]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(SLEN05, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_RegexWithWrongStringInObject_ExceptionThrown() {
        var schema =
            """
            { "key1": @regex("[A-Za-z0-9]+@gmail\\.com") }
            """.replace("\\", "\\\\");
        var json =
            """
            {
                "key1": "new example@gmail.com"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(REGX01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_EnumWithWrongStringInObject_ExceptionThrown() {
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
                "key2": "val4"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(ENUM01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_EmailWithWrongStringInObject_ExceptionThrown() {
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
                "key1": "email@test@example.com",
                "key2": "Email_Test@example.org"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(EMAL01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_UrlWithWrongStringAddressInObject_ExceptionThrown() {
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
                "key1": "https:// www.example.com/",
                "key2": "https://www.<example>.com/test/",
                "key3": "ftps://subdomain.`example`.com/test#section?query=string"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(URLA01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_UrlWithSchemeAndWrongStringAddressInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": @url("ftps") #string,
                "key2": @url #string
            }
            """;
        var json =
            """
            {
                "key1": "ssh://www.example.com/test/",
                "key2": "ftp://www.example.com/"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(URLA04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_PhoneWithWrongStringInObject_ExceptionThrown() {
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
                "key1": "Phone 01737048177",
                "key2": "+880:1737048177",
                "key3": "0088/01737048177"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(PHON01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_EmptyStringInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": @nonempty #string,
                "key2": @nonempty #string #null
            }
            """;
        var json =
            """
            {
                "key1": "",
                "key2": null
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(NEMT01, exception.getCode());
        exception.printStackTrace();
    }
}