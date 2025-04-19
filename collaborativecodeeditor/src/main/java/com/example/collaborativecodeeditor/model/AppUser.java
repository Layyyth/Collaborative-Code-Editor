package com.example.collaborativecodeeditor.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String picture;

    private String role = "USER"; // USER | ADMIN | VIEWER

    private boolean online = false;

    private boolean isEditing = false;

    private Instant lastSeen = Instant.now();

    private Instant createdAt = Instant.now();

    public AppUser() {}

    public AppUser(String name, String email, String picture, String role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.online = true;
        this.lastSeen = Instant.now();
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPicture() { return picture; }

    public void setPicture(String picture) { this.picture = picture; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public boolean isOnline() { return online; }

    public void setOnline(boolean online) { this.online = online; }

    public boolean isEditing() { return isEditing; }

    public void setEditing(boolean editing) { isEditing = editing; }

    public Instant getLastSeen() { return lastSeen; }

    public void setLastSeen(Instant lastSeen) { this.lastSeen = lastSeen; }

    public Instant getCreatedAt() { return createdAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
