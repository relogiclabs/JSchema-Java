package com.relogiclabs.json.schema.internal.util;

import com.relogiclabs.json.schema.collection.IndexMap;
import com.relogiclabs.json.schema.type.JNode;
import com.relogiclabs.json.schema.type.JProperty;
import com.relogiclabs.json.schema.type.JString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class CollectionHelper {
    public static <K, V> void merge(Map<K, List<V>> target, Map<K, List<V>> source) {
        source.forEach((sk, sv) -> {
            var tv = target.get(sk);
            if(tv != null) tv.addAll(sv);
            else target.put(sk, sv);
        });
    }

    public static Set<String> containsKeys(IndexMap<String, JProperty> map, JString... keys) {
        Set<String> set = Arrays.stream(keys).map(JString::getValue).collect(toSet());
        map.values().forEach(p -> set.remove(p.getKey()));
        return set;
    }

    public static Set<JNode> containsValues(IndexMap<String, JProperty> map, JNode... values) {
        // Here values should be distinct on parameter
        Set<JNode> set = Arrays.stream(values).collect(toSet());
        map.values().forEach(p -> set.remove(p.getValue()));
        return set;
    }

    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        List<T> list = new ArrayList<>(elements.length);
        for(var e : elements) if(e != null) list.add(e);
        return Collections.unmodifiableList(list);
    }

    @SafeVarargs
    public static <T> void addToList(Collection<T> source, T... elements) {
        for(var e : elements) if(e != null) source.add(e);
    }

    @SafeVarargs
    public static <T> void addToList(Collection<T> source, Collection<? extends T>... collections) {
        for(var c : collections) if(c != null) source.addAll(c);
    }

    public static <T> T getLast(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
    }
}