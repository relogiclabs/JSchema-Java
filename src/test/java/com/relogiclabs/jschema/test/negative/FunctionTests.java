package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.ClassInstantiationException;
import com.relogiclabs.jschema.exception.DuplicateImportException;
import com.relogiclabs.jschema.exception.FunctionNotFoundException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.exception.InvalidFunctionException;
import com.relogiclabs.jschema.exception.InvalidImportException;
import com.relogiclabs.jschema.exception.NoClassFoundException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.FNCDEF01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCSIG01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCSIG02;
import static com.relogiclabs.jschema.message.ErrorCode.FNTRGT01;
import static com.relogiclabs.jschema.message.ErrorCode.IMPCLS01;
import static com.relogiclabs.jschema.message.ErrorCode.IMPCLS02;
import static com.relogiclabs.jschema.message.ErrorCode.IMPDUP01;
import static com.relogiclabs.jschema.message.ErrorCode.INSTCR03;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FunctionTests {
    @Test
    public void When_FunctionAppliedOnWrongTargetType_ExceptionThrown() {
        var schema =
            """
            %schema: @range(10, 20)
            """;
        var json =
            """
            "test"
            """;

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNTRGT01, exception.getCode());
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
        assertEquals(IMPCLS02, exception.getCode());
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
        var exception = assertThrows(NoClassFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(IMPCLS01, exception.getCode());
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
        assertEquals(IMPDUP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalImportInstantiationNotSuccessful_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions5
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ClassInstantiationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(INSTCR03, exception.getCode());
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
        assertEquals(FNCSIG01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalFunctionMissingMandatoryTargetParameter_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions3
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(InvalidFunctionException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNCSIG02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalFunctionNotExists_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.external.ExternalFunctions4
            %schema: @notExist #integer
            """;
        var json = "10";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionNotFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNCDEF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalFunctionThrowArbitraryException_ExceptionThrown() {
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
        var json =
            """
            "test"
            """;
        JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionValidationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNTRGT01, exception.getCode());
        exception.printStackTrace();
    }
}