package com.relogiclabs.jschema.test.external;

import com.relogiclabs.jschema.function.FunctionProvider;
import com.relogiclabs.jschema.node.JNumber;
import com.relogiclabs.jschema.tree.RuntimeContext;

// Functions for negative (error) test cases
public class ExternalFunctions4 extends FunctionProvider {
    public ExternalFunctions4(RuntimeContext runtime) {
        super(runtime);
    }

    public boolean canTest(JNumber target) {
        // If you just want to throw any exception without details
        return fail(new RuntimeException("something went wrong"));
    }
}