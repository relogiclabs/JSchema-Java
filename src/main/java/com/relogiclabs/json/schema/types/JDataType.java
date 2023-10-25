package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.DefinitionNotFoundException;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.internal.message.MatchReport;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.message.MessageFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;

import static com.relogiclabs.json.schema.internal.message.MatchReport.AliasError;
import static com.relogiclabs.json.schema.internal.message.MatchReport.ArgumentError;
import static com.relogiclabs.json.schema.internal.message.MatchReport.Success;
import static com.relogiclabs.json.schema.internal.message.MatchReport.TypeError;
import static com.relogiclabs.json.schema.internal.message.MessageHelper.DataTypeArgumentFailed;
import static com.relogiclabs.json.schema.internal.message.MessageHelper.DataTypeMismatch;
import static com.relogiclabs.json.schema.internal.message.MessageHelper.InvalidNestedDataType;
import static com.relogiclabs.json.schema.internal.util.StreamHelper.allTrue;
import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP03;
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
        if(!nested) return isMatchCurrent(node);
        if(!(node instanceof JComposite composite)) return false;
        return composite.components().stream().map(this::isMatchCurrent).allMatch(allTrue());
    }

    private boolean isMatchCurrent(JNode node) {
        return matchCurrent(node) == Success;
    }

    private MatchReport matchCurrent(JNode node) {
        var result = jsonType.match(node) ? Success : TypeError;
        if(alias == null || result != Success) return result;
        var validator = getRuntime().getDefinitions().get(alias);
        if(validator == null) return AliasError;
        result = validator.match(node) ? Success : ArgumentError;
        return result;
    }

    public boolean matchForReport(JNode node) {
        if(!nested) return matchForReport(node, false);
        if(!(node instanceof JComposite composite))
            return failWith(new JsonSchemaException(
                    new ErrorDetail(DTYP03, InvalidNestedDataType),
                    ExpectedHelper.asInvalidNestedDataType(this),
                    ActualHelper.asInvalidNestedDataType(node)));
        boolean result = true;
        for(var c : composite.components()) result &= matchForReport(c, true);
        return result;
    }

    private boolean matchForReport(JNode node, boolean nested) {
        var result = matchCurrent(node);
        if(result == TypeError) return failWith(new JsonSchemaException(
                new ErrorDetail(TypeError.getCode(nested), DataTypeMismatch),
                ExpectedHelper.asDataTypeMismatch(this),
                ActualHelper.asDataTypeMismatch(node)));
        if(result == AliasError) return failWith(new DefinitionNotFoundException(
                MessageFormatter.formatForSchema(AliasError.getCode(nested),
                        "No definition found for " + quote(alias), getContext())));
        if(result == ArgumentError) return failWith(new JsonSchemaException(
                new ErrorDetail(ArgumentError.getCode(nested), DataTypeArgumentFailed),
                ExpectedHelper.asDataTypeArgumentFailed(this),
                ActualHelper.asDataTypeArgumentFailed(node)));
        return true;
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