package com.example.collaborativecodeeditor.controllers;

import com.example.collaborativecodeeditor.model.AppUser;
import com.example.collaborativecodeeditor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class PublicController {

    private final UserRepository userRepository;

    @Autowired
    public PublicController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/online")
    public List<AppUser> getOnlineUsers() {
        return userRepository.findAll().stream()
                .filter(AppUser::isOnline)
                .collect(Collectors.toList());
    }
}
