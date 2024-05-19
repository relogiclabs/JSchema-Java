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

import static com.relogiclabs.jschema.message.ErrorCode.AIDX01;
import static com.relogiclabs.jschema.message.ErrorCode.AIDX02;
import static com.relogiclabs.jschema.message.ErrorCode.ARNG01;
import static com.relogiclabs.jschema.message.ErrorCode.ARNG02;
import static com.relogiclabs.jschema.message.ErrorCode.ARNG03;
import static com.relogiclabs.jschema.message.ErrorCode.CALR01;
import static com.relogiclabs.jschema.message.ErrorCode.FNVK01;
import static com.relogiclabs.jschema.message.ErrorCode.FPRM01;
import static com.relogiclabs.jschema.message.ErrorCode.RETN01;
import static com.relogiclabs.jschema.message.ErrorCode.SIDX01;
import static com.relogiclabs.jschema.message.ErrorCode.SIDX02;
import static com.relogiclabs.jschema.message.ErrorCode.SRNG01;
import static com.relogiclabs.jschema.message.ErrorCode.SRNG02;
import static com.relogiclabs.jschema.message.ErrorCode.SRNG03;
import static com.relogiclabs.jschema.message.ErrorCode.TRGT01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

public final class ScriptErrorHelper {
    static final String OpAddition = "addition";
    static final String OpSubtraction = "subtraction";
    static final String OpMultiplication = "multiplication";
    static final String OpDivision = "division";
    static final String OpModulus = "modulus";
    static final String OpUnaryPlus = "unary plus";
    static final String OpUnaryMinus = "unary minus";
    static final String OpBracket = "bracket";
    static final String OpIndex = "index";
    static final String OpRange = "range";
    static final String OpRangeSetup = "range setup";
    static final String OpComparison = "comparison";
    static final String OpProperty = "property access";
    static final String OpIncrement = "increment";
    static final String OpDecrement = "decrement";
    static final String OpBracketedAssignment = "bracketed assignment";
    static final String OpPropertyAssignment = "property assignment";
    static final String OpAdditionAssignment = "addition assignment";
    static final String OpSubtractionAssignment = "subtraction assignment";
    static final String OpMultiplicationAssignment = "multiplication assignment";
    static final String OpDivisionAssignment = "division assignment";
    static final String OpModulusAssignment = "modulus assignment";

    private ScriptErrorHelper() {
        throw new UnsupportedOperationException("This class is not intended for instantiation");
    }

    static InvalidFunctionException failOnDuplicateParameterName(TerminalNode node) {
        return new InvalidFunctionException(formatForSchema(FPRM01,
            "Duplicate parameter name '" + node.getText() + "'", node.getSymbol()));
    }

    static ScriptRuntimeException failOnInvalidReturnType(EValue value, Token token) {
        return new ScriptRuntimeException(formatForSchema(RETN01,
            "Invalid return type " + value.getType() + " for constraint function", token));
    }

    static ScriptRuntimeException failOnPropertyNotExist(String code, EObject object,
                String property, Token token) {
        return new ScriptRuntimeException(formatForSchema(code, "Property '" + property
            + "' not exist in " + object.getType(), token));
    }

    static RuntimeException failOnIndexOutOfBounds(EString string, EInteger index,
                Token token, RuntimeException cause) {
        var indexValue = index.getValue();
        if(indexValue < 0) return new ScriptRuntimeException(formatForSchema(SIDX02,
            "Invalid negative index " + index + " for string starts at 0", token), cause);
        var length = string.length();
        if(indexValue >= length) return new ScriptRuntimeException(formatForSchema(SIDX01,
            "Index " + index + " out of bounds for string length " + length, token), cause);
        return cause;
    }

    static RuntimeException failOnIndexOutOfBounds(EArray array, EInteger index,
                Token token, RuntimeException cause) {
        var indexValue = index.getValue();
        if(indexValue < 0) return new ScriptRuntimeException(formatForSchema(AIDX02,
            "Invalid negative index " + index + " for array starts at 0", token), cause);
        var size = array.size();
        if(indexValue >= size) return new ScriptRuntimeException(formatForSchema(AIDX01,
            "Index " + index + " out of bounds for array length " + size, token), cause);
        return cause;
    }

    static RuntimeException failOnInvalidRangeIndex(EString string, GRange range, Token token,
                RuntimeException cause) {
        int length = string.length(), start = range.getStart(length), end = range.getEnd(length);
        if(start < 0 || start > length) throw new ScriptRuntimeException(formatForSchema(SRNG01,
            "Range start index " + start + " out of bounds for string length " + length,
            token), cause);
        if(end < 0 || end > length) return new ScriptRuntimeException(formatForSchema(SRNG02,
            "Range end index " + end + " out of bounds for string length " + length,
            token), cause);
        if(start > end) return new ScriptRuntimeException(formatForSchema(SRNG03,
            "Range start index " + start + " > end index " + end + " for string length " + length,
            token), cause);
        return cause;
    }

