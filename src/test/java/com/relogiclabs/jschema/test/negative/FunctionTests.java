package com.relogiclabs.jschema.test.negative;

import com.relogiclabs.jschema.JsonAssert;
import com.relogiclabs.jschema.JsonSchema;
import com.relogiclabs.jschema.exception.ClassInstantiationException;
import com.relogiclabs.jschema.exception.DuplicateImportException;
import com.relogiclabs.jschema.exception.FunctionNotFoundException;
import com.relogiclabs.jschema.exception.FunctionValidationException;
import com.relogiclabs.jschema.exception.InvalidFunctionException;
import com.relogiclabs.jschema.exception.InvalidImportException;
import com.relogiclabs.jschema.exception.InvalidReturnTypeException;
import com.relogiclabs.jschema.exception.NoClassFoundException;
import com.relogiclabs.jschema.exception.TargetInvocationException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.jschema.message.ErrorCode.FNCCAL01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCDEF01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCRET01;
import static com.relogiclabs.jschema.message.ErrorCode.FNCSIG01;
import static com.relogiclabs.jschema.message.ErrorCode.FNTRGT01;
import static com.relogiclabs.jschema.message.ErrorCode.IMPCLS01;
import static com.relogiclabs.jschema.message.ErrorCode.IMPDUP01;
import static com.relogiclabs.jschema.message.ErrorCode.IMPLOD01;
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
    public void When_ImportNotExtendsOrImplementsCorrectType_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.ConstraintExtension3
            %schema: @odd #integer
            """;
        var json = "2";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(InvalidImportException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(IMPLOD01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalImportNonExisting_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.json.schema.negative.test.NonExisting
            %schema: @odd #integer
            """;
        var json = "2";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(NoClassFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(IMPCLS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ImportDuplicationOccurred_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.GeneralExtension1
            %import: com.relogiclabs.jschema.test.extension.GeneralExtension1
            %schema: @odd #integer
            """;
        var json = "2";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(DuplicateImportException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(IMPDUP01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ImportInstantiationNotSuccessful_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.ConstraintExtension4
            %schema: @odd #integer
            """;
        var json = "2";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(ClassInstantiationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(INSTCR03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ImportedFunctionReturnTypeInvalid_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.ConstraintExtension2
            %schema: @odd #integer
            """;
        var json = "2";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(InvalidReturnTypeException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNCRET01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ImportedFunctionMissingMandatoryTargetParameter_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.GeneralExtension3
            %schema: @odd #integer
            """;
        var json = "2";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(InvalidFunctionException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNCSIG01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ConstraintFunctionNotExistsInImports_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.GeneralExtension1
            %schema: @notExist #integer
            """;
        var json = "2";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(FunctionNotFoundException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNCDEF01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ImportedFunctionThrowArbitraryException_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.GeneralExtension4
            %schema: @odd #integer
            """;
        var json = "2";

        JsonSchema.isValid(schema, json);
        var exception = assertThrows(RuntimeException.class,
            () -> JsonAssert.isValid(schema, json));
        exception.printStackTrace();
    }

    @Test
    public void When_ImportedFunctionThrowCheckedException_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.GeneralExtension5
            %schema: @odd #integer
            """;
        var json = "2";

        //JsonSchema.isValid(schema, json);
        var exception = assertThrows(TargetInvocationException.class,
            () -> JsonAssert.isValid(schema, json));
        assertEquals(FNCCAL01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_IncompatibleTargetForImportedFunction_ExceptionThrown() {
        var schema =
            """
            %import: com.relogiclabs.jschema.test.extension.ConstraintExtension1
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