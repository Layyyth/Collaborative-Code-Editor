package com.example.collaborativecodeeditor.repository;

import com.example.collaborativecodeeditor.model.CodeVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeVersionRepository extends JpaRepository<CodeVersion, Long> {
    List<CodeVersion> findAllByOrderByTimestampDesc();
}
