package org.example.confidentialite.Dto;


import java.time.LocalDateTime;

public record PersonnelDto(
        String IdPersonnel,
        String Name,
        String Department,
        LocalDateTime date,
        LocalDateTime updatedAt
) {
}
