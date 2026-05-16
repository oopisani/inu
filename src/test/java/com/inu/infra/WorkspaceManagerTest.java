package com.inu.infra;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WorkspaceManagerTest {

    @Test
    void shouldCreateRootDirectory() {
        WorkspaceManager workspaceManager = new WorkspaceManager();
        workspaceManager.setup();

        assertTrue(Files.exists(
                Path.of(System.getProperty("user.home"), "inu-workspace")
        ));
    }
}
