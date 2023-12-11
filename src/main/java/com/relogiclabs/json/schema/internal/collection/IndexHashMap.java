package com.relogiclabs.json.schema.internal.collection;

import com.relogiclabs.json.schema.collection.IndexMap;
import com.relogiclabs.json.schema.collection.Keyable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class IndexHashMap<TK, TV extends Keyable<TK>> implements IndexMap<TK, TV> {

    private boolean unmodifiable;
    private Map<TK, TV> map;
    private List<TV> list;

    public IndexHashMap(Collection<? extends TV> c) {
        map = new HashMap<>();
        list = new ArrayList<>();
        addAll(c);
    }

    @Override
    public TV get(int index) {
        return list.get(index);
    }

    @Override
    public TV get(TK key) {
        return map.get(key);
    }

    @Override
    public Set<TK> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<TV> values() {
        return list;
    }

    @Override
    public boolean isUnmodifiable() {
        return unmodifiable;
    }

    @Override
    public IndexHashMap<TK, TV> asUnmodifiable() {
        if(unmodifiable) return this;
        list = Collections.unmodifiableList(list);
        map = Collections.unmodifiableMap(map);
        unmodifiable = true;
        return this;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<TV> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(TV value) {
        if(map.containsKey(value.getKey())) return false;
        list.add(value);
        map.put(value.getKey(), value);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if(o instanceof Keyable<?> value) {
            boolean result = list.remove(value);
            if(result) map.remove(value.getKey());
            return result;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object o : c) if(!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends TV> c) {
        boolean result = false;
        for(TV value : c) result |= add(value);
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for(Object o : c) result |= remove(o);
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = false;
        for(TV value : list) if(!c.contains(value)) result |= remove(value);
        return result;
    }

    @Override
    public void clear() {
        list.clear();
        map.clear();
    }
}