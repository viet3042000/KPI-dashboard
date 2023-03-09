package com.b4t.app.service.dto.lgsp;

public enum SyncLGSPError {
    SUCCESS("0"),
    FAIL("1"),
    UNAUTHORIZED("2"),
    NOT_FOUND("3"),
    WRONG_STRUCTURE("4"),
    APPROVED("5");

    private final String value;

    SyncLGSPError(String value) {
        this.value = value;
    }

    public final String value() {
        return this.value;
    }

}
