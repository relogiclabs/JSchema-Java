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
import java.util.Collection;
import java.util.List;

import static com.relogiclabs.json.schema.internal.message.MessageHelper.ValidationFailed;
import static com.relogiclabs.json.schema.internal.util.StreamHelper.allTrue;
import static com.relogiclabs.json.schema.internal.util.StreamHelper.anyTrue;
import static com.relogiclabs.json.schema.internal.util.StringHelper.toUnitedString;
import static com.relogiclabs.json.schema.message.ErrorCode.VALD01;
import static java.util.Collections.unmodifiableCollection;

@Getter
@EqualsAndHashCode
public class JValidator extends JBranch {
    public static final String OPTIONAL_MARKER = "?";

    private Collection<JNode> children;
    private final JNode value;
    private final List<JFunction> functions;
    private final List<JDataType> dataTypes;
    private final boolean optional;

    public JValidator(Builder builder) {
        super(builder.relations, builder.context);
        this.value = builder.value;
        this.functions = builder.functions;
        this.dataTypes = builder.dataTypes;
        this.optional = builder.optional;
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
        children = new ArrayList<>();
        addToList(children, value);
        addToList(children, functions);
        addToList(children, dataTypes);
        children = unmodifiableCollection(children);
        return super.initialize();
    }

    private void addToList(Collection<JNode> list, JNode node) {
        if(node != null) list.add(node);
    }

    private void addToList(Collection<JNode> list, List<? extends JNode> nodes) {
        if(nodes != null) list.addAll(nodes);
    }

    @Override
    public boolean match(JNode node) {
        boolean rValue = true;
        var other = castType(node, JsonTypable.class);
        if(other == null) return false;
        if(node instanceof JNull && dataTypes.stream()
                .map(JDataType::isMatchNull).anyMatch(anyTrue())) return true;
        if(value != null) rValue &= value.match(other.getNode());
        if(!rValue) return failWith(new JsonSchemaException(
                new ErrorDetail(VALD01, ValidationFailed),
                ExpectedHelper.asValueMismatch(value),
                ActualHelper.asValueMismatch(node)));
        var rDataType = matchDataType(node);
        if(!rDataType) dataTypes.forEach(d -> d.matchForReport(node));
        var fDataType = rDataType && dataTypes.size() != 0;
        boolean rFunction = allTrue(functions.stream()
                .filter(f -> f.isApplicable(node) || !fDataType)
                .map(f -> f.match(node)));
        return rValue & rDataType & rFunction;
    }

    private boolean matchDataType(JNode node) {
        var list1 = dataTypes.stream().filter(d -> !d.getNested()).map(d -> d.match(node)).toList();
        var result1 = list1.stream().anyMatch(anyTrue()) || list1.size() == 0;
        var list2 = dataTypes.stream().filter(d -> d.getNested() && (d.isApplicable(node) || !result1))
            .map(d -> d.match(node)).toList();
        var result2 = list2.stream().anyMatch(anyTrue()) || list2.size() == 0;
        return result1 && result2;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(value != null) builder.append(value);
        builder.append(toUnitedString(functions, " ", " "));
        builder.append(toUnitedString(dataTypes, " ", " "));
        if(optional) builder.append(" " + OPTIONAL_MARKER);
        return builder.toString().trim();
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder extends JNode.Builder<Builder> {
        protected JNode value;
        protected List<JFunction> functions;
        protected List<JDataType> dataTypes;
        protected boolean optional;

        @Override
        public JValidator build() {
            return new JValidator(this).initialize();
        }
    }
}