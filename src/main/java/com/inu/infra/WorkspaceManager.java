package com.inu.infra;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/**
 * Responsible for the directory infrastructure, extension governance,
 * orchestration of the data flow (Raw to Inbox), and script inventory management.
 */
public class WorkspaceManager {
    private final Path raw;
    private final Path inbox;
    private final Path study;
    private final Path work;
    private final Path cyber;
    private final Path sys;
    private final Path temp;
    private final Path general;
    private final Path root;
    public static final Logger logger = LogManager.getLogger(WorkspaceManager.class);

    public WorkspaceManager() {
        this.root = Path.of(System.getProperty("user.home"), "inu-workspace");
        this.raw = root.resolve("raw");
        this.inbox = root.resolve("scripts/inbox");
        this.study = inbox.resolve("study");
        this.work = inbox.resolve("work");
        this.cyber = inbox.resolve("cyber");
        this.sys = inbox.resolve("sys");
        this.temp = inbox.resolve("temp");
        this.general = inbox.resolve("general");
    }

    /**
     * Initializes the directory structure required for system operation.
     * Creates the root folders, input areas (Raw/Inbox), and script categories.
     */
    public void setup() {
        try {
            Files.createDirectories(root);
            Files.createDirectories(raw);
            Files.createDirectories(inbox);
            Files.createDirectories(study);
            Files.createDirectories(work);
            Files.createDirectories(cyber);
            Files.createDirectories(sys);
            Files.createDirectories(temp);
            Files.createDirectories(general);

            logger.info("Environment setup completed successfully at inu-workspace");
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize workspace infrastructure", e);
        }
    }

    /**
     * Defines the file governance criteria, accepting only supported extensions.
     *
     * @return A filter configured with py, sh and ps1 extensions.
     */
    public SuffixFileFilter filterExt() {
        String[] extensions = {"py", "sh", "ps1"};
        return new SuffixFileFilter(extensions);
    }

    /**
     * Performs the ingestion of new scripts, moving valid files
     * from the input folder (Raw) to the processing area (Inbox).
     */
    public void syncToInbox() {
        File rawDir = raw.toFile();
        File inboxDir = inbox.toFile();
        SuffixFileFilter filter = filterExt();
        int count = 0;
        File[] scripts = rawDir.listFiles((FileFilter) filter);
        if (scripts != null) {
            for (File el : scripts) {
                try {
                        FileUtils.moveFileToDirectory(el, inboxDir, true);
                        logger.info("Moved file: {} to inbox", el.getName());
                        count++;

                } catch (IOException e) {
                    logger.error("Failed to move script {}: {}", el.getName(), e.getMessage());
                }
            }
        }
        int movedCount = count;

        if (movedCount > 0) {
            logger.info("Merge completed: {} scripts moved from RAW to INBOX.", movedCount);
        }  else {
                logger.info("Nothing to merge: RAW folder is empty or contains no valid scripts.");
        }
    }

    /**
     * Lists all pending scripts located directly in the root of the inbox directory.
     * These represent files that have not yet been sorted into specific tag folders.
     */
    public void pendingScripts() {
        logger.debug("Checking for pending scripts in inbox root.");
        File inboxDir = inbox.toFile();
        File[] files = inboxDir.listFiles(File::isFile);

        if(files == null) {
            logger.error("Critical: Could not list files in inbox path: {}", inboxDir.getAbsolutePath());
            System.out.println("(!) System error: Inbox unreachable.");
            return;
        }
        if (files.length == 0) {
            System.out.println("Nothing pending here!");
            return;
        }
            for (File file : files) {
                System.out.printf("  └── %s\n", file.getName().toUpperCase());
            }
    }

    /**
     * Performs a full scan of all tag folders inside the inbox.
     * Displays a structured layout showing each category (tag) and its respective scripts.
     */
    public void allScripts() {
        logger.info("Starting a full scan of all tag folders in inbox...");
        File inboxDir = inbox.toFile();
        File[] folders = inboxDir.listFiles(File::isDirectory);

        if (folders == null || folders.length == 0) {
            logger.warn("Full scan aborted: No tag folders found in {}", inboxDir.getAbsolutePath());
            System.out.println("All Tag Folders are empty.");
            return;
        }
        int cont = 0;
        for (File el : folders) {
            System.out.printf("\n%s:\n", el.getName().toUpperCase());

            Collection<File> scripts = FileUtils.listFiles(
                    el,
                    FileFileFilter.FILE,    // Only files are allowed
                    TrueFileFilter.INSTANCE // Recurse into all subdirectories
            );

            if (!scripts.isEmpty()) {
                for (File script : scripts) {
                    cont++;
                    System.out.println("  └── " + script.getName());
                }
            } else {
                System.out.println("  -> (empty)");
            }
        }
        System.out.println();
        logger.info("Scan completed. {} scripts found.", cont);
    }

    /**
     * Lists all available scripts inside a specific tag folder.
     * Prevents checking the root inbox here, redirecting the user if necessary.
     *
     * @param tag The name of the target tag directory to scan.
     */
    public void scriptsPerTag(String tag) {
        logger.info("Requesting scripts for specific tag: '{}'", tag);

            File tagPath = tag.equals("inbox")
                    ? inbox.toFile()
                    : inbox.resolve(tag).toFile();

            if(tag.equalsIgnoreCase("inbox")) {
                logger.warn("User attempted to list 'inbox' as a tag. Redirection suggested.");
                System.out.println("You shouldn't check inbox here. Check at option 'pending'.");
                return;
            }

            if (!tagPath.exists() || !tagPath.isDirectory()) {
                logger.warn("Tag folder search failed. Directory not found: {}", tagPath.getAbsolutePath());
                System.out.println("(!) Tag '" + tag + "' not found.");
                return;
            }
            Collection<File> scripts = FileUtils.listFiles(tagPath,
                    FileFileFilter.FILE,
                    TrueFileFilter.INSTANCE);

             if (!scripts.isEmpty()) {
                 logger.info("Successfully listed {} scripts for tag '{}'", scripts.size(), tag);
                for (File script : scripts) {
                    System.out.printf("  └── %s\n", script.getName().toUpperCase());
                }
            } else {
                System.out.println("Nothing here yet!");
            }

    }
}








