package com.relogiclabs.jschema.test.positive;

import com.relogiclabs.jschema.JsonAssert;
import org.junit.jupiter.api.Test;

public class DirectiveTests {
    @Test
    public void When_SchemaTitleGiven_ValidTrue() {
        var schema =
            """
            %title: "This is a title for this schema"
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_SchemaVersionGiven_ValidTrue() {
        var schema =
            """
            %version: "1.0.0-alpha"
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10
            }
            """;
        JsonAssert.isValid(schema, json);
    }

    @Test
    public void When_SchemaTitleAndVersionGiven_ValidTrue() {
        var schema =
            """
            %title: "This is a title for this schema"
            %version: "1.0.0-beta"
            %schema:
            {
                "key1": #integer
            }
            """;
        var json =
            """
            {
                "key1": 10
            }
            """;
        JsonAssert.isValid(schema, json);
    }
}