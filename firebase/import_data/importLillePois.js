const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR LILLE ---
const lillePoisData = {
  // Monuments emblématiques
  "poi_grand_place": {
    "name": "Grand'Place de Lille",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_vielle_bourse": {
    "name": "Vieille Bourse",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 550,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_porte_paris": {
    "name": "Porte de Paris",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Musées et culture
  "poi_palais_beaux_arts": {
    "name": "Palais des Beaux-Arts de Lille",
    "location": { "latitude": 50.6328, "longitude": 3.0583 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_musee_art_moderne": {
    "name": "Musée d'Art Moderne et Contemporain",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_hospice_comtesse": {
    "name": "Hospice Comtesse",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Quartiers emblématiques
  "poi_vieux_lille": {
    "name": "Vieux Lille",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_wazemmes": {
    "name": "Quartier Wazemmes",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_esquermes": {
    "name": "Quartier Esquermes",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Parcs et espaces naturels
  "poi_parque_citadelle": {
    "name": "Parc de la Citadelle",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_jardin_vauban": {
    "name": "Jardin Vauban",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_parque_heron": {
    "name": "Parc d'Héron",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Édifices religieux
  "poi_cathedrale_treille": {
    "name": "Cathédrale Notre-Dame-de-la-Treille",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_eglise_saint_maurice": {
    "name": "Église Saint-Maurice",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_eglise_saint_catherine": {
    "name": "Église Sainte-Catherine",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Transports et infrastructures
  "poi_gare_lille_flandres": {
    "name": "Gare Lille-Flandres",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_gare_lille_europe": {
    "name": "Gare Lille-Europe",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_aeroport_lille": {
    "name": "Aéroport de Lille-Lesquin",
    "location": { "latitude": 50.5678, "longitude": 3.0876 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Sport et loisirs
  "poi_stade_pierre_mauroy": {
    "name": "Stade Pierre-Mauroy",
    "location": { "latitude": 50.6128, "longitude": 3.0132 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_piscine_olympique": {
    "name": "Piscine Olympique",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture et gastronomie
  "poi_marche_wazemmes": {
    "name": "Marché de Wazemmes",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_maison_folle": {
    "name": "Maison Folie de Wazemmes",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Places et espaces publics
  "poi_place_republique": {
    "name": "Place de la République",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_place_rihour": {
    "name": "Place Rihour",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Éducation et recherche
  "poi_universite_lille": {
    "name": "Université de Lille",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_catho_lille": {
    "name": "Université Catholique de Lille",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture locale
  "poi_semaine_flandrienne": {
    "name": "Siège de la Semaine Flandrienne",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_brigade_nord": {
    "name": "Brigade du Nord",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 150,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  }
};

async function importLillePois() {
    console.log("Starting Lille POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(lillePoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(lillePoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(lillePoisData).length} POIs for Lille.`);

        console.log("Lille POIs import complete!");
    } catch (error) {
        console.error("Error during Lille POIs import:", error);
    }
}

importLillePois();