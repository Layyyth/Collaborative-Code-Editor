package com.example.collaborativecodeeditor.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CodeFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @Lob
    private String content;

    private int version;

    private LocalDateTime lastModified;

    public CodeFile() {}

    public CodeFile(String filename, String content, int version, LocalDateTime lastModified) {
        this.filename = filename;
        this.content = content;
        this.version = version;
        this.lastModified = lastModified;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public LocalDateTime getLastModified() {
        return lastModified;
    }
    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
}
