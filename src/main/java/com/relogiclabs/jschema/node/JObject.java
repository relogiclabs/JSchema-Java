package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.collection.IndexMap;
import com.relogiclabs.jschema.exception.PropertyOrderException;
import com.relogiclabs.jschema.exception.UpdateNotSupportedException;
import com.relogiclabs.jschema.exception.ValueValidationException;
import com.relogiclabs.jschema.internal.builder.JObjectBuilder;
import com.relogiclabs.jschema.internal.message.ActualHelper;
import com.relogiclabs.jschema.internal.message.ExpectedHelper;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.relogiclabs.jschema.internal.message.MessageHelper.PropertyNotFound;
import static com.relogiclabs.jschema.internal.message.MessageHelper.PropertyOrderMismatched;
import static com.relogiclabs.jschema.internal.message.MessageHelper.UndefinedPropertyFound;
import static com.relogiclabs.jschema.internal.util.CommonHelper.nonNullFrom;
import static com.relogiclabs.jschema.internal.util.StringHelper.joinWith;
import static com.relogiclabs.jschema.message.ErrorCode.PRTFND01;
import static com.relogiclabs.jschema.message.ErrorCode.PRTORD01;
import static com.relogiclabs.jschema.message.ErrorCode.PRTUDF01;
import static com.relogiclabs.jschema.message.ErrorCode.ROOBJT01;
import static java.util.Objects.requireNonNull;

@EqualsAndHashCode
public final class JObject extends JComposite implements EObject, Iterable<JProperty> {
    @Getter private final IndexMap<String, JProperty> properties;
    private final List<JNode> components;

    private JObject(JObjectBuilder builder) {
        super(builder);
        properties = requireNonNull(builder.properties());
        components = properties.values().stream().map(JProperty::getValue).toList();
        children = properties.values();
    }

    public static JObject from(JObjectBuilder builder) {
        return new JObject(builder).initialize();
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JObject.class);
        if(other == null) return false;
        var result = true;
        var unresolved = new HashSet<>(other.properties.keySet());
        for(var i = 0; i < properties.size(); i++) {
            var thisProp = properties.get(i);
            try {
                var otherProp = getOtherProp(other, i);
                if(otherProp != null) {
                    result &= thisProp.getValue().match(otherProp.getValue());
                    unresolved.remove(thisProp.getKey());
                    continue;
                }
                if(!((JValidator) thisProp.getValue()).isOptional())
                    result &= fail(new ValueValidationException(
                        new ErrorDetail(PRTFND01, PropertyNotFound),
                        ExpectedHelper.asPropertyNotFound(thisProp),
                        ActualHelper.asPropertyNotFound(node, thisProp, other)));
            } catch(PropertyOrderException e) {
                result &= fail(new ValueValidationException(
                    new ErrorDetail(e.getCode(), e.getMessage()),
                    ExpectedHelper.asPropertyOrderMismatched(thisProp),
                    ActualHelper.asPropertyOrderMismatched(nonNullFrom(e.getFailOn(), other)), e));
                unresolved.remove(thisProp.getKey());
            }
        }
        if(unresolved.isEmpty() || getRuntime().getPragmas()
            .isIgnoreUndefinedProperties()) return result;
        for(String key : unresolved) {
            var property = other.properties.get(key);
            result &= fail(new ValueValidationException(
                new ErrorDetail(PRTUDF01, UndefinedPropertyFound),
                ExpectedHelper.asUndefinedPropertyFound(this, property),
                ActualHelper.asUndefinedPropertyFound(property)));
        }
        return result;
    }

    private JProperty getOtherProp(JObject other, int index) {
        var thisProp = properties.get(index);
        JProperty otherProp = null;
        if(!getRuntime().getPragmas().isIgnoreObjectPropertyOrder()) {
            var atProp = getPropAt(other.properties, index);
            if(areKeysEqual(atProp, thisProp)) otherProp = atProp;
            JProperty existing = null;
            if(otherProp == null) existing = other.properties.get(thisProp.getKey());
            if(otherProp == null && existing != null) throw new PropertyOrderException(PRTORD01,
                PropertyOrderMismatched, atProp);
        } else otherProp = other.properties.get(thisProp.getKey());
        return otherProp;
    }

    private static JProperty getPropAt(IndexMap<String, JProperty> properties, int index) {
        return index >= properties.size() ? null : properties.get(index);
    }

    private static boolean areKeysEqual(JProperty p1, JProperty p2) {
        if(p1 == null || p2 == null) return false;
        return p1.getKey().equals(p2.getKey());
    }

    @Override
    public Collection<? extends JNode> components() {
        return components;
    }

    @Override
    public EValue get(String key) {
        var property = properties.get(key);
        return property == null ? null : property.getValue();
    }

    @Override
    public void set(String key, EValue value) {
        throw new UpdateNotSupportedException(ROOBJT01, "Readonly object cannot be updated");
    }

    @Override
    public int size() {
        return properties.size();
    }

    @Override
    public Set<String> keySet() {
        return properties.keySet();
    }

    @Override
    public Iterator<JProperty> iterator() {
        return properties.iterator();
    }

    @Override
    public String toString() {
        return joinWith(properties.values(), ", ", "{", "}");
    }
}