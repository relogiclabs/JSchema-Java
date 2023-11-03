package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.ArrayElementNotFound;
import static com.relogiclabs.json.schema.internal.util.StringHelper.join;
import static com.relogiclabs.json.schema.message.ErrorCode.ARRY01;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JArray extends JComposite {
    private final List<JNode> elements;

    private JArray(Builder builder) {
        super(builder);
        elements = requireNonNull(builder.elements);
        children = elements;
    }

    @Override
    public JsonType getType() {
        return JsonType.ARRAY;
    }

    @Override
    public Collection<? extends JNode> components() {
        return elements;
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JArray.class);
        if(other == null) return false;
        var result = true;
        for(var i = 0; i < elements.size(); i++) {
            if(i >= other.elements.size() && !((JValidator) elements.get(i)).getOptional())
                return failWith(new JsonSchemaException(
                        new ErrorDetail(ARRY01, ArrayElementNotFound),
                        ExpectedHelper.asArrayElementNotFound(elements.get(i), i),
                        ActualHelper.asArrayElementNotFound(other, i)));
            if(i >= other.elements.size()) continue;
            result &= elements.get(i).match(other.elements.get(i));
        }
        return result;
    }

    @Override
    public String toString() {
        return "[" + join(elements, ", ") + "]";
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        private List<JNode> elements;

        @Override
        public JArray build() {
            return build(new JArray(this));
        }
    }
}