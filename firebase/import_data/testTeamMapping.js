const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function testTeamMapping() {
    try {
        console.log("=== TESTING TEAM MAPPING ===");
        
        // Test the specific POI that was showing wrong team
        const testPoiDoc = await db.collection('pois').doc('poi_test_galatee').get();
        
        if (testPoiDoc.exists) {
            const data = testPoiDoc.data();
            console.log(`\nPOI: ${testPoiDoc.id}`);
            console.log('Name:', data.name);
            console.log('ownerTeamId:', data.ownerTeamId);
            
            // Extract team number
            if (data.ownerTeamId && data.ownerTeamId.startsWith('team_')) {
                const teamNumber = data.ownerTeamId.substring(5);
                console.log('Extracted team number:', teamNumber);
                
                // Get corresponding team name
                const teamDoc = await db.collection('teams').doc(data.ownerTeamId).get();
                if (teamDoc.exists) {
                    const teamData = teamDoc.data();
                    console.log('Team name from Firebase:', teamData.name);
                    console.log('Expected display: "Équipe:', teamData.name + '"');
                } else {
                    console.log('Team document not found!');
                }
            }
        }
        
        console.log('\n=== ALL TEAMS ===');
        const teamsSnapshot = await db.collection('teams').get();
        for (const teamDoc of teamsSnapshot.docs) {
            const teamData = teamDoc.data();
            console.log(`${teamDoc.id} -> ${teamData.name}`);
        }
        
    } catch (error) {
        console.error('Error testing team mapping:', error);
    }
}

testTeamMapping().then(() => {
    process.exit(0);
});