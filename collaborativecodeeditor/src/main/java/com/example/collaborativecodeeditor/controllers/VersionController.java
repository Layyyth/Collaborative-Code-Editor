package com.example.collaborativecodeeditor.controllers;

import com.example.collaborativecodeeditor.model.CodeVersion;
import com.example.collaborativecodeeditor.model.CodeEditMessage;
import com.example.collaborativecodeeditor.repository.CodeVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/versions")
public class VersionController {

    @Autowired
    private CodeVersionRepository versionRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @GetMapping
    public List<CodeVersion> getAllVersions() {
        return versionRepository.findAllByOrderByTimestampDesc();
    }


    @PostMapping("/revert/{id}")
    public ResponseEntity<String> revertToVersion(@PathVariable Long id) {
        Optional<CodeVersion> version = versionRepository.findById(id);

        if (version.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CodeVersion v = version.get();


        CodeEditMessage message = new CodeEditMessage(v.getUser(), v.getCode(), System.currentTimeMillis());
        messagingTemplate.convertAndSend("/topic/code", message);

        return ResponseEntity.ok("Reverted to version " + id + " successfully.");
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveManualVersion(@RequestBody CodeEditMessage request) {
        CodeVersion version = new CodeVersion();
        version.setUser(request.getUser());
        version.setCode(request.getCode());
        version.setTimestamp(System.currentTimeMillis());

        versionRepository.save(version);
        return ResponseEntity.ok("Version saved successfully.");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAllVersions() {
        versionRepository.deleteAll();
        return ResponseEntity.ok("All versions wiped 💣");
    }


}
