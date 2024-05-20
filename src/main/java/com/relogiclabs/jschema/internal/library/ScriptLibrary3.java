package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GParameter;
import com.relogiclabs.jschema.internal.script.RFunction;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areCompatible;
import static com.relogiclabs.jschema.internal.script.RFunction.hasVariadic;
import static com.relogiclabs.jschema.message.ErrorCode.FNVK03;

public final class ScriptLibrary3 extends ScriptLibrary2 {
    private static final ScriptLibrary3 LIBRARY = new ScriptLibrary3();
    private final Map<String, RFunction> functions;

    @Getter
    private static class LibraryFunction implements RFunction {
        private final GParameter[] parameters;
        private final boolean variadic;
        private final FunctionEvaluator body;

        public LibraryFunction(FunctionEvaluator body, String... parameters) {
            this.body = body;
            this.parameters = toParameters(parameters);
            this.variadic = hasVariadic(this.parameters);
        }

        private static GParameter[] toParameters(String... names) {
            return Arrays.stream(names).map(GParameter::new).toArray(GParameter[]::new);
        }

        @Override
        public ScriptScope bind(ScriptScope parentScope, List<EValue> arguments) {
            areCompatible(parameters, arguments, FNVK03);
            return parentScope;
        }

        @Override
        public EValue invoke(ScriptScope functionScope, List<EValue> arguments) {
            return body.evaluate(functionScope, arguments);
        }
    }

    private ScriptLibrary3() {
        functions = new HashMap<>(10);
        addFunction(Print_F1, ScriptLibrary2::printFunction, Message_Id);
        addFunction(Fail_F1, ScriptLibrary2::failFunction1, Message_Id);
        addFunction(Fail_F2, ScriptLibrary2::failFunction2, Code_Id, Message_Id);
        addFunction(Fail_F4, ScriptLibrary2::failFunction4, Code_Id, Message_Id, Expected_Id, Actual_Id);
        addFunction(Expected_F1, ScriptLibrary2::expectedFunction1, Message_Id);
        addFunction(Expected_F2, ScriptLibrary2::expectedFunction2, Node_Id, Message_Id);
        addFunction(Actual_F1, ScriptLibrary2::actualFunction1, Message_Id);
        addFunction(Actual_F2, ScriptLibrary2::actualFunction2, Node_Id, Message_Id);
        addFunction(Ticks_F0, ScriptLibrary2::ticksFunction);
    }

    private void addFunction(String functionId, FunctionEvaluator body, String... parameters) {
        functions.put(functionId, new LibraryFunction(body, parameters));
    }

    public static EValue resolveStatic(String name) {
        return LIBRARY.functions.get(name);
    }
}