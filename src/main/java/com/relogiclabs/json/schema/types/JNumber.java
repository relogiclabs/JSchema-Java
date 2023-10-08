package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.tree.Context;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

public abstract class JNumber extends JPrimitive {
    protected JNumber(Map<JNode, JNode> relations, Context context) {
        super(relations, context);
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

    public boolean areEqual(double value1, double value2) {
        return getRuntime().areEqual(value1, value2);
    }

    @Override
    public JsonType getType() {
        return JsonType.NUMBER;
    }

    @Setter
    @Accessors(fluent = true)
    public abstract static class Builder<T extends Number>
            extends JNode.Builder<Builder<T>> {
        protected T value;
    }
}