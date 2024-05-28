package com.relogiclabs.jschema.message;

import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.capitalize;

@Getter
public final class ErrorDetail {
    private final String code;
    private final String message;

    public ErrorDetail(String code, String message) {
        this.code = code;
        this.message = capitalize(message);
    }
}