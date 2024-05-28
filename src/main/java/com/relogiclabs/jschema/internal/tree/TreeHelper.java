package com.relogiclabs.jschema.internal.tree;

import com.relogiclabs.jschema.exception.DuplicatePropertyKeyException;
import com.relogiclabs.jschema.message.ErrorDetail;
import com.relogiclabs.jschema.node.JProperty;
import com.relogiclabs.jschema.tree.TreeType;

import java.util.List;
import java.util.function.Function;

import static com.relogiclabs.jschema.internal.util.StreamHelper.halt;
import static com.relogiclabs.jschema.message.ErrorCode.PROP03;
import static com.relogiclabs.jschema.message.ErrorCode.PROP04;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForJson;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static com.relogiclabs.jschema.tree.TreeType.JSON_TREE;
import static com.relogiclabs.jschema.tree.TreeType.SCHEMA_TREE;
import static java.util.stream.Collectors.toMap;

public final class TreeHelper {
    public static List<JProperty> requireUniqueness(List<JProperty> list, TreeType treeType) {
        list.stream().collect(toMap(JProperty::getKey, Function.identity(),
                (p1, p2) -> halt(new DuplicatePropertyKeyException(format(treeType, p2)))
        ));
        return list;
    }

    private static ErrorDetail format(TreeType type, JProperty property) {
        if(type == JSON_TREE) return formatForJson(PROP03, getMessage(property), property);
        if(type == SCHEMA_TREE) return formatForSchema(PROP04, getMessage(property), property);
        throw new IllegalStateException("Invalid parser state");
    }

    private static String getMessage(JProperty property) {
        return "Multiple key with name '" + property.getKey() + "' found";
    }
}