package com.inu.app;

import com.inu.engine.ScheduleScript;
import com.inu.engine.Script;
import com.inu.infra.WorkspaceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Cli {
    public static final Logger logger = LogManager.getLogger(Cli.class);

    public static void main(String[] args)  {
        WorkspaceManager workspaceManager = new WorkspaceManager();

        if (args.length > 0) {
            cliRouter(args, workspaceManager);
        } else {
            System.out.println("No command provided.");
        }
    }

    public static void cliRouter(String[] args, WorkspaceManager workspaceManager) {
        String option = args[0].trim().toLowerCase(); // "run"
        String[] subArgs = Arrays.copyOfRange(args, 0, args.length);
        String input = String.join(" ",subArgs);

        if (args.length == 1) {
            switch (option) {
                case "help" -> Menu.displayHelp(true);
                case "init" -> workspaceManager.setup();
                case "sync" ->  workspaceManager.syncToInbox();
                default -> {
                    System.out.println("(!) Invalid command! Type 'help' to see available options.");
                    logger.warn("Unknown CLI command: '{}'", option);
                }

            }
            return;
        }
            try {
                switch (option) {
                    case "run", "del" -> {
                        CommandParser parser = new CommandParser(input);
                        Script script = new Script(parser.getName(),
                                parser.getExtension(),
                                parser.getTag());

                        if (option.equals("run")) {
                            script.run();
                        } else {
                            script.del();
                        }
                    }
                    case "task" -> {
                        CommandParser parser = new CommandParser(input);
                        ScheduleScript scheduleScript = new ScheduleScript(parser.getName(),
                                parser.getExtension(),
                                parser.getTag(),
                                parser.getFrequency(),
                                parser.getTiming());

                        scheduleScript.createSysTask();
                    }
                    case "list" -> {
                        if (input.equals("pending")) {
                            workspaceManager.pendingScripts();

                        } else if (input.equals("all")) {
                            workspaceManager.allScripts();

                        } else if (input.startsWith("tag")) { // "tag work"
                            String[] commandParts = input.split(" ");
                            if (commandParts.length == 2) {
                                String targetTag = commandParts[1];
                                workspaceManager.scriptsPerTag(targetTag);
                            } else {
                                logger.warn("Syntax: list tag <folder_name>");
                            }
                        } else {
                                logger.warn("List command not recognized.");
                        }
                    }
                    case "move" -> {
                        CommandParser parser = new CommandParser(input);
                        Script script = new Script(parser.getName(),
                                parser.getExtension(),
                                parser.getTag());

                        script.moveToTag();
                    }
                    default -> {
                        System.out.println("(!) Invalid command! Type 'help' to see available options.");
                        logger.warn("Unknown CLI command: '{}'", option);
                    }
                }

            } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                logger.warn("Input error [{}]: {}", e.getClass().getSimpleName(), e.getMessage());
            }

        }
    }
