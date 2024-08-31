package com.relogiclabs.jschema.node;

import com.relogiclabs.jschema.exception.InvalidDataTypeException;
import com.relogiclabs.jschema.exception.InvalidDateTimeException;
import com.relogiclabs.jschema.internal.util.LogHelper;
import com.relogiclabs.jschema.internal.util.MatchResult;
import com.relogiclabs.jschema.type.EType;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;

import java.util.HashMap;
import java.util.Map;

import static com.relogiclabs.jschema.internal.util.MatchResult.FAILURE;
import static com.relogiclabs.jschema.internal.util.MatchResult.SUCCESS;
import static com.relogiclabs.jschema.message.ErrorCode.DTYVDF01;
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

    public static JsonType from(Token token) {
        var type = stringTypeMap.get(token.getText());
        if(type == null) throw new InvalidDataTypeException(formatForSchema(DTYVDF01,
            "Invalid data type " + token.getText(), token));
        return new JsonType(type);
    }

    public MatchResult match(JNode node) {
        if(!typeClassMap.get(type).isInstance(node)) return FAILURE;
        try {
            if(type == DATE) {
                var string = (JString) node;
                var dateTime = node.getRuntime().getPragmas().getDateTypeParser()
                    .parse(string.getValue());
                string.setDerived(JDate.from(string, dateTime));
            } else if(type == TIME) {
                var string = (JString) node;
                var dateTime = node.getRuntime().getPragmas().getTimeTypeParser()
                    .parse(string.getValue());
                string.setDerived(JTime.from(string, dateTime));
            }
        } catch(InvalidDateTimeException e) {
            LogHelper.debug(e);
            return new MatchResult(false, e.getMessage(),
                e.getContext().getParser().getPattern());
        }
        return SUCCESS;
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