package com.gift2go.mapper;

import com.gift2go.dto.EntryFileDto;
import com.gift2go.dto.OutcomeFileDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntryFileMapper {

    OutcomeFileDto toOutcomeFile(EntryFileDto entryFileDto);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "transport", source = "transport")
    @Mapping(target = "topSpeed", source = "topSpeed")
    List<OutcomeFileDto> toOutcomeFile(List<EntryFileDto> entryFileDto);
}
