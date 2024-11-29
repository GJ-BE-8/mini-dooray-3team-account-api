package com.nhnacademy.minidooray3team.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testFromString_ValidStatus() {
        assertEquals(Status.ACTIVE, Status.fromString("active"));
        assertEquals(Status.DORMANT, Status.fromString("dormant"));
        assertEquals(Status.DELETED, Status.fromString("deleted"));
        assertEquals(Status.ACTIVE, Status.fromString("ACTIVE"));
        assertEquals(Status.DORMANT, Status.fromString("DORMANT"));
        assertEquals(Status.DELETED, Status.fromString("DELETED"));
    }

    @Test
    void testFromString_InvalidStatus() {
        assertEquals(Status.ACTIVE, Status.fromString("invalidStatus"));
    }

    @Test
    void testFromString_EmptyString() {
        assertEquals(Status.ACTIVE, Status.fromString(""));
    }

    @Test
    void testToJson() {
        assertEquals("active", Status.ACTIVE.toJson());
        assertEquals("dormant", Status.DORMANT.toJson());
        assertEquals("deleted", Status.DELETED.toJson());
    }
}
