const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function addMissingTeam() {
    try {
        console.log("Adding missing team_5...");
        
        const team5Data = {
            "teamId": "team_5",
            "name": "Les Innovateurs",
            "colorHex": "#800080"
        };
        
        await db.collection('teams').doc('team_5').set(team5Data);
        console.log("✅ Added team_5: Les Innovateurs");
        
        // Verify all teams
        console.log('\n=== ALL TEAMS AFTER ADDITION ===');
        const teamsSnapshot = await db.collection('teams').get();
        for (const teamDoc of teamsSnapshot.docs) {
            const teamData = teamDoc.data();
            console.log(`${teamDoc.id} -> ${teamData.name}`);
        }
        
    } catch (error) {
        console.error('Error adding missing team:', error);
    }
}

addMissingTeam().then(() => {
    process.exit(0);
});