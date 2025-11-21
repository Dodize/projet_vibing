# POI Distance Issue Analysis and Solutions

## Problem Summary
Users in Toulouse are seeing all POIs at ~4852km distance, which suggests coordinate or distance calculation issues.

## Root Causes Identified

### 1. Data Structure Mismatch (FIXED)
**Issue**: Firebase import scripts store POI coordinates in nested structure:
```javascript
"location": { "latitude": 43.6047, "longitude": 1.4442 }
```

**But Android Poi.java expected flat structure**:
```java
private double latitude;
private double longitude;
```

**Solution**: Modified `Poi.java` getters to handle both structures:
```java
public double getLatitude() {
    if (location != null) {
        return location.getLatitude();
    }
    return latitude;
}
```

### 2. Missing Toulouse POIs (LIKELY)
**Issue**: The database may not have the Toulouse POIs imported yet.

**Evidence**: 
- `importToulousePois.js` exists with 30+ Toulouse POIs
- All coordinates are valid for Toulouse area (43.6°N, 1.44°E)
- Firebase authentication errors preventing import verification

### 3. Field Name Mismatches (FIXED)
**Issue**: Import scripts use different field names than Android expects:
- `currentScore` vs `score`
- `ownerTeamId` vs `owningTeam`

**Solution**: Modified `Poi.java` getters to handle both field names.

## Files Modified

### 1. `app/src/main/java/com/example/vibing/models/Poi.java`
- Added support for nested `location` object (GeoPoint)
- Added compatibility for `currentScore` field
- Added compatibility for `ownerTeamId` field
- All getters now handle both old and new field structures

### 2. `app/src/main/java/com/example/vibing/ui/home/HomeViewModel.java`
- Added debug logging to show POI coordinates when loaded

## Distance Calculation Verification
The Haversine formula implementation in `HomeFragment.java:269-283` is correct:
- Uses proper Earth radius (6,371,000 meters)
- Converts radians correctly
- Returns distance in kilometers

## Test Results
Created `testPoiCoordinates.js` which confirmed:
- Coordinate extraction logic works correctly
- Toulouse POI coordinates are valid (within 42.5-44.5°N, 0.5-2.5°E)
- Distance calculations produce reasonable results (0.476km for nearby POIs)

## Next Steps Required

### 1. Import Toulouse POIs to Firebase
Run the import script (requires Firebase authentication):
```bash
node importToulousePois.js
```

### 2. Test the Android App
1. Build and run the app
2. Check logs for:
   - "HomeViewModel: POIs loaded successfully. Count: X"
   - "HomeViewModel: POI: [Name] at (lat, lon)"
   - "DISTANCE_DEBUG: Reference location: 43.6047,1.4442"
   - "DISTANCE_DEBUG: Distance: XXXm (X.X km)"

### 3. Verify Firebase Access
If Firebase authentication continues to fail:
1. Check service account permissions in Firebase Console
2. Verify the service account key is still valid
3. Consider using Firebase Console web interface to manually import POIs

## Expected Results After Fixes
- POIs should show distances in meters/kilometers, not 4852km
- Toulouse POIs should appear within 0-50km range
- Distance sorting should work correctly
- Map should center on Toulouse area

## Debug Commands
```bash
# Build Android app
./gradlew build

# Check logs
adb logcat | grep -E "(HomeViewModel|DISTANCE_DEBUG)"

# Test coordinate logic
node testPoiCoordinates.js
```