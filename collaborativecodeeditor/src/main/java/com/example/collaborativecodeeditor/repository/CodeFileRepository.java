package com.example.collaborativecodeeditor.repository;

import com.example.collaborativecodeeditor.model.CodeFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeFileRepository extends JpaRepository<CodeFile, Long> {
    CodeFile findByFilename(String filename);
}
