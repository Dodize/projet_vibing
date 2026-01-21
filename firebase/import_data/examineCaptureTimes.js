const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function examineCaptureTimes() {
    console.log("=== EXAMINING CAPTURE TIME CORRUPTION ===");
    console.log("Checking all POIs for captureTime corruption...\n");
    
    try {
        const poisSnapshot = await db.collection('pois').get();
        
        if (poisSnapshot.empty) {
            console.log("No POIs found in the collection.");
            return;
        }
        
        console.log(`Found ${poisSnapshot.size} POIs to examine.\n`);
        
        let totalPois = 0;
        let corruptedPois = 0;
        let recentCapturePois = 0;
        let missingCaptureTime = 0;
        let suspiciousPois = [];
        
        const now = new Date();
        const twoMinutesAgo = new Date(now.getTime() - 2 * 60 * 1000);
        const oneHourAgo = new Date(now.getTime() - 60 * 60 * 1000);
        
        console.log("Current time:", now.toISOString());
        console.log("Two minutes ago:", twoMinutesAgo.toISOString());
        console.log("One hour ago:", oneHourAgo.toISOString());
        console.log("");
        
        for (const doc of poisSnapshot.docs) {
            const data = doc.data();
            totalPois++;
            
            const poiId = doc.id;
            const poiName = data.name || 'Unknown';
            const currentScore = data.currentScore || data.score || 0;
            const ownerTeamId = data.ownerTeamId || 'null';
            
            // Check captureTime
            let captureTime = null;
            let captureTimeStr = 'MISSING';
            let timeSinceCapture = null;
            
            if (data.captureTime) {
                captureTime = data.captureTime.toDate ? data.captureTime.toDate() : new Date(data.captureTime);
                captureTimeStr = captureTime.toISOString();
                timeSinceCapture = now - captureTime;
            }
            
            // Check lastUpdated as fallback
            let lastUpdated = null;
            let lastUpdatedStr = 'MISSING';
            if (data.lastUpdated) {
                lastUpdated = data.lastUpdated.toDate ? data.lastUpdated.toDate() : new Date(data.lastUpdated);
                lastUpdatedStr = lastUpdated.toISOString();
            }
            
            // Detect corruption patterns
            let isCorrupted = false;
            let corruptionReason = '';
            
            if (captureTime) {
                // Pattern 1: Very recent captureTime but score is not at minimum
                if (timeSinceCapture < 120000 && currentScore > 20) { // Less than 2 minutes ago but score > 20
                    isCorrupted = true;
                    corruptionReason = `Recent capture (${Math.round(timeSinceCapture/1000)}s ago) but high score (${currentScore})`;
                    corruptedPois++;
                }
                
                // Pattern 2: Future captureTime
                if (captureTime > now) {
                    isCorrupted = true;
                    corruptionReason = `Future capture time (${captureTimeStr})`;
                    corruptedPois++;
                }
                
                // Pattern 3: Very recent captureTime (less than 1 hour ago)
                if (timeSinceCapture < 3600000) {
                    recentCapturePois++;
                }
            } else {
                missingCaptureTime++;
            }
            
            // Log suspicious POIs
            if (isCorrupted || timeSinceCapture < 3600000 || !captureTime) {
                suspiciousPois.push({
                    id: poiId,
                    name: poiName,
                    currentScore,
                    ownerTeamId,
                    captureTime: captureTimeStr,
                    lastUpdated: lastUpdatedStr,
                    timeSinceCapture: timeSinceCapture ? Math.round(timeSinceCapture/1000) : null,
                    isCorrupted,
                    corruptionReason
                });
            }
            
            // Print detailed info for first few POIs
            if (totalPois <= 5) {
                console.log(`=== POI ${totalPois}: ${poiName} (${poiId}) ===`);
                console.log(`  Current Score: ${currentScore}`);
                console.log(`  Owner Team: ${ownerTeamId}`);
                console.log(`  Capture Time: ${captureTimeStr}`);
                console.log(`  Time Since Capture: ${timeSinceCapture ? Math.round(timeSinceCapture/1000) + ' seconds' : 'N/A'}`);
                console.log(`  Last Updated: ${lastUpdatedStr}`);
                console.log(`  All Fields: ${Object.keys(data).join(', ')}`);
                console.log(`  CORRUPTED: ${isCorrupted ? 'YES - ' + corruptionReason : 'NO'}`);
                console.log("");
            }
        }
        
        // Summary statistics
        console.log("\n=== SUMMARY STATISTICS ===");
        console.log(`Total POIs examined: ${totalPois}`);
        console.log(`POIs with corrupted captureTime: ${corruptedPois}`);
        console.log(`POIs with recent captureTime (< 1 hour): ${recentCapturePois}`);
        console.log(`POIs missing captureTime: ${missingCaptureTime}`);
        console.log(`Corruption rate: ${((corruptedPois / totalPois) * 100).toFixed(2)}%`);
        
        // Show all corrupted POIs
        const corruptedList = suspiciousPois.filter(p => p.isCorrupted);
        if (corruptedList.length > 0) {
            console.log("\n=== CORRUPTED POIS ===");
            corruptedList.forEach(poi => {
                console.log(`❌ ${poi.name} (${poi.id})`);
                console.log(`   Score: ${poi.currentScore}, Time since capture: ${poi.timeSinceCapture}s`);
                console.log(`   Reason: ${poi.corruptionReason}`);
                console.log(`   Capture time: ${poi.captureTime}`);
            });
        }
        
        // Show recent POIs (potentially being updated frequently)
        const recentList = suspiciousPois.filter(p => !p.isCorrupted && p.timeSinceCapture !== null && p.timeSinceCapture < 3600);
        if (recentList.length > 0) {
            console.log("\n=== RECENTLY UPDATED POIS (Potential Auto-Update Issue) ===");
            recentList.slice(0, 10).forEach(poi => {
                console.log(`⚠️  ${poi.name} (${poi.id})`);
                console.log(`   Score: ${poi.currentScore}, Time since capture: ${poi.timeSinceCapture}s`);
                console.log(`   Capture time: ${poi.captureTime}`);
            });
            if (recentList.length > 10) {
                console.log(`   ... and ${recentList.length - 10} more recent POIs`);
            }
        }
        
        // Show POIs missing captureTime
        const missingList = suspiciousPois.filter(p => p.captureTime === 'MISSING');
        if (missingList.length > 0) {
            console.log("\n=== POIS MISSING CAPTURE TIME ===");
            missingList.slice(0, 10).forEach(poi => {
                console.log(`❓ ${poi.name} (${poi.id})`);
                console.log(`   Score: ${poi.currentScore}, Last Updated: ${poi.lastUpdated}`);
            });
            if (missingList.length > 10) {
                console.log(`   ... and ${missingList.length - 10} more POIs missing captureTime`);
            }
        }
        
        console.log("\n=== ANALYSIS COMPLETE ===");
        
    } catch (error) {
        console.error("Error examining capture times:", error);
    }
}

// Run the examination
examineCaptureTimes().then(() => {
    console.log("\nExamination complete!");
    process.exit(0);
}).catch(error => {
    console.error("Script failed:", error);
    process.exit(1);
});