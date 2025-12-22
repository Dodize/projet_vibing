const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK with service account
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function listAllPois() {
    console.log("Listing all POI documents in Firebase...");
    
    try {
        const poisSnapshot = await db.collection('pois').get();
        
        if (poisSnapshot.empty) {
            console.log("No POIs found in the collection.");
            return;
        }
        
        console.log(`Found ${poisSnapshot.size} POIs:`);
        
        poisSnapshot.forEach(doc => {
            const data = doc.data();
            console.log(`ID: "${doc.id}" -> Name: "${data.name || 'No name'}"`);
        });
        
    } catch (error) {
        console.error("Error listing POIs:", error);
    }
}

// Run the function
listAllPois().then(() => {
    console.log("POI listing complete!");
    process.exit(0);
}).catch(error => {
    console.error("Script failed:", error);
    process.exit(1);
});