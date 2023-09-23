package com.relogiclabs.json.schema.negative.function;

import com.relogiclabs.json.schema.types.JNumber;

public class ExternalFunctions1 {
    public boolean odd(JNumber target) {
        boolean result = (target.toDouble() % 2 != 0);
        if(!result) throw new RuntimeException("Not an odd number");
        return true;
    }
}