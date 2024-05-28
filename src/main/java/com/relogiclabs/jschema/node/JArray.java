package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.exception.MisplacedOptionalException;
import com.relogiclabs.jschema.exception.UpdateNotSupportedException;
import com.relogiclabs.jschema.internal.builder.JArrayBuilder;
import com.relogiclabs.jschema.internal.message.ActualHelper;
import com.relogiclabs.jschema.internal.message.ExpectedHelper;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.relogiclabs.jschema.internal.message.MessageHelper.ArrayElementNotFound;
import static com.relogiclabs.jschema.internal.util.StringHelper.joinWith;
import static com.relogiclabs.jschema.message.ErrorCode.ARRY01;
import static com.relogiclabs.jschema.message.ErrorCode.ARRY02;
import static com.relogiclabs.jschema.message.ErrorCode.AUPD01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JArray extends JComposite implements EArray, Iterable<JNode> {
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
                return fail(new MisplacedOptionalException(formatForSchema(ARRY02,
                        "Mandatory array element cannot appear after optional element",
                        elements.get(i))));
            if(i >= other.elements.size() && !optional)
                return fail(new JsonSchemaException(
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
            return validator.isOptional();
        return false;
    }

    @Override
    public EValue get(int index) {
        return elements.get(index);
    }

    @Override
    public void set(int index, EValue value) {
        throw new UpdateNotSupportedException(AUPD01, "Readonly array cannot be updated");
    }

    @Override
    public List<? extends EValue> elements() {
        return elements;
    }

    @Override
    public Iterator<JNode> iterator() {
        return elements.iterator();
    }

    @Override
    public String toString() {
        return joinWith(elements, ", ", "[", "]");
    }
}