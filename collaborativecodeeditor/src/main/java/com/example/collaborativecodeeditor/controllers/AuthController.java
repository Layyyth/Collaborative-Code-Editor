package com.example.collaborativecodeeditor.controllers;

import com.example.collaborativecodeeditor.model.AppUser;
import com.example.collaborativecodeeditor.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<AppUser> getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        AppUser user = userDetails.getUser();

        return ResponseEntity.ok(user);
    }
}
