const admin = require('firebase-admin');
const serviceAccount = require('./firebase/vibingn7-firebase-adminsdk-fbsvc-ad7d4a5efd.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- 1. TEAMS DATA ---
const teamsData = [
  {
    "teamId": "team_1",
    "name": "Les Conquérants",
    "colorHex": "#FF0000"
  },
  {
    "teamId": "team_2",
    "name": "Les Explorateurs",
    "colorHex": "#0000FF"
  },
  {
    "teamId": "team_3",
    "name": "Les Stratèges",
    "colorHex": "#00FF00"
  },
  {
    "teamId": "team_4",
    "name": "Les Gardiens",
    "colorHex": "#FFA500"
  },
  {
    "teamId": "team_5",
    "name": "Les Innovateurs",
    "colorHex": "#800080"
  }
];

// --- 2. USERS DATA ---
const usersData = [
  {
    "userId": "user_abc123",
    "pseudo": "AlphaStriker",
    "teamId": "team_1",
    "currency": 500
  },
  {
    "userId": "user_def456",
    "pseudo": "BetaExplorer",
    "teamId": "team_2",
    "currency": 1200
  },
  {
    "userId": "user_ghi789",
    "pseudo": "GammaMaster",
    "teamId": "team_1",
    "currency": 80
  }
];

async function importBasicData() {
    console.log("Starting basic Firebase data import (Teams and Users)...");

    try {
        // 1. Import Teams
        const teamPromises = teamsData.map(team => 
            db.collection('teams').doc(team.teamId).set(team)
        );
        await Promise.all(teamPromises);
        console.log(`Successfully imported ${teamsData.length} teams.`);

        // 2. Import Users
        const userPromises = usersData.map(user => 
            db.collection('users').doc(user.userId).set(user)
        );
        await Promise.all(userPromises);
        console.log(`Successfully imported ${usersData.length} users.`);

        console.log("Basic data import complete!");
    } catch (error) {
        console.error("Error during basic data import:", error);
    }
}

importBasicData();