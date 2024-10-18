package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.exception.ArrayIndexOutOfBoundsException;
import com.relogiclabs.jschema.exception.DuplicateParameterException;
import com.relogiclabs.jschema.exception.FunctionNotFoundException;
import com.relogiclabs.jschema.exception.InvalidContextException;
import com.relogiclabs.jschema.exception.InvalidLeftValueException;
import com.relogiclabs.jschema.exception.InvalidReturnTypeException;
import com.relogiclabs.jschema.exception.InvocationRuntimeException;
import com.relogiclabs.jschema.exception.MethodNotFoundException;
import com.relogiclabs.jschema.exception.PropertyNotFoundException;
import com.relogiclabs.jschema.exception.ScriptOperationException;
import com.relogiclabs.jschema.exception.StringIndexOutOfBoundsException;
import com.relogiclabs.jschema.exception.SystemOperationException;
import com.relogiclabs.jschema.exception.UpdateNotSupportedException;
import com.relogiclabs.jschema.exception.VariableNotFoundException;
import com.relogiclabs.jschema.exception.VariadicInvocationException;
import com.relogiclabs.jschema.internal.script.GRange;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EInteger;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import static com.relogiclabs.jschema.message.ErrorCode.ARRIDX01;
import static com.relogiclabs.jschema.message.ErrorCode.ARRIDX02;
import static com.relogiclabs.jschema.message.ErrorCode.ARRRNG01;
import static com.relogiclabs.jschema.message.ErrorCode.ARRRNG02;
import static com.relogiclabs.jschema.message.ErrorCode.ARRRNG03;
import static com.relogiclabs.jschema.message.ErrorCode.CALRSE01;
import static com.relogiclabs.jschema.message.ErrorCode.FNSNVK01;
import static com.relogiclabs.jschema.message.ErrorCode.MTHNVK01;
import static com.relogiclabs.jschema.message.ErrorCode.PRMDUP01;
import static com.relogiclabs.jschema.message.ErrorCode.RETNSE02;
import static com.relogiclabs.jschema.message.ErrorCode.STRIDX01;
import static com.relogiclabs.jschema.message.ErrorCode.STRIDX02;
import static com.relogiclabs.jschema.message.ErrorCode.STRRNG01;
import static com.relogiclabs.jschema.message.ErrorCode.STRRNG02;
import static com.relogiclabs.jschema.message.ErrorCode.STRRNG03;
import static com.relogiclabs.jschema.message.ErrorCode.TRGTSE01;
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

    static DuplicateParameterException failOnDuplicateParameterName(TerminalNode node) {
        return new DuplicateParameterException(formatForSchema(PRMDUP01,
            "Duplicate parameter name '" + node.getText() + "'", node.getSymbol()));
    }

    static InvalidReturnTypeException failOnInvalidReturnType(EValue value, Token token) {
        return new InvalidReturnTypeException(formatForSchema(RETNSE02,
            "Invalid return type " + value.getType() + " for constraint function", token));
    }

    static PropertyNotFoundException failOnPropertyNotFound(String code, EObject object,
                String property, Token token) {
        return new PropertyNotFoundException(formatForSchema(code, "Property '" + property
            + "' not found in " + object.getType(), token));
    }

    static RuntimeException failOnIndexOutOfBounds(EString string, EInteger index,
                Token token, RuntimeException cause) {
        var indexValue = index.getValue();
        if(indexValue < 0) return new StringIndexOutOfBoundsException(formatForSchema(STRIDX02,
            "Invalid negative index " + index + " for string starts at 0", token), cause);
        var length = string.length();
        if(indexValue >= length) return new StringIndexOutOfBoundsException(formatForSchema(STRIDX01,
            "Index " + index + " out of bounds for string length " + length, token), cause);
        return cause;
    }

    static RuntimeException failOnIndexOutOfBounds(EArray array, EInteger index,
                Token token, RuntimeException cause) {
        var indexValue = index.getValue();
        if(indexValue < 0) return new ArrayIndexOutOfBoundsException(formatForSchema(ARRIDX02,
            "Invalid negative index " + index + " for array starts at 0", token), cause);
        var size = array.size();
        if(indexValue >= size) return new ArrayIndexOutOfBoundsException(formatForSchema(ARRIDX01,
            "Index " + index + " out of bounds for array length " + size, token), cause);
        return cause;
    }

    static RuntimeException failOnInvalidIndexRange(EString string, GRange range, Token token,
                RuntimeException cause) {
        int length = string.length(), start = range.getStart(length), end = range.getEnd(length);
        if(start < 0 || start > length)
            return new StringIndexOutOfBoundsException(formatForSchema(STRRNG01,
                "Range start index " + start + " out of bounds for string length " + length,
                token), cause);
        if(end < 0 || end > length)
            return new StringIndexOutOfBoundsException(formatForSchema(STRRNG02,
                "Range end index " + end + " out of bounds for string length " + length,
                token), cause);
        if(start > end) return new StringIndexOutOfBoundsException(formatForSchema(STRRNG03,
            "Range start index " + start + " > end index " + end + " for string length " + length,
            token), cause);
        return cause;
    }

    static RuntimeException failOnInvalidIndexRange(EArray array, GRange range, Token token,
                RuntimeException cause) {
        int size = array.size(), start = range.getStart(size), end = range.getEnd(size);
        if(start < 0 || start > size)
            return new ArrayIndexOutOfBoundsException(formatForSchema(ARRRNG01,
                "Range start index " + start + " out of bounds for array length " + size,
                token), cause);
        if(end < 0 || end > size)
            return new ArrayIndexOutOfBoundsException(formatForSchema(ARRRNG02,
                "Range end index " + end + " out of bounds for array length " + size,
                token), cause);
        if(start > end) return new ArrayIndexOutOfBoundsException(formatForSchema(ARRRNG03,
            "Range start index " + start + " > end index " + end + " for array length " + size,
            token), cause);
        return cause;
    }

    static InvalidLeftValueException failOnInvalidLeftValueIncrement(String code, Token token) {
        return new InvalidLeftValueException(formatForSchema(code,
            "Invalid l-value for increment (readonly)", token));
    }

    static InvalidLeftValueException failOnInvalidLeftValueDecrement(String code, Token token) {
        return new InvalidLeftValueException(formatForSchema(code,
            "Invalid l-value for decrement (readonly)", token));
    }

    static InvalidLeftValueException failOnInvalidLeftValueAssignment(String code, Token token) {
        return new InvalidLeftValueException(formatForSchema(code,
            "Invalid l-value for assignment (readonly)", token));
    }

    static InvalidContextException failOnTargetNotFound(Token token) {
        return new InvalidContextException(TRGTSE01, "Target not found", token);
    }

    static InvalidContextException failOnCallerNotFound(Token token) {
        return new InvalidContextException(CALRSE01, "Caller not found", token);
    }

    static VariableNotFoundException failOnVariableNotFound(String code, Token identifier) {
        return new VariableNotFoundException(formatForSchema(code,
            "Variable '" + identifier.getText() + "' not found", identifier));
    }

    static FunctionNotFoundException failOnFunctionNotFound(String name, int argCount,
                Token token) {
        return new FunctionNotFoundException(formatForSchema(FNSNVK01,
            "Script function '" + name + "' for " + argCount + " argument(s) not found", token));
    }

    static MethodNotFoundException failOnMethodNotFound(String name, int argCount, EValue self,
                Token token) {
        return new MethodNotFoundException(formatForSchema(MTHNVK01, "Script method '"
            + name + "' for " + argCount + " argument(s) not found in " + self.getType(), token));
    }

    static VariadicInvocationException failOnVariadicArity(String code) {
        return new VariadicInvocationException(code,
            "Too few arguments for invocation of variadic function");
    }

    static InvocationRuntimeException failOnFixedArity(String code) {
        return new InvocationRuntimeException(code,
            "Invalid number of arguments for invocation of function");
    }

    static SystemOperationException failOnSystemException(String code,
                Exception exception, Token token) {
        return new SystemOperationException(formatForSchema(code, exception.getMessage(),
            token), exception);
    }

    static ScriptOperationException failOnOperation(String code, String operation,
                EValue value, Token token, Throwable cause) {
        return new ScriptOperationException(formatForSchema(code, "Invalid " + operation
            + " operation on type " + value.getType(), token), cause);
    }

    static ScriptOperationException failOnOperation(String code, String operation,
                EValue value, TerminalNode node) {
        return failOnOperation(code, operation, value, node.getSymbol(), null);
    }

    static ScriptOperationException failOnOperation(String code, String operation,
                EValue value1, EValue value2, Token token, Throwable cause) {
        return new ScriptOperationException(formatForSchema(code, "Invalid " + operation
            + " operation on types " + value1.getType() + " and " + value2.getType(),
            token), cause);
    }

    static ScriptOperationException failOnOperation(String code, String operation,
                EValue value1, EValue value2, TerminalNode node) {
        return failOnOperation(code, operation, value1, value2, node.getSymbol(), null);
    }

    static UpdateNotSupportedException failOnStringUpdate(String code, TerminalNode node) {
        return new UpdateNotSupportedException(formatForSchema(code,
            "Immutable string characters cannot be updated", node.getSymbol()));
    }

    static UpdateNotSupportedException failOnArrayRangeUpdate(String code, TerminalNode node) {
        return new UpdateNotSupportedException(formatForSchema(code,
            "Update a range of elements in array is not supported", node.getSymbol()));
    }
}