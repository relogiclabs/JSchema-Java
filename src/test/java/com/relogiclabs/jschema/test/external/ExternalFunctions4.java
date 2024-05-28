package com.relogiclabs.jschema.test.external;

import com.relogiclabs.jschema.function.FunctionProvider;
import com.relogiclabs.jschema.node.JNumber;

// Functions for negative (error) test cases
public class ExternalFunctions4 extends FunctionProvider {
    public boolean canTest(JNumber target) {
        // Any RuntimeException can be thrown without details
        return fail(new RuntimeException("something went wrong"));
    }
}