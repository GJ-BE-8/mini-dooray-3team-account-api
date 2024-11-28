package com.nhnacademy.minidooray3team.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    ACTIVE, DORMANT, DELETED;

    @JsonCreator
    public static Status fromString(String str) {
        for (Status status : Status.values()) {
            if (status.name().equalsIgnoreCase(str)) {
                return status;
            }
        }
        // default
        return ACTIVE;
    }

    @JsonValue
    public String toJson() {
        return this.name().toLowerCase();
    }
}

