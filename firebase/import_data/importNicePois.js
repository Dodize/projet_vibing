const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR NICE ---
const nicePoisData = {
  // Monuments emblématiques
  "poi_promenade_anglais": {
    "name": "Promenade des Anglais",
    "location": { "latitude": 43.6889, "longitude": 7.2424 },
    "ownerTeamId": null,
    "currentScore": 700,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_vieux_nice": {
    "name": "Vieux Nice",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_colline_chateau": {
    "name": "Colline du Château",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 550,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Musées et culture
  "poi_musee_matisse": {
    "name": "Musée Matisse",
    "location": { "latitude": 43.7194, "longitude": 7.2778 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_musee_chagall": {
    "name": "Musée National Marc Chagall",
    "location": { "latitude": 43.7208, "longitude": 7.2789 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_musee_beaux_arts": {
    "name": "Musée des Beaux-Arts de Nice",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Quartiers emblématiques
  "poi_port_lympia": {
    "name": "Port Lympia",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_cimiez": {
    "name": "Quartier Cimiez",
    "location": { "latitude": 43.7194, "longitude": 7.2778 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_libération": {
    "name": "Quartier Libération",
    "location": { "latitude": 43.7069, "longitude": 7.2656 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Parcs et espaces naturels
  "poi_jardin_albert_1er": {
    "name": "Jardin Albert Ier",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_parce_phoenix": {
    "name": "Parc Phoenix",
    "location": { "latitude": 43.6478, "longitude": 7.2219 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_jardin_thiers": {
    "name": "Jardin des Arènes de Cimiez",
    "location": { "latitude": 43.7194, "longitude": 7.2778 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Édifices religieux
  "poi_cathedrale_orthodoxe": {
    "name": "Cathédrale Orthodoxe Russe",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_basilique_notre_dame": {
    "name": "Basilique Notre-Dame",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_chapelle_misericorde": {
    "name": "Chapelle de la Miséricorde",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Transports et infrastructures
  "poi_aeroport_nice": {
    "name": "Aéroport Nice Côte d'Azur",
    "location": { "latitude": 43.6584, "longitude": 7.2158 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_gare_nice_ville": {
    "name": "Gare de Nice-Ville",
    "location": { "latitude": 43.7069, "longitude": 7.2656 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_port_helice": {
    "name": "Port de l'Hélice",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Sport et loisirs
  "poi_allianz_riviera": {
    "name": "Allianz Riviera",
    "location": { "latitude": 43.6944, "longitude": 7.2194 },
    "ownerTeamId": null,
    "currentScore": 100,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_patinoire_jean_bouin": {
    "name": "Patinoire Jean Bouin",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture et gastronomie
  "poi_marche_aux_fleurs": {
    "name": "Marché aux Fleurs",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_cours_saleyas": {
    "name": "Cours Saleya",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Plages et bord de mer
  "poi_plage_ponchettes": {
    "name": "Plage des Ponchettes",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_plage_carras": {
    "name": "Plage Carras",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Places et espaces publics
  "poi_place_massena": {
    "name": "Place Masséna",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_place_garibaldi": {
    "name": "Place Garibaldi",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Éducation et recherche
  "poi_universite_nice": {
    "name": "Université Côte d'Azur",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_observatoire_nice": {
    "name": "Observatoire de Nice",
    "location": { "latitude": 43.7292, "longitude": 7.2956 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture locale
  "poi_opera_nice": {
    "name": "Opéra de Nice",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_theatre_nice": {
    "name": "Théâtre National de Nice",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  }
};

async function importNicePois() {
    console.log("Starting Nice POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(nicePoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(nicePoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(nicePoisData).length} POIs for Nice.`);

        console.log("Nice POIs import complete!");
    } catch (error) {
        console.error("Error during Nice POIs import:", error);
    }
}

importNicePois();