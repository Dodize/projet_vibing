const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK with service account
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function updateAllPoiScores() {
    console.log("Starting POI score updates...");
    
    try {
        // Get all POIs from the collection
        const poisSnapshot = await db.collection('pois').get();
        
        if (poisSnapshot.empty) {
            console.log("No POIs found in the collection.");
            return;
        }
        
        console.log(`Found ${poisSnapshot.size} POIs to update.`);
        
        // Update each POI's score to 100
        const updatePromises = [];
        
        poisSnapshot.forEach(doc => {
            const poiData = doc.data();
            console.log(`Updating POI: ${poiData.name || doc.id} - Current score: ${poiData.currentScore || poiData.score || 'undefined'}`);
            
            const updatePromise = db.collection('pois').doc(doc.id).update({
                currentScore: 100,
                lastUpdated: admin.firestore.Timestamp.now()
            });
            
            updatePromises.push(updatePromise);
        });
        
        // Execute all updates
        await Promise.all(updatePromises);
        
        console.log(`Successfully updated ${poisSnapshot.size} POIs with score 100.`);
        
    } catch (error) {
        console.error("Error updating POI scores:", error);
    }
}

// Run the update function
updateAllPoiScores().then(() => {
    console.log("Score update complete!");
    process.exit(0);
}).catch(error => {
    console.error("Script failed:", error);
    process.exit(1);
});