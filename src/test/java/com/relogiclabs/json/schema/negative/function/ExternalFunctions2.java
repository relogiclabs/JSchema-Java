package com.relogiclabs.json.schema.negative.function;

import com.relogiclabs.json.schema.function.FunctionBase;
import com.relogiclabs.json.schema.tree.RuntimeContext;
import com.relogiclabs.json.schema.types.JNumber;

public class ExternalFunctions2 extends FunctionBase {
    public ExternalFunctions2(RuntimeContext runtime) {
        super(runtime);
    }

    public void odd(JNumber source) {
        boolean result = source.toDouble() % 2 != 0;
        if(!result) throw new RuntimeException("Not an odd number");
    }
}