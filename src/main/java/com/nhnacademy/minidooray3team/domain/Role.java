package com.nhnacademy.minidooray3team.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    MEMBER, ADMIN;

    @JsonCreator
    public static Role fromString(String str) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(str)) {
                return role;
            }
        }
        // default
        return MEMBER;
    }

    @JsonValue
    public String toJson() {
        return this.name().toLowerCase();
    }
}
