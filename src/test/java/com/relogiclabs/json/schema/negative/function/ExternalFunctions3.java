package com.relogiclabs.json.schema.negative.function;

import com.relogiclabs.json.schema.function.FunctionBase;
import com.relogiclabs.json.schema.tree.RuntimeContext;

public class ExternalFunctions3 extends FunctionBase {
    public ExternalFunctions3(RuntimeContext runtime) {
        super(runtime);
    }

    public boolean odd() {
        throw new UnsupportedOperationException();
    }
}
