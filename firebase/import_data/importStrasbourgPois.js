const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR STRASBOURG ---
const strasbourgPoisData = {
  // Monuments emblématiques
  "poi_cathedrale_notre_dame": {
    "name": "Cathédrale Notre-Dame de Strasbourg",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 800,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_kammerzell": {
    "name": "Maison Kammerzell",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_place_kleber": {
    "name": "Place Kléber",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Musées et culture
  "poi_musee_alsace": {
    "name": "Musée Alsacien",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_musee_beaux_arts": {
    "name": "Musée des Beaux-Arts de Strasbourg",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_musee_tom_uhl": {
    "name": "Musée Tomi Ungerer",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Quartiers emblématiques
  "poi_petite_france": {
    "name": "Quartier de la Petite France",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_neustadt": {
    "name": "Quartier Neustadt",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_krutenau": {
    "name": "Quartier Krutenau",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Parcs et espaces naturels
  "poi_orangerie": {
    "name": "Parc de l'Orangerie",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_jardin_botanique": {
    "name": "Jardin Botanique de l'Université",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_parce_contades": {
    "name": "Parc de Contades",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Édifices religieux
  "poi_eglise_saint_thomas": {
    "name": "Église Saint-Thomas",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_eglise_saint_pierre": {
    "name": "Église Saint-Pierre-le-Jeune",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_mosquee": {
    "name": "Mosquée de Strasbourg",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Transports et infrastructures
  "poi_gare_strasbourg": {
    "name": "Gare de Strasbourg",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_aeroport_strasbourg": {
    "name": "Aéroport de Strasbourg-Entzheim",
    "location": { "latitude": 48.5492, "longitude": 7.6436 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_pont_corbeau": {
    "name": "Pont du Corbeau",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Institutions européennes
  "poi_parlement_europeen": {
    "name": "Parlement Européen",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_conseil_europe": {
    "name": "Conseil de l'Europe",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_cour_europeenne": {
    "name": "Cour Européenne des Droits de l'Homme",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Sport et loisirs
  "poi_stade_meinau": {
    "name": "Stade de la Meinau",
    "location": { "latitude": 48.5569, "longitude": 7.7656 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_rhin_europe": {
    "name": "Rhénus Sport",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture et gastronomie
  "poi_marche_gaub": {
    "name": "Marché Gaub",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_place_broglie": {
    "name": "Place Broglie",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Places et espaces publics
  "poi_place_gutenberg": {
    "name": "Place Gutenberg",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_place_republique": {
    "name": "Place de la République",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Éducation et recherche
  "poi_universite_strasbourg": {
    "name": "Université de Strasbourg",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_bibliotheque_nationale": {
    "name": "Bibliothèque Nationale et Universitaire",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture locale
  "poi_marche_noel": {
    "name": "Marché de Noël de Strasbourg",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_batorama": {
    "name": "Batorama - Visites en bateau",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  }
};

async function importStrasbourgPois() {
    console.log("Starting Strasbourg POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(strasbourgPoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(strasbourgPoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(strasbourgPoisData).length} POIs for Strasbourg.`);

        console.log("Strasbourg POIs import complete!");
    } catch (error) {
        console.error("Error during Strasbourg POIs import:", error);
    }
}

importStrasbourgPois();