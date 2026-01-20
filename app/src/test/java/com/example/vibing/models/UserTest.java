package com.example.vibing.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserTest {
    
    private User user;
    
    @Before
    public void setUp() {
        user = new User("testUser", "team1", "Team A");
    }
    
    @Test
    public void testDefaultConstructor() {
        User defaultUser = new User();
        assertNull(defaultUser.getId());
        assertNull(defaultUser.getUsername());
        assertNull(defaultUser.getTeamId());
        assertNull(defaultUser.getTeamName());
        assertEquals(0, defaultUser.getMoney());
        assertEquals(0, defaultUser.getCreatedAt());
        assertNull(defaultUser.getVisitedPois());
    }
    
    @Test
    public void testParameterizedConstructor() {
        assertEquals("testUser", user.getUsername());
        assertEquals("team1", user.getTeamId());
        assertEquals("Team A", user.getTeamName());
        assertEquals(0, user.getMoney());
        assertTrue(user.getCreatedAt() > 0);
        assertNull(user.getVisitedPois());
    }
    
    @Test
    public void testSettersAndGetters() {
        user.setId("user123");
        user.setUsername("updatedUser");
        user.setTeamId("team2");
        user.setTeamName("Team B");
        user.setMoney(500);
        user.setCreatedAt(1234567890L);
        
        assertEquals("user123", user.getId());
        assertEquals("updatedUser", user.getUsername());
        assertEquals("team2", user.getTeamId());
        assertEquals("Team B", user.getTeamName());
        assertEquals(500, user.getMoney());
        assertEquals(1234567890L, user.getCreatedAt());
    }
    
    @Test
    public void testVisitedPoisHandling() {
        Map<String, String> poi1 = new HashMap<>();
        poi1.put("id", "poi1");
        poi1.put("name", "POI 1");
        
        Map<String, String> poi2 = new HashMap<>();
        poi2.put("id", "poi2");
        poi2.put("name", "POI 2");
        
        List<Map<String, String>> visitedPois = Arrays.asList(poi1, poi2);
        user.setVisitedPois(visitedPois);
        
        assertEquals(visitedPois, user.getVisitedPois());
        assertEquals(2, user.getVisitedPois().size());
        assertEquals("poi1", user.getVisitedPois().get(0).get("id"));
        assertEquals("POI 2", user.getVisitedPois().get(1).get("name"));
    }
    
    @Test
    public void testMoneyOperations() {
        assertEquals(0, user.getMoney());
        
        user.setMoney(100);
        assertEquals(100, user.getMoney());
        
        user.setMoney(-50);
        assertEquals(-50, user.getMoney());
    }
    
    @Test
    public void testCreatedAtInitialization() {
        long beforeCreation = System.currentTimeMillis();
        User newUser = new User("user", "team1", "Team A");
        long afterCreation = System.currentTimeMillis();
        
        assertTrue(newUser.getCreatedAt() >= beforeCreation);
        assertTrue(newUser.getCreatedAt() <= afterCreation);
    }
}