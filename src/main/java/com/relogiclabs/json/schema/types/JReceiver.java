package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.NoValueReceivedException;
import com.relogiclabs.json.schema.exception.ReceiverNotFoundException;
import com.relogiclabs.json.schema.message.MessageFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.message.ErrorCode.RECV01;
import static com.relogiclabs.json.schema.message.ErrorCode.RECV02;
import static com.relogiclabs.json.schema.message.ErrorCode.RECV03;
import static com.relogiclabs.json.schema.message.ErrorCode.RECV04;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JReceiver extends JLeaf {
    private final String name;

    private JReceiver(Builder builder) {
        super(builder);
        name = requireNonNull(builder.name);
    }

    @Override
    public boolean match(JNode node) {
        throw new IllegalStateException("Invalid runtime state");
    }

    public int getValueCount() {
        var list = getRuntime().fetch(this);
        if(list == null) throw new ReceiverNotFoundException(MessageFormatter
                .formatForSchema(RECV01, concat("Receiver '", name, "' not found"), getContext()));
        return list.size();
    }

    @SuppressWarnings("unchecked")
    public <T extends JNode> T getValueNode() {
        var list = getRuntime().fetch(this);
        if(list == null) throw new ReceiverNotFoundException(MessageFormatter
                .formatForSchema(RECV02, concat("Receiver '", name, "' not found"), getContext()));
        if(list.size() == 0) throw new NoValueReceivedException(MessageFormatter
                .formatForSchema(RECV03, concat("No value received for '", name, "'"), getContext()));
        if(list.size() > 1) throw new IllegalStateException("Multiple values found");
        return (T) list.get(0);
    }

    @SuppressWarnings("unchecked")
    public <T extends JNode> List<T> getValueNodes() {
        var list = getRuntime().fetch(this);
        if(list == null) throw new ReceiverNotFoundException(MessageFormatter
                .formatForSchema(RECV04, concat("Receiver '", name, "' not found"), getContext()));
        return list.stream().map(i -> (T) i).toList();
    }

    @Override
    public String toString() {
        return name;
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        private String name;

        @Override
        public JReceiver build() {
            return build(new JReceiver(this));
        }
    }
}