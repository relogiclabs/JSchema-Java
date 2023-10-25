package com.relogiclabs.json.schema.internal.message;

import static com.relogiclabs.json.schema.message.ErrorCode.DEFI03;
import static com.relogiclabs.json.schema.message.ErrorCode.DEFI04;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP04;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP05;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP06;
import static com.relogiclabs.json.schema.message.ErrorCode.DTYP07;

public enum MatchReport {
    Success,
    TypeError(DTYP04, DTYP06),
    ArgumentError(DTYP05, DTYP07),
    AliasError(DEFI03, DEFI04);

    private final String code1;
    private final String code2;

    MatchReport(String code1, String code2) {
        this.code1 = code1;
        this.code2 = code2;
    }

    MatchReport() {
        this(null, null);
    }

    public String getCode(boolean nested) {
        return nested ? code2 : code1;
    }
}