package com.example.collaborativecodeeditor.controllers;

import com.example.collaborativecodeeditor.model.CodeEditMessage;

import com.example.collaborativecodeeditor.repository.CodeVersionRepository;
import com.example.collaborativecodeeditor.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class CodeEditController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private CodeVersionRepository versionRepository;

    @Autowired
    private UserRepository userRepository; // Inject UserRepo

    @MessageMapping("/edit")
    public void processEdit(CodeEditMessage message) {
        // Broadcast live code to all users
        messagingTemplate.convertAndSend("/topic/code", message);

        // Save user activity (editing, last seen)
        userRepository.findByEmail(message.getUser()).ifPresent(user -> {
            user.setEditing(true);
            user.setLastSeen(Instant.now());
            userRepository.save(user);
        });

    }
}
