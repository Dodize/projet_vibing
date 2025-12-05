const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR LYON ---
const lyonPoisData = {
  // Monuments emblématiques
  "poi_basilique_fourviere": {
    "name": "Basilique Notre-Dame de Fourvière",
    "location": { "latitude": 45.7640, "longitude": 4.8357 },
    "ownerTeamId": null,
    "currentScore": 700,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_vieux_lyon": {
    "name": "Vieux Lyon",
    "location": { "latitude": 45.7625, "longitude": 4.8326 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_place_bellecour": {
    "name": "Place Bellecour",
    "location": { "latitude": 45.7577, "longitude": 4.8327 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Musées et culture
  "poi_musee_beaux_arts": {
    "name": "Musée des Beaux-Arts de Lyon",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_musee_gallo_romain": {
    "name": "Musée Gallo-Romain",
    "location": { "latitude": 45.7640, "longitude": 4.8357 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_confluences": {
    "name": "Musée des Confluences",
    "location": { "latitude": 45.7346, "longitude": 4.8234 },
    "ownerTeamId": null,
    "currentScore": 550,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Quartiers emblématiques
  "poi_croix_rousse": {
    "name": "Quartier Croix-Rousse",
    "location": { "latitude": 45.7734, "longitude": 4.8345 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_presquile": {
    "name": "Presqu'île de Lyon",
    "location": { "latitude": 45.7577, "longitude": 4.8327 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_part_dieu": {
    "name": "Quartier Part-Dieu",
    "location": { "latitude": 45.7616, "longitude": 4.8609 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Parcs et espaces naturels
  "poi_parque_tete_or": {
    "name": "Parc de la Tête d'Or",
    "location": { "latitude": 45.7800, "longitude": 4.8600 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_parcs_miribel_jonage": {
    "name": "Parc de Miribel-Jonage",
    "location": { "latitude": 45.8167, "longitude": 4.9833 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_jardin_rosa_mir": {
    "name": "Jardin Rosa Mir",
    "location": { "latitude": 45.7734, "longitude": 4.8345 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Édifices religieux
  "poi_cathedrale_saint_jean": {
    "name": "Cathédrale Saint-Jean-Baptiste",
    "location": { "latitude": 45.7640, "longitude": 4.8357 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_eglise_saint_nizier": {
    "name": "Église Saint-Nizier",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Transports et infrastructures
  "poi_gare_part_dieu": {
    "name": "Gare de Lyon Part-Dieu",
    "location": { "latitude": 45.7616, "longitude": 4.8609 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_aeroport_lyon": {
    "name": "Aéroport Lyon-Saint Exupéry",
    "location": { "latitude": 45.7256, "longitude": 5.0811 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Sport et loisirs
  "poi_groupama_stadium": {
    "name": "Groupama Stadium",
    "location": { "latitude": 45.7640, "longitude": 4.8357 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_patinage_lyon": {
    "name": "Patinoire Charlemagne",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture et gastronomie
  "poi_halle_de_la_martiniere": {
    "name": "Halle de la Martinière",
    "location": { "latitude": 45.7577, "longitude": 4.8327 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_maison_canuts": {
    "name": "Maison des Canuts",
    "location": { "latitude": 45.7734, "longitude": 4.8345 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Ponts et rivières
  "poi_passerelle_saint_georges": {
    "name": "Passerelle Saint-Georges",
    "location": { "latitude": 45.7640, "longitude": 4.8357 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_pont_bonnevay": {
    "name": "Pont Bonnevaux",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 180,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Éducation et recherche
  "poi_universite_lyon": {
    "name": "Université Claude Bernard Lyon 1",
    "location": { "latitude": 45.7847, "longitude": 4.8700 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_insa_lyon": {
    "name": "INSA Lyon",
    "location": { "latitude": 45.7847, "longitude": 4.8700 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Places et espaces publics
  "poi_place_terreaux": {
    "name": "Place des Terreaux",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_place_jacobins": {
    "name": "Place des Jacobins",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  }
};

async function importLyonPois() {
    console.log("Starting Lyon POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(lyonPoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(lyonPoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(lyonPoisData).length} POIs for Lyon.`);

        console.log("Lyon POIs import complete!");
    } catch (error) {
        console.error("Error during Lyon POIs import:", error);
    }
}

importLyonPois();