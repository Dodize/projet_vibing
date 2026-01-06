const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR NANTES ---
const nantesPoisData = {
  // Monuments emblématiques
  "poi_machines_ile": {
    "name": "Les Machines de l'Île",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 800,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_chateau_ducs_bretagne": {
    "name": "Château des Ducs de Bretagne",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 700,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_jules_verne": {
    "name": "Musée Jules Verne",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Musées et culture
  "poi_musee_beaux_arts": {
    "name": "Musée d'Arts de Nantes",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 550,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_musee_histoire": {
    "name": "Musée d'Histoire de Nantes",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_musee_jardin_plantes": {
    "name": "Musée et Jardin des Plantes",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Quartiers emblématiques
  "poi_bouffay": {
    "name": "Quartier Bouffay",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_graslin": {
    "name": "Quartier Graslin",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_madeleine": {
    "name": "Quartier de la Madeleine",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Parcs et espaces naturels
  "poi_jardin_plantes": {
    "name": "Jardin des Plantes",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_parce_procure": {
    "name": "Parc de la Procure",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_ile_versailles": {
    "name": "Île de Versailles",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Édifices religieux
  "poi_cathedrale_pierre": {
    "name": "Cathédrale Saint-Pierre-et-Saint-Paul",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_basilique_saint_nicolas": {
    "name": "Basilique Saint-Nicolas",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_eglise_saint_croix": {
    "name": "Église Sainte-Croix",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Transports et infrastructures
  "poi_gare_nantes": {
    "name": "Gare de Nantes",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_aeroport_nantes": {
    "name": "Aéroport Nantes Atlantique",
    "location": { "latitude": 47.1536, "longitude": -1.6111 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_pont_chevir": {
    "name": "Pont de Cheviré",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Sport et loisirs
  "poi_stade_boujoire": {
    "name": "Stade de la Beaujoire",
    "location": { "latitude": 47.2569, "longitude": -1.5256 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_zenith_nantes": {
    "name": "Zénith de Nantes",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture et gastronomie
  "poi_marche_talensac": {
    "name": "Marché de Talensac",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_passage_pommeraye": {
    "name": "Passage Pommeraye",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Lieux symboliques
  "poi_jardin_anne_bretagne": {
    "name": "Jardin Anne de Bretagne",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_tour_bretagne": {
    "name": "Tour Bretagne",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Places et espaces publics
  "poi_place_royale": {
    "name": "Place Royale",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_place_graslin": {
    "name": "Place Graslin",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Éducation et recherche
  "poi_universite_nantes": {
    "name": "Université de Nantes",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_ecole_centrale": {
    "name": "École Centrale de Nantes",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture locale
  "poi_memoire_esclavage": {
    "name": "Mémoire de l'Esclavage",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_ile_nantes": {
    "name": "Île de Nantes",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  }
};

async function importNantesPois() {
    console.log("Starting Nantes POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(nantesPoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(nantesPoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(nantesPoisData).length} POIs for Nantes.`);

        console.log("Nantes POIs import complete!");
    } catch (error) {
        console.error("Error during Nantes POIs import:", error);
    }
}

importNantesPois();