package com.inu.engine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * ScheduleScript: A specialization of the Script entity focused on automation management.
 * Responsible for integrating scripts with the Windows Task Scheduler (schtasks),
 * managing identities, frequencies, and execution timings.
 */
public class ScheduleScript extends Script {

private final String frequency;
private final String timing;
private static final Logger logger = LogManager.getLogger(ScheduleScript.class);


    public ScheduleScript(String name, String extension, String tag, String frequency, String timing) {
        super(name, extension, tag);
        this.frequency = frequency;
        this.timing = timing;
    }

    /**
     * Registers the script in the Windows Task Scheduler via CLI (schtasks).
     * Sets execution with limited privileges for security purposes and overwrites duplicate tasks (/f).
     *
     */
    public void createSysTask() {
            Path script = this.getScript();

            if (Files.exists(script)) {
                // Configures the native Windows call for scheduled task creation
                ProcessBuilder pb = new ProcessBuilder(
                        "schtasks", "/create",
                        "/tn", this.getScriptName(), // Task name
                        "/tr", script.toAbsolutePath().toString(), // Script script
                        "/sc", frequency, // Frequency (e.g., daily, weekly)
                        "/st", timing, // Execution time (e.g., 12:15)
                        "/rl", "LIMITED", // Run with limited privileges, for security
                        "/f" // Force creation if task already exists
                );
                try {
                    Process process = pb.start();
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        // Exit code 1 or non-zero typically indicates invalid syntax, arguments, or task conflicts.
                        logger.info("[SUCCESS] Task scheduled: {}", this.getScriptName());
                    } else {
                        logger.error("Windows rejected task creation. Exit: {} (Hex: 0x{})",
                                exitCode, Integer.toHexString(exitCode).toUpperCase());
                        logger.error("Action: Ensure English frequency names (DAILY/WEEKLY) and run the application outside the IDE environment.");
                    }
                } catch(IOException e) {
                    logger.fatal(
                            "I/O failure while running '{}'", getScriptName(), e);
                } catch(InterruptedException e) {
                    //  Restores the interrupt status to maintain current thread integrity.
                    // "By convention, any method that exits by throwing an InterruptedException clears interrupt status".
                    Thread.currentThread().interrupt();
                    logger.fatal("Execution interrupted for '{}'", getScriptName(), e);

                   }
                } else {
                 logger.warn("Script '{}' not found. Check the corresponding folder.", getScriptName());
               }
            }
    }

