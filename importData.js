const admin = require('firebase-admin');
const serviceAccount = require('./firebase/vibingn7-firebase-adminsdk-fbsvc-ad7d4a5efd.json'); // IMPORTANT: Replace with your actual path

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
        // If you are using Firestore in a local emulator, you might need:
        // projectId: "your-project-id"
    });
}

const db = admin.firestore();

// --- 1. TEAMS DATA ---
const teamsData = [
  {
    "teamId": "team_1",
    "name": "Les Conqu\u00e9rants",
    "colorHex": "#FF0000"
  },
  {
    "teamId": "team_2",
    "name": "Les Explorateurs",
    "colorHex": "#0000FF"
  },
  {
    "teamId": "team_3",
    "name": "Les Strat\u00e8ges",
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

// --- 3. POIS DATA ---
// Note: Firestore GeoPoints are complex to represent directly in a simple JSON object for batch writing.
// For simplicity in this script, we will use a structure that Firestore might accept, but you may need to
// manually convert lat/lng to GeoPoint objects if this batch write fails due to type mismatch.
const poisData = {
  "poi_eiffel_tower": {
    "name": "Tour Eiffel",
    "location": { "latitude": 48.8584, "longitude": 2.2945 },
    "ownerTeamId": "team_3",
    "currentScore": 1500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle est la hauteur approximative de la Tour Eiffel ?",
      "options": [
        "250 m\u00e8tres",
        "330 m\u00e8tres",
        "400 m\u00e8tres"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Architecture"
    }
  },
  "poi_local_statue": {
    "name": "Statue Locale X",
    "location": { "latitude": 48.8600, "longitude": 2.3376 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T12:00:00Z")),
    "qcm": {
      "question": "De quelle ann\u00e9e date la construction de ce monument ?",
      "options": [
        "1950",
        "1985",
        "2001"
      ],
      "correctAnswerIndex": 0,
      "theme": "Histoire Locale"
    }
  }
};

async function importData() {
    console.log("Starting Firebase data import...");

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

    // 3. Import POIs
    const poiPromises = Object.keys(poisData).map(poiId => 
        db.collection('pois').doc(poiId).set(poisData[poiId])
    );
    await Promise.all(poiPromises);
    console.log(`Successfully imported ${Object.keys(poisData).length} POIs.`);

    console.log("Data import complete!");
}

importData().catch(error => {
    console.error("Error during data import:", error);
});