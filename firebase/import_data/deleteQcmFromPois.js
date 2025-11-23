const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function deleteQcmFieldFromAllPois() {
    try {
        console.log('Starting to DELETE qcm field from all POIs...');
        
        // Get all documents from pois collection
        const poisSnapshot = await db.collection('pois').get();
        
        if (poisSnapshot.empty) {
            console.log('No POIs found in database.');
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
                // Use FieldValue.delete() to remove the qcm field
                await db.collection('pois').doc(doc.id).update({
                    qcm: admin.firestore.FieldValue.delete()
                });
                
                console.log(`✓ DELETED qcm field from POI: ${doc.id} (${poiData.name || 'unnamed'})`);
                updatedCount++;
            } else {
                console.log(`- No qcm field found in POI: ${doc.id} (${poiData.name || 'unnamed'})`);
                skippedCount++;
            }
        }
        
        console.log('\n=== SUMMARY ===');
        console.log(`Total POIs processed: ${poisSnapshot.size}`);
        console.log(`POIs updated (qcm DELETED): ${updatedCount}`);
        console.log(`POIs skipped (no qcm field): ${skippedCount}`);
        console.log('QCM field DELETION completed successfully!');
        
    } catch (error) {
        console.error('Error deleting qcm field from POIs:', error);
    }
}

// Run the function
deleteQcmFieldFromAllPois().then(() => {
    console.log('Script execution completed.');
    process.exit(0);
}).catch((error) => {
    console.error('Script failed:', error);
    process.exit(1);
});