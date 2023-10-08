package com.relogiclabs.json.schema.internal.util;

import com.relogiclabs.json.schema.collection.IndexMap;
import com.relogiclabs.json.schema.types.JNode;
import com.relogiclabs.json.schema.types.JProperty;
import com.relogiclabs.json.schema.types.JString;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class CollectionHelper {
    public static <K, V> void merge(Map<K, List<V>> target, Map<K, List<V>> source) {
        source.forEach((sk, sv) -> {
            var tv = target.get(sk);
            if(tv != null) tv.addAll(sv);
            else target.put(sk, sv);
        });
    }

    public static Set<String> containsKeys(IndexMap<String, JProperty> map, JString... keys) {
        Set<String> set = Arrays.stream(keys).map(JString::getValue).collect(toSet());
        map.values().stream().forEach(p -> set.remove(p.getKey()));
        return set;
    }

    public static Set<JNode> containsValues(IndexMap<String, JProperty> map, JNode... values) {
        // Here values should be distinct on parameter
        Set<JNode> set = Arrays.stream(values).collect(toSet());
        map.values().stream().forEach(p -> set.remove(p.getValue()));
        return set;
    }
}