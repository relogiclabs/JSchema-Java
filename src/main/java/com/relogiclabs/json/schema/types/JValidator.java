package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.JsonSchemaException;
import com.relogiclabs.json.schema.internal.message.ActualHelper;
import com.relogiclabs.json.schema.internal.message.ExpectedHelper;
import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.ValidationFailed;
import static com.relogiclabs.json.schema.internal.util.CollectionHelper.addToList;
import static com.relogiclabs.json.schema.internal.util.StreamHelper.allTrue;
import static com.relogiclabs.json.schema.internal.util.StreamHelper.anyTrue;
import static com.relogiclabs.json.schema.internal.util.StringHelper.join;
import static com.relogiclabs.json.schema.message.ErrorCode.VALD01;
import static java.util.Collections.unmodifiableCollection;

@Getter
@EqualsAndHashCode
public final class JValidator extends JBranch {
    public static final String OPTIONAL_MARKER = "?";

    private final JNode value;
    private final List<JFunction> functions;
    private final List<JDataType> dataTypes;
    private final List<JReceiver> receivers;
    private final boolean optional;

    private JValidator(Builder builder) {
        super(builder);
        value = builder.value;
        functions = builder.functions;
        dataTypes = builder.dataTypes;
        receivers = builder.receivers;
        optional = builder.optional;
        getRuntime().register(receivers);
        var nodes = new ArrayList<JNode>();
        addToList(nodes, value);
        addToList(nodes, functions, dataTypes, receivers);
        children = unmodifiableCollection(nodes);
    }

    @Override
    public boolean match(JNode node) {
        boolean rValue = true;
        var other = castType(node, JsonTypable.class);
        if(other == null) return false;
        getRuntime().receive(receivers, node);
        if(node instanceof JNull && dataTypes.stream()
                .map(JDataType::isMatchNull).anyMatch(anyTrue())) return true;
        if(value != null) rValue &= value.match(other.getNode());
        if(!rValue) return failWith(new JsonSchemaException(
                new ErrorDetail(VALD01, ValidationFailed),
                ExpectedHelper.asValueMismatch(value),
                ActualHelper.asValueMismatch(node)));
        var rDataType = matchDataType(node);
        var fDataType = rDataType && dataTypes.size() != 0;
        boolean rFunction = allTrue(functions.stream()
                .filter(f -> f.isApplicable(node) || !fDataType)
                .map(f -> f.match(node)));
        return rValue & rDataType & rFunction;
    }

    private boolean matchDataType(JNode node) {
        if(getRuntime().tryExecute(() -> checkDataType(node))) return true;
        dataTypes.stream().filter(d -> !d.getNested()).forEach(d -> d.matchForReport(node));
        dataTypes.stream().filter(d -> d.getNested()).forEach(d -> d.matchForReport(node));
        return false;
    }

    private boolean checkDataType(JNode node) {
        var list1 = dataTypes.stream().filter(d -> !d.getNested()).map(d -> d.match(node)).toList();
        var result1 = list1.stream().anyMatch(anyTrue());
        var list2 = dataTypes.stream().filter(d -> d.getNested() && (d.isApplicable(node) || !result1))
                .map(d -> d.match(node)).toList();
        var result2 = list2.stream().anyMatch(anyTrue()) || list2.size() == 0;
        return (result1 || list1.size() == 0) && result2;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(value != null) builder.append(value);
        builder.append(join(functions, " ", " "));
        builder.append(join(dataTypes, " ", " "));
        if(optional) builder.append(" " + OPTIONAL_MARKER);
        return builder.toString().trim();
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        private JNode value;
        private List<JFunction> functions;
        private List<JDataType> dataTypes;
        private List<JReceiver> receivers;
        private boolean optional;

        @Override
        public JValidator build() {
            return build(new JValidator(this));
        }
    }
}