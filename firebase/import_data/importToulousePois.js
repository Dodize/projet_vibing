const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Load corrected coordinates from JSON file
const correctedCoords = require('./pois_toulouse_corrected.json');

// Special correction for Minimes (near Blagnac, west of Toulouse)
const minimesCorrection = {
    latitude: 43.6150,
    longitude: 1.3890
};

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR TOULOUSE ---
const toulousePoisData = {
  // Centre-ville historique
  "poi_capitole": {
    "name": "Capitole de Toulouse",
    "location": { "latitude": correctedCoords.poi_capitole.latitude, "longitude": correctedCoords.poi_capitole.longitude },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),

  },
  "poi_place_du_capitole": {
    "name": "Place du Capitole",
    "location": { "latitude": correctedCoords.poi_place_du_capitole.latitude, "longitude": correctedCoords.poi_place_du_capitole.longitude },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_basilique_saint_sernin": {
    "name": "Basilique Saint-Sernin",
    "location": { "latitude": 43.6083156, "longitude": 1.4418039 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_jacobins": {
    "name": "Couvent des Jacobins",
    "location": { "latitude": 43.60344, "longitude": 1.44042 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_pont_neuf": {
    "name": "Pont Neuf de Toulouse",
    "location": { "latitude": 43.60318, "longitude": 1.43021 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Quartiers et monuments
  "poi_carmes": {
    "name": "Quartier des Carmes",
    "location": { "latitude": 43.59886, "longitude": 1.43896 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_augustins": {
    "name": "Musée des Augustins",
    "location": { "latitude": 43.60367, "longitude": 1.43501 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_hotel_d_assezet": {
    "name": "Hôtel d'Assézat",
    "location": { "latitude": 43.60301, "longitude": 1.43718 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Parcs et espaces verts
  "poi_jardin_des_plantes": {
    "name": "Jardin des Plantes",
    "location": { "latitude": 43.5929, "longitude": 1.45329 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_jardin_royal": {
    "name": "Jardin Royal",
    "location": { "latitude": 43.60351, "longitude": 1.4298 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_grand_rond": {
    "name": "Grand Rond",
    "location": { "latitude": 43.60778, "longitude": 1.43802 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Éducation et recherche
  "poi_universite_capitole": {
    "name": "Université Toulouse Capitole",
    "location": { "latitude": 43.6071, "longitude": 1.4472 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_paul_sabatier": {
    "name": "Université Paul Sabatier",
    "location": { "latitude": 43.56095, "longitude": 1.46685 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Transports et infrastructures
  "poi_matabiau": {
    "name": "Gare Matabiau",
    "location": { "latitude": 43.61108, "longitude": 1.45327 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_aeroport_blagnac": {
    "name": "Aéroport Toulouse-Blagnac",
    "location": { "latitude": 43.62998, "longitude": 1.36304 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture et divertissement
  "poi_theatre_du_capitole": {
    "name": "Théâtre du Capitole",
    "location": { "latitude": 43.60465, "longitude": 1.44421 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_zenith": {
    "name": "Zénith de Toulouse",
    "location": { "latitude": 43.582, "longitude": 1.4339 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_abattoirs": {
    "name": "Musée des Abattoirs",
    "location": { "latitude": 43.5996, "longitude": 1.4259 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Sport et loisirs
  "poi_stadium_toulouse": {
    "name": "Stadium de Toulouse",
    "location": { "latitude": 43.58135, "longitude": 1.43195 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_piscine_alfred_junquet": {
    "name": "Piscine Alfred Junquet",
    "location": { "latitude": 43.60582, "longitude": 1.44775 },
    "ownerTeamId": null,
    "currentScore": 100,
"lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z"))
  },
  
  // Quartiers résidentiels et commerces
  "poi_saint_cyprien": {
    "name": "Quartier Saint-Cyprien",
    "location": { "latitude": 43.5978, "longitude": 1.4256 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_minimes": {
    "name": "Quartier des Minimes",
    "location": { "latitude": minimesCorrection.latitude, "longitude": minimesCorrection.longitude },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_saint_michel": {
    "name": "Église Saint-Michel",
    "location": { "latitude": 43.59978, "longitude": 1.44012 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Ponts et Garonne
  "poi_pont_saint_pierre": {
    "name": "Pont Saint-Pierre",
    "location": { "latitude": 43.603, "longitude": 1.4325 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_pont_des_demoiselles": {
    "name": "Pont des Demoiselles",
    "location": { "latitude": 43.57893, "longitude": 1.4432 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Industrie et technologie
  "poi_aeroscopia": {
    "name": "Musée Aeroscopia",
    "location": { "latitude": 43.63252, "longitude": 1.36888 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_cite_de_l_espace": {
    "name": "Cité de l'Espace",
    "location": { "latitude": 43.5856, "longitude": 1.4987 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Patrimoine religieux
  "poi_cathedrale_saint_etienne": {
    "name": "Cathédrale Saint-Étienne",
    "location": { "latitude": 43.6045, "longitude": 1.4502 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_basilique_notre_dame_de_la_daurade": {
    "name": "Basilique Notre-Dame de la Daurade",
    "location": { "latitude": 43.60008, "longitude": 1.43278 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Places et espaces publics
  "poi_place_wilson": {
    "name": "Place Wilson",
    "location": { "latitude": 43.60779, "longitude": 1.4456 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_place_esquirol": {
    "name": "Place Esquirol",
    "location": { "latitude": 43.6032, "longitude": 1.4401 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Marchés et commerce
  "poi_marche_victor_hugo": {
    "name": "Marché Victor Hugo",
    "location": { "latitude": 43.60562, "longitude": 1.44779 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_marche_des_carmes": {
    "name": "Marché des Carmes",
    "location": { "latitude": 43.59886, "longitude": 1.43896 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  }
};

async function importToulousePois() {
    console.log("Starting Toulouse POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(toulousePoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(toulousePoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(toulousePoisData).length} POIs for Toulouse.`);

        console.log("Toulouse POIs import complete!");
    } catch (error) {
        console.error("Error during Toulouse POIs import:", error);
    }
}

importToulousePois();