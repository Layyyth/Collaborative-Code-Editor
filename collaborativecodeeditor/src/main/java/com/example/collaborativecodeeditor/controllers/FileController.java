package com.example.collaborativecodeeditor.controllers;

import com.example.collaborativecodeeditor.model.CodeFile;
import com.example.collaborativecodeeditor.repository.CodeFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private CodeFileRepository codeFileRepository;


    @GetMapping
    public List<CodeFile> getAllFiles() {
        return codeFileRepository.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<CodeFile> getFileById(@PathVariable Long id) {
        return codeFileRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public CodeFile createFile(@RequestBody CodeFile codeFile) {
        codeFile.setVersion(1);
        codeFile.setLastModified(LocalDateTime.now());
        return codeFileRepository.save(codeFile);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CodeFile> updateFile(@PathVariable Long id, @RequestBody CodeFile fileDetails) {
        return codeFileRepository.findById(id).map(file -> {
            file.setFilename(fileDetails.getFilename());
            file.setContent(fileDetails.getContent());
            file.setVersion(file.getVersion() + 1);
            file.setLastModified(LocalDateTime.now());
            return ResponseEntity.ok(codeFileRepository.save(file));
        }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        return codeFileRepository.findById(id).map(file -> {
            codeFileRepository.delete(file);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
