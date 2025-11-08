package com.gift2go.mapper;

import com.gift2go.dto.EntryFileDto;
import com.gift2go.dto.OutcomeFileDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EntryFileMapperTest {

    EntryFileMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new EntryFileMapperImpl();
    }

    @AfterEach
    void cleanUp() {
        mapper = null;
    }

    @Test
    void map_single_success() {
        EntryFileDto entry = new EntryFileDto(
                UUID.randomUUID(), "1X1D14", "John Smith", "Likes Mango", "Rides A Bike", 5.5, 12.1
        );

        OutcomeFileDto outcome = mapper.toOutcomeFile(entry);

        assertThat(outcome).isNotNull();
        assertThat(outcome.name()).isEqualTo(entry.name());
        assertThat(outcome.transport()).isEqualTo(entry.transport());
        assertThat(outcome.topSpeed()).isEqualTo(entry.topSpeed());
    }

    @Test
    void map_list_success() {
        List<EntryFileDto> entries = List.of(
                new EntryFileDto(UUID.randomUUID(), "1X1D14", "John Smith", "Likes Mango", "Rides A Bike", 5.5, 12.1),
                new EntryFileDto(UUID.randomUUID(), "2X2D24", "Mike Smith", "Likes Grape", "Drives an SUV", 35.0, 95.5)
        );

        List<OutcomeFileDto> outcomes = mapper.toOutcomeFile(entries);
        assertThat(outcomes).hasSize(2);

        OutcomeFileDto first = outcomes.get(0);
        assertThat(first.name()).isEqualTo(entries.get(0).name());
        assertThat(first.transport()).isEqualTo(entries.get(0).transport());
        assertThat(first.topSpeed()).isEqualTo(entries.get(0).topSpeed());

        OutcomeFileDto second = outcomes.get(1);
        assertThat(second.name()).isEqualTo(entries.get(1).name());
        assertThat(second.transport()).isEqualTo(entries.get(1).transport());
        assertThat(second.topSpeed()).isEqualTo(entries.get(1).topSpeed());
    }
}
