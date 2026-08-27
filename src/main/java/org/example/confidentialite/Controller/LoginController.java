package org.example.confidentialite.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.confidentialite.Dto.LoginDTO;
import org.example.confidentialite.Dto.LoginResDto;
import org.example.confidentialite.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {


    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final SecurityContextRepository securityContextRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<?> currentUser(Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(authentication.getPrincipal());
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(
            @RequestBody LoginDTO loginDto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {


        String idPersonnel = loginDto.idPersonnel();
        String Dep = loginDto.dep();

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                idPersonnel,
                                ""
                        )
                );


        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);


        securityContextRepository.saveContext(
                context,
                request,
                response
        );


        return ResponseEntity.ok(
                authService.login(idPersonnel, Dep)
        );
    }


}