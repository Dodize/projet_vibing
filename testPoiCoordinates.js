// Test script to verify POI coordinate handling without Firebase connection
const admin = require('firebase-admin');

// Mock the Toulouse POI data structure from importToulousePois.js
const mockPoisData = {
  "poi_capitole": {
    "name": "Capitole de Toulouse",
    "location": { "latitude": 43.6047, "longitude": 1.4442 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": new Date(),
    "qcm": {
      "question": "Quelle est la fonction principale du Capitole de Toulouse ?",
      "options": [
        "Musée d'art",
        "Hôtel de ville et théâtre",
        "Cathédrale",
        "Université"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Institutions"
    }
  },
  "poi_basilique_saint_sernin": {
    "name": "Basilique Saint-Sernin",
    "location": { "latitude": 43.6082, "longitude": 1.4408 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": new Date(),
    "qcm": {
      "question": "Quel style architectural principal caractérise la Basilique Saint-Sernin ?",
      "options": [
        "Gothique",
        "Roman",
        "Baroque",
        "Renaissance"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  }
};

// Simulate the Android Poi class coordinate extraction
function extractCoordinates(poiData) {
  let latitude, longitude;
  
  // This simulates the fixed Poi.java getters
  if (poiData.location && typeof poiData.location.latitude === 'number') {
    latitude = poiData.location.latitude;
    longitude = poiData.location.longitude;
    console.log(`Using nested location structure for ${poiData.name}`);
  } else if (typeof poiData.latitude === 'number') {
    latitude = poiData.latitude;
    longitude = poiData.longitude;
    console.log(`Using flat coordinate structure for ${poiData.name}`);
  } else {
    console.log(`ERROR: No valid coordinates found for ${poiData.name}`);
    return null;
  }
  
  return { latitude, longitude };
}

// Haversine distance calculation (same as Android)
function calculateDistance(lat1, lon1, lat2, lon2) {
  const dLat = (lat2 - lat1) * Math.PI / 180;
  const dLon = (lon2 - lon1) * Math.PI / 180;
  const a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
             Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * 
             Math.sin(dLon/2) * Math.sin(dLon/2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
  const distanceInMeters = 6371000 * c; // Earth radius in meters
  return distanceInMeters / 1000.0; // Convert to km
}

console.log("=== TESTING POI COORDINATE EXTRACTION AND DISTANCE CALCULATION ===");

// Test with Toulouse center as reference
const toulouseCenter = { latitude: 43.6047, longitude: 1.4442 };

Object.keys(mockPoisData).forEach(poiId => {
  const poiData = mockPoisData[poiId];
  console.log(`\n--- Testing ${poiData.name} ---`);
  
  const coords = extractCoordinates(poiData);
  if (coords) {
    const distance = calculateDistance(
      toulouseCenter.latitude, toulouseCenter.longitude,
      coords.latitude, coords.longitude
    );
    
    console.log(`Coordinates: ${coords.latitude}, ${coords.longitude}`);
    console.log(`Distance from Toulouse center: ${distance.toFixed(3)} km`);
    
    // Check if coordinates are reasonable
    if (coords.latitude >= 42.5 && coords.latitude <= 44.5 && 
        coords.longitude >= 0.5 && coords.longitude <= 2.5) {
      console.log("✓ Coordinates are valid for Toulouse area");
    } else {
      console.log("✗ Coordinates are NOT valid for Toulouse area");
    }
  }
});

console.log("\n=== SUMMARY ===");
console.log("The issue is likely that:");
console.log("1. Firebase stores POIs with nested 'location' object");
console.log("2. Android Poi.java expects flat latitude/longitude fields");
console.log("3. The fixed Poi.java should handle both structures");
console.log("4. Make sure Toulouse POIs are imported to Firebase");