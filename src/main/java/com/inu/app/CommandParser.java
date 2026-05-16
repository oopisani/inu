package com.inu.app;


public class CommandParser {
     private final String name;
     private final String tag;
     private final String extension;
     private String frequency;
     private String timing;

     public CommandParser(String input) {
         String[] tokens = input.trim().split("\\s+"); // "scriptname.ext" "work" or  "scriptname.ext" "work" "timing" "frequency"
         if (tokens.length < 2) {
             throw new IllegalArgumentException("Expected at least: <script> <tag>");
         }
         this.name = tokens[0];
         this.tag = tokens[1];

         int index = name.lastIndexOf(".");
         if (index == -1) {
             throw new IllegalArgumentException("Script name without extension");
         }

         this.extension = tokens[0].substring(index + 1);

         if (tokens.length == 3) {
             throw new IllegalArgumentException("Incomplete command. If scheduling a task, expected: <script> <tag> <timing> <frequency>");
         }
         if (tokens.length == 4) {
             this.timing = tokens[2];
             this.frequency = tokens[3];
         } else {
             if (tokens.length > 4) {
                 throw new IllegalArgumentException("Too many arguments. Expected max 4: <script> <tag> [timing] [frequency]");
             }
         }
     }

    public String getFrequency() { return frequency; }

    public String getName() { return name; }

    public String getExtension() { return extension; }

    public String getTag() { return tag; }

    public String getTiming() { return timing; }
 }
