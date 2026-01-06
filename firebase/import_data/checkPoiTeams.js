const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function checkPoiTeams() {
    try {
        console.log("Checking POI team assignments...");
        
        // Check a few specific POIs
        const poiIds = ['poi_test_galatee', 'poi_capitole', 'poi_zenith'];
        
        for (const poiId of poiIds) {
            const doc = await db.collection('pois').doc(poiId).get();
            
            if (doc.exists) {
                const data = doc.data();
                console.log(`\n=== POI: ${poiId} ===`);
                console.log('Name:', data.name);
                console.log('ownerTeamId:', data.ownerTeamId);
                console.log('All fields:', Object.keys(data));
            } else {
                console.log(`POI ${poiId} not found`);
            }
        }
        
        // Also check all teams data
        console.log('\n=== TEAMS DATA ===');
        const teamsSnapshot = await db.collection('teams').get();
        for (const teamDoc of teamsSnapshot.docs) {
            const teamData = teamDoc.data();
            console.log(`Team ${teamDoc.id}: ${teamData.name}`);
        }
        
    } catch (error) {
        console.error('Error checking POI teams:', error);
    }
}

checkPoiTeams().then(() => {
    process.exit(0);
});