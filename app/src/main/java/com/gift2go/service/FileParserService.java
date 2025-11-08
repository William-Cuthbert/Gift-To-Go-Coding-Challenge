package com.gift2go.service;

import com.gift2go.dto.EntryFileDto;
import com.gift2go.exception.FileParserException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileParserService {

    private static final int NUMBER_COLUMNS_REQUIRED = 7;

    public List<EntryFileDto> parse(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            log.info("Parsing files process has started.");
            return reader.lines()
                    .filter(line -> !line.isBlank())
                    .map(this::mapToEntryFileDto)
                    .collect(Collectors.toList());

        } catch (FileParserException | IOException e) {
            log.error("Failed to parse the entry file: {}", file.getName());
            throw new FileParserException("Failed to parse the entry file: " + e.getMessage());
        }
    }

    private EntryFileDto mapToEntryFileDto(String line) {
        String[] sections = line.split("\\|");
        if (sections.length != NUMBER_COLUMNS_REQUIRED) {
            log.error("Parse error due to mismatch number of columns, expected {} but there are: {}",
                    NUMBER_COLUMNS_REQUIRED, sections.length);
            throw new FileParserException("Invalid line format: " + line);
        }

        return new EntryFileDto(
                UUID.fromString(sections[0].trim()),
                sections[1].trim(),
                sections[2].trim(),
                sections[3].trim(),
                sections[4].trim(),
                Double.valueOf(sections[5].trim()),
                Double.valueOf(sections[6].trim()));
    }
}
