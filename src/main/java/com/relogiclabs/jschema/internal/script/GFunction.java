package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.internal.engine.Evaluator;
import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areCompatible;
import static com.relogiclabs.jschema.internal.script.GParameter.hasVariadic;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.subList;
import static com.relogiclabs.jschema.internal.util.CommonHelper.hasFlag;
import static com.relogiclabs.jschema.message.ErrorCode.FNSNVK02;

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
    public EValue invoke(List<EValue> arguments, ScriptScope parentScope) {
        return invoke(bind(arguments, parentScope));
    }

    private ScriptScope bind(List<EValue> arguments, ScriptScope parentScope) {
        areCompatible(parameters, arguments, FNSNVK02);
        var scope = new ScriptScope(parentScope);
        if(parameters.length == 0) return scope;
        var endIndex = parameters.length - 1;
        for(int i = 0; i < endIndex; i++)
            scope.addVariable(parameters[i].getName(), arguments.get(i));
        if(parameters[endIndex].isVariadic()) scope.addVariable(parameters[endIndex].getName(),
            new GArray(subList(arguments, endIndex)));
        else scope.addVariable(parameters[endIndex].getName(), arguments.get(endIndex));
        return scope;
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