package com.relogiclabs.json.schema.tree;

import com.relogiclabs.json.schema.type.JNode;
import com.relogiclabs.json.schema.type.JReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReceiverRegistry implements Iterable<Map.Entry<JReceiver, List<JNode>>> {
    private final Map<JReceiver, List<JNode>> receivers;

    public ReceiverRegistry() {
        this.receivers = new HashMap<>();
    }

    public void register(List<JReceiver> list) {
        for(var r : list) receivers.put(r, new ArrayList<>());
    }

    public void receive(List<JReceiver> list, JNode node) {
        for(var r : list) receivers.get(r).add(node);
    }

    public List<JNode> fetch(JReceiver receiver) {
        return receivers.get(receiver);
    }

    public void clear() {
        for(var v : receivers.values()) v.clear();
    }

    @Override
    public Iterator<Map.Entry<JReceiver, List<JNode>>> iterator() {
        return receivers.entrySet().iterator();
    }
}