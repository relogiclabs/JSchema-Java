package com.relogiclabs.jschema.test.external;

import com.relogiclabs.jschema.function.FunctionProvider;
import com.relogiclabs.jschema.node.JNumber;

// Functions for negative (error) test cases
public class ExternalFunctions2 extends FunctionProvider {
    public void odd(JNumber target) {
        // Precision loss is not considered here
        if(target.toDouble() % 2 != 0) return;
        throw new RuntimeException("Not an odd number");
    }
}