package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.NoValueReceivedException;
import com.relogiclabs.jschema.exception.ReceiverNotFoundException;
import com.relogiclabs.jschema.internal.builder.JReceiverBuilder;
import com.relogiclabs.jschema.type.EArray;
import com.relogiclabs.jschema.type.EValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Iterator;
import java.util.List;

import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.RECV01;
import static com.relogiclabs.jschema.message.ErrorCode.RECV03;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JReceiver extends JLeaf implements EArray, Iterable<JNode> {
    private final String name;

    private JReceiver(JReceiverBuilder builder) {
        super(builder);
        name = requireNonNull(builder.name());
    }

    public static JReceiver from(JReceiverBuilder builder) {
        return new JReceiver(builder).initialize();
    }

    @Override
    public boolean match(JNode node) {
        throw new IllegalStateException("Invalid runtime state");
    }

    public int getValueCount() {
        return fetchValueNodes().size();
    }

    private List<JNode> fetchValueNodes() {
        var list = getRuntime().getReceivers().fetch(this);
        if(list == null) throw new ReceiverNotFoundException(formatForSchema(RECV01,
                concat("Receiver '", name, "' not found"), this));
        return list;
    }

    @SuppressWarnings("unchecked")
    public <T extends JNode> T getValueNode() {
        var list = fetchValueNodes();
        if(list.isEmpty()) throw new NoValueReceivedException(formatForSchema(RECV03,
                concat("No value received for '", name, "'"), this));
        if(list.size() > 1) throw new UnsupportedOperationException("Multiple values exist");
        return (T) list.get(0);
    }

    @SuppressWarnings("unchecked")
    public <T extends JNode> List<T> getValueNodes() {
        var list = fetchValueNodes();
        return list.stream().map(i -> (T) i).toList();
    }

    @Override
    public EValue get(int index) {
        return fetchValueNodes().get(index);
    }

    @Override
    public List<? extends EValue> elements() {
        return fetchValueNodes();
    }

    @Override
    public Iterator<JNode> iterator() {
        return fetchValueNodes().iterator();
    }

    @Override
    public String toString() {
        return name;
    }
}