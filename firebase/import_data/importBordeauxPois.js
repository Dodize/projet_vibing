const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR BORDEAUX ---
const bordeauxPoisData = {
  // Monuments emblématiques
  "poi_place_bourse": {
    "name": "Place de la Bourse",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_grosse_cloche": {
    "name": "Grosse Cloche",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_port_cailhau": {
    "name": "Porte Cailhau",
    "location": { "latitude": 44.8394, "longitude": -0.5722 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Musées et culture
  "poi_musee_aquitaine": {
    "name": "Musée d'Aquitaine",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_musee_beaux_arts": {
    "name": "Musée des Beaux-Arts de Bordeaux",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_cite_du_vin": {
    "name": "Cité du Vin",
    "location": { "latitude": 44.8467, "longitude": -0.5606 },
    "ownerTeamId": null,
    "currentScore": 650,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Quartiers emblématiques
  "poi_quartier_saint_pierre": {
    "name": "Quartier Saint-Pierre",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_chartrons": {
    "name": "Quartier des Chartrons",
    "location": { "latitude": 44.8467, "longitude": -0.5606 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_saint_michel": {
    "name": "Quartier Saint-Michel",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Parcs et espaces naturels
  "poi_jardin_public": {
    "name": "Jardin Public",
    "location": { "latitude": 44.8467, "longitude": -0.5606 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_parc_bordelais": {
    "name": "Parc Bordelais",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_jardin_botanique": {
    "name": "Jardin Botanique",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Édifices religieux
  "poi_cathedrale_saint_andre": {
    "name": "Cathédrale Saint-André",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_basilique_saint_seurin": {
    "name": "Basilique Saint-Seurin",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_basilique_saint_michel": {
    "name": "Basilique Saint-Michel",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Transports et infrastructures
  "poi_gare_saint_jean": {
    "name": "Gare Saint-Jean",
    "location": { "latitude": 44.8256, "longitude": -0.5569 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_aeroport_bordeaux": {
    "name": "Aéroport Bordeaux-Mérignac",
    "location": { "latitude": 44.8289, "longitude": -0.7156 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_pont_pierre": {
    "name": "Pont de Pierre",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Sport et loisirs
  "poi_stade_matmut_atlantique": {
    "name": "Stade Matmut Atlantique",
    "location": { "latitude": 44.8989, "longitude": -0.5678 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_patinoire_meriadeck": {
    "name": "Patinoire de Mériadeck",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture et gastronomie
  "poi_marche_des_capucins": {
    "name": "Marché des Capucins",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_grand_theatre": {
    "name": "Grand Théâtre de Bordeaux",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Places et espaces publics
  "poi_place_parlement": {
    "name": "Place du Parlement",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_place_de_la_victoire": {
    "name": "Place de la Victoire",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Éducation et recherche
  "poi_universite_bordeaux": {
    "name": "Université de Bordeaux",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_sciences_po_bordeaux": {
    "name": "Sciences Po Bordeaux",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture du vin
  "poi_maison_du_vin_bordeaux": {
    "name": "Maison du Vin de Bordeaux",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_ecole_du_vin": {
    "name": "École du Vin de Bordeaux",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  }
};

async function importBordeauxPois() {
    console.log("Starting Bordeaux POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(bordeauxPoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(bordeauxPoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(bordeauxPoisData).length} POIs for Bordeaux.`);

        console.log("Bordeaux POIs import complete!");
    } catch (error) {
        console.error("Error during Bordeaux POIs import:", error);
    }
}

importBordeauxPois();