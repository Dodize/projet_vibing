const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function fixAllPoiIds() {
    try {
        console.log("Fixing all POIs with null poiId...");
        
        const snapshot = await db.collection('pois').get();
        let fixedCount = 0;
        let totalCount = 0;
        
        for (const doc of snapshot.docs) {
            totalCount++;
            const poiData = doc.data();
            
            // Check if poiId is null or missing
            if (!poiData.poiId || poiData.poiId === null) {
                // Update the POI to set the poiId field to the document ID
                await doc.ref.update({
                    poiId: doc.id
                });
                
                fixedCount++;
                console.log(`✅ Fixed POI ${doc.id}: set poiId to '${doc.id}'`);
            }
        }
        
        console.log(`\nSummary: Fixed ${fixedCount} out of ${totalCount} POIs`);
        
    } catch (error) {
        console.error('Error fixing POI IDs:', error);
    }
}

fixAllPoiIds().then(() => {
    process.exit(0);
});