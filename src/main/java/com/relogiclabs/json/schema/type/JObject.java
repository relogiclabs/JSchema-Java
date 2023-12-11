package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.collection.IndexMap;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.builder.JObjectBuilder;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.PropertyNotFound;
import static com.relogiclabs.json.schema.internal.message.MessageHelper.PropertyOrderMismatch;
import static com.relogiclabs.json.schema.internal.message.MessageHelper.UndefinedPropertyFound;
import static com.relogiclabs.json.schema.internal.util.MiscellaneousHelper.nonNull;
import static com.relogiclabs.json.schema.internal.util.StringHelper.join;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP05;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP06;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP07;
import static java.util.Objects.requireNonNull;

@EqualsAndHashCode
public final class JObject extends JComposite {
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
    public JsonType getType() {
        return JsonType.OBJECT;
    }

    @Override
    public boolean match(JNode node) {
        var other = castType(node, JObject.class);
        if(other == null) return false;
        var result = true;
        var unresolved = new HashSet<>(other.properties.keySet());
        for(var i = 0; i < properties.size(); i++) {
            var thisProp = properties.get(i);
            var otherProp = getOtherProp(other, i);
            if(otherProp != null) {
                result &= thisProp.getValue().match(otherProp.getValue());
                unresolved.remove(thisProp.getKey());
                continue;
            }
            if(!((JValidator) thisProp.getValue()).getOptional())
                return failWith(new JsonSchemaException(
                        new ErrorDetail(PROP05, PropertyNotFound),
                        ExpectedHelper.asPropertyNotFound(thisProp),
                        ActualHelper.asPropertyNotFound(node, thisProp)));
        }
        if(unresolved.isEmpty() || getRuntime().getPragmas()
                .getIgnoreUndefinedProperties()) return result;
        for(String key : unresolved) {
            var property = other.properties.get(key);
            result &= failWith(new JsonSchemaException(
                    new ErrorDetail(PROP06, UndefinedPropertyFound),
                    ExpectedHelper.asUndefinedProperty(this, property),
                    ActualHelper.asUndefinedProperty(property)));
        }
        return result;
    }

    private JProperty getOtherProp(JObject other, int index) {
        var thisProp = properties.get(index);
        JProperty otherProp = null;
        if(!getRuntime().getPragmas().getIgnoreObjectPropertyOrder()) {
            var atProp = getPropAt(other.properties, index);
            if(areKeysEqual(atProp, thisProp)) otherProp = atProp;
            JProperty existing = null;
            if(otherProp == null) existing = other.properties.get(thisProp.getKey());
            if(otherProp == null && existing != null)
                failWith(new JsonSchemaException(
                        new ErrorDetail(PROP07, PropertyOrderMismatch),
                        ExpectedHelper.asPropertyOrderMismatch(thisProp),
                        ActualHelper.asPropertyOrderMismatch(nonNull(atProp, other))));
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
    public String toString() {
        return "{" + join(properties.values(), ", ") + "}";
    }

    @Override
    public Collection<? extends JNode> components() {
        return components;
    }
}