package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.collection.IndexHashMap;
import com.relogiclabs.json.schema.collection.IndexMap;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
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
import static com.relogiclabs.json.schema.internal.util.StringHelper.toUnitedString;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP05;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP06;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP07;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JObject extends JComposite {
    private final IndexMap<String, JProperty> properties;
    private final List<JNode> components;

    private JObject(Builder builder) {
        super(builder.relations, builder.context);
        this.properties = requireNonNull(builder.properties);
        this.components = builder.properties.values()
                .stream().map(JProperty::getValue).toList();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public JsonType getType() {
        return JsonType.OBJECT;
    }

    @Override
    public Collection<? extends JNode> getChildren() {
        return properties.values();
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
        if(unresolved.size() > 0 && !getRuntime().getIgnoreUndefinedProperties()) {
            for(String key : unresolved) {
                var property = other.properties.get(key);
                return failWith(new JsonSchemaException(
                        new ErrorDetail(PROP06, UndefinedPropertyFound),
                        ExpectedHelper.asUndefinedProperty(this, property),
                        ActualHelper.asUndefinedProperty(property)));
            }
        }
        return result;
    }

    private JProperty getOtherProp(JObject other, int index) {
        var thisProp = properties.get(index);
        JProperty otherProp = null;
        if(!getRuntime().getIgnoreObjectPropertyOrder()) {
            var atProp = getPropAt(other.properties, index);
            if(isKeyEquals(atProp, thisProp)) otherProp = atProp;
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

    private JProperty getPropAt(IndexMap<String, JProperty> properties, int index) {
        return index >= properties.size() ? null : properties.get(index);
    }

    private boolean isKeyEquals(JProperty p1, JProperty p2) {
        if(p1 == null || p2 == null) return false;
        return p1.getKey().equals(p2.getKey());
    }

    @Override
    public String toString() {
        return "{" + toUnitedString(properties.values(), ", ") + "}";
    }

    @Override
    public Collection<? extends JNode> components() {
        return components;
    }

    public static class Builder extends JNode.Builder<Builder> {
        protected IndexMap<String, JProperty> properties;

        public Builder properties(List<JProperty> properties) {
            var indexMap = new IndexHashMap<>(properties);
            indexMap.makeReadOnly();
            this.properties = indexMap;
            return this;
        }

        @Override
        public JObject build() {
            return new JObject(this).initialize();
        }
    }
}