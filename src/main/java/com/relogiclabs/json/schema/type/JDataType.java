package com.relogiclabs.json.schema.type;

import com.relogiclabs.json.schema.exception.DefinitionNotFoundException;
import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.builder.JDataTypeBuilder;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import com.relogiclabs.json.schema.util.Reference;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.DataTypeArgumentFailed;
import static com.relogiclabs.json.schema.internal.message.MessageHelper.DataTypeMismatch;
import static com.relogiclabs.json.schema.internal.util.CollectionHelper.asList;
import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;
import static com.relogiclabs.json.schema.message.ErrorCode.DEFI03;
import static com.relogiclabs.json.schema.message.ErrorCode.DEFI04;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP04;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP05;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP06;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP07;
import static com.relogiclabs.json.schema.message.MessageFormatter.formatForSchema;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

@Getter
@EqualsAndHashCode
public final class JDataType extends JBranch implements NestedMode {
    static final String NESTED_MARKER = "*";
    static final String DATA_TYPE_NAME = "DATA_TYPE_NAME";
    private final JsonType jsonType;
    private final JAlias alias;
    private final boolean nested;

    private JDataType(JDataTypeBuilder builder) {
        super(builder);
        jsonType = requireNonNull(builder.jsonType());
        nested = requireNonNull(builder.nested());
        alias = builder.alias();
        children = asList(alias);
    }

    public static JDataType from(JDataTypeBuilder builder) {
        return new JDataType(builder).initialize();
    }

    @Override
    public boolean match(JNode node) {
        Reference<String> error = new Reference<>();
        if(!jsonType.match(node, error)) return failTypeWith(new JsonSchemaException(
                new ErrorDetail(nested ? DTYP06 : DTYP04,
                        formatMessage(DataTypeMismatch, error.getValue())),
                ExpectedHelper.asDataTypeMismatch(this),
                ActualHelper.asDataTypeMismatch(node)));
        if(alias == null) return true;
        var validator = getRuntime().getDefinitions().get(alias);
        if(validator == null) return failWith(new DefinitionNotFoundException(
                formatForSchema(nested ? DEFI04 : DEFI03, "No definition found for "
                        + quote(alias), this)));
        if(!validator.match(node)) return failWith(new JsonSchemaException(
                new ErrorDetail(nested ? DTYP07 : DTYP05, DataTypeArgumentFailed),
                ExpectedHelper.asDataTypeArgumentFailed(this),
                ActualHelper.asDataTypeArgumentFailed(node)));
        return true;
    }

    private boolean failTypeWith(JsonSchemaException exception) {
        exception.setAttribute(DATA_TYPE_NAME, toString(true));
        return failWith(exception);
    }

    private static String formatMessage(String main, String optional) {
        return isEmpty(optional) ? main : concat(main, " (", uncapitalize(optional), ")");
    }

    boolean isApplicable(JNode node) {
        return !nested || node instanceof JComposite;
    }

    boolean isMatchNull() {
        return !nested && jsonType == JsonType.NULL;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean baseForm) {
        var builder = new StringBuilder(jsonType.toString());
        if(nested && !baseForm) builder.append(NESTED_MARKER);
        if(alias != null && !baseForm) builder.append("(").append(alias).append(")");
        return builder.toString();
    }
}