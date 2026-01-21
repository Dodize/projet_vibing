package com.example.vibing.models;

import java.util.List;
import java.util.Map;

public class User {
    private String id;
    private String username;
    private String teamId;
    private String teamName;
private int money;
    private long createdAt;
    private List<Map<String, String>> visitedPois;

    public User() {
    }

public User(String username, String teamId, String teamName) {
        this.username = username;
        this.teamId = teamId;
        this.teamName = teamName;
        this.money = 0;
        this.createdAt = System.currentTimeMillis();
        this.visitedPois = null; // Initialize as empty list
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

public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = Math.max(0, money); // Empêcher les valeurs négatives
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public List<Map<String, String>> getVisitedPois() {
        return visitedPois;
    }

    public void setVisitedPois(List<Map<String, String>> visitedPois) {
        this.visitedPois = visitedPois;
    }
}