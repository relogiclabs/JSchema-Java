package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.DefinitionNotFoundException;
import com.relogiclabs.jschema.exception.JsonSchemaException;
import com.relogiclabs.jschema.internal.builder.JDataTypeBuilder;
import com.relogiclabs.jschema.internal.message.ActualHelper;
import com.relogiclabs.jschema.internal.message.ExpectedHelper;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.util.Reference;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.relogiclabs.jschema.internal.message.MessageHelper.DataTypeArgumentFailed;
import static com.relogiclabs.jschema.internal.message.MessageHelper.DataTypeMismatch;
import static com.relogiclabs.jschema.internal.util.CollectionHelper.asList;
import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.DEFI03;
import static com.relogiclabs.jschema.message.ErrorCode.DEFI04;
import static com.relogiclabs.jschema.message.ErrorCode.DTYP04;
import static com.relogiclabs.jschema.message.ErrorCode.DTYP05;
import static com.relogiclabs.jschema.message.ErrorCode.DTYP06;
import static com.relogiclabs.jschema.message.ErrorCode.DTYP07;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
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
        if(validator == null) return fail(new DefinitionNotFoundException(formatForSchema(
                nested ? DEFI04 : DEFI03, concat("No definition found for '", alias, "'"), this)));
        if(!validator.match(node)) return fail(new JsonSchemaException(
                new ErrorDetail(nested ? DTYP07 : DTYP05, DataTypeArgumentFailed),
                ExpectedHelper.asDataTypeArgumentFailed(this),
                ActualHelper.asDataTypeArgumentFailed(node)));
        return true;
    }

    private boolean failTypeWith(JsonSchemaException exception) {
        exception.setAttribute(DATA_TYPE_NAME, toString(true));
        return fail(exception);
    }

    private static String formatMessage(String main, String optional) {
        return isEmpty(optional) ? main : concat(main, " (", uncapitalize(optional), ")");
    }

    boolean isApplicable(JNode node) {
        return !nested || node instanceof JComposite;
    }

    boolean isMatchNull() {
        return !nested && jsonType.isNullType();
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