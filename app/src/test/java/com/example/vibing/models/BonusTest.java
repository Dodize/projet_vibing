package com.example.vibing.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BonusTest {
    
    private Bonus freezeBonus;
    private Bonus boostBonus;
    
    @Before
    public void setUp() {
        freezeBonus = new Bonus(BonusType.FREEZE_SCORE);
        boostBonus = new Bonus(BonusType.BOOST_SCORE);
    }
    
    @Test
    public void testDefaultConstructor() {
        assertEquals(BonusType.FREEZE_SCORE, freezeBonus.getType());
        assertFalse(freezeBonus.isActive());
    }
    
    @Test
    public void testActivateFreezeBonus() {
        freezeBonus.activate();
        
        assertTrue(freezeBonus.isActive());
    }
    
    @Test
    public void testActivateBoostBonus() {
        boostBonus.activate();
        
        assertTrue(boostBonus.isActive());
    }
    
    @Test
    public void testDeactivate() {
        freezeBonus.activate();
        freezeBonus.deactivate();
        
        assertFalse(freezeBonus.isActive());
    }
    
    @Test
    public void testIsExpiredWithActiveBonus() throws InterruptedException {
        freezeBonus.activate();
        
        // Le bonus ne devrait pas être expiré immédiatement
        assertFalse(freezeBonus.isExpired());
        
        // Test avec un bonus instantané (duration = 0)
        assertFalse(boostBonus.isExpired());
    }
    
    @Test
    public void testIsExpiredWithInactiveBonus() {
        assertFalse(freezeBonus.isExpired());
    }
    
    @Test
    public void testGetRemainingTimeWithActiveBonus() {
        freezeBonus.activate();
        
        long remainingTime = freezeBonus.getRemainingTime();
        assertTrue(remainingTime > 0);
        assertTrue(remainingTime <= 7200000);
    }
    
    @Test
    public void testGetRemainingTimeWithInactiveBonus() {
        assertEquals(0, freezeBonus.getRemainingTime());
    }
    
    @Test
    public void testGetRemainingTimeWithInstantBonus() {
        boostBonus.activate();
        assertEquals(0, boostBonus.getRemainingTime());
    }
    
    @Test
    public void testGetRemainingTimeNeverNegative() {
        freezeBonus.activate();
        
        // Même si le temps est écoulé, le résultat ne doit pas être négatif
        long remainingTime = freezeBonus.getRemainingTime();
        assertTrue(remainingTime >= 0);
    }
}