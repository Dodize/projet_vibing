const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function testTeamNameMapping() {
    try {
        console.log("=== TESTING TEAM NAME MAPPING ===");
        
        // Get all teams from Firebase
        const teamsSnapshot = await db.collection('teams').get();
        const firebaseTeams = {};
        
        for (const teamDoc of teamsSnapshot.docs) {
            const teamData = teamDoc.data();
            const teamNumber = teamDoc.id.replace('team_', '');
            firebaseTeams[teamNumber] = teamData.name;
            console.log(`Firebase: team_${teamNumber} -> ${teamData.name}`);
        }
        
        console.log('\n=== ANDROID CODE MAPPING ===');
        console.log('Android getTeamName() method should return:');
        console.log('teamId 1 -> "Les Conquérants"');
        console.log('teamId 2 -> "Les Explorateurs"');
        console.log('teamId 3 -> "Les Stratèges"');
        console.log('teamId 4 -> "Les Gardiens"');
        console.log('teamId 5 -> "Les Innovateurs"');
        
        console.log('\n=== VERIFICATION ===');
        const expectedMapping = {
            '1': 'Les Conquérants',
            '2': 'Les Explorateurs', 
            '3': 'Les Stratèges',
            '4': 'Les Gardiens',
            '5': 'Les Innovateurs'
        };
        
        let allMatch = true;
        for (const [teamNumber, expectedName] of Object.entries(expectedMapping)) {
            const firebaseName = firebaseTeams[teamNumber];
            const matches = firebaseName === expectedName;
            console.log(`Team ${teamNumber}: ${matches ? '✅' : '❌'} Firebase="${firebaseName}" vs Expected="${expectedName}"`);
            if (!matches) allMatch = false;
        }
        
        console.log(`\n=== RESULT ===`);
        console.log(allMatch ? '✅ All team names match!' : '❌ Some team names do not match');
        
        // Test specific POI
        console.log('\n=== TESTING SPECIFIC POI ===');
        const testPoi = await db.collection('pois').doc('poi_test_galatee').get();
        if (testPoi.exists) {
            const poiData = testPoi.data();
            const teamNumber = poiData.ownerTeamId.replace('team_', '');
            const teamName = firebaseTeams[teamNumber];
            console.log(`POI "${poiData.name}" has ownerTeamId: ${poiData.ownerTeamId}`);
            console.log(`Should display team: ${teamName}`);
            console.log(`Android should show: "Équipe: ${teamName}"`);
        }
        
    } catch (error) {
        console.error('Error testing team name mapping:', error);
    }
}

testTeamNameMapping().then(() => {
    process.exit(0);
});