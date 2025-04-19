package com.example.collaborativecodeeditor.controllers;

import com.example.collaborativecodeeditor.model.AppUser;
import com.example.collaborativecodeeditor.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }


    @PutMapping("/users/{email}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateUserRole(@PathVariable String email,
                                                 @RequestBody RoleUpdateRequest request) {
        String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
        String role = request.getRole().toUpperCase();

        if (!role.equals("USER") && !role.equals("ADMIN") && !role.equals("VIEWER")) {
            return ResponseEntity.badRequest().body("Invalid role: " + role);
        }

        return userRepository.findByEmail(decodedEmail).map(user -> {
            user.setRole(role);
            userRepository.save(user);
            return ResponseEntity.ok("Role updated to " + role);
        }).orElse(ResponseEntity.notFound().build());
    }


    public static class RoleUpdateRequest {
        private String role;
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
