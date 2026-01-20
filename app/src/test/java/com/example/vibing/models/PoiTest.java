package com.example.vibing.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class PoiTest {
    
    private Poi poi;
    private MockedStatic<Log> mockedLog;
    
    @Before
    public void setUp() {
        mockedLog = Mockito.mockStatic(Log.class);
        poi = new Poi("Test POI", 48.8566, 2.3522, 100, 1);
    }
    
    @Test
    public void testDefaultConstructor() {
        Poi defaultPoi = new Poi();
        assertEquals(100.0, defaultPoi.getRadius(), 0.001);
    }
    
    @Test
    public void testParameterizedConstructor() {
        assertEquals("Test POI", poi.getName());
        assertEquals(48.8566, poi.getLatitude(), 0.0001);
        assertEquals(2.3522, poi.getLongitude(), 0.0001);
        assertEquals(100, poi.getScore());
        assertEquals(1, poi.getOwningTeam());
        assertEquals(100.0, poi.getRadius(), 0.001);
    }
    
    @Test
    public void testConstructorWithRadius() {
        Poi poiWithRadius = new Poi("Test POI", 48.8566, 2.3522, 100, 1, 150.0);
        assertEquals(150.0, poiWithRadius.getRadius(), 0.001);
    }
    
    @Test
    public void testSettersAndGetters() {
        poi.setName("Updated POI");
        poi.setLatitude(45.7640);
        poi.setLongitude(4.8357);
        poi.setScore(200);
        poi.setOwningTeam(2);
        poi.setId("test-id");
        poi.setRadius(120.0);
        
        assertEquals("Updated POI", poi.getName());
        assertEquals(45.7640, poi.getLatitude(), 0.0001);
        assertEquals(4.8357, poi.getLongitude(), 0.0001);
        assertEquals(200, poi.getScore());
        assertEquals(2, poi.getOwningTeam());
        assertEquals("test-id", poi.getId());
        assertEquals(120.0, poi.getRadius(), 0.001);
    }
    
    @Test
    public void testGetScoreWithDifferentValues() {
        poi.setScore(150);
        assertEquals(150, poi.getScore());
        
        poi.setScore(0);
        assertEquals(0, poi.getScore());
        
        poi.setScore(-50);
        assertEquals(-50, poi.getScore());
    }
    
    @Test
    public void testGetOwningTeamWithOwnerTeamId() {
        poi.setOwnerTeamId("team_3");
        assertEquals(3, poi.getOwningTeam());
        
        poi.setOwnerTeamId("4");
        assertEquals(4, poi.getOwningTeam());
        
        poi.setOwnerTeamId("null");
        assertEquals(0, poi.getOwningTeam());
        
        poi.setOwnerTeamId("");
        assertEquals(0, poi.getOwningTeam());
        
        poi.setOwnerTeamId("team_6");
        assertEquals(0, poi.getOwningTeam());
    }
    
    @Test
    public void testDateHandling() {
        Date now = new Date();
        poi.setCaptureTime(now);
        poi.setLastUpdated(now);
        poi.setFreezeUntil(now);
        poi.setFreezeBonusUntil(now);
        
        assertEquals(now, poi.getCaptureTime());
        assertEquals(now, poi.getLastUpdated());
        assertEquals(now, poi.getFreezeUntil());
        assertEquals(now, poi.getFreezeBonusUntil());
    }
    
    @Test
    public void testKeywordsHandling() {
        java.util.List<String> keywords = java.util.Arrays.asList("test", "poi", "location");
        poi.setKeywords(keywords);
        
        assertEquals(keywords, poi.getKeywords());
    }
    
    @org.junit.After
    public void tearDown() {
        if (mockedLog != null) {
            mockedLog.close();
        }
    }
}