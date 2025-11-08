package com.gift2go.service;

import com.gift2go.dto.EntryFileDto;
import com.gift2go.exception.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    private ValidationService validationService;
    private final boolean skipValidation = false;

    @BeforeEach
    void setup() {
        validationService = new ValidationService(skipValidation);
    }

    @AfterEach
    void cleanUp() {
        validationService = null;
    }

    @Test
    void skip_validation_enabled_success() {
        validationService = new ValidationService(true);
        var entries = List.of(new EntryFileDto(null,null,null,null,null,null,null));

        assertDoesNotThrow(() -> validationService.validate(entries));
    }

    @ParameterizedTest
    @CsvSource(value = {
            " ,1X1D14,John Smith,Likes Mango,Drives Car,5.5,10.0",
            "00000000-0000-0000-0000-000000000000,,John Smith,Likes Mango,Drives Car,5.5,10.0",
            "00000000-0000-0000-0000-000000000000,1X1D14,,Likes Mango,Drives Car,5.5,10.0",
            "00000000-0000-0000-0000-000000000000,1X1D14,John Smith,Likes Mango,Drives Car,-1.0,10.0",
            "00000000-0000-0000-0000-000000000000,1X1D14,John Smith,Likes Mango,Drives Car,5.5,-10.0"
    })
    void validate_failure(String uuidStr, String id, String name, String transport, String likes,
                          Double avgSpeed, Double topSpeed) {
        UUID uuid = null;
        if (uuidStr != null && !uuidStr.isBlank()) {
            uuid = UUID.fromString(uuidStr);
        }
        var entry = List.of(new EntryFileDto(uuid, id, name, likes, transport, avgSpeed, topSpeed));

        assertThrows(ValidationException.class, () -> validationService.validate(entry));
    }

    @Test
    void validate_success() {
        var entry = List.of(new EntryFileDto(UUID.randomUUID(), "1X1D14", "John Smith",
                "Likes Mango", "Drives Car", 5.5, 10.0));

        assertDoesNotThrow(() -> validationService.validate(entry));
    }
}
