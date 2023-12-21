package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.exception.NoValueReceivedException;
import com.relogiclabs.json.schema.exception.ReceiverNotFoundException;
import com.relogiclabs.json.schema.internal.builder.JReceiverBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.message.ErrorCode.RECV01;
import static com.relogiclabs.json.schema.message.ErrorCode.RECV02;
import static com.relogiclabs.json.schema.message.ErrorCode.RECV03;
import static com.relogiclabs.json.schema.message.ErrorCode.RECV04;
import static com.relogiclabs.json.schema.message.MessageFormatter.formatForSchema;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public final class JReceiver extends JLeaf {
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
        var list = getRuntime().getReceivers().fetch(this);
        if(list == null) throw new ReceiverNotFoundException(
                formatForSchema(RECV01, concat("Receiver '", name, "' not found"), this));
        return list.size();
    }

    @SuppressWarnings("unchecked")
    public <T extends JNode> T getValueNode() {
        var list = getRuntime().getReceivers().fetch(this);
        if(list == null) throw new ReceiverNotFoundException(
                formatForSchema(RECV02, concat("Receiver '", name, "' not found"), this));
        if(list.isEmpty()) throw new NoValueReceivedException(
                formatForSchema(RECV03, concat("No value received for '", name, "'"), this));
        if(list.size() > 1) throw new UnsupportedOperationException("Multiple values exist");
        return (T) list.get(0);
    }

    @SuppressWarnings("unchecked")
    public <T extends JNode> List<T> getValueNodes() {
        var list = getRuntime().getReceivers().fetch(this);
        if(list == null) throw new ReceiverNotFoundException(
                formatForSchema(RECV04, concat("Receiver '", name, "' not found"), this));
        return list.stream().map(i -> (T) i).toList();
    }

    @Override
    public String toString() {
        return name;
    }
}