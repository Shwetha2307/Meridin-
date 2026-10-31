package com.example.javprod.web;

import com.example.javprod.domain.AppUser;
import com.example.javprod.domain.Permission;
import com.example.javprod.domain.Role;
import com.example.javprod.dto.AuthUserDTO;
import com.example.javprod.dto.LoginRequest;
import com.example.javprod.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final SecurityContextRepository securityContextRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthUserDTO> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletRequest httpRequest,
        HttpServletResponse httpResponse
    ) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username().trim(), request.password())
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, httpRequest, httpResponse);

        AppUser user = appUserRepository.findByUsernameIgnoreCase(authentication.getName())
            .orElseThrow(() -> new BadCredentialsException("User profile not found"));

        return ResponseEntity.ok(AuthUserDTO.fromEntity(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<AuthUserDTO> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
            || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return appUserRepository.findByUsernameIgnoreCase(authentication.getName())
            .map(user -> ResponseEntity.ok(AuthUserDTO.fromEntity(user)))
            .orElseGet(() -> {
                // Support @WithMockUser in tests
                Set<String> authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

                Role resolvedRole = Role.EMPLOYEE;
                if (authorities.contains("ROLE_ADMIN")) resolvedRole = Role.ADMIN;
                else if (authorities.contains("ROLE_HR_MANAGER")) resolvedRole = Role.HR_MANAGER;
                else if (authorities.contains("ROLE_MANAGER")) resolvedRole = Role.MANAGER;

                Set<String> perms = resolvedRole.getPermissions().stream()
                    .map(Permission::name)
                    .collect(Collectors.toSet());

                return ResponseEntity.ok(new AuthUserDTO(
                    0L,
                    authentication.getName(),
                    authentication.getName(),
                    resolvedRole,
                    resolvedRole.getLabel(),
                    null,
                    authentication.getName() + "@enterprise.io",
                    resolvedRole.getLabel(),
                    "Corporate",
                    perms
                ));
            });
    }

    @GetMapping("/demo-accounts")
    public ResponseEntity<List<Map<String, String>>> getDemoAccounts() {
        return ResponseEntity.ok(List.of(
            Map.of(
                "username", "admin",
                "password", "admin123",
                "displayName", "Marcus Vance",
                "role", "ADMIN",
                "roleLabel", "Administrator",
                "jobTitle", "VP of Software Engineering",
                "summary", "Full system control: manage workforce, salaries, departments, leave approvals, and asset registry."
            ),
            Map.of(
                "username", "hr",
                "password", "hr123",
                "displayName", "Elena Rostova",
                "role", "HR_MANAGER",
                "roleLabel", "HR Manager",
                "jobTitle", "Director of People & Culture",
                "summary", "People operations: onboard/edit employees, view compensation, approve leaves, and assign hardware."
            ),
            Map.of(
                "username", "manager",
                "password", "manager123",
                "displayName", "Alexander Wright",
                "role", "MANAGER",
                "roleLabel", "Department Manager",
                "jobTitle", "Principal Cloud Architect",
                "summary", "Team leadership: view directory (salaries redacted), approve/reject direct report leave requests."
            ),
            Map.of(
                "username", "employee",
                "password", "emp123",
                "displayName", "Liam O'Connor",
                "role", "EMPLOYEE",
                "roleLabel", "Employee",
                "jobTitle", "DevOps & SRE Engineer",
                "summary", "Self-service portal: browse colleague directory, submit personal leave requests, view assigned hardware."
            )
        ));
    }
}
