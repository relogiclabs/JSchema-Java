package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.internal.builder.JPrimitiveBuilder;
import com.relogiclabs.jschema.type.ENumber;

public abstract class JNumber extends JPrimitive implements ENumber, Comparable<ENumber> {
    JNumber(JPrimitiveBuilder<?> builder) {
        super(builder);
    }

    @Override
    public int compareTo(ENumber other) {
        return compareTo(other.toDouble());
    }

    public int compareTo(double other) {
        return getRuntime().compare(toDouble(), other);
    }

    boolean areEqual(double value1, double value2) {
        return getRuntime().areEqual(value1, value2);
    }
}