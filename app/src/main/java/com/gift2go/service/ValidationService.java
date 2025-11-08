package com.gift2go.service;

import com.gift2go.dto.EntryFileDto;
import com.gift2go.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ValidationService {

    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");
    private static final Pattern LIKES_PATTERN = Pattern.compile("^Likes\\s.+$");
    private static final Pattern TRANSPORT_PATTERN = Pattern.compile("^(Drives|Rides)\\s.+$");
    private static final Pattern ID_PATTERN = Pattern.compile("^\\dX\\dD\\d{2}$");

    private final boolean skipValidationFlag;

    @Autowired
    public ValidationService(@Value("${feature.validation.skip:false}") boolean skipValidationFlag) {
        this.skipValidationFlag = skipValidationFlag;
    }

    public void validate(List<EntryFileDto> entries) {
        log.info("Entry file input validation process has started");
        if (skipValidationFlag) {
            log.warn("Skip validation is enabled");
            return;
        }
        entries.parallelStream()
                .filter(Objects::nonNull)
                .forEach(this::validateEntry);
        log.info("Entry file input invalidation process has completed");
    }

    private void validateEntry(EntryFileDto entry) {
        if (Objects.isNull(entry.uuid()) || !UUID_PATTERN.matcher(entry.uuid().toString()).matches()) {
            log.error("Missing or invalid field: UUID");
            throw new ValidationException("UUID invalid or missing");
        }
        if (Objects.isNull(entry.id()) || entry.id().isBlank() || !ID_PATTERN.matcher(entry.id()).matches()) {
            log.error("Missing or invalid field: ID");
            throw new ValidationException("ID invalid or missing");
        }
        if (Objects.isNull(entry.name()) || entry.name().isBlank()) {
            log.error("Missing or invalid field: Name");
            throw new ValidationException("Name invalid or missing");
        }
        if (Objects.isNull(entry.transport()) || entry.transport().isBlank() || !TRANSPORT_PATTERN.matcher(entry.transport()).matches()) {
            log.error("Missing or invalid field: Transport");
            throw new ValidationException("Transport invalid or missing");
        }
        if (Objects.isNull(entry.likes()) || entry.likes().isBlank() || !LIKES_PATTERN.matcher(entry.likes()).matches()) {
            log.error("Missing or invalid field: Likes");
            throw new ValidationException("Likes invalid or missing");
        }
        if (Objects.isNull(entry.avgSpeed()) || entry.avgSpeed() < 0) {
            log.error("Missing or invalid field: Avg Speed");
            throw new ValidationException("Avg Speed invalid or missing");
        }
        if (Objects.isNull(entry.topSpeed()) || entry.topSpeed() < entry.avgSpeed()) {
            log.error("Missing or invalid field: Top Speed");
            throw new ValidationException("Top Speed invalid or missing");
        }
    }
}
