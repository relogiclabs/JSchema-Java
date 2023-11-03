package com.relogiclabs.json.schema.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.relogiclabs.json.schema.internal.util.StringHelper.concat;

@Getter
@AllArgsConstructor
public final class ErrorDetail {
    private String code;
    private String message;

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