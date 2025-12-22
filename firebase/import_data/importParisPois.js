const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR PARIS ---
const parisPoisData = {
  // Monuments emblématiques
  "poi_tour_eiffel": {
    "name": "Tour Eiffel",
    "location": { "latitude": 48.8584, "longitude": 2.2945 },
    "ownerTeamId": null,
    "currentScore": 800,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_arc_triomphe": {
    "name": "Arc de Triomphe",
    "location": { "latitude": 48.8738, "longitude": 2.2950 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_notre_dame": {
    "name": "Cathédrale Notre-Dame de Paris",
    "location": { "latitude": 48.8530, "longitude": 2.3499 },
    "ownerTeamId": null,
    "currentScore": 700,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Musées prestigieux
  "poi_louvre": {
    "name": "Musée du Louvre",
    "location": { "latitude": 48.8606, "longitude": 2.3376 },
    "ownerTeamId": null,
    "currentScore": 750,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_musee_orsay": {
    "name": "Musée d'Orsay",
    "location": { "latitude": 48.8600, "longitude": 2.3266 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_centre_pompidou": {
    "name": "Centre Pompidou",
    "location": { "latitude": 48.8606, "longitude": 2.3525 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Quartiers célèbres
  "poi_montmartre": {
    "name": "Quartier Montmartre",
    "location": { "latitude": 48.8867, "longitude": 2.3431 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_quartier_latin": {
    "name": "Quartier Latin",
    "location": { "latitude": 48.8519, "longitude": 2.3421 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_le_marais": {
    "name": "Quartier du Marais",
    "location": { "latitude": 48.8566, "longitude": 2.3614 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Places et jardins
  "poi_place_vendome": {
    "name": "Place Vendôme",
    "location": { "latitude": 48.8676, "longitude": 2.3285 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_place_vosges": {
    "name": "Place des Vosges",
    "location": { "latitude": 48.8555, "longitude": 2.3658 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_jardin_tuileries": {
    "name": "Jardin des Tuileries",
    "location": { "latitude": 48.8635, "longitude": 2.3275 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_jardin_luxembourg": {
    "name": "Jardin du Luxembourg",
    "location": { "latitude": 48.8462, "longitude": 2.3372 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Monuments religieux
  "poi_sainte_chapelle": {
    "name": "Sainte-Chapelle",
    "location": { "latitude": 48.8535, "longitude": 2.3452 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_panthéon": {
    "name": "Panthéon",
    "location": { "latitude": 48.8461, "longitude": 2.3456 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Transports et infrastructures
  "poi_gare_nord": {
    "name": "Gare du Nord",
    "location": { "latitude": 48.8809, "longitude": 2.3553 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_gare_lazare": {
    "name": "Gare Saint-Lazare",
    "location": { "latitude": 48.8760, "longitude": 2.3256 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture et divertissement
  "poi_opera_garnier": {
    "name": "Opéra Garnier",
    "location": { "latitude": 48.8719, "longitude": 2.3316 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_champs_elysees": {
    "name": "Avenue des Champs-Élysées",
    "location": { "latitude": 48.8658, "longitude": 2.3070 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Îles et ponts
  "poi_ile_cite": {
    "name": "Île de la Cité",
    "location": { "latitude": 48.8549, "longitude": 2.3476 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_ile_saint_louis": {
    "name": "Île Saint-Louis",
    "location": { "latitude": 48.8525, "longitude": 2.3552 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Éducation et culture
  "poi_college_france": {
    "name": "Collège de France",
    "location": { "latitude": 48.8500, "longitude": 2.3440 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_bibliotheque_francois_mitterrand": {
    "name": "Bibliothèque François Mitterrand",
    "location": { "latitude": 48.8255, "longitude": 2.3749 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  }
};

async function importParisPois() {
    console.log("Starting Paris POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(parisPoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(parisPoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(parisPoisData).length} POIs for Paris.`);

        console.log("Paris POIs import complete!");
    } catch (error) {
        console.error("Error during Paris POIs import:", error);
    }
}

importParisPois();