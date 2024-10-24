package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.exception.TypeNotSupportedException;
import com.relogiclabs.jschema.type.EType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static com.relogiclabs.jschema.message.ErrorCode.SCRTYP01;
import static com.relogiclabs.jschema.type.EType.ANY;
import static com.relogiclabs.jschema.type.EType.ARRAY;
import static com.relogiclabs.jschema.type.EType.BOOLEAN;
import static com.relogiclabs.jschema.type.EType.COMPOSITE;
import static com.relogiclabs.jschema.type.EType.DATE;
import static com.relogiclabs.jschema.type.EType.DATETIME;
import static com.relogiclabs.jschema.type.EType.DOUBLE;
import static com.relogiclabs.jschema.type.EType.FLOAT;
import static com.relogiclabs.jschema.type.EType.INTEGER;
import static com.relogiclabs.jschema.type.EType.NATIVE;
import static com.relogiclabs.jschema.type.EType.NULL;
import static com.relogiclabs.jschema.type.EType.NUMBER;
import static com.relogiclabs.jschema.type.EType.OBJECT;
import static com.relogiclabs.jschema.type.EType.PRIMITIVE;
import static com.relogiclabs.jschema.type.EType.RANGE;
import static com.relogiclabs.jschema.type.EType.STRING;
import static com.relogiclabs.jschema.type.EType.TIME;
import static com.relogiclabs.jschema.type.EType.UNDEFINED;
import static java.util.Arrays.stream;

@Getter
@RequiredArgsConstructor
public enum ScriptType {
    T_ANY(ANY, "Y/"),
    T_PRIMITIVE(PRIMITIVE, "P/"),
    T_COMPOSITE(COMPOSITE, "C/"),
    T_NUMBER(NUMBER, "N/"),
    T_INTEGER(INTEGER, "I/"),
    T_FLOAT(FLOAT, "F/"),
    T_DOUBLE(DOUBLE, "D/"),
    T_BOOLEAN(BOOLEAN, "B/"),
    T_STRING(STRING, "S/"),
    T_DATETIME(DATETIME, "E/"),
    T_DATE(DATE, "G/"),
    T_TIME(TIME, "T/"),
    T_NULL(NULL, "L/"),
    T_UNDEFINED(UNDEFINED, "U/"),
    T_RANGE(RANGE, "R/"),
    T_NATIVE(NATIVE, "V/"),
    T_ARRAY(ARRAY, "A/"),
    T_OBJECT(OBJECT, "O/");

    private static final Map<EType, String> TYPE_CODE = mapTypeCode();
    private static final Map<EType, String[]> TYPE_BRANCH = mapTypeBranch();

    private static Map<EType, String> mapTypeCode() {
        var typeCodeMap = new EnumMap<EType, String>(EType.class);
        for(var t : ScriptType.values()) typeCodeMap.put(t.type, t.code);
        return typeCodeMap;
    }

    private static Map<EType, String[]> mapTypeBranch() {
        var eTypes = EType.values();
        var typeBranchMap = new EnumMap<EType, String[]>(EType.class);
        for(var s : ScriptType.values()) {
            var codes = stream(eTypes)
                .filter(e -> isSubType(s.type, e)).map(TYPE_CODE::get)
                .filter(Objects::nonNull).toArray(String[]::new);
            typeBranchMap.put(s.type, codes);
        }
        return typeBranchMap;
    }

    private static boolean isSubType(EType parent, EType current) {
        do {
            if(current == parent) return true;
            current = current.getParent();
        } while(current != null);
        return false;
    }

    private final EType type;
    private final String code;

    public static String[] getBranchCodes(EType type) {
        var codes = TYPE_BRANCH.get(type);
        if(codes == null) throw new TypeNotSupportedException(SCRTYP01,
            "Unsupported script type for registration");
        return codes;
    }

    public static String getCode(EType type) {
        return TYPE_CODE.get(type);
    }
}