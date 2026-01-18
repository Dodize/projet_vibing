package com.example.vibing.models;

public class Bonus {
    private BonusType type;
    private boolean isActive;
    private long activationTime;
    private long duration;

    public Bonus(BonusType type) {
        this.type = type;
        this.isActive = false;
        this.activationTime = 0;
        this.duration = 0;
    }

    public BonusType getType() {
        return type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        this.isActive = true;
        this.activationTime = System.currentTimeMillis();
        
        // Définir la durée selon le type de bonus
        switch (type) {
            case FREEZE_SCORE:
                this.duration = 3600000; // 1 heure
                break;
            case BOOST_SCORE:
                this.duration = 0; // Bonus instantané
                break;
        }
    }

    public void deactivate() {
        this.isActive = false;
        this.activationTime = 0;
        this.duration = 0;
    }

    public boolean isExpired() {
        if (!isActive || duration == 0) {
            return false;
        }
        return System.currentTimeMillis() > (activationTime + duration);
    }

    public long getRemainingTime() {
        if (!isActive || duration == 0) {
            return 0;
        }
        long remaining = (activationTime + duration) - System.currentTimeMillis();
        return Math.max(0, remaining);
    }
}