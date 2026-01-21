const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function removeQcmFieldFromAllPois() {
    try {
        console.log('Starting to remove qcm field from all POIs...');
        
        // Get all documents from the pois collection
        const poisSnapshot = await db.collection('pois').get();
        
        if (poisSnapshot.empty) {
            console.log('No POIs found in the database.');
            return;
        }
        
        console.log(`Found ${poisSnapshot.size} POIs to process.`);
        
        let updatedCount = 0;
        let skippedCount = 0;
        
        // Process each POI document
        for (const doc of poisSnapshot.docs) {
            const poiData = doc.data();
            
            // Check if qcm field exists
            if (poiData.qcm) {
                // Remove the qcm field by creating a new object without it
                const { qcm, ...updatedPoiData } = poiData;
                
                // Update the document in Firestore
                await db.collection('pois').doc(doc.id).set(updatedPoiData, { merge: true });
                
                console.log(`✓ Removed qcm field from POI: ${doc.id} (${poiData.name || 'unnamed'})`);
                updatedCount++;
            } else {
                console.log(`- No qcm field found in POI: ${doc.id} (${poiData.name || 'unnamed'})`);
                skippedCount++;
            }
        }
        
        console.log('\n=== SUMMARY ===');
        console.log(`Total POIs processed: ${poisSnapshot.size}`);
        console.log(`POIs updated (qcm removed): ${updatedCount}`);
        console.log(`POIs skipped (no qcm field): ${skippedCount}`);
        console.log('QCM field removal completed successfully!');
        
    } catch (error) {
        console.error('Error removing qcm field from POIs:', error);
    }
}

// Run the function
removeQcmFieldFromAllPois().then(() => {
    console.log('Script execution completed.');
    process.exit(0);
}).catch((error) => {
    console.error('Script failed:', error);
    process.exit(1);
});