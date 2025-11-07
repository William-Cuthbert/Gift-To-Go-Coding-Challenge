package com.gift2go;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceTest {

    @InjectMocks
    private FileService fileService;

    private EntryFileDto entryFile;
    private OutcomeFileDto outcomeFile;

    @BeforeEach
    void setUp() {
        entryFile = new EntryFileDto(UUID.randomUUID(), "1X1D14", "John Smith",
                "Likes Apricots", "Rides A Bike",6.2,12.1);
        outcomeFile = new OutcomeFileDto(entryFile.name(), entryFile.transport(), entryFile.topSpeed());
    }

    @AfterEach
    void cleanUp() {
        fileService = null;
        entryFile = null;
        outcomeFile = null;
    }

    @Test
    void generateOutcomeFileFailed() {
        assertNotNull(entryFile);
        assertNotNull(outcomeFile);
    }

    @Test
    void generateOutcomeFileSuccess() {

    }
}
