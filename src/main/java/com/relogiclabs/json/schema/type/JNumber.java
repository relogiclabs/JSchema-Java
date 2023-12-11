package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.internal.builder.JPrimitiveBuilder;

public abstract class JNumber extends JPrimitive {
    JNumber(JPrimitiveBuilder<?> builder) {
        super(builder);
    }

    public abstract double toDouble();

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

    @Override
    public JsonType getType() {
        return JsonType.NUMBER;
    }
}