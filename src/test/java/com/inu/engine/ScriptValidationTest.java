package com.inu.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ScriptValidationTest {

    @Test
    void shouldRejectInvalidExtension() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Script("test.exe", "exe", "work");
        });
    }
}