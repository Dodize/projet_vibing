package com.example.vibing.models;

public enum BonusType {
    FREEZE_SCORE("Figer le score", 30, "Empêche le score de la zone de décroître pendant 1 heure"),
    BOOST_SCORE("Augmenter le score", 50, "Ajoute 20 points à votre score pour ce QCM");

    private final String displayName;
    private final int cost;
    private final String description;

    BonusType(String displayName, int cost, String description) {
        this.displayName = displayName;
        this.cost = cost;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }
}