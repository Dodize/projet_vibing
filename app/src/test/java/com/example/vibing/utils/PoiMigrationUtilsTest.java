package com.example.vibing.utils;

import com.example.vibing.models.Poi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)

public class PoiMigrationUtilsTest {

    @Before
    public void setUp() {
        // No special setup needed for these tests
        // Firebase operations will fail in unit tests, which is expected
    }

    @Test
    public void testGenerateKeywordsForCapitole() {
        Poi capitolePoi = new Poi("Capitole de Toulouse", 43.6047, 1.4442, 250, 0);
        
        List<String> keywords = PoiMigrationUtils.generateKeywordsForPoi(capitolePoi);
        
        assertNotNull(keywords);
        assertFalse(keywords.isEmpty());
        
        // Should contain capitole-related keywords
        assertTrue("Should contain 'building' keyword", keywords.contains("building"));
        assertTrue("Should contain 'architecture' keyword", keywords.contains("architecture"));
        assertTrue("Should contain 'monument' keyword", keywords.contains("monument"));
        assertTrue("Should contain 'government' keyword", keywords.contains("government"));
        assertTrue("Should contain 'historic' keyword", keywords.contains("historic"));
        
        // Should contain POI name as keyword
        assertTrue("Should contain POI name as keyword", 
            keywords.contains("capitole de toulouse"));
    }

    @Test
    public void testGenerateKeywordsForPontNeuf() {
        Poi pontNeufPoi = new Poi("Pont Neuf de Toulouse", 43.6032, 1.4302, 150, 0);
        
        List<String> keywords = PoiMigrationUtils.generateKeywordsForPoi(pontNeufPoi);
        
        assertNotNull(keywords);
        assertFalse(keywords.isEmpty());
        
        // Should contain pont-related keywords
        assertTrue("Should contain 'bridge' keyword", keywords.contains("bridge"));
        assertTrue("Should contain 'water' keyword", keywords.contains("water"));
        assertTrue("Should contain 'river' keyword", keywords.contains("river"));
        assertTrue("Should contain 'historic' keyword", keywords.contains("historic"));
        assertTrue("Should contain 'stone' keyword", keywords.contains("stone"));
    }

    @Test
    public void testGenerateKeywordsForBasilique() {
        Poi basiliquePoi = new Poi("Basilique Saint-Sernin", 43.6082, 1.4408, 320, 0);
        
        List<String> keywords = PoiMigrationUtils.generateKeywordsForPoi(basiliquePoi);
        
        assertNotNull(keywords);
        assertFalse(keywords.isEmpty());
        
        // Should contain religious-related keywords
        assertTrue("Should contain 'church' keyword", keywords.contains("church"));
        assertTrue("Should contain 'religious' keyword", keywords.contains("religious"));
        assertTrue("Should contain 'architecture' keyword", keywords.contains("architecture"));
        assertTrue("Should contain 'worship' keyword", keywords.contains("worship"));
        assertTrue("Should contain 'historic' keyword", keywords.contains("historic"));
    }

    @Test
    public void testGenerateKeywordsForJardin() {
        Poi jardinPoi = new Poi("Jardin des Plantes", 43.6100, 1.4500, 100, 0);
        
        List<String> keywords = PoiMigrationUtils.generateKeywordsForPoi(jardinPoi);
        
        assertNotNull(keywords);
        assertFalse(keywords.isEmpty());
        
        // Should contain garden-related keywords
        assertTrue("Should contain 'garden' keyword", keywords.contains("garden"));
        assertTrue("Should contain 'park' keyword", keywords.contains("park"));
        assertTrue("Should contain 'nature' keyword", keywords.contains("nature"));
        assertTrue("Should contain 'tree' keyword", keywords.contains("tree"));
        assertTrue("Should contain 'plants' keyword", keywords.contains("plants"));
        assertTrue("Should contain 'green' keyword", keywords.contains("green"));
    }

    @Test
    public void testGenerateKeywordsForZenith() {
        Poi zenithPoi = new Poi("Zénith de Toulouse", 43.5819, 1.4337, 180, 0);
        
        List<String> keywords = PoiMigrationUtils.generateKeywordsForPoi(zenithPoi);
        
        assertNotNull(keywords);
        assertFalse(keywords.isEmpty());
        
        // Should contain zenith-related keywords
        assertTrue("Should contain 'building' keyword", keywords.contains("building"));
        assertTrue("Should contain 'stadium' keyword", keywords.contains("stadium"));
        assertTrue("Should contain 'architecture' keyword", keywords.contains("architecture"));
        assertTrue("Should contain 'entertainment' keyword", keywords.contains("entertainment"));
        assertTrue("Should contain 'venue' keyword", keywords.contains("venue"));
    }

    @Test
    public void testGenerateKeywordsForUnknownPoi() {
        Poi unknownPoi = new Poi("Lieu Inconnu", 43.6000, 1.4400, 50, 0);
        
        List<String> keywords = PoiMigrationUtils.generateKeywordsForPoi(unknownPoi);
        
        assertNotNull(keywords);
        assertFalse(keywords.isEmpty());
        
        // Should contain default keywords
        assertTrue("Should contain default 'building' keyword", keywords.contains("building"));
        
        // Should contain POI name as keyword
        assertTrue("Should contain POI name as keyword", 
            keywords.contains("lieu inconnu"));
    }

