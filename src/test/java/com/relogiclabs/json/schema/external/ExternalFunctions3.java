package com.relogiclabs.json.schema.external;

import com.relogiclabs.json.schema.function.FunctionBase;
import com.relogiclabs.json.schema.tree.RuntimeContext;

// Functions for negative (error) test cases
public class ExternalFunctions3 extends FunctionBase {
    public ExternalFunctions3(RuntimeContext runtime) {
        super(runtime);
    }

    public boolean odd() {
        throw new UnsupportedOperationException();
    }
}