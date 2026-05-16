package com.inu.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandParserTest {

    @Test
    void shouldParseBasicCommand() {
        CommandParser parser = new CommandParser("script.py work");

        assertEquals("script.py", parser.getName());
        assertEquals("work", parser.getTag());
        assertEquals("py", parser.getExtension());
    }
}