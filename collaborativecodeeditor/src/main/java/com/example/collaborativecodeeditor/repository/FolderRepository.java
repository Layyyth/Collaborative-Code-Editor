package com.example.collaborativecodeeditor.repository;

import com.example.collaborativecodeeditor.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {}

