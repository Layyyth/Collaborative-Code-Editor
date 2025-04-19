package com.example.collaborativecodeeditor.model;

import jakarta.persistence.*;

@Entity
@Table(name = "code_version")
public class CodeVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String user;

    @Lob
    private String code;

    private long timestamp;

    public CodeVersion() {}

    public CodeVersion(String user, String code, long timestamp) {
        this.user = user;
        this.code = code;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
