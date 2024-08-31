package com.relogiclabs.jschema.exception;

import com.relogiclabs.jschema.node.JProperty;
import lombok.Getter;

@Getter
public class PropertyOrderException extends BaseRuntimeException {
    private final JProperty failOn;

    public PropertyOrderException(String code, String message, JProperty failOn) {
        super(code, message);
        this.failOn = failOn;
    }
}