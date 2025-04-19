package com.example.collaborativecodeeditor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class CodeEdit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Lob
    private String code;

    private long timestamp;

    public CodeEdit() {}

    public CodeEdit(String user, String code, long timestamp) {
        this.username = user;
        this.code = code;
        this.timestamp = timestamp;
    }

}
