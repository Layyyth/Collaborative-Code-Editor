package com.example.collaborativecodeeditor.model;

import jakarta.persistence.*;

@Entity
public class CodeEditMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String user;

    @Lob
    private String code;

    private long timestamp;

    public CodeEditMessage() {}

    public CodeEditMessage(String user, String code, long timestamp) {
        this.user = user;
        this.code = code;
        this.timestamp = timestamp;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
