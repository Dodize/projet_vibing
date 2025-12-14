const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function checkTestPoi() {
    try {
        console.log("=== CHECKING TEST POI ===");
        
        const testPoiDoc = await db.collection('pois').doc('poi_test_galatee').get();
        
        if (testPoiDoc.exists) {
            const data = testPoiDoc.data();
            console.log(`POI: ${testPoiDoc.id}`);
            console.log('Name:', data.name);
            console.log('Current ownerTeamId:', data.ownerTeamId);
            
            // If you want to change it to team_4 (Les Gardiens):
            console.log('\n=== UPDATING TO TEAM_4 (Les Gardiens) ===');
            await testPoiDoc.ref.update({
                ownerTeamId: 'team_4'
            });
            console.log('✅ Updated POI to team_4 (Les Gardiens)');
            
            // Verify the update
            const updatedDoc = await testPoiDoc.ref.get();
            const updatedData = updatedDoc.data();
            console.log('New ownerTeamId:', updatedData.ownerTeamId);
            
            // Get the corresponding team name
            const teamDoc = await db.collection('teams').doc('team_4').get();
            if (teamDoc.exists) {
                const teamData = teamDoc.data();
                console.log('Team name:', teamData.name);
                console.log(`Expected display: "Équipe: ${teamData.name}"`);
            }
        } else {
            console.log('POI not found');
        }
        
    } catch (error) {
        console.error('Error checking test POI:', error);
    }
}

checkTestPoi().then(() => {
    process.exit(0);
});