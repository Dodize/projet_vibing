const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// Available team IDs from the database
const TEAM_IDS = [
    "team_1", // Les Conquérants (Rouge)
    "team_2", // Les Explorateurs (Bleu)
    "team_3", // Les Stratèges (Vert)
    "team_4", // Les Gardiens (Orange)
    "team_5"  // Les Innovateurs (Violet)
];

// Function to get a random team ID
function getRandomTeamId() {
    // 30% chance of being neutral (null)
    if (Math.random() < 0.3) {
        return "null";
    }
    // 70% chance of being assigned to a team
    const randomIndex = Math.floor(Math.random() * TEAM_IDS.length);
    return TEAM_IDS[randomIndex];
}

// Function to get team distribution for better balance
function getBalancedTeamId(currentIndex, totalPois) {
    // Add some randomness: 20% chance to be neutral
    if (Math.random() < 0.2) {
        return "null";
    }
    
    // Distribute teams more evenly across POIs
    const teamIndex = currentIndex % TEAM_IDS.length;
    
    // Add some variation: sometimes shift to adjacent team
    let finalIndex = teamIndex;
    if (Math.random() < 0.3) {
        const variation = Math.random() < 0.5 ? -1 : 1;
        finalIndex = (teamIndex + variation + TEAM_IDS.length) % TEAM_IDS.length;
    }
    
    return TEAM_IDS[finalIndex];
}

async function assignRandomTeamsToPois() {
    console.log("=== ASSIGNING RANDOM TEAMS TO POIS ===");
    
    try {
        // Get all POIs from Firestore
        const poisSnapshot = await db.collection('pois').get();
        
        if (poisSnapshot.empty) {
            console.log("No POIs found in the database. Please import POIs first.");
            return;
        }
        
        console.log(`Found ${poisSnapshot.size} POIs to update...`);
        
        let teamDistribution = {};
        let neutralCount = 0;
        let updatePromises = [];
        
        // Process each POI
        poisSnapshot.forEach((doc, index) => {
            const poiData = doc.data();
            const poiId = doc.id;
            
            // Simple random assignment: 30% neutral, 70% teams
            let newTeamId;
            if (Math.random() < 0.3) {
                newTeamId = "null"; // neutral
            } else {
                // Assign to a random team
                const randomIndex = Math.floor(Math.random() * TEAM_IDS.length);
                newTeamId = TEAM_IDS[randomIndex];
            }
            
            // Track distribution
            if (newTeamId === "null") {
                neutralCount++;
            } else {
                teamDistribution[newTeamId] = (teamDistribution[newTeamId] || 0) + 1;
            }
            
            console.log(`Assigning ${poiData.name || poiId} -> ${newTeamId === "null" ? 'NEUTRAL' : newTeamId}`);
            
            // Create clean updated data with only required fields
            let updatedData = {
                name: poiData.name,
                location: poiData.location,
                currentScore: poiData.currentScore,
                qcm: poiData.qcm,
                lastUpdated: admin.firestore.Timestamp.now()
            };
            
            // Only add ownerTeamId if it's not null
            if (newTeamId !== "null") {
                updatedData.ownerTeamId = newTeamId;
            }
            
            const updatePromise = db.collection('pois').doc(poiId).set(updatedData, { merge: true });
            
            updatePromises.push(updatePromise);
        });
        
        // Execute all updates
        await Promise.all(updatePromises);
        
        console.log("\n=== UPDATE COMPLETE ===");
        console.log(`Successfully updated ${poisSnapshot.size} POIs`);
        console.log("\nTeam Distribution:");
        console.log(`Neutral POIs: ${neutralCount}`);
        
        Object.keys(teamDistribution).forEach(teamId => {
            console.log(`${teamId}: ${teamDistribution[teamId]} POIs`);
        });
        
        // Calculate percentages
        const total = poisSnapshot.size;
        console.log("\nPercentage Distribution:");
        console.log(`Neutral: ${((neutralCount / total) * 100).toFixed(1)}%`);
        
        Object.keys(teamDistribution).forEach(teamId => {
            const percentage = ((teamDistribution[teamId] / total) * 100).toFixed(1);
            console.log(`${teamId}: ${percentage}%`);
        });
        
    } catch (error) {
        console.error("Error assigning teams to POIs:", error);
    }
}

// Function to reset all POIs to neutral
async function resetAllPoisToNeutral() {
    console.log("=== RESETTING ALL POIS TO NEUTRAL ===");
    
    try {
        const poisSnapshot = await db.collection('pois').get();
        
        if (poisSnapshot.empty) {
            console.log("No POIs found in the database.");
            return;
        }
        
        let updatePromises = [];
        
        poisSnapshot.forEach(doc => {
            const currentData = doc.data();
            let updatedData = { ...currentData };
            updatedData.lastUpdated = admin.firestore.Timestamp.now();
            updatedData.ownerTeamId = "null";
            
            const updatePromise = db.collection('pois').doc(doc.id).set(updatedData, { merge: true });
            updatePromises.push(updatePromise);
        });
        
        await Promise.all(updatePromises);
        console.log(`Successfully reset ${poisSnapshot.size} POIs to neutral.`);
        
    } catch (error) {
        console.error("Error resetting POIs:", error);
    }
}

// Function to show current team distribution
async function showCurrentDistribution() {
    console.log("=== CURRENT TEAM DISTRIBUTION ===");
    
    try {
        const poisSnapshot = await db.collection('pois').get();
        
        let teamDistribution = {};
        let neutralCount = 0;
        
        poisSnapshot.forEach(doc => {
            const poiData = doc.data();
            const teamId = poiData.ownerTeamId;
            
            if (teamId === null || teamId === undefined || teamId === "null") {
                neutralCount++;
            } else {
                teamDistribution[teamId] = (teamDistribution[teamId] || 0) + 1;
            }
        });
        
        const total = poisSnapshot.size;
        console.log(`Total POIs: ${total}`);
        console.log(`Neutral: ${neutralCount} (${((neutralCount / total) * 100).toFixed(1)}%)`);
        
        Object.keys(teamDistribution).forEach(teamId => {
            const count = teamDistribution[teamId];
            const percentage = ((count / total) * 100).toFixed(1);
            console.log(`${teamId}: ${count} (${percentage}%)`);
        });
        
    } catch (error) {
        console.error("Error getting current distribution:", error);
    }
}

// Main execution
async function main() {
    const args = process.argv.slice(2);
    const command = args[0];
    
    switch (command) {
        case 'assign':
            await assignRandomTeamsToPois();
            break;
        case 'reset':
            await resetAllPoisToNeutral();
            break;
        case 'show':
            await showCurrentDistribution();
            break;
        default:
            console.log("Usage:");
            console.log("  node assignTeamsToPois.js assign  - Assign random teams to all POIs");
            console.log("  node assignTeamsToPois.js reset   - Reset all POIs to neutral");
            console.log("  node assignTeamsToPois.js show    - Show current team distribution");
            break;
    }
}

main();