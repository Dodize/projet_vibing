package com.example.vibing.models;

public class User {
    private String id;
    private String username;
    private String teamId;
    private String teamName;
    private String teamColor;
    private long createdAt;

    public User() {
    }

    public User(String username, String teamId, String teamName, String teamColor) {
        this.username = username;
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamColor = teamColor;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}