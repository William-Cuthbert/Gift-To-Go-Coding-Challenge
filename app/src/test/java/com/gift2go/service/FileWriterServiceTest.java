package com.gift2go.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gift2go.dto.OutcomeFileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileWriterServiceTest {

    private FileWriterService fileWriterService;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        fileWriterService = new FileWriterService(mapper);
    }

    @Test
    void writeOutcomeFile_throwsException_whenObjectCannotBeSerialized() {
        Object badObject = new Object() {
            public Object self = this;
        };

        assertThrows(IllegalStateException.class, () -> fileWriterService.createFile(badObject, "BadFile"));
    }

    @Test
    void writeOutcomeFile_createsFileAndWritesJson() throws Exception {
        List<OutcomeFileDto> outcomes = List.of(
                new OutcomeFileDto("John Smith", "Rides A Bike", 12.1),
                new OutcomeFileDto("Mike Smith", "Drives an SUV", 95.5)
        );

        File file = fileWriterService.createFile(outcomes, "OutcomeFileTest");

        assertThat(file).exists();
        String content = Files.readString(file.toPath());
        assertThat(content).contains("John Smith", "Mike Smith", "Rides A Bike", "Drives an SUV", "12.1", "95.5");

        file.delete();
    }
}
