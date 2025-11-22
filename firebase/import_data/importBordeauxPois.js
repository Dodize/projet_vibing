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
    "qcm": {
      "question": "Quelle caractéristique principale a la Place de la Bourse ?",
      "options": [
        "Fontaine monumentale",
        "Miroir d'eau",
        "Statue équestre",
        "Colonne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Urbanisme"
    }
  },
  "poi_grosse_cloche": {
    "name": "Grosse Cloche",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle était la fonction originale de la Grosse Cloche ?",
      "options": [
        "Clocher d'église",
        "Beffroi municipal",
        "Tour de guet",
        "Horloge publique"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Monuments"
    }
  },
  "poi_port_cailhau": {
    "name": "Porte Cailhau",
    "location": { "latitude": 44.8394, "longitude": -0.5722 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle était la fonction de la Porte Cailhau ?",
      "options": [
        "Porte de ville fortifiée",
        "Entrée de château",
        "Porte de monastère",
        "Porte de marché"
      ],
      "correctAnswerIndex": 0,
      "theme": "Histoire et Architecture"
    }
  },
  
  // Musées et culture
  "poi_musee_aquitaine": {
    "name": "Musée d'Aquitaine",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle période historique couvre principalement le Musée d'Aquitaine ?",
      "options": [
        "Antiquité uniquement",
        "Moyen Âge uniquement",
        "Préhistoire à nos jours",
        "Époque contemporaine uniquement"
      ],
      "correctAnswerIndex": 2,
      "theme": "Musées et Histoire"
    }
  },
  "poi_musee_beaux_arts": {
    "name": "Musée des Beaux-Arts de Bordeaux",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quel type de bâtiment se trouve le Musée des Beaux-Arts ?",
      "options": [
        "Ancien couvent",
        "Hôtel particulier",
        "Ancienne gare",
        "Palais municipal"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Architecture"
    }
  },
  "poi_cite_du_vin": {
    "name": "Cité du Vin",
    "location": { "latitude": 44.8467, "longitude": -0.5606 },
    "ownerTeamId": null,
    "currentScore": 650,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique architecturale distinctive a la Cité du Vin ?",
      "options": [
        "Style classique",
        "Forme de vin tournant",
        "Architecture gothique",
        "Style industriel"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Culture"
    }
  },
  
  // Quartiers emblématiques
  "poi_quartier_saint_pierre": {
    "name": "Quartier Saint-Pierre",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique définit le quartier Saint-Pierre ?",
      "options": [
        "Quartier d'affaires",
        "Quartier historique et gastronomique",
        "Quartier universitaire",
        "Quartier portuaire"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Culture"
    }
  },
  "poi_chartrons": {
    "name": "Quartier des Chartrons",
    "location": { "latitude": 44.8467, "longitude": -0.5606 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle activité historique a fait la réputation des Chartrons ?",
      "options": [
        "Textile",
        "Commerce du vin",
        "Métallurgie",
        "Construction navale"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Quartiers"
    }
  },
  "poi_saint_michel": {
    "name": "Quartier Saint-Michel",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le quartier Saint-Michel ?",
      "options": [
        "Marché aux puces",
        "Basilique et marché",
        "Quartier asiatique",
        "Zone commerciale moderne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Culture"
    }
  },
  
  // Parcs et espaces naturels
  "poi_jardin_public": {
    "name": "Jardin Public",
    "location": { "latitude": 44.8467, "longitude": -0.5606 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style de jardin caractérise le Jardin Public ?",
      "options": [
        "Jardin à la française",
        "Jardin anglais",
        "Jardin méditerranéen",
        "Jardin japonais"
      ],
      "correctAnswerIndex": 0,
      "theme": "Jardins et Nature"
    }
  },
  "poi_parc_bordelais": {
    "name": "Parc Bordelais",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Parc Bordelais ?",
      "options": [
        "Plus grand parc de la ville",
        "Parc botanique",
        "Parc animalier",
        "Jardin historique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Nature et Loisirs"
    }
  },
  "poi_jardin_botanique": {
    "name": "Jardin Botanique",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Où se trouve le Jardin Botanique de Bordeaux ?",
      "options": [
        "Jardin Public",
        "Place de la Bourse",
        "Quartier des Chartrons",
        "Cité du Vin"
      ],
      "correctAnswerIndex": 0,
      "theme": "Nature et Culture"
    }
  },
  
  // Édifices religieux
  "poi_cathedrale_saint_andre": {
    "name": "Cathédrale Saint-André",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural principal caractérise la Cathédrale Saint-André ?",
      "options": [
        "Gothique",
        "Roman",
        "Baroque",
        "Néo-classique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_basilique_saint_seurin": {
    "name": "Basilique Saint-Seurin",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Basilique Saint-Seurin ?",
      "options": [
        "Crypte mérovingienne",
        "Clocher tors",
        "Façade baroque",
        "Vitraux modernes"
      ],
      "correctAnswerIndex": 0,
      "theme": "Histoire Religieuse"
    }
  },
  "poi_basilique_saint_michel": {
    "name": "Basilique Saint-Michel",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique distinctive a la Basilique Saint-Michel ?",
      "options": [
        "Flèche gothique",
        "Dôme byzantin",
        "Tour romane",
        "Façade classique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture Religieuse"
    }
  },
  
  // Transports et infrastructures
  "poi_gare_saint_jean": {
    "name": "Gare Saint-Jean",
    "location": { "latitude": 44.8256, "longitude": -0.5569 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de train dessert principalement la Gare Saint-Jean ?",
      "options": [
        "TER uniquement",
        "TGV et grandes lignes",
        "Métro",
        "Tramway"
      ],
      "correctAnswerIndex": 1,
      "theme": "Transports"
    }
  },
  "poi_aeroport_bordeaux": {
    "name": "Aéroport Bordeaux-Mérignac",
    "location": { "latitude": 44.8289, "longitude": -0.7156 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quelle commune se trouve l'aéroport de Bordeaux ?",
      "options": [
        "Bordeaux",
        "Mérignac",
        "Pessac",
        "Mérignac"
      ],
      "correctAnswerIndex": 1,
      "theme": "Transports et Géographie"
    }
  },
  "poi_pont_pierre": {
    "name": "Pont de Pierre",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Sous quel règne le Pont de Pierre a-t-il été construit ?",
      "options": [
        "Louis XIV",
        "Napoléon Ier",
        "Louis-Philippe",
        "Napoléon III"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Architecture"
    }
  },
  
  // Sport et loisirs
  "poi_stade_matmut_atlantique": {
    "name": "Stade Matmut Atlantique",
    "location": { "latitude": 44.8989, "longitude": -0.5678 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel club de football joue au Matmut Atlantique ?",
      "options": [
        "PSG",
        "Lyon",
        "Bordeaux",
        "Marseille"
      ],
      "correctAnswerIndex": 2,
      "theme": "Sport"
    }
  },
  "poi_patinoire_meriadeck": {
    "name": "Patinoire de Mériadeck",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quel quartier se trouve la patinoire de Mériadeck ?",
      "options": [
        "Centre-ville",
        "Quartier d'affaires",
        "Quartier portuaire",
        "Zone universitaire"
      ],
      "correctAnswerIndex": 1,
      "theme": "Sports et Urbanisme"
    }
  },
  
  // Culture et gastronomie
  "poi_marche_des_capucins": {
    "name": "Marché des Capucins",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Marché des Capucins ?",
      "options": [
        "Marché bio uniquement",
        "Plus grand marché de Bordeaux",
        "Marché nocturne",
        "Marché aux antiquités"
      ],
      "correctAnswerIndex": 1,
      "theme": "Commerce et Gastronomie"
    }
  },
  "poi_grand_theatre": {
    "name": "Grand Théâtre de Bordeaux",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise le Grand Théâtre ?",
      "options": [
        "Gothique",
        "Néo-classique",
        "Baroque",
        "Art déco"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Spectacles"
    }
  },
  
  // Places et espaces publics
  "poi_place_parlement": {
    "name": "Place du Parlement",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique a la Place du Parlement ?",
      "options": [
        "Fontaine monumentale",
        "Architecture homogène",
        "Marché quotidien",
        "Statue équestre"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Urbanisme"
    }
  },
  "poi_place_de_la_victoire": {
    "name": "Place de la Victoire",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel monument se trouve sur la Place de la Victoire ?",
      "options": [
        "Porte triomphale",
        "Colonne",
        "Statue",
        "Fontaine"
      ],
      "correctAnswerIndex": 0,
      "theme": "Monuments et Places"
    }
  },
  
  // Éducation et recherche
  "poi_universite_bordeaux": {
    "name": "Université de Bordeaux",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle discipline principale est enseignée à l'Université de Bordeaux ?",
      "options": [
        "Sciences exactes",
        "Lettres et sciences humaines",
        "Droit et économie",
        "Toutes les disciplines"
      ],
      "correctAnswerIndex": 3,
      "theme": "Éducation"
    }
  },
  "poi_sciences_po_bordeaux": {
    "name": "Sciences Po Bordeaux",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle formation principale propose Sciences Po Bordeaux ?",
      "options": [
        "Sciences exactes",
        "Sciences politiques et sociales",
        "Arts et culture",
        "Médecine"
      ],
      "correctAnswerIndex": 1,
      "theme": "Éducation"
    }
  },
  
  // Culture du vin
  "poi_maison_du_vin_bordeaux": {
    "name": "Maison du Vin de Bordeaux",
    "location": { "latitude": 44.8378, "longitude": -0.5792 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle peut-on découvrir à la Maison du Vin ?",
      "options": [
        "Dégustations et expositions",
        "Vente de vin uniquement",
        "Restaurant gastronomique",
        "Musée d'art"
      ],
      "correctAnswerIndex": 0,
      "theme": "Culture et Gastronomie"
    }
  },
  "poi_ecole_du_vin": {
    "name": "École du Vin de Bordeaux",
    "location": { "latitude": 44.8389, "longitude": -0.5778 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de formation propose l'École du Vin ?",
      "options": [
        "Formation professionnelle",
        "Initiation à l'œnologie",
        "Commerce international",
        "Viticulture"
      ],
      "correctAnswerIndex": 1,
      "theme": "Formation et Culture"
    }
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