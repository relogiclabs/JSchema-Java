package com.relogiclabs.jschema.internal.library;

import com.relogiclabs.jschema.internal.engine.ScopeContext;
import com.relogiclabs.jschema.internal.script.GParameter;
import com.relogiclabs.jschema.internal.script.RFunction;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

import static com.relogiclabs.jschema.internal.engine.ScriptTreeHelper.areCompatible;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.getLast;
import static com.relogiclabs.jschema.message.ErrorCode.FUNS06;

@Getter
public class NFunction implements RFunction {
    private final NHandler handler;
    private final GParameter[] parameters;

    public NFunction(NHandler handler, String... parameters) {
        this.handler = handler;
        this.parameters = toParameters(parameters);
    }

    private static GParameter[] toParameters(String... names) {
        return Arrays.stream(names).map(GParameter::new).toArray(GParameter[]::new);
    }

    @Override
    public EValue invoke(ScopeContext functionScope, List<EValue> arguments) {
        return handler.invoke(functionScope, arguments);
    }

    @Override
    public ScopeContext bind(ScopeContext parentScope, List<EValue> arguments) {
        areCompatible(parameters, arguments, FUNS06);
        return parentScope;
    }

    @Override
    public boolean isVariadic() {
        if(parameters.length == 0) return false;
        return getLast(parameters).isVariadic();
    }
}