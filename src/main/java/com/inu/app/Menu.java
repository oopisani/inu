package com.inu.app;
import com.inu.infra.WorkspaceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.inu.engine.Script;
import com.inu.engine.ScheduleScript;

import java.util.Scanner;

public class Menu {

    private static final Logger logger = LogManager.getLogger(Menu.class);

    public static void main(String[] args) {
        WorkspaceManager workspaceManager = new WorkspaceManager();
        Scanner scan = new Scanner(System.in);

        if (args.length > 0) {
            Cli.cliRouter(args, workspaceManager);
            return;
        }
        while (true) {
            System.out.println(LOGO);
            menu();
            System.out.print("> Selection: ");
            var option = scan.nextLine().trim().toLowerCase();
            if (option.equals("ex")) {
                System.out.println("Exiting inu... Goodbye!");
                break;
            } else {
                router(option, workspaceManager, scan);
            }
        }
    }
        public static void router (String option, WorkspaceManager workspaceManager, Scanner scan)  {
            switch (option) {
                case "init" -> initHandler(workspaceManager);
                case "sync" -> workspaceManager.syncToInbox();
                case "move" -> moveHandler(workspaceManager, scan);
                case "run" -> runHandler(scan);
                case "del" -> deleteHandler(scan);
                case "task" -> scheduleHandler(scan);
                case "list" -> listHandler(workspaceManager, scan);
                case "help" -> displayHelp(true);
                default -> {
                    System.out.println("(!) Invalid command! Type 'help' to see available options.");
                    logger.warn("Unknown command: '{}'.", option);
                }
            }
        }
        public static void initHandler(WorkspaceManager workspaceManager) {
            logger.info("Starting environment initialization...");
        try {
            workspaceManager.setup();
        } catch (RuntimeException e) {
            logger.error("Environment initialization failed", e);
           }
        }
        public static void listHandler(WorkspaceManager workspaceManager, Scanner scan) {

            while (true) {
                System.out.println();
                System.out.println("--- SCRIPT EXPLORER ---");
                System.out.println("\nOptions: [all] | [tag] | [pending] | [<]");
                System.out.print("Choice: ");
                String input = scan.nextLine().trim().toLowerCase();

                if (input.equals("<")) {
                    break;
                }
                if (input.isBlank()) {
                    logger.warn("Script input cannot be empty");
                    continue;
                }
                if  (input.equals("all")) {
                    workspaceManager.allScripts();

                } else if (input.equals("pending")) {
                    workspaceManager.pendingScripts();

                } else if (input.equals("tag")) {
                    System.out.println("Options: Enter tag name | [<]");
                    System.out.print("Choice: ");
                    String tagName = scan.nextLine().trim().toLowerCase();

                    if (tagName.equals("<")) {
                        break;
                    }
                    if (tagName.isBlank()) {
                        logger.warn("Script input cannot be empty");
                        continue;
                    }
                    workspaceManager.scriptsPerTag(tagName);
                }  else {
                    logger.warn("Invalid command '{}'", input);
                }

               }
        }

        public static void scheduleHandler(Scanner scan) {

            while (true) {
                System.out.println("\n--- TASK SCHEDULER ---");
                System.out.println();
                System.out.println("Format: name tag timing frequency | (Example: weather.py work 12:00 daily)");
                System.out.println("Options: Enter script | [<]");
                System.out.print("Choice: ");
                String input = scan.nextLine().trim().toLowerCase();

                if (input.equals("<")) {
                    break;
                }
                if (input.isBlank()) {
                    logger.warn("Script input cannot be empty.");
                    continue;
                }
                logger.info("Starting task scheduling...");
                try {
                    CommandParser parser = new CommandParser(input);
                    ScheduleScript scheduleScript = new ScheduleScript(parser.getName(),
                            parser.getExtension(),
                            parser.getTag(),
                            parser.getFrequency(),
                            parser.getTiming());

                    scheduleScript.createSysTask();

                } catch (IllegalArgumentException | NullPointerException e) {
                    logger.warn("Validation error: {}", e.getMessage());

                }
            }
        }

