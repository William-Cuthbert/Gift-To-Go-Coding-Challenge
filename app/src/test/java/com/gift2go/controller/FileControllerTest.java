package com.gift2go.controller;

import com.gift2go.FileController;
import com.gift2go.service.FileService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    void processFile_shouldReturnOutcomeFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "EntryFile.txt", "text/plain", "dummy content".getBytes()
        );

        File dummyOutcome = File.createTempFile("OutcomeFileTest", ".json");
        when(fileService.processAndGenerateOutcomeFile(file)).thenReturn(dummyOutcome);

        mockMvc.perform(multipart("/v1/files/process").file(file))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + dummyOutcome.getName()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        dummyOutcome.delete();
    }
}

