package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.type.EValue;

public interface JsonTypable extends EValue {
    JNode getNode();
}