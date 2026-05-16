package com.inu.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandParserValidationTest {

    @Test
    void shouldThrowExceptionWhenInputIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CommandParser("invalid");
        });
    }
}
