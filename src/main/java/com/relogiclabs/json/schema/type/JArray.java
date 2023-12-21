package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.exception.MisplacedOptionalException;
import com.relogiclabs.json.schema.internal.builder.JArrayBuilder;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.ArrayElementNotFound;
import static com.relogiclabs.json.schema.internal.util.StringHelper.joinWith;
import static com.relogiclabs.json.schema.message.ErrorCode.ARRY01;
import static com.relogiclabs.json.schema.message.ErrorCode.ARRY02;
import static com.relogiclabs.json.schema.message.MessageFormatter.formatForSchema;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JArray extends JComposite {
    private final List<JNode> elements;

    private JArray(JArrayBuilder builder) {
        super(builder);
        elements = requireNonNull(builder.elements());
        children = elements;
    }

    public static JArray from(JArrayBuilder builder) {
        return new JArray(builder).initialize();
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
        boolean result = true, restOptional = false;
        for(var i = 0; i < elements.size(); i++) {
            var optional = isOptional(elements.get(i));
            if((restOptional |= optional) != optional)
                return failWith(new MisplacedOptionalException(formatForSchema(ARRY02,
                        "Mandatory array element cannot appear after optional element",
                        elements.get(i))));
            if(i >= other.elements.size() && !optional)
                return failWith(new JsonSchemaException(
                        new ErrorDetail(ARRY01, ArrayElementNotFound),
                        ExpectedHelper.asArrayElementNotFound(elements.get(i), i),
                        ActualHelper.asArrayElementNotFound(other, i)));
            if(i >= other.elements.size()) continue;
            result &= elements.get(i).match(other.elements.get(i));
        }
        return result;
    }

    private static boolean isOptional(JNode node) {
        if(node instanceof JValidator validator)
            return validator.getOptional();
        return false;
    }

    @Override
    public String toString() {
        return joinWith(elements, ", ", "[", "]");
    }
}