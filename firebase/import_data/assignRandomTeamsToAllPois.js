const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// Function to assign random teams to all POIs
async function assignRandomTeamsToAllPois() {
    console.log("Starting random team assignment to all POIs...");

    try {
        // Get all POIs from the database
        const poisSnapshot = await db.collection('pois').get();
        
        if (poisSnapshot.empty) {
            console.log("No POIs found in the database.");
            return;
        }

        console.log(`Found ${poisSnapshot.size} POIs to update.`);

        // Team IDs: 1-5 (no neutral team 0)
        const teamIds = [1, 2, 3, 4, 5];
        const teamNames = {
            1: "Les Conquérants (Rouge)",
            2: "Les Explorateurs (Bleu)", 
            3: "Les Stratèges (Vert)",
            4: "Les Gardiens (Orange)",
            5: "Les Innovateurs (Violet)"
        };

        let updatePromises = [];
        let teamCounts = { 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 };

        // Process each POI
        poisSnapshot.forEach((doc) => {
            const poiData = doc.data();
            const poiId = doc.id;
            
            // Assign random team (1-5)
            const randomTeamId = teamIds[Math.floor(Math.random() * teamIds.length)];
            
            // Update the POI with the new team
            const updatePromise = db.collection('pois').doc(poiId).update({
                ownerTeamId: `team_${randomTeamId}`,
                lastUpdated: admin.firestore.Timestamp.fromDate(new Date())
            });
            
            updatePromises.push(updatePromise);
            teamCounts[randomTeamId]++;
            
            console.log(`POI "${poiData.name || poiId}" assigned to ${teamNames[randomTeamId]} (team_${randomTeamId})`);
        });

        // Execute all updates
        await Promise.all(updatePromises);

        console.log("\n=== TEAM ASSIGNMENT SUMMARY ===");
        console.log(`Total POIs updated: ${poisSnapshot.size}`);
        Object.keys(teamCounts).forEach(teamId => {
            const percentage = ((teamCounts[teamId] / poisSnapshot.size) * 100).toFixed(1);
            console.log(`${teamNames[teamId]}: ${teamCounts[teamId]} POIs (${percentage}%)`);
        });
        console.log("=== END SUMMARY ===");

        console.log("\nRandom team assignment completed successfully!");

    } catch (error) {
        console.error("Error during random team assignment:", error);
    }
}

// Execute the function
assignRandomTeamsToAllPois();