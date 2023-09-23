package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.DefinitionNotFoundException;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.message.MessageFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.DataTypeMismatch;
import static com.relogiclabs.json.schema.internal.message.MessageHelper.InvalidNestedDataType;
import static com.relogiclabs.json.schema.internal.util.StreamHelper.allTrue;
import static com.relogiclabs.json.schema.message.ErrorCode.DEFI03;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP02;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP04;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP05;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP06;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

@Getter
@EqualsAndHashCode
public class JDataType extends JBranch implements NestedMode {
    private Collection<JNode> children;
    private final JsonType jsonType;
    private final JAlias alias;
    private final boolean nested;

    private JDataType(Builder builder) {
        super(builder.relations, builder.context);
        this.jsonType = requireNonNull(builder.jsonType);
        this.alias = builder.alias;
        this.nested = builder.nested;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Collection<? extends JNode> getChildren() {
        return children;
    }

    @Override
    protected <T extends JNode> T initialize() {
        children = alias != null ? List.of(alias) : emptyList();
        return super.initialize();
    }

    @Override
    public boolean match(JNode node) {
        if(!nested) return jsonType.match(node);
        if(!(node instanceof JComposite composite))
            return failWith(new JsonSchemaException(
                    new ErrorDetail(DTYP02, InvalidNestedDataType),
                    ExpectedHelper.asInvalidDataType(this),
                    ActualHelper.asInvalidDataType(node)));
        return composite.components().stream().map(this::matchCurrent).allMatch(allTrue());
    }

    private boolean matchCurrent(JNode node) {
        boolean result = true;
        result &= jsonType.match(node);
        if(alias == null) return result;
        var validator = getRuntime().getDefinitions().get(alias);
        if(validator == null) return failWith(new DefinitionNotFoundException(
                MessageFormatter.formatForSchema(DEFI03, alias.getName(), getContext())));
        result &= validator.match(node);
        return result;
    }

    public boolean matchForReport(JNode node) {
        if(!nested && !jsonType.match(node))
            return failWith(new JsonSchemaException(
                    new ErrorDetail(DTYP04, DataTypeMismatch),
                    ExpectedHelper.asDataTypeMismatch(this),
                    ActualHelper.asDataTypeMismatch(node)));
        if(!(node instanceof JComposite composite))
            return failWith(new JsonSchemaException(
                    new ErrorDetail(DTYP05, InvalidNestedDataType),
                    ExpectedHelper.asInvalidDataType(this),
                    ActualHelper.asInvalidDataType(node)));
        boolean result = true;
        for(var c : composite.components()) {
            if(!matchCurrent(c)) result &= failWith(new JsonSchemaException(
                    new ErrorDetail(DTYP06, DataTypeMismatch),
                    ExpectedHelper.asDataTypeMismatch(this),
                    ActualHelper.asDataTypeMismatch(c)));
        }
        return result;
    }

    public boolean isApplicable(JNode node) {
        return !nested || node instanceof JComposite;
    }

    public boolean isMatchNull() {
        return !nested && jsonType == JsonType.NULL;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean baseForm) {
        var builder = new StringBuilder(jsonType.toString());
        if(nested && !baseForm) builder.append(NestedMode.NESTED_MARKER);
        if(alias != null && !baseForm) builder.append("(" + alias + ")");
        return builder.toString();
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        protected JsonType jsonType;
        protected JAlias alias;
        protected boolean nested;

        @Override
        public JDataType build() {
            return new JDataType(this).initialize();
        }
    }
}