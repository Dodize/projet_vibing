package com.example.vibing.models;

public class Team {
    private String id;
    private String name;
    private String color;
    private String teamId;
    private String colorHex;
    private int memberCount;

public Team() {
        this.memberCount = 0;
    }

    public Team(String name, String color) {
        this.name = name;
        this.color = color;
        this.memberCount = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getColorHex() {
        return colorHex;
    }

public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public boolean canJoin(int totalTeams, int totalPlayers) {
        if (totalTeams <= 0) return false;
        
        double expectedPercentage = 1.0 / totalTeams;
        double tolerance = 0.05; // 5%
        double futurePercentage = (double) (memberCount + 1) / totalPlayers;
        
        return Math.abs(futurePercentage - expectedPercentage) <= tolerance;
    }

    public int getRemainingSlots(int totalTeams, int totalPlayers) {
        if (totalTeams <= 0 || totalPlayers <= 0) return Integer.MAX_VALUE;
        
        double expectedPercentage = 1.0 / totalTeams;
        double tolerance = 0.05;
        int maxAllowed = (int) Math.ceil(totalPlayers * (expectedPercentage + tolerance));
        
        return Math.max(0, maxAllowed - memberCount);
    }
}