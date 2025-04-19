package com.example.collaborativecodeeditor.repository;

import com.example.collaborativecodeeditor.model.FileEntity;
import com.example.collaborativecodeeditor.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByFolder(Folder folder);
}
