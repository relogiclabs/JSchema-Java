package com.relogiclabs.json.schema.exception;

import com.relogiclabs.json.schema.message.ErrorDetail;
import lombok.Getter;

public class CommonException extends RuntimeException {

    @Getter
    private String code;

    public CommonException() {}

    public CommonException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CommonException(String code, String message) {
        this(code, message, null);
    }

    public CommonException(ErrorDetail detail, Throwable cause) {
        this(detail.getCode(), detail.getMessage(), cause);
    }

    public CommonException(ErrorDetail detail) {
        this(detail, null);
    }
}
