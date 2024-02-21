package com.relogiclabs.jschema.test.external;

import com.relogiclabs.jschema.function.FunctionProvider;
import com.relogiclabs.jschema.tree.RuntimeContext;

// Functions for negative (error) test cases
public class ExternalFunctions3 extends FunctionProvider {
    public ExternalFunctions3(RuntimeContext runtime) {
        super(runtime);
    }

    public boolean odd() {
        throw new UnsupportedOperationException();
    }
}