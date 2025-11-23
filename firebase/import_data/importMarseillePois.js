const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR MARSEILLE ---
const marseillePoisData = {
  // Monuments emblématiques
  "poi_notre_dame_garde": {
    "name": "Notre-Dame de la Garde",
    "location": { "latitude": 43.3410, "longitude": 5.3719 },
    "ownerTeamId": null,
    "currentScore": 700,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_vieux_port": {
    "name": "Vieux Port de Marseille",
    "location": { "latitude": 43.2950, "longitude": 5.3766 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_chateau_dif": {
    "name": "Château d'If",
    "location": { "latitude": 43.2799, "longitude": 5.3216 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Musées et culture
  "poi_mucem": {
    "name": "MUCEM - Musée des Civilisations",
    "location": { "latitude": 43.2956, "longitude": 5.3638 },
    "ownerTeamId": null,
    "currentScore": 550,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_palais_longchamp": {
    "name": "Palais Longchamp",
    "location": { "latitude": 43.3076, "longitude": 5.3989 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_vieille_charite": {
    "name": "La Vieille Charité",
    "location": { "latitude": 43.2955, "longitude": 5.3691 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Quartiers emblématiques
  "poi_panier": {
    "name": "Quartier du Panier",
    "location": { "latitude": 43.2958, "longitude": 5.3696 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_cours_julien": {
    "name": "Cours Julien",
    "location": { "latitude": 43.2989, "longitude": 5.3801 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_belsunce": {
    "name": "Quartier Belsunce",
    "location": { "latitude": 43.2987, "longitude": 5.3762 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Parcs et espaces naturels
  "poi_parc_borely": {
    "name": "Parc Borély",
    "location": { "latitude": 43.2744, "longitude": 5.3939 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_calanques": {
    "name": "Calanques de Marseille",
    "location": { "latitude": 43.2240, "longitude": 5.4450 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_parc_longchamp": {
    "name": "Parc Longchamp",
    "location": { "latitude": 43.3076, "longitude": 5.3989 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Édifices religieux
  "poi_cathedrale_marseille": {
    "name": "Cathédrale La Major",
    "location": { "latitude": 43.2956, "longitude": 5.3638 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_abbaye_saint_victor": {
    "name": "Abbaye Saint-Victor",
    "location": { "latitude": 43.2917, "longitude": 5.3630 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Transports et infrastructures
  "poi_gare_saint_charles": {
    "name": "Gare Saint-Charles",
    "location": { "latitude": 43.3026, "longitude": 5.3801 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_aeroport_marseille": {
    "name": "Aéroport Marseille-Provence",
    "location": { "latitude": 43.4399, "longitude": 5.2214 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Sport et loisirs
  "poi_velodrome": {
    "name": "Stade Vélodrome",
    "location": { "latitude": 43.2698, "longitude": 5.3959 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_palais_des_sports": {
    "name": "Palais des Sports",
    "location": { "latitude": 43.2678, "longitude": 5.3989 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Culture maritime
  "poi_musee_marine": {
    "name": "Musée de la Marine",
    "location": { "latitude": 43.2956, "longitude": 5.3638 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_fort_saint_jean": {
    "name": "Fort Saint-Jean",
    "location": { "latitude": 43.2956, "longitude": 5.3638 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Marchés et commerce
  "poi_marche_prado": {
    "name": "Marché du Prado",
    "location": { "latitude": 43.2845, "longitude": 5.3890 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_marche_capucins": {
    "name": "Marché des Capucins",
    "location": { "latitude": 43.2987, "longitude": 5.3762 },
    "ownerTeamId": null,
    "currentScore": 180,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  
  // Plages et bord de mer
  "poi_plage_prado": {
    "name": "Plages du Prado",
    "location": { "latitude": 43.2744, "longitude": 5.3939 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  },
  "poi_pointe_rouge": {
    "name": "Pointe Rouge",
    "location": { "latitude": 43.2678, "longitude": 5.3989 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
  }
};

async function importMarseillePois() {
    console.log("Starting Marseille POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(marseillePoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(marseillePoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(marseillePoisData).length} POIs for Marseille.`);

        console.log("Marseille POIs import complete!");
    } catch (error) {
        console.error("Error during Marseille POIs import:", error);
    }
}

importMarseillePois();