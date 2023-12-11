package com.relogiclabs.json.schema.internal.builder;

import com.relogiclabs.json.schema.exception.InvalidPragmaValueException;
import com.relogiclabs.json.schema.exception.PragmaNotFoundException;
import com.relogiclabs.json.schema.internal.tree.PragmaDescriptor;
import com.relogiclabs.json.schema.type.JNode;
import com.relogiclabs.json.schema.type.JPragma;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;
import static com.relogiclabs.json.schema.message.ErrorCode.PRAG01;
import static com.relogiclabs.json.schema.message.ErrorCode.PRAG02;
import static com.relogiclabs.json.schema.message.MessageFormatter.formatForSchema;

@Getter @Setter
@Accessors(fluent = true)
public class JPragmaBuilder extends JNodeBuilder<JPragmaBuilder> {
    private String name;
    private JNode value;

    private void checkPragma() {
        var descriptor = PragmaDescriptor.from(name);
        if (descriptor == null)
            throw new PragmaNotFoundException(formatForSchema(PRAG01,
                    concat("Invalid pragma ", quote(name), " with value ",
                            value.getOutline(), " found"), context()));
        if (!descriptor.matchType(value.getClass()))
            throw new InvalidPragmaValueException(formatForSchema(PRAG02,
                    concat("Invalid value ", value.getOutline(),
                            " for pragma ", quote(name), " found"), value));
    }

    @Override
    public JPragma build() {
        checkPragma();
        return JPragma.from(this);
    }
}