package com.relogiclabs.jschema.tree;

import com.relogiclabs.jschema.node.JRoot;

public interface DataTree {
    boolean match(DataTree dataTree);
    RuntimeContext getRuntime();
    JRoot getRoot();
    TreeType getType();
}