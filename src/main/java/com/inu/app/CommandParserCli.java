package com.inu.app;

public class CommandParserCli {
    private final String name;
    private final String tag;
    private final String extension;
    private final String command;
    private String frequency;
    private String timing;


    public CommandParserCli(String input) {
        String[] tokens = input.trim().split("\\s+"); // "scriptname.ext" "work" or  "scriptname.ext" "work" "timing" "frequency"
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Expected at least: <command> <script> <tag>");
        }
        this.command = tokens[0];
        this.name = tokens[1];
        this.tag = tokens[2];

        int index = name.lastIndexOf(".");
        if (index == -1) {
            throw new IllegalArgumentException("Script name without extension");
        }

        this.extension = tokens[1].substring(index + 1);

        if (tokens.length == 4) {
            throw new IllegalArgumentException("Incomplete command. If scheduling a task, expected: <command> <script> <tag> <timing> <frequency>");
        }
        if (tokens.length == 5) {
            this.timing = tokens[3];
            this.frequency = tokens[4];
        } else {
            if (tokens.length > 5) {
                throw new IllegalArgumentException("Too many arguments. Expected max 5: <command> <script> <tag> [timing] [frequency]");
            }
        }
    }


    public String getFrequency() { return frequency; }

    public String getName() { return name; }

    public String getExtension() { return extension; }

    public String getTag() { return tag; }

    public String getTiming() { return timing; }
}

