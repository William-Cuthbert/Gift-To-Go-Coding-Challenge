package com.gift2go;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ValidationServiceTest {

    private final ValidationService validationService = new ValidationService();

    @Test
    void validate_success() {
        var entry = List.of(new EntryFileDto(UUID.randomUUID(), "1X1", "John Smith",
                "Likes Mango", "Drives Car", 5.5, 10.0));
        assertDoesNotThrow(() -> validationService.validate(entry));
    }

    @ParameterizedTest
    @CsvSource(value = {"","","","","","",""})
    void validate_failure(String uuid, String id, String name, String transport, String likes,
                          Double avgSpeed, Double topSpeed) {
        var entry = List.of(new EntryFileDto(UUID.fromString(uuid), id, name, likes, transport, avgSpeed, topSpeed));
        assertThrows(Exception.class, () -> validationService.validate(entry));
    }
}