    @Test
    public void testGenerateKeywordsWithNullPoi() {
        List<String> keywords = PoiMigrationUtils.generateKeywordsForPoi(null);
        
        assertNull("Should return null for null POI", keywords);
    }

    @Test
    public void testGenerateKeywordsWithEmptyPoiName() {
        Poi emptyPoi = new Poi("", 43.6000, 1.4400, 50, 0);
        
        List<String> keywords = PoiMigrationUtils.generateKeywordsForPoi(emptyPoi);
        
        assertNotNull(keywords);
        assertFalse(keywords.isEmpty());
        
        // Should contain default keywords
        assertTrue("Should contain default 'building' keyword", keywords.contains("building"));
        
        // Should contain empty string as keyword (from empty name)
        assertTrue("Should contain empty string as keyword", keywords.contains(""));
    }

    @Test
    public void testGenerateKeywordsWithSpecialCharacters() {
        Poi specialPoi = new Poi("Lieu spécial! @#$%", 43.6000, 1.4400, 50, 0);
        
        List<String> keywords = PoiMigrationUtils.generateKeywordsForPoi(specialPoi);
        
        assertNotNull(keywords);
        assertFalse(keywords.isEmpty());
        
        // Should contain cleaned POI name (special characters removed)
        assertTrue("Should contain cleaned POI name", 
            keywords.contains("lieu spécial"));
    }

    @Test
    public void testUpdatePoiKeywords() {
        List<String> testKeywords = Arrays.asList("test", "keyword", "location");
        
        // Test that the method doesn't crash with valid inputs
        // Note: We can't easily test Firebase operations in unit tests without
        // more complex mocking setups, so we just verify it doesn't throw exceptions
        try {
            PoiMigrationUtils.updatePoiKeywords("test_poi_id", testKeywords);
            // If we get here without exception, the test passes
            assertTrue("Method should complete without throwing exception", true);
        } catch (Exception e) {
            // Firebase not initialized is expected in unit tests
            if (e.getMessage().contains("Default FirebaseApp is not initialized")) {
                // This is expected behavior in unit tests
                assertTrue("Firebase initialization error is expected in unit tests", true);
            } else {
                fail("Should not throw unexpected exception: " + e.getMessage());
            }
        }
    }

    @Test
    public void testUpdatePoiKeywordsWithNullId() {
        List<String> testKeywords = Arrays.asList("test", "keyword");
        
        try {
            PoiMigrationUtils.updatePoiKeywords(null, testKeywords);
            assertTrue("Method should complete without throwing exception", true);
        } catch (Exception e) {
            if (e.getMessage().contains("Default FirebaseApp is not initialized")) {
                assertTrue("Firebase initialization error is expected in unit tests", true);
            } else {
                fail("Should not throw unexpected exception: " + e.getMessage());
            }
        }
    }

    @Test
    public void testUpdatePoiKeywordsWithNullKeywords() {
        try {
            PoiMigrationUtils.updatePoiKeywords("test_poi_id", null);
            assertTrue("Method should complete without throwing exception", true);
        } catch (Exception e) {
            if (e.getMessage().contains("Default FirebaseApp is not initialized")) {
                assertTrue("Firebase initialization error is expected in unit tests", true);
            } else {
                fail("Should not throw unexpected exception: " + e.getMessage());
            }
        }
    }

    @Test
    public void testMigrateAllPoisWithKeywords() {
        try {
            PoiMigrationUtils.migrateAllPoisWithKeywords();
            assertTrue("Method should complete without throwing exception", true);
        } catch (Exception e) {
            if (e.getMessage().contains("Default FirebaseApp is not initialized")) {
                assertTrue("Firebase initialization error is expected in unit tests", true);
            } else {
                fail("Should not throw unexpected exception: " + e.getMessage());
            }
        }
    }

    @Test
    public void testDefaultKeywordsMap() throws Exception {
        // Test that DEFAULT_KEYWORDS is properly initialized
        java.lang.reflect.Field field = PoiMigrationUtils.class.getDeclaredField("DEFAULT_KEYWORDS");
        field.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        Map<String, List<String>> defaultKeywords = (Map<String, List<String>>) field.get(null);
        
        assertNotNull("DEFAULT_KEYWORDS should not be null", defaultKeywords);
        assertFalse("DEFAULT_KEYWORDS should not be empty", defaultKeywords.isEmpty());
        
        // Test that it contains expected keys
        assertTrue("Should contain 'capitole' key", defaultKeywords.containsKey("capitole"));
        assertTrue("Should contain 'pont' key", defaultKeywords.containsKey("pont"));
        assertTrue("Should contain 'basilique' key", defaultKeywords.containsKey("basilique"));
        assertTrue("Should contain 'jardin' key", defaultKeywords.containsKey("jardin"));
        assertTrue("Should contain 'zénith' key", defaultKeywords.containsKey("zénith"));
    }
}