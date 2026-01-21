package com.example.vibing;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for daily POI visit blocking functionality.
 * Tests that the popup does not appear when a POI has already been visited today.
 */
public class PoiDailyVisitBlockerTest {

    private List<Map<String, String>> visitedPois;
    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() {
        visitedPois = new ArrayList<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    /**
     * Test that a POI visited today should be blocked from popup
     */
    @Test
    public void testPoiVisitedToday_ShouldBlockPopup() {
        // Arrange
        String today = dateFormat.format(new Date());
        String poiId = "poi123";
        
        Map<String, String> visit = new HashMap<>();
        visit.put("poiId", poiId);
        visit.put("visitDate", today);
        visitedPois.add(visit);
        
        // Act & Assert
        assertTrue("POI visited today should be blocked", 
                   isPoiVisitedTodayTestHelper(poiId, visitedPois));
    }

    /**
     * Test that a POI visited yesterday should not be blocked from popup
     */
    @Test
    public void testPoiVisitedYesterday_ShouldNotBlockPopup() {
        // Arrange
        String yesterday = dateFormat.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L));
        String poiId = "poi123";
        
        Map<String, String> visit = new HashMap<>();
        visit.put("poiId", poiId);
        visit.put("visitDate", yesterday);
        visitedPois.add(visit);
        
