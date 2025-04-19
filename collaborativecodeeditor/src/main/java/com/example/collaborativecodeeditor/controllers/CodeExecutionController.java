package com.example.collaborativecodeeditor.controllers;

import com.example.collaborativecodeeditor.model.CodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;

@RestController
@RequestMapping("/run")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CodeExecutionController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<String> runCode(@RequestBody CodeRequest request) {
        try {
            String language = request.getLanguage();
            String code = request.getCode();

            if (language == null || code == null) {
                return ResponseEntity.badRequest().body("Language and code must not be null.");
            }

            if ("cpp".equalsIgnoreCase(language)) {
                String output = runCppInDocker(code);


                messagingTemplate.convertAndSend("/topic/output", output);

                return ResponseEntity.ok(output);
            }

            return ResponseEntity.badRequest().body("Unsupported language: " + language);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Server Error: " + e.getMessage());
        }
    }

    private String runCppInDocker(String code) throws IOException, InterruptedException {
        File tempDir = Files.createTempDirectory("cpp-code").toFile();
        File sourceFile = new File(tempDir, "main.cpp");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {
            writer.write(code);
        }

        ProcessBuilder docker = new ProcessBuilder(
                "docker", "run", "--rm",
                "-v", tempDir.getAbsolutePath() + ":/app",
                "cpp-runner"
        );

        docker.redirectErrorStream(true);
        Process process = docker.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        process.waitFor();

        return output.toString().isBlank() ? "Executed with no output." : output.toString();
    }
}
