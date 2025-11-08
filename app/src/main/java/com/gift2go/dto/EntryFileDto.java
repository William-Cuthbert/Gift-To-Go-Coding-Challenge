package com.gift2go.dto;

import java.util.UUID;

public record EntryFileDto(UUID uuid, String id, String name, String likes,
                           String transport, Double avgSpeed, Double topSpeed) {
}
