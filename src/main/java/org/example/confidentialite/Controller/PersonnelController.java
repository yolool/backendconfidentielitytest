package org.example.confidentialite.Controller;

import lombok.RequiredArgsConstructor;
import org.example.confidentialite.Dto.DepartementDto;
import org.example.confidentialite.Dto.PersonnelDto;
import org.example.confidentialite.Service.PersonnelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Personnel")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PersonnelController {
    private final PersonnelService personnelService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<?> findAllPersonnels() {
        try {
            List<PersonnelDto> personnelDtos = personnelService.findAllPersonnels();
            return ResponseEntity.ok(personnelDtos);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/deps")
    public ResponseEntity<?> findAllDepartement() {
        try {
            List<DepartementDto> personnelDtos = personnelService.findAllDepartments();
            return ResponseEntity.ok(personnelDtos);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<?> findPersonnel(
            @PathVariable String id) {
        try {
            PersonnelDto personnelDto = personnelService.findPersonnelById(id);
            return ResponseEntity.ok(personnelDto);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
