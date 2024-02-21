package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.internal.engine.Evaluator;
import com.relogiclabs.jschema.internal.engine.ScopeContext;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areCompatible;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.getLast;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.subList;
import static com.relogiclabs.jschema.internal.util.MiscellaneousHelper.hasFlag;
import static com.relogiclabs.jschema.message.ErrorCode.FUNS05;

@Getter
@RequiredArgsConstructor
public final class GFunction implements RFunction {
    public static final String CONSTRAINT_MARKER = "@";
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
        this.variadic = parameters.length != 0 && getLast(parameters).isVariadic();
    }

    @Override
    public EValue invoke(ScopeContext functionScope, List<EValue> arguments) {
        return invoke(functionScope);
    }

    public EValue invoke(ScopeContext functionScope) {
        var v1 = getBody().evaluate(functionScope);
        if(v1 instanceof GControl ctrl) return ctrl.getValue();
        return VOID;
    }

    @Override
    public ScopeContext bind(ScopeContext parentScope, List<EValue> arguments) {
        areCompatible(parameters, arguments, FUNS05);
        var scope = new ScopeContext(parentScope);
        var i = 0;
        for(var p : parameters) {
            if(p.isVariadic()) scope.addVariable(p.getName(), new GArray(subList(arguments, i)));
            else scope.addVariable(p.getName(), arguments.get(i++));
        }
        return scope;
    }

    public boolean isFuture() {
        return hasFlag(mode, FUTURE_MODE);
    }

    public static boolean isConstraint(int mode) {
        return hasFlag(mode, CONSTRAINT_MODE);
    }
}