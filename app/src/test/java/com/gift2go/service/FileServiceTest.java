package com.gift2go.service;

import com.gift2go.dto.EntryFileDto;
import com.gift2go.dto.OutcomeFileDto;
import com.gift2go.exception.ValidationException;
import com.gift2go.mapper.EntryFileMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    @Mock private ValidationService validationService;
    @Mock private FileWriterService fileWriterService;
    @Mock private FileParserService fileParserService;
    @Mock private EntryFileMapper mapper;
    @InjectMocks private FileService fileService;

    private EntryFileDto entryFile;
    private OutcomeFileDto outcomeFile;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        entryFile = new EntryFileDto(UUID.randomUUID(), "1X1D14", "John Smith",
                "Likes Apricots", "Rides A Bike",6.2,12.1);
        outcomeFile = new OutcomeFileDto(entryFile.name(), entryFile.transport(), entryFile.topSpeed());
    }

    @AfterEach
    void cleanUp() throws Exception {
        mocks.close();
        entryFile = null;
        outcomeFile = null;
    }

    @Test
    void process_outcome_failed_due_invalid_content() {
        MultipartFile malformedFile = new MockMultipartFile("file", "EntryFile.txt", "text/plain", "badline".getBytes());

        when(fileParserService.parse(malformedFile)).thenThrow(new IllegalArgumentException("Invalid line format"));

        assertThrows(IllegalArgumentException.class, () -> fileService.processAndGenerateOutcomeFile(malformedFile));

        verifyNoInteractions(validationService, mapper, fileWriterService);
    }

    @Test
    void process_outcome_failed_due_validation_errors() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "EntryFile.txt", "text/plain", "validline".getBytes());

        when(fileParserService.parse(file)).thenReturn(List.of(entryFile));
        doThrow(new ValidationException("Validation failed")).when(validationService).validate(List.of(entryFile));

        assertThrows(ValidationException.class, () -> fileService.processAndGenerateOutcomeFile(file));

        verify(validationService, times(1)).validate(List.of(entryFile));
        verifyNoInteractions(mapper, fileWriterService);
    }


    @Test
    void process_outcome_file_success() throws IOException {
        String content = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1";
        MultipartFile multipartFile = new MockMultipartFile("file", "EntryFile.txt", "text/plain", content.getBytes(StandardCharsets.UTF_8));

        when(fileParserService.parse(multipartFile)).thenReturn(List.of(entryFile));
        when(mapper.toOutcomeFile(List.of(entryFile))).thenReturn(List.of(outcomeFile));

        File testFile = File.createTempFile("OutcomeFileTest", ".json");
        when(fileWriterService.createFile(any(), eq("OutcomeFile"))).thenReturn(testFile);

        File result = fileService.processAndGenerateOutcomeFile(multipartFile);

        verify(validationService, times(1)).validate(List.of(entryFile));
        verify(mapper, times(1)).toOutcomeFile(List.of(entryFile));
        verify(fileParserService, times(1)).parse(multipartFile);
        verify(fileWriterService, times(1)).createFile(List.of(outcomeFile), "OutcomeFile");

        assertEquals(testFile.getAbsolutePath(), result.getAbsolutePath());
        testFile.delete();
        result.delete();
    }
}
