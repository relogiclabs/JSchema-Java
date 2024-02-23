package com.relogiclabs.jschema.message;

import lombok.Getter;

import static com.relogiclabs.jschema.internal.util.StringHelper.concat;
import static org.apache.commons.lang3.StringUtils.capitalize;

@Getter
public final class ErrorDetail {
    private final String code;
    private final String message;

    public ErrorDetail(String code, String message) {
        this.code = code;
        this.message = capitalize(message);
    }

    public ErrorDetail(String code, Object m1, Object m2) {
        this(code, concat(m1, m2));
    }

    public ErrorDetail(String code, Object m1, Object m2, Object m3) {
        this(code, concat(m1, m2, m3));
    }

    public ErrorDetail(String code, Object m1, Object m2, Object m3, Object m4) {
        this(code, concat(m1, m2, m3, m4));
    }

    public ErrorDetail(String code, Object m1, Object m2, Object m3, Object m4, Object m5) {
        this(code, concat(m1, m2, m3, m4, m5));
    }
}