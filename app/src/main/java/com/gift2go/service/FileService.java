package com.gift2go.service;

import com.gift2go.mapper.EntryFileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class FileService {

    private final FileParserService parser;
    private final ValidationService validationService;
    private final EntryFileMapper mapper;
    private final FileWriterService fileWriterService;

    @Autowired
    public FileService(FileParserService parser, EntryFileMapper entryFileMapper, ValidationService validationService,
                       FileWriterService fileWriterService) {
        this.parser = parser;
        this.mapper = entryFileMapper;
        this.validationService = validationService;
        this.fileWriterService = fileWriterService;
    }

    public File processAndGenerateOutcomeFile(MultipartFile file) throws IOException {
        log.info("Processing file: {}", file.getOriginalFilename());
        var entries = parser.parse(file);

        validationService.validate(entries);

        var outcomes = mapper.toOutcomeFile(entries);

        var outcomeFile = fileWriterService.createFile(outcomes, "OutcomeFile");

        log.info("Outcome file created without errors: {}", outcomeFile.getAbsolutePath());
        return outcomeFile;
    }
}