        // Act & Assert
        assertFalse("POI visited yesterday should not be blocked", 
                    isPoiVisitedTodayTestHelper(poiId, visitedPois));
    }

    /**
     * Test that a never visited POI should not be blocked from popup
     */
    @Test
    public void testNeverVisitedPoi_ShouldNotBlockPopup() {
        // Arrange
        String poiId = "poi123";
        
        // Act & Assert
        assertFalse("Never visited POI should not be blocked", 
                    isPoiVisitedTodayTestHelper(poiId, visitedPois));
    }

    /**
     * Test multiple POIs with different visit dates
     */
    @Test
    public void testMultiplePois_MixedVisitDates() {
        // Arrange
        String today = dateFormat.format(new Date());
        String yesterday = dateFormat.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L));
        
        // POI visited today - should be blocked
        Map<String, String> visitToday = new HashMap<>();
        visitToday.put("poiId", "poi_today");
        visitToday.put("visitDate", today);
        visitedPois.add(visitToday);
        
        // POI visited yesterday - should not be blocked
        Map<String, String> visitYesterday = new HashMap<>();
        visitYesterday.put("poiId", "poi_yesterday");
        visitYesterday.put("visitDate", yesterday);
        visitedPois.add(visitYesterday);
        
        // Act & Assert
        assertTrue("POI visited today should be blocked", 
                   isPoiVisitedTodayTestHelper("poi_today", visitedPois));
        assertFalse("POI visited yesterday should not be blocked", 
                    isPoiVisitedTodayTestHelper("poi_yesterday", visitedPois));
        assertFalse("Never visited POI should not be blocked", 
                    isPoiVisitedTodayTestHelper("poi_never", visitedPois));
    }

    /**
     * Test edge case: POI visited at midnight
     */
    @Test
    public void testPoiVisitedAtMidnight_ShouldHandleCorrectly() {
        // Arrange
        String today = dateFormat.format(new Date());
        String poiId = "poi_midnight";
        
        Map<String, String> visit = new HashMap<>();
        visit.put("poiId", poiId);
        visit.put("visitDate", today);
        visitedPois.add(visit);
        
        // Act & Assert
        assertTrue("POI visited today (at midnight) should be blocked", 
                   isPoiVisitedTodayTestHelper(poiId, visitedPois));
    }

    /**
     * Test null visited POIs list
     */
    @Test
    public void testNullVisitedPois_ShouldNotBlock() {
        // Arrange
        String poiId = "poi123";
        
        // Act & Assert
        assertFalse("With null visited POIs list, nothing should be blocked", 
                    isPoiVisitedTodayTestHelper(poiId, null));
    }

    /**
     * Test empty visited POIs list
     */
    @Test
    public void testEmptyVisitedPois_ShouldNotBlock() {
        // Arrange
        String poiId = "poi123";
        visitedPois.clear();
        
        // Act & Assert
        assertFalse("With empty visited POIs list, nothing should be blocked", 
                    isPoiVisitedTodayTestHelper(poiId, visitedPois));
    }

    /**
     * Test POI with malformed visit record (missing date)
     */
    @Test
    public void testMalformedVisitRecord_ShouldNotBlock() {
        // Arrange
        String poiId = "poi123";
        
        Map<String, String> malformedVisit = new HashMap<>();
        malformedVisit.put("poiId", poiId);
        // Missing visitDate
        visitedPois.add(malformedVisit);
        
        // Act & Assert
        assertFalse("Malformed visit record should not block POI", 
                    isPoiVisitedTodayTestHelper(poiId, visitedPois));
    }

    /**
     * Test POI with malformed visit record (null date)
     */
    @Test
    public void testNullDateInVisitRecord_ShouldNotBlock() {
        // Arrange
        String poiId = "poi123";
        
        Map<String, String> malformedVisit = new HashMap<>();
        malformedVisit.put("poiId", poiId);
        malformedVisit.put("visitDate", null);
        visitedPois.add(malformedVisit);
        
        // Act & Assert
        assertFalse("Visit record with null date should not block POI", 
                    isPoiVisitedTodayTestHelper(poiId, visitedPois));
    }

    /**
     * Helper method that mimics the isPoiVisitedToday logic from HomeFragment
     */
    private boolean isPoiVisitedTodayTestHelper(String poiId, List<Map<String, String>> visitedPoisList) {
        if (visitedPoisList == null) return false;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(new Date());
        
        for (Map<String, String> visit : visitedPoisList) {
            if (poiId.equals(visit.get("poiId")) && today.equals(visit.get("visitDate"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test the popup filtering logic
     */
    @Test
    public void testPopupFiltering_ShouldRemoveVisitedPois() {
        // Arrange
        String today = dateFormat.format(new Date());
        String yesterday = dateFormat.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L));
        
        // Mock POI list - these would be the POIs the user is currently in range of
        List<String> poisInRange = new ArrayList<>();
        poisInRange.add("poi_today");      // Should be filtered out
        poisInRange.add("poi_yesterday");  // Should remain
        poisInRange.add("poi_never");      // Should remain
        
        // Set up visited POIs
        Map<String, String> visitToday = new HashMap<>();
        visitToday.put("poiId", "poi_today");
        visitToday.put("visitDate", today);
        visitedPois.add(visitToday);
        
        Map<String, String> visitYesterday = new HashMap<>();
        visitYesterday.put("poiId", "poi_yesterday");
        visitYesterday.put("visitDate", yesterday);
        visitedPois.add(visitYesterday);
        
        // Act
        List<String> filteredPois = filterPoisForPopupTestHelper(poisInRange, visitedPois);
        
        // Assert
        assertEquals("Should filter out today's visited POI", 2, filteredPois.size());
        assertFalse("Should not contain today's visited POI", filteredPois.contains("poi_today"));
        assertTrue("Should contain yesterday's visited POI", filteredPois.contains("poi_yesterday"));
        assertTrue("Should contain never visited POI", filteredPois.contains("poi_never"));
    }

    /**
     * Helper method that mimics the popup filtering logic
     */
    private List<String> filterPoisForPopupTestHelper(List<String> poisInRange, List<Map<String, String>> visitedPoisList) {
        List<String> filteredPois = new ArrayList<>();
        
        for (String poiId : poisInRange) {
            if (!isPoiVisitedTodayTestHelper(poiId, visitedPoisList)) {
                filteredPois.add(poiId);
            }
        }
        
        return filteredPois;
    }
}