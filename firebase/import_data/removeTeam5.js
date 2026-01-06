const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function removeTeam5() {
    try {
        console.log("Removing team_5 from Firebase...");
        
        // Delete team_5 from teams collection
        await db.collection('teams').doc('team_5').delete();
        console.log("✅ Deleted team_5 from teams collection");
        
        // Update any POIs that have team_5 to set them to neutral (team_0)
        const poisSnapshot = await db.collection('pois')
            .where('ownerTeamId', '==', 'team_5')
            .get();
        
        console.log(`Found ${poisSnapshot.size} POIs with team_5`);
        
        for (const poiDoc of poisSnapshot.docs) {
            await poiDoc.ref.update({
                ownerTeamId: null  // Set to neutral
            });
            console.log(`✅ Updated POI ${poiDoc.id} to neutral`);
        }
        
        // Verify remaining teams
        console.log('\n=== REMAINING TEAMS ===');
        const teamsSnapshot = await db.collection('teams').get();
        for (const teamDoc of teamsSnapshot.docs) {
            const teamData = teamDoc.data();
            console.log(`${teamDoc.id} -> ${teamData.name}`);
        }
        
    } catch (error) {
        console.error('Error removing team 5:', error);
    }
}

removeTeam5().then(() => {
    process.exit(0);
});