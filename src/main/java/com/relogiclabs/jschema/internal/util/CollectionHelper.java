package com.relogiclabs.jschema.internal.util;

import com.relogiclabs.jschema.collection.IndexMap;
import com.relogiclabs.jschema.node.JProperty;
import com.relogiclabs.jschema.type.EString;
import com.relogiclabs.jschema.type.EValue;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

public final class CollectionHelper {
    private CollectionHelper() {
        throw new UnsupportedOperationException("This class is not intended for instantiation");
    }

    public static Set<String> containsKeys(IndexMap<String, JProperty> map, EString... keys) {
        Set<String> set = stream(keys).map(EString::getValue).collect(toSet());
        map.values().forEach(p -> set.remove(p.getKey()));
        return set;
    }

    public static Set<EValue> containsValues(IndexMap<String, JProperty> map, EValue... values) {
        Set<EValue> set = stream(values).collect(toSet());
        map.values().forEach(p -> set.remove(p.getValue()));
        return set;
    }

    @SafeVarargs
    public static <T> List<T> asNonNullList(T... elements) {
        return stream(elements).filter(Objects::nonNull).toList();
    }

    @SafeVarargs
    public static <T> void addIfNonNull(Collection<T> source, T... elements) {
        stream(elements).filter(Objects::nonNull).forEach(source::add);
    }

    @SafeVarargs
    public static <T> void addIfNonNull(Collection<T> source, Collection<? extends T>... collections) {
        stream(collections).filter(Objects::nonNull).forEach(source::addAll);
    }

    @SafeVarargs
    public static <T> T[] toArray(T... elements) {
        return elements;
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