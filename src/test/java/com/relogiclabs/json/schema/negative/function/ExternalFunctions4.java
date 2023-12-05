package com.relogiclabs.json.schema.negative.function;

import com.relogiclabs.json.schema.function.FunctionBase;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.types.JNumber;

public class ExternalFunctions4 extends FunctionBase {
    public ExternalFunctions4(RuntimeContext runtime) {
        super(runtime);
    }

    public boolean canTest(JNumber target) {
        // If you just want to throw any exception without details
        return failWith(new RuntimeException("something went wrong"));
    }
}