package com.relogiclabs.jschema.internal.util;

import com.relogiclabs.jschema.collection.IndexMap;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JProperty;
import com.relogiclabs.jschema.node.JString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class CollectionHelper {
    private CollectionHelper() {
        throw new UnsupportedOperationException("This class is not intended for instantiation");
    }

    public static Set<String> containsKeys(IndexMap<String, JProperty> map, JString... keys) {
        Set<String> set = Arrays.stream(keys).map(JString::getValue).collect(toSet());
        map.values().forEach(p -> set.remove(p.getKey()));
        return set;
    }

    public static Set<JNode> containsValues(IndexMap<String, JProperty> map, JNode... values) {
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
        return list.get(list.size() - 1);
    }

    public static <T> T tryGetLast(List<T> list) {
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    public static <T> List<T> subList(List<T> list, int fromIndex) {
        return list.subList(fromIndex, list.size());
    }
}