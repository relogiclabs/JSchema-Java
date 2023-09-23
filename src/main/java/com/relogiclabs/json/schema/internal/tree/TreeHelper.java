package com.relogiclabs.json.schema.internal.tree;

import com.relogiclabs.json.schema.exception.DuplicatePropertyKeyException;
import com.relogiclabs.json.schema.message.MessageFormatter;
import com.relogiclabs.json.schema.types.JProperty;

import java.util.List;
import java.util.function.Function;

import static com.relogiclabs.json.schema.internal.util.StreamHelper.halt;
import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;
import static com.relogiclabs.json.schema.internal.util.StringHelper.quote;
import static com.relogiclabs.json.schema.message.ErrorCode.PROP03;
import static java.util.stream.Collectors.toMap;

public class TreeHelper {

    public static List<JProperty> checkForDuplicate(List<JProperty> list) {
        list.stream().collect(toMap(JProperty::getKey, Function.identity(),
                (p1, p2) -> halt(new DuplicatePropertyKeyException(MessageFormatter.formatForJson(
                        PROP03, concat("Multiple key with name ", quote(p2.getKey()), " found"),
                        p2.getContext())))
        ));
        return list;
    }
}