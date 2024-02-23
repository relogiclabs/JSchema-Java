package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JPrimitiveBuilder;
import com.relogiclabs.jschema.type.ENumber;

public abstract class JNumber extends JPrimitive implements ENumber {
    JNumber(JPrimitiveBuilder<?> builder) {
        super(builder);
    }

    public double compare(JNumber number) {
        return compare(number.toDouble());
    }

    public double compare(double other) {
        double number = toDouble();
        if(areEqual(number, other)) return 0;
        return Math.signum(number - other);
    }

    boolean areEqual(double value1, double value2) {
        return getRuntime().areEqual(value1, value2);
    }
}