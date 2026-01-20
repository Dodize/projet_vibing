package com.example.vibing.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TeamTest {
    
    private Team team;
    
    @Before
    public void setUp() {
        team = new Team("Test Team", "#FF0000");
    }
    
    @Test
    public void testDefaultConstructor() {
        Team defaultTeam = new Team();
        assertNull(defaultTeam.getId());
        assertNull(defaultTeam.getName());
        assertNull(defaultTeam.getColor());
        assertNull(defaultTeam.getTeamId());
        assertNull(defaultTeam.getColorHex());
        assertEquals(0, defaultTeam.getMemberCount());
    }
    
    @Test
    public void testParameterizedConstructor() {
        assertEquals("Test Team", team.getName());
        assertEquals("#FF0000", team.getColor());
        assertEquals(0, team.getMemberCount());
    }
    
    @Test
    public void testSettersAndGetters() {
        team.setId("team123");
        team.setName("Updated Team");
        team.setColor("#00FF00");
        team.setTeamId("team_1");
        team.setColorHex("#00FF00");
        team.setMemberCount(5);
        
        assertEquals("team123", team.getId());
        assertEquals("Updated Team", team.getName());
        assertEquals("#00FF00", team.getColor());
        assertEquals("team_1", team.getTeamId());
        assertEquals("#00FF00", team.getColorHex());
        assertEquals(5, team.getMemberCount());
    }
    
    @Test
    public void testCanJoinWithValidParameters() {
        team.setMemberCount(4);
        
        // 4 équipes, 20 joueurs totaux = 5 joueurs par équipe attendus
        // 4 membres actuels + 1 = 5, ce qui est exactement le ratio attendu (5/20 = 0.25)
        assertTrue(team.canJoin(4, 20));
    }
    
    @Test
    public void testCanJoinWithInvalidParameters() {
        team.setMemberCount(6);
        
        // 4 équipes, 20 joueurs totaux = 5 joueurs par équipe attendus
        // 6 membres actuels + 1 = 7, ce qui dépasse la tolérance de 5%
        assertFalse(team.canJoin(4, 20));
    }
    
    @Test
    public void testCanJoinWithZeroTeams() {
        assertFalse(team.canJoin(0, 20));
        assertFalse(team.canJoin(-1, 20));
    }
    
    @Test
    public void testGetRemainingSlots() {
        team.setMemberCount(3);
        
        // 4 équipes, 20 joueurs totaux = 5 joueurs par équipe attendus
        // Tolérance de 5% = 6 joueurs max autorisés (Math.ceil(20 * 0.30))
        int remainingSlots = team.getRemainingSlots(4, 20);
        assertEquals(3, remainingSlots); // 6 - 3 = 3
    }
    
    @Test
    public void testGetRemainingSlotsWithFullTeam() {
        team.setMemberCount(6);
        
        // 4 équipes, 20 joueurs totaux, tolérance 5% = 6 max autorisés
        int remainingSlots = team.getRemainingSlots(4, 20);
        assertEquals(0, remainingSlots); // Équipe pleine
    }
    
    @Test
    public void testGetRemainingSlotsWithOverfullTeam() {
        team.setMemberCount(7);
        
        // 4 équipes, 20 joueurs totaux
        int remainingSlots = team.getRemainingSlots(4, 20);
        assertEquals(0, remainingSlots); // Pas de places disponibles
    }
    
    @Test
    public void testGetRemainingSlotsWithInvalidParameters() {
        assertEquals(Integer.MAX_VALUE, team.getRemainingSlots(0, 20));
        assertEquals(Integer.MAX_VALUE, team.getRemainingSlots(4, 0));
        assertEquals(Integer.MAX_VALUE, team.getRemainingSlots(-1, 20));
    }
    
    @Test
    public void testMemberCountOperations() {
        assertEquals(0, team.getMemberCount());
        
        team.setMemberCount(10);
        assertEquals(10, team.getMemberCount());
        
        team.setMemberCount(-5);
        assertEquals(-5, team.getMemberCount());
    }
}