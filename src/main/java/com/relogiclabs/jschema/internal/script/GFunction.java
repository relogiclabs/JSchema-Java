package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.internal.engine.Evaluator;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areCompatible;
import static com.relogiclabs.jschema.internal.script.RFunction.hasVariadic;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.subList;
import static com.relogiclabs.jschema.internal.util.CommonHelper.hasFlag;
import static com.relogiclabs.jschema.message.ErrorCode.FNVK02;

@Getter
@RequiredArgsConstructor
public final class GFunction implements RFunction {
    public static final int CONSTRAINT_MODE = 1;
    public static final int FUTURE_MODE = 3;
    public static final int SUBROUTINE_MODE = 4;

    private final GParameter[] parameters;
    private final boolean variadic;
    private final Evaluator body;
    private final int mode;

    public GFunction(GParameter[] parameters, Evaluator body, int mode) {
        this.parameters = parameters;
        this.body = body;
        this.mode = mode;
        this.variadic = hasVariadic(parameters);
    }

    @Override
    public ScriptScope bind(ScriptScope parentScope, List<EValue> arguments) {
        areCompatible(parameters, arguments, FNVK02);
        var scope = new ScriptScope(parentScope);
        var i = 0;
        for(var p : parameters) scope.addVariable(p.getName(), p.isVariadic()
                ? new GArray(subList(arguments, i)) : arguments.get(i++));
        return scope;
    }

    @Override
    public EValue invoke(ScriptScope functionScope, List<EValue> arguments) {
        return invoke(functionScope);
    }

    public EValue invoke(ScriptScope functionScope) {
        var result = getBody().evaluate(functionScope);
        if(result instanceof GControl ctrl) return ctrl.getValue();
        return VOID;
    }

    public boolean isConstraint() {
        return hasFlag(mode, CONSTRAINT_MODE);
    }

    public boolean isFuture() {
        return hasFlag(mode, FUTURE_MODE);
    }

    public boolean isSubroutine() {
        return hasFlag(mode, SUBROUTINE_MODE);
    }
}