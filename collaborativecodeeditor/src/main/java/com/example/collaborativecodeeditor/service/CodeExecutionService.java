package com.example.collaborativecodeeditor.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class CodeExecutionService {
    public String executeCode(String code) {
        try {
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", code);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            output.append("Exited with code ").append(exitCode);
            return output.toString();
        } catch (Exception e) {
            return "Error executing code: " + e.getMessage();
        }
    }
}
