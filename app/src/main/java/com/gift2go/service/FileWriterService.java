package com.gift2go.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class FileWriterService {

    private static final String FILE_FORMAT = ".json";
    private final ObjectMapper objectMapper;

    @Autowired
    public FileWriterService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public File createFile(Object data, String fileName) throws IOException {
        try {
            log.info("Creating a file with data");
            Path tempFile = Files.createTempFile(fileName, FILE_FORMAT);
            objectMapper.writeValue(tempFile.toFile(), data);
            return tempFile.toFile();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create or write outcome file", e);
        }
    }
}
