const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function removePoiIdField() {
    try {
        console.log("Removing poiId field from all POIs...");
        
        const snapshot = await db.collection('pois').get();
        let removedCount = 0;
        let totalCount = 0;
        
        for (const doc of snapshot.docs) {
            totalCount++;
            const poiData = doc.data();
            
            // Check if poiId field exists
            if (poiData.poiId) {
                // Remove the poiId field
                await doc.ref.update({
                    poiId: admin.firestore.FieldValue.delete()
                });
                
                removedCount++;
                console.log(`✅ Removed poiId field from POI ${doc.id}`);
            }
        }
        
        console.log(`\nSummary: Removed poiId field from ${removedCount} out of ${totalCount} POIs`);
        
    } catch (error) {
        console.error('Error removing poiId field:', error);
    }
}

removePoiIdField().then(() => {
    process.exit(0);
});