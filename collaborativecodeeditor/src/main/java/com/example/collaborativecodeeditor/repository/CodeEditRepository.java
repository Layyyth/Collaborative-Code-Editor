package com.example.collaborativecodeeditor.repository;

import com.example.collaborativecodeeditor.model.CodeEditMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeEditRepository extends JpaRepository<CodeEditMessage, Long> {
}