package com.example.collaborativecodeeditor.service;

import com.example.collaborativecodeeditor.model.CodeFile;
import com.example.collaborativecodeeditor.repository.CodeFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private CodeFileRepository codeFileRepository;

    public List<CodeFile> getAllFiles() {
        return codeFileRepository.findAll();
    }

    public Optional<CodeFile> getFileById(Long id) {
        return codeFileRepository.findById(id);
    }

    public CodeFile createFile(CodeFile file) {
        file.setVersion(1);
        file.setLastModified(LocalDateTime.now());
        return codeFileRepository.save(file);
    }

    public Optional<CodeFile> updateFile(Long id, CodeFile fileDetails) {
        return codeFileRepository.findById(id).map(file -> {
            file.setFilename(fileDetails.getFilename());
            file.setContent(fileDetails.getContent());
            file.setVersion(file.getVersion() + 1);
            file.setLastModified(LocalDateTime.now());
            return codeFileRepository.save(file);
        });
    }

    public void deleteFile(Long id) {
        codeFileRepository.deleteById(id);
    }
}
