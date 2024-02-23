package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.exception.InvalidFunctionException;
import com.relogiclabs.jschema.exception.ScriptInvocationException;
import com.relogiclabs.jschema.exception.ScriptOperationException;
import com.relogiclabs.jschema.exception.ScriptRuntimeException;
import com.relogiclabs.jschema.exception.SystemOperationException;
import com.relogiclabs.jschema.internal.script.GRange;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.ASIN01;
import static com.relogiclabs.jschema.message.ErrorCode.CALR01;
import static com.relogiclabs.jschema.message.ErrorCode.FUNS01;
import static com.relogiclabs.jschema.message.ErrorCode.FUNS04;
import static com.relogiclabs.jschema.message.ErrorCode.INDX02;
import static com.relogiclabs.jschema.message.ErrorCode.INDX03;
import static com.relogiclabs.jschema.message.ErrorCode.INDX04;
import static com.relogiclabs.jschema.message.ErrorCode.INDX05;
import static com.relogiclabs.jschema.message.ErrorCode.PRPS02;
import static com.relogiclabs.jschema.message.ErrorCode.RETN01;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS04;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS05;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS06;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS07;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS08;
import static com.relogiclabs.jschema.message.ErrorCode.RNGS09;
import static com.relogiclabs.jschema.message.ErrorCode.TRGT01;
import static com.relogiclabs.jschema.message.ErrorCode.VARD02;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static com.relogiclabs.jschema.message.OutlineFormatter.createOutline;


public final class ScriptErrorHelper {
    private ScriptErrorHelper() {
        throw new UnsupportedOperationException();
    }

    static InvalidFunctionException failOnDuplicateParameterName(TerminalNode node) {
        return new InvalidFunctionException(formatForSchema(FUNS01,
                concat("Duplicate parameter name '", node.getText(), "'"), node.getSymbol()));
    }

    static ScriptRuntimeException failOnInvalidReturnType(EValue value, Token token) {
        return new ScriptRuntimeException(formatForSchema(RETN01,
                concat("Invalid return type ", value.getType(), " for constraint function"), token));
    }

    static ScriptRuntimeException failOnPropertyNotExist(EObject object, String property,
                Token token) {
        return new ScriptRuntimeException(formatForSchema(PRPS02, concat("Property '",
                property, "' not exist in readonly ", object.getType()), token));
    }

    static RuntimeException failOnIndexOutOfBounds(EString string, EInteger index,
                Token token, RuntimeException cause) {
        var indexValue = index.getValue();
        if(indexValue < 0) return new ScriptRuntimeException(formatForSchema(INDX03,
                concat("Invalid negative index ", index, " for string starts at 0"),
                token), cause);
        var length = string.length();
        if(indexValue >= length) return new ScriptRuntimeException(formatForSchema(INDX02,
                concat("Index ", index, " out of bounds for string length ", length),
                token), cause);
        return cause;
    }

    static RuntimeException failOnIndexOutOfBounds(EArray array, EInteger index,
                Token token, RuntimeException cause) {
        var indexValue = index.getValue();
        if(indexValue < 0) return new ScriptRuntimeException(formatForSchema(INDX05,
                concat("Invalid negative index ", index, " for array starts at 0"),
                token), cause);
        var size = array.size();
        if(indexValue >= size) return new ScriptRuntimeException(formatForSchema(INDX04,
                concat("Index ", index, " out of bounds for array length ", size),
                token), cause);
        return cause;
    }

    static RuntimeException failOnInvalidRangeIndex(EString string, GRange range, Token token,
                RuntimeException cause) {
        int length = string.length(), start = range.getStart(length), end = range.getEnd(length);
        if(start < 0 || start > length) throw new ScriptRuntimeException(formatForSchema(RNGS04,
                concat("Range start index ", start, " out of bounds for string length ", length),
                token), cause);
        if(end < 0 || end > length) return new ScriptRuntimeException(formatForSchema(RNGS05,
                concat("Range end index ", end, " out of bounds for string length ", length),
                token), cause);
        if(start > end) return new ScriptRuntimeException(formatForSchema(RNGS06,
                concat("Range start index ", start, " > end index ", end, " for string ",
                        createOutline(string)), token), cause);
        return cause;
    }

