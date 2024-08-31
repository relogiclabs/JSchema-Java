package com.relogiclabs.jschema.internal.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class MatchResult {
    public static final MatchResult SUCCESS = new MatchResult(true);
    public static final MatchResult FAILURE = new MatchResult(false);

    private final boolean success;
    private final String note;
    private final String pattern;

    public MatchResult(boolean success) {
        this.success = success;
        this.note = null;
        this.pattern = null;
    }
}