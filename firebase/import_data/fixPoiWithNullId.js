const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function fixPoiWithNullId() {
    try {
        console.log("Searching for POI with name 'Test Galatee'...");
        
        // Search for POI with the problematic name
        const snapshot = await db.collection('pois')
            .where('name', '==', 'Test Galatee')
            .get();
        
        if (snapshot.empty) {
            console.log("No POI found with name 'Test Galatee'");
            return;
        }
        
        // Process each matching POI
        for (const doc of snapshot.docs) {
            const poiData = doc.data();
            console.log(`Found POI: ${doc.id}`);
            console.log('POI data:', poiData);
            
            // Check if the document ID is being used as poiId in the data
            if (!poiData.poiId || poiData.poiId === null) {
                console.log(`POI ID is null or missing. Setting poiId to document ID: ${doc.id}`);
                
                // Update the POI to set the poiId field to the document ID
                await doc.ref.update({
                    poiId: doc.id
                });
                
                console.log(`✅ Fixed POI ${doc.id}: set poiId to '${doc.id}'`);
            } else {
                console.log(`POI already has poiId: ${poiData.poiId}`);
            }
        }
        
    } catch (error) {
        console.error('Error fixing POI:', error);
    }
}

async function listAllPois() {
    try {
        console.log("\nListing all POIs to check for null IDs...");
        
        const snapshot = await db.collection('pois').get();
        
        for (const doc of snapshot.docs) {
            const poiData = doc.data();
            const hasNullId = !poiData.poiId || poiData.poiId === null;
            
            console.log(`POI: ${doc.id} | Name: ${poiData.name || 'No name'} | poiId: ${poiData.poiId || 'NULL'} ${hasNullId ? '❌' : '✅'}`);
        }
        
    } catch (error) {
        console.error('Error listing POIs:', error);
    }
}

async function main() {
    await fixPoiWithNullId();
    await listAllPois();
    process.exit(0);
}

main();