package com.relogiclabs.json.schema.negative;

import com.relogiclabs.json.schema.JsonAssert;
import com.relogiclabs.json.schema.exception.ClassInstantiationException;
import com.relogiclabs.json.schema.exception.DuplicateIncludeException;
import com.relogiclabs.json.schema.exception.FunctionNotFoundException;
import com.relogiclabs.json.schema.exception.InvalidFunctionException;
import com.relogiclabs.json.schema.exception.InvalidIncludeException;
import com.relogiclabs.json.schema.exception.NotFoundClassException;
import org.junit.jupiter.api.Test;

import static com.relogiclabs.json.schema.message.ErrorCode.CLAS01;
import static com.relogiclabs.json.schema.message.ErrorCode.CLAS02;
import static com.relogiclabs.json.schema.message.ErrorCode.CLAS03;
import static com.relogiclabs.json.schema.message.ErrorCode.CLAS04;
import static com.relogiclabs.json.schema.message.ErrorCode.FUNC01;
import static com.relogiclabs.json.schema.message.ErrorCode.FUNC02;
import static com.relogiclabs.json.schema.message.ErrorCode.FUNC05;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FunctionTests {

    @Test
    public void When_ExternalIncludeNotInheritBaseClass_ExceptionThrown() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.negative.function.ExternalFunctions1
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.IsValid(schema, json);
        var exception = assertThrows(InvalidIncludeException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(CLAS03, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalIncludeNotExisting_ExceptionThrown() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.negative.function.NotExisting
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.IsValid(schema, json);
        var exception = assertThrows(NotFoundClassException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(CLAS02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalIncludeDuplicationOccurred_ExceptionThrown() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.positive.ExternalFunctions
            %include: com.relogiclabs.json.schema.positive.ExternalFunctions
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.IsValid(schema, json);
        var exception = assertThrows(DuplicateIncludeException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(CLAS01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalIncludeInstantiationNotCompleted_ExceptionThrown() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.negative.function.ExternalFunctions5
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.IsValid(schema, json);
        var exception = assertThrows(ClassInstantiationException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(CLAS04, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalFunctionWrongReturnType_ExceptionThrown() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.negative.function.ExternalFunctions2
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.IsValid(schema, json);
        var exception = assertThrows(InvalidFunctionException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FUNC01, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalFunctionWrongParameterNumber_ExceptionThrown() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.negative.function.ExternalFunctions3
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.IsValid(schema, json);
        var exception = assertThrows(InvalidFunctionException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FUNC02, exception.getCode());
        exception.printStackTrace();
    }

    @Test
    public void When_ExternalFunctionNotExists_ExceptionThrown() {
        var schema =
            """
            %include: com.relogiclabs.json.schema.negative.function.ExternalFunctions4
            %schema: @odd #integer
            """;
        var json = "10";

        //JsonSchema.IsValid(schema, json);
        var exception = assertThrows(FunctionNotFoundException.class,
                () -> JsonAssert.isValid(schema, json));
        assertEquals(FUNC05, exception.getCode());
        exception.printStackTrace();
    }
}