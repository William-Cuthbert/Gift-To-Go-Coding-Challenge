package com.gift2go;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileParserService {

    public List<EntryFileDto> parse(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines()
                    .filter(line -> !line.isBlank())
                    .map(line -> {
                        String[] sections = line.split("\\|");
                        if (sections.length != 7) {
                            throw new IllegalArgumentException("Invalid line format: " + line);
                        }
                        return new EntryFileDto(
                                UUID.fromString(sections[0]),
                                sections[1],
                                sections[2],
                                sections[3],
                                sections[4],
                                Double.valueOf(sections[5]),
                                Double.valueOf(sections[6]));
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse the entry file: " + e.getMessage(), e);
        }
    }
}
