package com.inu.engine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
            // For scheduling, we must join the python, powershell, or bash prefix with the script's absolute path,
            // as Windows expects a single unified command string for the /tr argument (unlike the direct execution in the run method).
           String commandToSchedule = String.join(" ", getProcessBuilder(getExt(), script.toFile()).command());

            if(!Files.exists(script)) {
                logger.warn("Script '{}' not found. Check the corresponding folder.", getScriptName());
                return;
            }
                try {
                     // Configures the native Windows call for scheduled task creation
                    ProcessBuilder pb = new ProcessBuilder(
                            "schtasks", "/create",
                            "/tn", this.getScriptName(), // Task name
                            "/tr", commandToSchedule, // Script script
                            "/sc", frequency, // Frequency (e.g., daily, weekly)
                            "/st", timing, // Execution time (e.g., 12:15)
                            "/rl", "LIMITED", // Run with limited privileges, for security
                            "/f" // Force creation if task already exists
                    );
                    pb.redirectErrorStream(true);
                    Process process = pb.start();
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("CP850")));
                    br.lins().forEach(logger::warn);
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        logger.info("[SUCCESS] Task scheduled: {}", this.getScriptName());
                    } else {
                        // Exit code 1 or non-zero typically indicates invalid syntax, arguments, or task conflicts.
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

            }
    }

