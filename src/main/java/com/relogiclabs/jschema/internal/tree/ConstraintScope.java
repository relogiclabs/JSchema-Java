package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.internal.engine.ScriptScope;
import com.relogiclabs.jschema.internal.script.GLeftValue;
import com.relogiclabs.jschema.node.JReceiver;
import com.relogiclabs.jschema.type.EValue;

import java.util.ArrayList;
import java.util.List;

final class ConstraintScope extends ScriptScope {
    private final List<GLeftValue> receivers = new ArrayList<>(4);

    public ConstraintScope(ScriptScope parent) {
        super(parent);
    }

    @Override
    public GLeftValue addVariable(String name, EValue value) {
        var lvalue = super.addVariable(name, value);
        if(value instanceof JReceiver) receivers.add(lvalue);
        return lvalue;
    }

    public void unpackReceivers() {
        for(var lvalue : receivers) {
            var list = ((JReceiver) lvalue.getValue()).getValueNodes();
            if(list.size() == 1) lvalue.setValue(list.get(0));
        }
    }
}