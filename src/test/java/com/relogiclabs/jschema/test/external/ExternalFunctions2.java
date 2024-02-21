package com.relogiclabs.jschema.test.external;

import com.relogiclabs.jschema.function.FunctionProvider;
import com.relogiclabs.jschema.node.JNumber;
import com.relogiclabs.jschema.tree.RuntimeContext;

// Functions for negative (error) test cases
public class ExternalFunctions2 extends FunctionProvider {
    public ExternalFunctions2(RuntimeContext runtime) {
        super(runtime);
    }

    public void odd(JNumber source) {
        boolean result = source.toDouble() % 2 != 0;
        if(!result) throw new RuntimeException("Not an odd number");
    }
}