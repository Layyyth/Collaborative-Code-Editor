package com.example.collaborativecodeeditor.security;

import com.example.collaborativecodeeditor.model.AppUser;
import com.example.collaborativecodeeditor.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
public class CustomOAuth2SuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Autowired
    public CustomOAuth2SuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauthUser.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");


        AppUser appUser = userRepository.findByEmail(email).orElseGet(() -> {
            String role = email.equals("layth.admin@example.com") ? "ADMIN" : "USER";
            return userRepository.save(new AppUser(name, email, picture, role));
        });

        appUser.setOnline(true);
        appUser.setLastSeen(Instant.now());
        userRepository.save(appUser);


        CustomUserDetails userDetails = new CustomUserDetails(appUser);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());


        response.sendRedirect(UriComponentsBuilder
                .fromUriString("http://localhost:3000/editor")
                .toUriString());
    }
}
