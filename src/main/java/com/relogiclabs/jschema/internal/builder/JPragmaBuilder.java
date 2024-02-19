package com.relogiclabs.jschema.internal.builder;

import com.relogiclabs.jschema.exception.InvalidPragmaValueException;
import com.relogiclabs.jschema.exception.PragmaNotFoundException;
import com.relogiclabs.jschema.internal.tree.PragmaDescriptor;
import com.relogiclabs.jschema.node.JNode;
import com.relogiclabs.jschema.node.JPragma;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.PRAG01;
import static com.relogiclabs.jschema.message.ErrorCode.PRAG02;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;

@Getter @Setter
@Accessors(fluent = true)
public final class JPragmaBuilder extends JNodeBuilder<JPragmaBuilder> {
    private String name;
    private JNode value;

    private void checkPragma() {
        var descriptor = PragmaDescriptor.from(name);
        if (descriptor == null)
            throw new PragmaNotFoundException(formatForSchema(PRAG01, concat("Invalid pragma '",
                    name, "' with value ", value.getOutline(), " found"), context()));
        if (!descriptor.matchType(value.getClass()))
            throw new InvalidPragmaValueException(formatForSchema(PRAG02, concat("Invalid value ",
                    value.getOutline(), " for pragma '", name, "' found"), value));
    }

    @Override
    public JPragma build() {
        checkPragma();
        return JPragma.from(this);
    }
}