package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.DataTypeValidationException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.exception.JsonLexerException;
import com.relogiclabs.jschema.exception.SchemaLexerException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS01;
import static com.relogiclabs.jschema.message.ErrorCode.DTYPMS02;
import static com.relogiclabs.jschema.message.ErrorCode.EMALCF01;
import static com.relogiclabs.jschema.message.ErrorCode.EMPTCF01;
import static com.relogiclabs.jschema.message.ErrorCode.ENMSTR01;
import static com.relogiclabs.jschema.message.ErrorCode.IPV4CF01;
import static com.relogiclabs.jschema.message.ErrorCode.IPV6CF01;
import static com.relogiclabs.jschema.message.ErrorCode.JSNLEX01;
import static com.relogiclabs.jschema.message.ErrorCode.LENSTR01;
import static com.relogiclabs.jschema.message.ErrorCode.LENSTR03;
import static com.relogiclabs.jschema.message.ErrorCode.LENSTR04;
import static com.relogiclabs.jschema.message.ErrorCode.LENSTR05;
import static com.relogiclabs.jschema.message.ErrorCode.PHONCF01;
import static com.relogiclabs.jschema.message.ErrorCode.REGXCF01;
import static com.relogiclabs.jschema.message.ErrorCode.SCMLEX01;
import static com.relogiclabs.jschema.message.ErrorCode.URLADR01;
import static com.relogiclabs.jschema.message.ErrorCode.URLSCM02;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringTests {
    @Test
    public void When_JsonNotString_ExceptionThrown() {
        var schema = "#string";
        var json = "10";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidUnicodeStringInSchema_ExceptionThrown() {
        var schema =
            """
            "\\uX9f2\\uY9f3\\r\\n\\t" #string
            """;
        var json =
            """
            "\\u29f2\\u29f3\\r\\n\\t"
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(SchemaLexerException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(SCMLEX01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidUnicodeStringInJson_ExceptionThrown() {
        var schema =
            """
            "\\u29f2\\u29f3\\r\\n\\t" #string
            """;
        var json =
            """
            "\\uX9f2\\uY9f3\\r\\n\\t"
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonLexerException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(JSNLEX01, exception.getCode());
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
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
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
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotNestedStringInArray_ExceptionThrown() {
        var schema =
            """
            #string*
            """;
        var json =
            """
            ["value1", 10, "value3"]
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_JsonNotNestedStringInObject_ExceptionThrown() {
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
        var exception = assertThrows(DataTypeValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(DTYPMS02, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENSTR01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENSTR03, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENSTR04, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(LENSTR05, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(REGXCF01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ENMSTR01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(EMALCF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_UrlWithWrongFormatInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": @url #string,
                "key2": @url #string,
                "key3": @url #string
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(URLADR01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_UrlWithSchemeMismatchedInObject_ExceptionThrown() {
        var schema =
            """
            {
                "key1": @url("ftp", "ftps") #string,
                "key2": @url("http", "https") #string,
                "key3": @url("irc") #string
            }
            """;
        var json =
            """
            {
                "key1": "ssh://www.example.com/test/",
                "key2": "ftp://www.example.com/",
                "key3": "http://www.example.com/test?q=str"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(URLSCM02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_MalformedPhoneNumberInObject_ExceptionThrown() {
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
                "key1": "Phone: 01737048177",
                "key2": "+880:1737048177",
                "key3": "0088/01737048177"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(PHONCF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidIPv4AddressInObject_ExceptionThrown() {
        var schema =
            """
            {
                "ip1v4": @ipv4 #string,
                "ip2v4": @ipv(4) #string,
                "ip3vAny": @ipv(4, 6) #string
            }
            """;
        var json =
            """
            {
                "ip1v4": "13.0.0",
                "ip2v4": "13.0.0.256",
                "ip3vAny": "13:0:0:0"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(IPV4CF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_InvalidIPv6AddressInObject_ExceptionThrown() {
        var schema =
            """
            {
                "ip1v6": @ipv6 #string,
                "ip2v6": @ipv(6) #string,
                "ip3vAny": @ipv(4, 6) #string
            }
            """;
        var json =
            """
            {
                "ip1v6": "130d::1310:500c:d01::22",
                "ip2v6": "130c:1310:500c:d01:0:0:22",
                "ip3vAny": "13cd:500c::192.0.2.256"
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(IPV6CF01, exception.getCode());
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
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(EMPTCF01, exception.getCode());
        exception.printStackTrace();
    }
}