package com.relogiclabs.jschema.test.external;

import com.relogiclabs.jschema.node.JNumber;

// Functions for negative (error) test cases
public class ExternalFunctions1 {
    public boolean odd(JNumber target) {
        boolean result = (target.toDouble() % 2 != 0);
        if(!result) throw new RuntimeException("Not an odd number");
        return true;
    }
}