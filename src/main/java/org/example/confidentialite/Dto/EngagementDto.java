package org.example.confidentialite.Dto;



import java.time.LocalDateTime;

public record EngagementDto(
         Long id,
         String name,
         String type,
         String statut,
         String url,
         byte[] Document,
         String IdPersonnel,
         LocalDateTime date,
         LocalDateTime updatedAt
) {
}
