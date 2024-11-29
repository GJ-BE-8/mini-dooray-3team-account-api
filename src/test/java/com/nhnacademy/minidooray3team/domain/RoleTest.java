package com.nhnacademy.minidooray3team.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testFromString_ValidRole() {
        assertEquals(Role.MEMBER, Role.fromString("member"));
        assertEquals(Role.ADMIN, Role.fromString("admin"));
        assertEquals(Role.MEMBER, Role.fromString("MEMBER"));
        assertEquals(Role.ADMIN, Role.fromString("ADMIN"));
    }

    @Test
    void testFromString_InvalidRole() {
        assertEquals(Role.MEMBER, Role.fromString("invalidRole"));
    }

    @Test
    void testFromString_EmptyString() {
        assertEquals(Role.MEMBER, Role.fromString(""));
    }

    @Test
    void testToJson() {
        assertEquals("member", Role.MEMBER.toJson());
        assertEquals("admin", Role.ADMIN.toJson());
    }
}