    static RuntimeException failOnInvalidRangeIndex(EArray array, GRange range, Token token,
                RuntimeException cause) {
        int size = array.size(), start = range.getStart(size), end = range.getEnd(size);
        if(start < 0 || start > size) throw new ScriptRuntimeException(formatForSchema(ARNG01,
            "Range start index " + start + " out of bounds for array length " + size,
            token), cause);
        if(end < 0 || end > size) return new ScriptRuntimeException(formatForSchema(ARNG02,
            "Range end index " + end + " out of bounds for array length " + size, token), cause);
        if(start > end) return new ScriptRuntimeException(formatForSchema(ARNG03,
            "Range start index " + start + " > end index " + end + " for array length " + size,
            token), cause);
        return cause;
    }

    static ScriptRuntimeException failOnInvalidLeftValueIncrement(String code, Token token) {
        return new ScriptRuntimeException(formatForSchema(code,
            "Invalid l-value for increment (readonly)", token));
    }

    static ScriptRuntimeException failOnInvalidLeftValueDecrement(String code, Token token) {
        return new ScriptRuntimeException(formatForSchema(code,
            "Invalid l-value for decrement (readonly)", token));
    }

    static ScriptRuntimeException failOnInvalidLeftValueAssignment(String code, Token token) {
        return new ScriptRuntimeException(formatForSchema(code,
            "Invalid l-value for assignment (readonly)", token));
    }

    static ScriptInvocationException failOnTargetNotFound(Token token) {
        return new ScriptInvocationException(TRGT01, "Target not found for function '%s'", token);
    }

    static ScriptInvocationException failOnCallerNotFound(Token token) {
        return new ScriptInvocationException(CALR01, "Caller not found for function '%s'", token);
    }

    static ScriptRuntimeException failOnIdentifierNotFound(String code, Token identifier) {
        return new ScriptRuntimeException(formatForSchema(code,
            "Identifier '" + identifier.getText() + "' not found", identifier));
    }

    static ScriptRuntimeException failOnFunctionNotFound(String name, int argCount, Token token) {
        return new ScriptRuntimeException(formatForSchema(FNVK01,
            "Function '" + name + "' with " + argCount + " parameter(s) not found", token));
    }

    static ScriptRuntimeException failOnRuntime(String code, String message,
                Token token, Throwable cause) {
        return new ScriptRuntimeException(formatForSchema(code, message, token), cause);
    }

    static ScriptInvocationException failOnVariadicArity(String code) {
        return new ScriptInvocationException(code,
            "Too few arguments for invocation of variadic function '%s'");
    }

    static ScriptInvocationException failOnFixedArity(String code) {
        return new ScriptInvocationException(code,
            "Invalid number of arguments for invocation of function '%s'");
    }

    static SystemOperationException failOnSystemException(String code,
                Exception exception, Token token) {
        return new SystemOperationException(formatForSchema(code, exception.getMessage(),
            token), exception);
    }

    static ScriptRuntimeException failOnOperation(String code, String operation,
                EValue value, Token token, Throwable cause) {
        return new ScriptOperationException(formatForSchema(code, "Invalid " + operation
            + " operation on type " + value.getType(), token), cause);
    }

    static ScriptRuntimeException failOnOperation(String code, String operation,
                EValue value, TerminalNode node) {
        return failOnOperation(code, operation, value, node.getSymbol(), null);
    }

    static ScriptRuntimeException failOnOperation(String code, String operation,
                EValue value1, EValue value2, Token token, Throwable cause) {
        return new ScriptOperationException(formatForSchema(code, "Invalid " + operation
            + " operation on types " + value1.getType() + " and " + value2.getType(),
            token), cause);
    }

    static ScriptRuntimeException failOnOperation(String code, String operation,
                EValue value1, EValue value2, TerminalNode node) {
        return failOnOperation(code, operation, value1, value2, node.getSymbol(), null);
    }

    static ScriptRuntimeException failOnStringUpdate(String code, TerminalNode node) {
        return new ScriptRuntimeException(formatForSchema(code,
            "Immutable string characters cannot be updated", node.getSymbol()));
    }

    static ScriptRuntimeException failOnArrayRangeUpdate(String code, TerminalNode node) {
        return new ScriptRuntimeException(formatForSchema(code,
            "Update a range of elements in array is not supported", node.getSymbol()));
    }
}