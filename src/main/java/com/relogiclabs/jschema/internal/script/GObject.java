package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.relogiclabs.jschema.internal.util.StringHelper.joinWith;
import static com.relogiclabs.jschema.internal.util.StringHelper.quote;

@Getter
@EqualsAndHashCode
public final class GObject implements EObject {
    private final Map<String, EValue> properties;

    public GObject(int capacity) {
        properties = new LinkedHashMap<>(capacity);
    }

    public GObject(EObject value) {
        this(value.size());
        for(var e : value.keySet()) properties.put(e, new GReference(value.get(e)));
    }

    public GObject(List<String> keys, List<EValue> values) {
        this(keys.size());
        for(int i = 0; i < keys.size(); i++)
            properties.put(keys.get(i), new GReference(values.get(i)));
    }

    @Override
    public EValue get(String key) {
        var value = properties.get(key);
        if(value == null) properties.put(key, value = new GReference(VOID));
        return value;
    }

    public void set(String key, EValue value) {
        properties.put(key, value);
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
    public String toString() {
        return joinWith(properties.entrySet().stream()
                .map(e -> quote(e.getKey()) + ": " + e.getValue()), ", ", "{", "}");
    }
}