        public static void deleteHandler(Scanner scan) {
        while(true) {
            System.out.println("\n--- SCRIPT " + "DELETER" + " ---");
            System.out.println();
            System.out.println("Options: Enter script | [<]");
            System.out.print("Choice: ");
            var input = scan.nextLine().trim().toLowerCase();

            if (input.equals("<")) {
                break;
            }
            if(input.isBlank()) {
                logger.warn("Script input cannot be empty.");
                continue;
            }
            logger.info("Deleting script...");
            try {
                CommandParser parser = new CommandParser(input);
                Script script = new Script(parser.getName(),
                        parser.getExtension(),
                        parser.getTag());

              script.del();
            } catch(IllegalArgumentException e) {
                logger.warn("Validation error: {}", e.getMessage());
            }
          }
        }
       public static void runHandler(Scanner scan) {
           while(true) {
               System.out.println("\n--- SCRIPT " + "RUNNER" + " ---");
               System.out.println();
               System.out.println("Options: Enter script | [<]");
               System.out.print("Choice: ");
               var input = scan.nextLine().trim().toLowerCase();

               if (input.equals("<")) {
                   break;
               }
               if(input.isBlank()) {
                   logger.warn("Script input cannot be empty.");
                   continue;
               }
               logger.info("Starting script execution...");
               try {
                   CommandParser parser = new CommandParser(input);
                   Script script = new Script(parser.getName(),
                           parser.getExtension(),
                           parser.getTag());

                   script.run();
               } catch(IllegalArgumentException e) {
                   logger.warn("Validation error: {}", e.getMessage());
               }
           }
       }

        public static void moveHandler(WorkspaceManager workspaceManager, Scanner scan) {
                 while (true) {
                     System.out.println("\n--- PENDING SCRIPTS (INBOX) ---");
                     System.out.println();
                     workspaceManager.pendingScripts();
                     System.out.println("Options: Enter script | [<] ");
                     System.out.print("Choice: ");
                     var input = scan.nextLine().trim().toLowerCase();

                     if (input.equals("<")) {
                         break;
                     }
                     if(input.isBlank()) {
                         logger.warn("Script input cannot be empty.");
                         continue;
                     }

                 logger.info("Moving script to tag...");
                 try {
                   CommandParser parser = new CommandParser(input);

                     Script script = new Script(parser.getName(),
                             parser.getExtension(),
                             parser.getTag());
                      script.moveToTag();

                    } catch(IllegalArgumentException e) {
                     logger.warn("Validation error: {}", e.getMessage());
                    }
                 }
        }

        public static void menu () {
            System.out.println("""
                   
                    inu - v0.1.0
                    https://github.com/oopisani
                   
                    USAGE
                      init    init environment
                      sync    sync scripts from raw to inbox
                      move    move script to tag
                      run     run script
                      del     delete script
                      list    list scripts
                      task    scheduleScript task (Windows Task Scheduler)
                      ex      exit
                   
                   """);
        }
      private static final String LOGO = """
         \s
          .Y#&B~   !B&#J.      \s
         .B@@@@&: ^&@@@@G.     \s
         ^@@@@@@^ ~@@@@@@:     \s
     7GP?.~PBBP~   !PBBP~.?GG! \s
    :@@@@#^   .7JY?~    ^#@@@&:\s
    .B@@@@?  7&@@@@@P.  J@@@@B.\s
     .7YJ~ .J@@@@@@@@B~  !YY7. \s
         !P&@@@@@@@@@@@BY^     \s
        G@@@@@@@@@@@@@@@@@!    \s
        Y@@@@@&BP5P#@@@@@#~    \s
   \s""";

        public static void displayHelp (boolean showMenu){
            if (showMenu) {
                help();
            }
        }
    public static void help () {
        System.out.println("""
       
       
        inu - Command Syntax
        
        1. SYSTEM SETUP 
           init                            initialize environment (create folders)
        
        2. SYNCHRONIZE (RAW > INBOX)
           sync                             sync approve scripts from RAW to INBOX
        
        3. MOVING (INBOX > TAG)
           name.ext tag                     move script to a specific tag folder
           
        4. EXECUTION
           name.ext tag                     run script
           name.ext tag                     delete script
           name.ext tag timing frequency    scheduleScript script (Windows Task Scheduler)
           
        5. UTILITY
           list                             list scripts
           help                             display help menu
           ex                               exit
        """);
    }

 }







