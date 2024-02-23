package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.ClassInstantiationException;
import com.relogiclabs.jschema.exception.DuplicateImportException;
import com.relogiclabs.jschema.exception.FunctionNotFoundException;
import com.relogiclabs.jschema.exception.InvalidFunctionException;
import com.relogiclabs.jschema.exception.InvalidImportException;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.exception.NotFoundClassException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.CLAS01;
import static com.relogiclabs.jschema.message.ErrorCode.CLAS02;
import static com.relogiclabs.jschema.message.ErrorCode.CLAS03;
import static com.relogiclabs.jschema.message.ErrorCode.CLAS04;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC01;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC02;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC03;
import static com.relogiclabs.jschema.message.ErrorCode.FUNC05;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FunctionTests {

    @Test
    public void When_FunctionAppliedOnWrongType_ExceptionThrown() {
        var schema =
                """
                %schema: @range(10, 20)
                """;
        var json =
                """
                "test"
                """;

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FUNC03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalImportNotInheritBaseClass_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions1
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(InvalidImportException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(CLAS03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalImportNotExisting_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.json.schema.negative.function.NotExisting
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(NotFoundClassException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(CLAS02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalImportDuplicationOccurred_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(DuplicateImportException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(CLAS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalImportInstantiationNotCompleted_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions5
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ClassInstantiationException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(CLAS04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalFunctionWrongReturnType_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions2
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(InvalidFunctionException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FUNC01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalFunctionWrongParameterNumber_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions3
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(InvalidFunctionException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FUNC02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalFunctionNotExists_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions4
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionNotFoundException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FUNC05, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_FunctionThrowArbitraryException_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions4
            %schema: @canTest #integer
            """;
        var json = "10";
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(RuntimeException.class,
                () -> JsonAssert.isValid(schema, json));
        exception.printStackTrace();
    }

    @Test
    public void When_IncompatibleTargetForExternalFunction_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions
            %schema: @even #string
            """;
        var json = "\"test\"";
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(JsonSchemaException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FUNC03, exception.getCode());
        exception.printStackTrace();
    }
}