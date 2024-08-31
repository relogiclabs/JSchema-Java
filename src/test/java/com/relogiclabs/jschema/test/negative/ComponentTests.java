package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.exception.AliasNotFoundException;
import com.relogiclabs.jschema.exception.DuplicateAliasException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.ALSDEF01;
import static com.relogiclabs.jschema.message.ErrorCode.ALSDUP01;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComponentTests {
    @Test
    public void When_ComponentNotDefined_ExceptionThrown() {
        var schema =
            """
            %schema: {
                "key1": $cmp1,
                "key2": $cmp1
            }
            """;
        var json =
            """
            {
                "key1": { "key1": 10, "key2": "value11" },
                "key2": { "key1": 20, "key2": "value22" }
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(AliasNotFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ALSDEF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ComponentWithDuplicateDefinition_ExceptionThrown() {
        var schema =
            """
            %define $cmp1: { "key1": #integer, "key2": #string }
            %schema: {
                "key1": $cmp1,
                "key2": $cmp1
            }
            %define $cmp1: [#string, #string]
            """;
        var json =
            """
            {
                "key1": { "key1": 10, "key2": "value11" },
                "key2": { "key1": 20, "key2": "value22" }
            }
            """;
        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(DuplicateAliasException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(ALSDUP01, exception.getCode());
        exception.printStackTrace();
    }
}