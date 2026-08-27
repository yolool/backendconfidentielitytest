package org.example.confidentialite.Service;

import lombok.AllArgsConstructor;
import org.example.confidentialite.Dto.EngagementDto;
import org.example.confidentialite.Entity.Engagement;
import org.example.confidentialite.Entity.Personnel;
import org.example.confidentialite.Mapping.EngagementMapper;
import org.example.confidentialite.Repository.EngagementRepo;
import org.example.confidentialite.Repository.PersonnelRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EngagementService {

    private final EngagementRepo engagementRepo;
    private final EngagementMapper engagementMapper;
    private final PersonnelRepo personnelRepo;

    public List<EngagementDto> findEngagements() {
           return engagementRepo.findAll().stream().map(engagementMapper)
                   .collect(Collectors.toList()) ;

    }

    public Engagement getEngagement(Long id) {
        return engagementRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("No engagement found with id: " + id));
    }

    public EngagementDto findEngagementById(Long id) {
        Engagement engagement = engagementRepo.findById(id).orElseThrow(()-> new RuntimeException("No engagement found with id: " + id));
        return engagementMapper.apply(engagement);
    }
    public EngagementDto UploadEngagement(MultipartFile file , String idPersonnel) throws IOException {
        Personnel personnel = null ;

        if (idPersonnel != null && !idPersonnel.isBlank()) {
            personnel = personnelRepo.findById(idPersonnel)
                    .orElseThrow(() ->
                            new RuntimeException("No personnel found with id: " + idPersonnel));
        }


        Engagement eng = new Engagement();
        eng.setName(file.getOriginalFilename());
        eng.setType(file.getContentType());
        eng.setDocument(file.getBytes());
        eng.setStatut("signed");
        eng.setPersonnel(personnel);
        eng = engagementRepo.save(eng);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads/documents/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        eng.setUrl("/uploads/documents/" + filename);
        eng = engagementRepo.save(eng);
        return engagementMapper.apply(eng);

    }

    public void DeleteEngagement(Long id) {
        engagementRepo.deleteById(id);
    }

    public EngagementDto FindEngagementByIdPersonnel(String idPersonnel) {
        String statut = engagementRepo.findByIdPersonnel(idPersonnel)
                .orElseThrow(() -> new RuntimeException("No engagement found with id: " + idPersonnel));

        return new EngagementDto(null, null, null, statut, null, null, null, null, null);
    }

}
