package com.relogiclabs.jschema.internal.engine;

import com.relogiclabs.jschema.internal.script.GLeftValue;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JReceiver;
import com.relogiclabs.jschema.type.EValue;

import java.util.ArrayList;
import java.util.List;

public final class ConstraintScope extends ScriptScope {
    private final List<GLeftValue> leftValues = new ArrayList<>(4);

    public ConstraintScope(ScriptScope parent) {
        super(parent);
    }

    @Override
    public GLeftValue addVariable(String key, EValue value) {
        var lvalue = super.addVariable(key, value);
        if(value instanceof JReceiver) leftValues.add(lvalue);
        return lvalue;
    }

    public void unpackReceivers() {
        for(var lvalue : leftValues) {
            var list = lvalue.getValue(JReceiver.class).getValueNodes(JNode.class);
            if(list.size() == 1) lvalue.setValue(list.get(0));
        }
    }
}