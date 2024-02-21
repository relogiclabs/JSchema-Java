package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.InvalidDataTypeException;
import com.relogiclabs.jschema.tree.Location;
import com.relogiclabs.jschema.type.EType;
import com.relogiclabs.jschema.util.Reference;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static com.relogiclabs.jschema.message.ErrorCode.DTYP01;
import static com.relogiclabs.jschema.message.MessageFormatter.formatForSchema;
import static com.relogiclabs.jschema.type.EType.ANY;
import static com.relogiclabs.jschema.type.EType.ARRAY;
import static com.relogiclabs.jschema.type.EType.BOOLEAN;
import static com.relogiclabs.jschema.type.EType.COMPOSITE;
import static com.relogiclabs.jschema.type.EType.DATE;
import static com.relogiclabs.jschema.type.EType.DOUBLE;
import static com.relogiclabs.jschema.type.EType.FLOAT;
import static com.relogiclabs.jschema.type.EType.INTEGER;
import static com.relogiclabs.jschema.type.EType.NULL;
import static com.relogiclabs.jschema.type.EType.NUMBER;
import static com.relogiclabs.jschema.type.EType.OBJECT;
import static com.relogiclabs.jschema.type.EType.PRIMITIVE;
import static com.relogiclabs.jschema.type.EType.STRING;
import static com.relogiclabs.jschema.type.EType.TIME;

@RequiredArgsConstructor
public final class JsonType {
    private static final Map<String, EType> stringTypeMap = new HashMap<>();
    private static final Map<EType, Class<?>> typeClassMap = new HashMap<>();
    private static final Map<Class<?>, EType> classTypeMap = new HashMap<>();

    private final EType type;

    static {
        mapType(BOOLEAN, JBoolean.class);
        mapType(INTEGER, JInteger.class);
        mapType(FLOAT, JFloat.class);
        mapType(DOUBLE, JDouble.class);
        mapType(NUMBER, JNumber.class);
        mapType(STRING, JString.class);
        mapType(ARRAY, JArray.class);
        mapType(OBJECT, JObject.class);
        mapType(NULL, JNull.class);
        mapType(DATE, JString.class);
        mapType(TIME, JString.class);
        mapType(PRIMITIVE, JPrimitive.class);
        mapType(COMPOSITE, JComposite.class);
        mapType(ANY, JsonTypable.class);
    }

    private static void mapType(EType type, Class<?> typeClass) {
        stringTypeMap.put(type.getName(), type);
        classTypeMap.putIfAbsent(typeClass, type);
        typeClassMap.put(type, typeClass);
    }

    public static JsonType from(TerminalNode node) {
        return from(node.getText(), Location.from(node.getSymbol()));
    }

    private static JsonType from(String name, Location location) {
        var type = stringTypeMap.get(name);
        if(type == null) throw new InvalidDataTypeException(formatForSchema(DTYP01,
                concat("Invalid data type ", name), location));
        return new JsonType(type);
    }

    public boolean match(JNode node, Reference<String> error) {
        if(!typeClassMap.get(type).isInstance(node)) return false;
        if(type == DATE) {
            var date = (JString) node;
            var dateTime = node.getRuntime().getPragmas().getDateTypeParser()
                    .tryParse(date.getValue(), error);
            if(dateTime == null) return false;
            date.setDerived(JDate.from(date, dateTime));
        } else if(type == TIME) {
            var time = (JString) node;
            var dateTime = node.getRuntime().getPragmas().getTimeTypeParser()
                    .tryParse(time.getValue(), error);
            if(dateTime == null) return false;
            time.setDerived(JTime.from(time, dateTime));
        }
        return true;
    }

    boolean isNullType() {
        return type == NULL;
    }

    public static EType getType(Class<?> type) {
        return classTypeMap.get(type);
    }

    @Override
    public String toString() {
        return type.getName();
    }
}