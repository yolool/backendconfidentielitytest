package org.example.confidentialite.Controller;

import lombok.RequiredArgsConstructor;
import org.example.confidentialite.Dto.EngagementDto;
import org.example.confidentialite.Entity.Engagement;
import org.example.confidentialite.Service.EmailService;
import org.example.confidentialite.Service.EngagementService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/engagement")
@RequiredArgsConstructor
public class EngagementController {

    private final EngagementService engagementService;
    private final EmailService emailService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadEngagement(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false, defaultValue = "") String idPersonnel,
            @RequestParam String subject) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            EngagementDto uploaded = engagementService.UploadEngagement(file, idPersonnel);

            byte[] fileBytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();

            emailService.sendEngagementEmailAsync(fileBytes, originalFilename, subject);

            return new ResponseEntity<>(uploaded, HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllEngagements() {
        try {
            List<EngagementDto> engagements = engagementService.findEngagements();
            return new ResponseEntity<>(engagements, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getEngagementById(@PathVariable Long id) {
        try {
            EngagementDto engagement = engagementService.findEngagementById(id);
            return new ResponseEntity<>(engagement, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEngagement(@PathVariable Long id) {
        try {
            engagementService.DeleteEngagement(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadEngagement(@PathVariable Long id) {

        Engagement engagement = engagementService.getEngagement(id);

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(engagement.getType());
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + engagement.getName() + "\"")
                .body(engagement.getDocument());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("perso/{idPersonnel}")
    public ResponseEntity<?> getEngagementByPersonalId(@PathVariable String idPersonnel) {
        try {
            EngagementDto  engagement = engagementService.FindEngagementByIdPersonnel(idPersonnel);
            return new ResponseEntity<>(engagement, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}