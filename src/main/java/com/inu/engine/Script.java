package com.inu.engine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Script: Entity responsible for identity management, tag-based routing,
 * and orchestration of execution (run) and deletion (del) operations.
 */
public class Script {
    private final Path script;
    private final Path scriptFolder;
    private final String scriptName;
    private final String folderName;
    private final String tag;
    private final String ext;
    private final List<String> SUPPORTED_TAGS = Arrays.asList("cyber", "study", "general", "sys", "work");
    private final List<String> SUPPORTED_EXTENSIONS = Arrays.asList("py", "sh", "ps1");
    private static final Logger logger = LogManager.getLogger(Script.class);


    public Script(String scriptName, String ext, String tag) {
        this.scriptName = scriptName;
        int index = scriptName.lastIndexOf(".");
        this.folderName = scriptName.substring(0,index);
        this.ext = ext;
        this.tag = tag;

        if (!SUPPORTED_TAGS.contains(tag)) {
            throw new IllegalArgumentException("Tag folder '" + tag + "' is not supported.");
        }
        if (!SUPPORTED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Extension '." + ext + "' is not supported.");
        }

        Path root = Path.of(System.getProperty("user.home"), "inu-workspace",
                "scripts",
                "inbox",
                tag,
                folderName);

        this.script = root.resolve(scriptName).normalize();
        if(!script.startsWith(root)) {
            throw new SecurityException("Access denied: Attempt to access a path outside the inu directory.");
        }
        this.scriptFolder = root;

    }

    public String getScriptName() {
        return scriptName;
    }

    public Path getScript() {
        return script;
    }

    /**
     * Moves scripts from the Inbox folder to the category folder (Tag) defined by the user.
     * The destination path abstracts the structure down to the file level.
     */
    public void moveToTag() {

        File scriptPending = Path.of(
                System.getProperty("user.home"),
                "inu-workspace",
                "scripts",
                "inbox",
                scriptName).toFile();
            File scriptFolderFile = scriptFolder.toFile();
            
        if(!scriptPending.exists()) {
            logger.warn("File not found in inbox '{}'", scriptName);
            return;
        }
      try {
        FileUtils.moveFileToDirectory(scriptPending,
                scriptFolderFile,
                true);
        logger.info("[SUCCESS] Script '{}' moved to tag folder: {}", scriptName, tag);

      } catch (IOException e) {
        logger.error(
            "I/O failure while routing script '{}'", scriptName, e);
         }
      }

    /**
     * Removes the script from the system, deleting the parent directory
     * to maintain structural integrity.
     */
    public void del() {
       Path folder = scriptFolder;
       if(Files.exists(script)) {
           try {
               FileUtils.deleteDirectory(folder.toFile());
               logger.info("[SUCCESS] Deleting script: {}", scriptName);

           } catch (IOException e) {
               logger.error(
                       "I/O failure while deleting script '{}'", scriptName, e);
           }
       } else {
           logger.warn("Script '{}' not found. Check the corresponding folder.", scriptName);
       }
    }

    /**
     * Orchestrates the script execution according to its suffix (extension) and location.
     */
    public void run() {
            Path script = this.getScript();
            if(Files.exists(script)) {
                try {
                    ProcessBuilder pb = getProcessBuilder(ext, script.toFile());
                    pb.inheritIO();
                    Process process = pb.start();
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        logger.info("[SUCCESS] Running script  '{}' with (exit '{}')", scriptName, exitCode);
                    } else {
                        logger.warn("Script '{}' finished with non-zero exit code: {} (Hex: 0x{})", scriptName,
                                exitCode, Integer.toHexString(exitCode).toUpperCase());
                    }
                } catch(IOException e) {
                        logger.fatal(
                            "I/O failure while running '{}'", scriptName, e);
                } catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                       logger.fatal("Execution interrupted for '{}'", scriptName, e);
                }
            } else {
                       logger.warn("Script '{}' not found. Check the corresponding folder.", scriptName);
            }
    }

    /**
     * Manufactures the ProcessBuilder configuration based on the script extension.
     */
    private @NotNull ProcessBuilder getProcessBuilder(String ext, File script) {
        ProcessBuilder pb;
        switch (ext) {
            case "py" -> pb = new ProcessBuilder("python", script.getAbsolutePath());
            case "ps1" ->
                    pb = new ProcessBuilder("powershell", "-ExecutionPolicy", "Bypass", "-File", script.getAbsolutePath());
            case "sh" -> pb = new ProcessBuilder("bash", script.getAbsolutePath());
            default -> throw new IllegalArgumentException(
                    "Unsupported extension: " + ext
            );
        }
        // Sets the working directory to the script's folder to support local dependencies (assets/files)
        pb.directory(scriptFolder.toFile());
        return pb;

    }

}
