package com.relogiclabs.json.schema.external;

import com.relogiclabs.json.schema.type.JNumber;

// Functions for negative (error) test cases
public class ExternalFunctions1 {
    public boolean odd(JNumber target) {
        boolean result = (target.toDouble() % 2 != 0);
        if(!result) throw new RuntimeException("Not an odd number");
        return true;
    }
}