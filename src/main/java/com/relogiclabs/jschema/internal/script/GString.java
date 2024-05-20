package com.relogiclabs.jschema.internal.script;

import com.relogiclabs.jschema.type.EString;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.relogiclabs.jschema.internal.util.StringHelper.quote;

@Getter
@RequiredArgsConstructor(staticName = "from")
public final class GString implements EString {
    private final String value;

    public static GString from(int value) {
        return new GString(Character.toString(value));
    }

    public static GString from(EString string, GRange range) {
        var value = string.getValue();
        var length = value.length();
        return GString.from(value.substring(range.getStart(length),
                range.getEnd(length)));
    }

    @Override
    public String toString() {
        return quote(value);
    }
}