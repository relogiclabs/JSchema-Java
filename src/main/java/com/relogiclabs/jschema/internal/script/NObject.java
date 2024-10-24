package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.exception.DataOwnerMismatchedException;
import com.relogiclabs.jschema.exception.UpdateNotSupportedException;
import com.relogiclabs.jschema.type.EObject;
import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.type.EValue;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.relogiclabs.jschema.internal.util.StringHelper.joinWith;
import static com.relogiclabs.jschema.internal.util.StringHelper.quote;
import static com.relogiclabs.jschema.message.ErrorCode.OWNRMS01;
import static com.relogiclabs.jschema.message.ErrorCode.RONOBJ01;
import static java.util.Objects.requireNonNull;

public class NObject implements EObject {
    private final Map<String, EValue> properties;

    @Getter @Setter
    private DataOwner owner;

    public NObject(int capacity, DataOwner owner) {
        this.properties = new HashMap<>(capacity);
        this.owner = owner;
    }

    @Override
    public EValue get(String key) {
        return properties.get(key);
    }

    @Override
    public void set(String key, EValue value) {
        throw new UpdateNotSupportedException(RONOBJ01, "Readonly object cannot be updated");
    }

    public void put(String key, EValue value) {
        requireNonNull(key, "Key cannot be null");
        requireNonNull(value, "Value cannot be null");
        properties.put(key, value);
    }

    public void requireOwner(DataOwner expected) {
        if(owner != expected) throw new DataOwnerMismatchedException(OWNRMS01,
            "Data owner must be '" + expected + "' but found '" + owner + "'");
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
    public EType getType() {
        return EType.NATIVE;
    }

    @Override
    public String toString() {
        return joinWith(properties.entrySet().stream()
            .map(e -> quote(e.getKey()) + ": " + e.getValue()), ", ", "{", "}");
    }
}