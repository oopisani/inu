package com.inu.engine;

import com.inu.app.CommandParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandParserScheduleScriptTest {

    @Test
    void shouldParseScheduledCommand() {
        CommandParser parser = new CommandParser("script.py work 12:00 daily");

        assertEquals("12:00", parser.getTiming());
        assertEquals("daily", parser.getFrequency());
    }
}