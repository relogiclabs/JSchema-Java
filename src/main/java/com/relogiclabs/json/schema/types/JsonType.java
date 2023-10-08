package com.relogiclabs.json.schema.types;

import com.relogiclabs.json.schema.exception.InvalidDataTypeException;
import com.relogiclabs.json.schema.internal.time.DateTimeValidator;
import com.relogiclabs.json.schema.message.MessageFormatter;
import com.relogiclabs.json.schema.tree.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.json.schema.message.ErrorCode.DTYP01;

@Getter
@AllArgsConstructor
public enum JsonType {
    BOOLEAN("#boolean", JBoolean.class),
    STRING("#string", JString.class),
    INTEGER("#integer", JInteger.class),
    FLOAT("#float", JFloat.class),
    DOUBLE("#double", JDouble.class),
    OBJECT("#object", JObject.class),
    ARRAY("#array", JArray.class),
    NULL("#null", JNull.class),
    NUMBER("#number", JNumber.class),
    DATE("#date", JString.class),
    TIME("#time", JString.class),
    ANY("#any", JsonTypable.class);

    private static final DateTimeValidator ISO_8601_DATE
            = new DateTimeValidator(DateTimeValidator.ISO_8601_DATE);
    private static final DateTimeValidator ISO_8601_TIME
            = new DateTimeValidator(DateTimeValidator.ISO_8601_TIME);

    private static final Map<String, JsonType> typeMap;

    private final String name;
    private final Class<?> type;


    static {
        typeMap = new HashMap<>();
        for(JsonType t : JsonType.values())
            typeMap.put(t.name, t);
    }

    public static JsonType from(TerminalNode node) {
        return from(node.getText(), Location.from(node.getSymbol()));
    }

    public static JsonType from(String name, Location location) {
        var result = typeMap.get(name);
        if(result == null) throw new InvalidDataTypeException(
                MessageFormatter.formatForSchema(DTYP01,
                        "Invalid data type " + name, location));
        return result;
    }

    public static JsonType from(Class<?> type) {
        for(var t : values()) {
            if(t.getType().isAssignableFrom(type)) return t;
        }
        return null;
    }

    public boolean match(JNode node) {
        if(!type.isInstance(node)) return false;
        if(this == DATE) return ISO_8601_DATE.IsValidDate(((JString) node).getValue());
        if(this == TIME) return ISO_8601_TIME.IsValidTime(((JString) node).getValue());
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}