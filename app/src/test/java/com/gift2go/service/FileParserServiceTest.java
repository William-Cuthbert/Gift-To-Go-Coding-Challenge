package com.gift2go.service;

import com.gift2go.exception.FileParserException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FileParserServiceTest {

    private final FileParserService parser = new FileParserService();

    @Test
    void should_throw_exception_invalid_line_format() {
        String invalidContent = "invalid_line_without_separators";
        MultipartFile mockFile = new MockMultipartFile("file", "EntryFile.txt",
                "text/plain", invalidContent.getBytes(StandardCharsets.UTF_8));
        assertThrows(FileParserException.class, () -> parser.parse(mockFile));
    }

    @Test
    void parse_success() {
        String content = """
                18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1
                3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5
                """;
        MultipartFile mockFile = new MockMultipartFile("file", "EntryFile.txt",
                "text/plain", content.getBytes(StandardCharsets.UTF_8));

        var result = parser.parse(mockFile);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("John Smith");
        assertThat(result.get(1).transport()).isEqualTo("Drives an SUV");
    }
}
