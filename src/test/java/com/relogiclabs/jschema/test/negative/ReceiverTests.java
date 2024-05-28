package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.InvalidReceiverStateException;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.exception.ReceiverNotFoundException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.RECV01;
import static com.relogiclabs.jschema.message.ErrorCode.RECV02;
import static com.relogiclabs.jschema.test.external.ExternalFunctions.CONDFUNC01;
import static com.relogiclabs.jschema.test.external.ExternalFunctions.CONDFUNC02;
import static com.relogiclabs.jschema.test.external.ExternalFunctions.MINMAX01;
import static com.relogiclabs.jschema.test.external.ExternalFunctions.SUMEQUAL01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReceiverTests {
    @Test
    public void When_WrongReceiverNameInObject_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions
            %schema:
            {
                "key1": #integer &someName,
                "key2": @condition(&notExist) #integer
            }
            """;
        var json =
            """
            {
                "key1": 5,
                "key2": 6
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ReceiverNotFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RECV01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_NoValueReceiveInObject_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions

            %schema:
            {
                "key1": #integer &relatedValue ?,
                "key2": @condition(&relatedValue) #integer ?
            }
            """;
        var json =
            """
            {
                "key2": 1
            }
            """;
        //JsonSchema.isValid(schema, json);
        //This exception is avoidable inside @condition when needed
        var exception = assertThrows(InvalidReceiverStateException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(RECV02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ConditionFailedInObject_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions

            %schema:
            {
                "key1": #integer &relatedValue ?,
                "key2": @condition(&relatedValue) #integer ?
            }
            """;
        var json =
            """
            {
                "key1": 11,
                "key2": 10
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(CONDFUNC01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ConditionAllFailedInObject_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions

            %define $numbers: @range(1, 10) #integer &relatedValues
            %schema:
            {
                "key1": #integer*($numbers) #array,
                "key2": @conditionAll(&relatedValues) #integer
            }
            """;
        var json =
            """
            {
                "key1": [1, 2, 3, 4, 5, 6],
                "key2": 6
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(CONDFUNC02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ReceiveWrongValuesInObject_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions
            %schema:
            {
                "key10": @sumEqual(&relatedData) #integer,
                "key1": #integer &relatedData,
                "key2": #integer &relatedData,
                "key3": #integer &relatedData,
                "key4": #integer &relatedData,
                "key5": #integer &relatedData
            }
            """;
        var json =
            """
            {
                "key1": 9,
                "key2": 5,
                "key3": 13,
                "key4": 60,
                "key5": 12,
                "key10": 100
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(SUMEQUAL01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_MultiReceiverFunctionWrongValuesInObject_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions

            %schema:
            {
                "key1": #integer &minData,
                "key2": @minmax(&minData, &maxData) #integer,
                "key3": #integer &maxData
            }
            """;
        var json =
            """
            {
                "key1": 1,
                "key2": 11,
                "key3": 10
            }
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(MINMAX01, exception.getCode());
        exception.printStackTrace();
    }
}