    static RuntimeException failOnInvalidRangeIndex(EArray array, GRange range, Token token,
                RuntimeException cause) {
        int size = array.size(), start = range.getStart(size), end = range.getEnd(size);
        if(start < 0 || start > size) throw new ScriptRuntimeException(formatForSchema(RNGS07,
                concat("Range start index ", start, " out of bounds for array length ", size),
                token), cause);
        if(end < 0 || end > size) return new ScriptRuntimeException(formatForSchema(RNGS08,
                concat("Range end index ", end, " out of bounds for array length ", size),
                token), cause);
        if(start > end) return new ScriptRuntimeException(formatForSchema(RNGS09,
                concat("Range start index ", start, " > end index ", end, " for array ",
                        createOutline(array)), token), cause);
        return cause;
    }

    static ScriptRuntimeException failOnInvalidLValueIncrement(String code, Token token) {
        return new ScriptRuntimeException(formatForSchema(code,
                "Invalid l-value for increment", token));
    }

    static ScriptRuntimeException failOnInvalidLValueDecrement(String code, Token token) {
        return new ScriptRuntimeException(formatForSchema(code,
                "Invalid l-value for decrement", token));
    }

    static ScriptRuntimeException failOnInvalidLValueAssignment(Token token) {
        return new ScriptRuntimeException(formatForSchema(ASIN01,
                "Invalid l-value for assignment", token));
    }

    static ScriptInvocationException failOnTargetNotFound(Token token) {
        return new ScriptInvocationException(TRGT01, "Target not found for function '%s'", token);
    }

    static ScriptInvocationException failOnCallerNotFound(Token token) {
        return new ScriptInvocationException(CALR01, "Caller not found for function '%s'", token);
    }

    static ScriptRuntimeException failOnIdentifierNotFound(Token identifier) {
        return new ScriptRuntimeException(formatForSchema(VARD02,
                concat("Identifier '", identifier.getText(), "' not found"), identifier));
    }

    static ScriptRuntimeException failOnFunctionNotFound(String name, int arity, Token token) {
        return new ScriptRuntimeException(formatForSchema(FUNS04,
                concat("Function '", name, "' with ", arity, " parameter(s) not found"), token));
    }

    static ScriptRuntimeException failOnRuntime(String code, String message,
                Token token, Throwable cause) {
        return new ScriptRuntimeException(formatForSchema(code, message, token), cause);
    }

    static ScriptInvocationException failOnVariadicArgument(String code) {
        return new ScriptInvocationException(code,
                "Too few argument for invocation of variadic function '%s'");
    }

    static ScriptInvocationException failOnFixedArgument(String code) {
        return new ScriptInvocationException(code,
                "Invalid number of arguments for function '%s'");
    }

    static SystemOperationException failOnSystemException(String code,
                Exception exception, Token token) {
        return new SystemOperationException(formatForSchema(code, exception.getMessage(),
                token), exception);
    }

    static ScriptRuntimeException failOnOperation(String code, String operationName,
                EValue value, Token token, Throwable cause) {
        return new ScriptOperationException(formatForSchema(code, concat("Invalid ",
                operationName, " operation on type ", value.getType()), token), cause);
    }

    static ScriptRuntimeException failOnOperation(String code, String operationName,
                EValue value, TerminalNode node) {
        return failOnOperation(code, operationName, value, node.getSymbol(), null);
    }

    static ScriptRuntimeException failOnOperation(String code, String operationName,
                EValue value1, EValue value2, Token token, Throwable cause) {
        return new ScriptOperationException(formatForSchema(code, concat("Invalid ",
                operationName, " operation on types ", value1.getType(), " and ",
                value2.getType()), token), cause);
    }

    static ScriptRuntimeException failOnOperation(String code, String operationName,
                EValue value1, EValue value2, TerminalNode node) {
        return failOnOperation(code, operationName, value1, value2, node.getSymbol(), null);
    }
}