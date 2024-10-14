package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.exception.UpdateNotSupportedException;
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
import static com.relogiclabs.jschema.message.ErrorCode.ROOBJT02;

@Getter
@EqualsAndHashCode
public final class GObject implements EObject {
    private final Map<String, EValue> properties;

    public GObject(int capacity) {
        properties = new LinkedHashMap<>(capacity);
    }

    public GObject(EObject value) {
        this(value.size());
        for(var k : value.keySet()) properties.put(k, new GLeftValue(value.get(k)));
    }

    public GObject(List<String> keys, List<EValue> values) {
        this(keys.size());
        for(int i = 0; i < keys.size(); i++)
            properties.put(keys.get(i), new GLeftValue(values.get(i)));
    }

    @Override
    public EValue get(String key) {
        return properties.get(key);
    }

    @Override
    public void set(String key, EValue value) {
        var oldValue = properties.get(key);
        if(oldValue == null) {
            properties.put(key, new GLeftValue(value));
            return;
        }
        if(!(oldValue instanceof GLeftValue l)) throw new UpdateNotSupportedException(ROOBJT02,
            "Readonly object property '" + key + "' cannot be updated");
        l.setValue(value);
    }

    @Override
    public boolean isReadonly() {
        return false;
    }

    public void put(String key, EValue value) {
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