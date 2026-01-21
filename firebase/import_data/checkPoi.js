const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function checkSpecificPoi() {
    try {
        // Check a specific POI to see if qcm field still exists
        const doc = await db.collection('pois').doc('poi_capitole').get();
        
        if (doc.exists) {
            const data = doc.data();
            console.log('POI: poi_capitole');
            console.log('Name:', data.name);
            console.log('Has qcm field:', 'qcm' in data);
            console.log('QCM field value:', data.qcm);
            console.log('All fields:', Object.keys(data));
        } else {
            console.log('POI not found');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

checkSpecificPoi().then(() => {
    process.exit(0);
});