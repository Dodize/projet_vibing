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
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle est la fonction principale du Capitole de Toulouse ?",
      "options": [
        "Musée d'art",
        "Hôtel de ville et théâtre",
        "Cathédrale",
        "Université"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Institutions"
    }
  },
  "poi_place_du_capitole": {
    "name": "Place du Capitole",
    "location": { "latitude": correctedCoords.poi_place_du_capitole.latitude, "longitude": correctedCoords.poi_place_du_capitole.longitude },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Que représente l'occitan 'Oc' dans la décoration de la place ?",
      "options": [
        "Le soleil",
        "L'eau",
        "Le 'oui' occitan",
        "Le feu"
      ],
      "correctAnswerIndex": 2,
      "theme": "Culture et Langue"
    }
  },
  "poi_basilique_saint_sernin": {
    "name": "Basilique Saint-Sernin",
    "location": { "latitude": 43.6083156, "longitude": 1.4418039 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural principal caractérise la Basilique Saint-Sernin ?",
      "options": [
        "Gothique",
        "Roman",
        "Baroque",
        "Renaissance"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_jacobins": {
    "name": "Couvent des Jacobins",
    "location": { "latitude": 43.60344, "longitude": 1.44042 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel ordre religieux occupait le Couvent des Jacobins ?",
      "options": [
        "Jésuites",
        "Dominicains",
        "Franciscains",
        "Bénédictins"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire Religieuse"
    }
  },
  "poi_pont_neuf": {
    "name": "Pont Neuf de Toulouse",
    "location": { "latitude": 43.60318, "longitude": 1.43021 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "En quelle année le Pont Neuf de Toulouse a-t-il été achevé ?",
      "options": [
        "1545",
        "1632",
        "1750",
        "1820"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Génie Civil"
    }
  },
  
  // Quartiers et monuments
  "poi_carmes": {
    "name": "Quartier des Carmes",
    "location": { "latitude": 43.59886, "longitude": 1.43896 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel marché célèbre se tient dans le quartier des Carmes ?",
      "options": [
        "Marché aux fleurs",
        "Marché aux puces",
        "Marché aux antiquités",
        "Marché bio"
      ],
      "correctAnswerIndex": 2,
      "theme": "Culture et Quartiers"
    }
  },
  "poi_augustins": {
    "name": "Musée des Augustins",
    "location": { "latitude": 43.60367, "longitude": 1.43501 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type d'art est principalement exposé au Musée des Augustins ?",
      "options": [
        "Art contemporain",
        "Art moderne",
        "Art médiéval et Renaissance",
        "Art égyptien"
      ],
      "correctAnswerIndex": 2,
      "theme": "Musées et Art"
    }
  },
  "poi_hotel_d_assezet": {
    "name": "Hôtel d'Assézat",
    "location": { "latitude": 43.60301, "longitude": 1.43718 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel célèbre académicien a habité l'Hôtel d'Assézat ?",
      "options": [
        "Voltaire",
        "Pierre de Fermat",
        "Montaigne",
        "Jean-Jacques Rousseau"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Sciences"
    }
  },
  
  // Parcs et espaces verts
  "poi_jardin_des_plantes": {
    "name": "Jardin des Plantes",
    "location": { "latitude": 43.5929, "longitude": 1.45329 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle est la superficie approximative du Jardin des Plantes ?",
      "options": [
        "3 hectares",
        "7 hectares",
        "12 hectares",
        "20 hectares"
      ],
      "correctAnswerIndex": 1,
      "theme": "Nature et Espaces Verts"
    }
  },
  "poi_jardin_royal": {
    "name": "Jardin Royal",
    "location": { "latitude": 43.60351, "longitude": 1.4298 },
    "ownerTeamId": null,
    "currentScore": 180,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel roi a donné son nom au Jardin Royal ?",
      "options": [
        "Louis XIV",
        "Louis XV",
        "Louis XVI",
        "Napoléon"
      ],
      "correctAnswerIndex": 2,
      "theme": "Histoire et Jardins"
    }
  },
  "poi_grand_rond": {
    "name": "Grand Rond",
    "location": { "latitude": 43.60778, "longitude": 1.43802 },
    "ownerTeamId": null,
    "currentScore": 150,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel musée important se trouve près du Grand Rond ?",
      "options": [
        "Musée d'Art Moderne",
        "Musée des Abattoirs",
        "Musée du Vieux Toulouse",
        "Muséum d'Histoire Naturelle"
      ],
      "correctAnswerIndex": 3,
      "theme": "Culture et Quartiers"
    }
  },
  
  // Éducation et recherche
  "poi_universite_capitole": {
    "name": "Université Toulouse Capitole",
    "location": { "latitude": 43.6071, "longitude": 1.4472 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle discipline principale est enseignée à l'Université Toulouse Capitole ?",
      "options": [
        "Sciences exactes",
        "Droit et sciences économiques",
        "Médecine",
        "Lettres et sciences humaines"
      ],
      "correctAnswerIndex": 1,
      "theme": "Éducation"
    }
  },
  "poi_paul_sabatier": {
    "name": "Université Paul Sabatier",
    "location": { "latitude": 43.56095, "longitude": 1.46685 },
    "ownerTeamId": null,
    "currentScore": 280,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel célèbre scientifique a donné son nom à l'Université Paul Sabatier ?",
      "options": [
        "Physicien nucléaire",
        "Chimiste et Prix Nobel",
        "Mathématicien",
        "Biologiste"
      ],
      "correctAnswerIndex": 1,
      "theme": "Sciences et Éducation"
    }
  },
  
  // Transports et infrastructures
  "poi_matabiau": {
    "name": "Gare Matabiau",
    "location": { "latitude": 43.61108, "longitude": 1.45327 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle grande ville française est directement reliée à Toulouse par TGV ?",
      "options": [
        "Lyon",
        "Marseille",
        "Paris",
        "Bordeaux"
      ],
      "correctAnswerIndex": 2,
      "theme": "Transports"
    }
  },
  "poi_aeroport_blagnac": {
    "name": "Aéroport Toulouse-Blagnac",
    "location": { "latitude": 43.62998, "longitude": 1.36304 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel constructeur aéronautique majeur est basé près de cet aéroport ?",
      "options": [
        "Dassault",
        "Airbus",
        "Boeing",
        "Lockheed Martin"
      ],
      "correctAnswerIndex": 1,
      "theme": "Industrie et Transports"
    }
  },
  
  // Culture et divertissement
  "poi_theatre_du_capitole": {
    "name": "Théâtre du Capitole",
    "location": { "latitude": 43.60465, "longitude": 1.44421 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de spectacle est principalement présenté au Théâtre du Capitole ?",
      "options": [
        "Pièces de théâtre classiques",
        "Opéras et ballets",
        "Concerts de rock",
        "Spectacles de rue"
      ],
      "correctAnswerIndex": 1,
      "theme": "Culture et Spectacles"
    }
  },
  "poi_zenith": {
    "name": "Zénith de Toulouse",
    "location": { "latitude": 43.582, "longitude": 1.4339 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle est la capacité approximative du Zénith de Toulouse ?",
      "options": [
        "3000 places",
        "6000 places",
        "9000 places",
        "12000 places"
      ],
      "correctAnswerIndex": 2,
      "theme": "Culture et Divertissement"
    }
  },
  "poi_abattoirs": {
    "name": "Musée des Abattoirs",
    "location": { "latitude": 43.5996, "longitude": 1.4259 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type d'art est principalement exposé aux Abattoirs ?",
      "options": [
        "Art classique",
        "Art contemporain",
        "Art médiéval",
        "Art ethnographique"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Art"
    }
  },
  
  // Sport et loisirs
  "poi_stadium_toulouse": {
    "name": "Stadium de Toulouse",
    "location": { "latitude": 43.58135, "longitude": 1.43195 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel événement sportif majeur s'est déroulé au Stadium en 1998 ?",
      "options": [
        "Finale de Ligue des Champions",
        "Match de Coupe du Monde de football",
        "Championnats d'Europe d'athlétisme",
        "Tour de France"
      ],
      "correctAnswerIndex": 1,
      "theme": "Sport"
    }
  },
  "poi_piscine_alfred_junquet": {
    "name": "Piscine Alfred Junquet",
    "location": { "latitude": 43.60582, "longitude": 1.44775 },
    "ownerTeamId": null,
    "currentScore": 150,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise la Piscine Alfred Junquet ?",
      "options": [
        "Moderne",
        "Art déco",
        "Contemporain",
        "Classique"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Sport"
    }
  },
  
  // Quartiers résidentiels et commerces
  "poi_saint_cyprien": {
    "name": "Quartier Saint-Cyprien",
    "location": { "latitude": 43.5978, "longitude": 1.4256 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel grand équipement culturel se trouve à Saint-Cyprien ?",
      "options": [
        "Médiathèque",
        "Salle de concert",
        "Théâtre",
        "Cinéma"
      ],
      "correctAnswerIndex": 0,
      "theme": "Quartiers et Équipements"
    }
  },
  "poi_minimes": {
    "name": "Quartier des Minimes",
    "location": { "latitude": minimesCorrection.latitude, "longitude": minimesCorrection.longitude },
    "ownerTeamId": null,
    "currentScore": 180,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel marché célèbre se tient dans le quartier des Minimes ?",
      "options": [
        "Marché aux puces",
        "Marché bio",
        "Grand marché du dimanche",
        "Marché aux fleurs"
      ],
      "correctAnswerIndex": 2,
      "theme": "Commerce et Quartiers"
    }
  },
  "poi_saint_michel": {
    "name": "Église Saint-Michel",
    "location": { "latitude": 43.59978, "longitude": 1.44012 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique architecturale distinctive a l'église Saint-Michel ?",
      "options": [
        "Clocher tors",
        "Façade baroque",
        "Vitraux modernes",
        "Dôme byzantin"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture Religieuse"
    }
  },
  
  // Ponts et Garonne
  "poi_pont_saint_pierre": {
    "name": "Pont Saint-Pierre",
    "location": { "latitude": 43.603, "longitude": 1.4325 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de pont est le Pont Saint-Pierre ?",
      "options": [
        "Pont en pierre",
        "Pont métallique",
        "Pont suspendu",
        "Pont levant"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Transports"
    }
  },
  "poi_pont_des_demoiselles": {
    "name": "Pont des Demoiselles",
    "location": { "latitude": 43.57893, "longitude": 1.4432 },
    "ownerTeamId": null,
    "currentScore": 180,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle légende est associée au Pont des Demoiselles ?",
      "options": [
        "Fantômes de jeunes filles",
        "Trésor caché",
        "Bataille historique",
        "Miracle religieux"
      ],
      "correctAnswerIndex": 0,
      "theme": "Légendes et Histoire"
    }
  },
  
  // Industrie et technologie
  "poi_aeroscopia": {
    "name": "Musée Aeroscopia",
    "location": { "latitude": 43.63252, "longitude": 1.36888 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel célèbre avion de ligne supersonique est exposé à Aeroscopia ?",
      "options": [
        "Boeing 747",
        "Airbus A380",
        "Concorde",
        "Mirage"
      ],
      "correctAnswerIndex": 2,
      "theme": "Aéronautique et Technologie"
    }
  },
  "poi_cite_de_l_espace": {
    "name": "Cité de l'Espace",
    "location": { "latitude": 43.5856, "longitude": 1.4987 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle réplique célèbre peut-on voir à la Cité de l'Espace ?",
      "options": [
        "Station spatiale internationale",
        "Fusée Ariane 5",
        "Lune rover",
        "Toutes les réponses"
      ],
      "correctAnswerIndex": 3,
      "theme": "Espace et Sciences"
    }
  },
  
  // Patrimoine religieux
  "poi_cathedrale_saint_etienne": {
    "name": "Cathédrale Saint-Étienne",
    "location": { "latitude": 43.6045, "longitude": 1.4502 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Pourquoi la Cathédrale Saint-Étienne a-t-elle une apparence asymétrique ?",
      "options": [
        "Erreur de construction",
        "Différentes époques de construction",
        "Dégâts de guerre",
        "Choix architectural délibéré"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_basilique_notre_dame_de_la_daurade": {
    "name": "Basilique Notre-Dame de la Daurade",
    "location": { "latitude": 43.60008, "longitude": 1.43278 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Que signifie 'Daurade' dans le nom de la basilique ?",
      "options": [
        "Or en occitan",
        "Dorure",
        "Poisson doré",
        "Soleil couchant"
      ],
      "correctAnswerIndex": 0,
      "theme": "Histoire et Langue"
    }
  },
  
  // Places et espaces publics
  "poi_place_wilson": {
    "name": "Place Wilson",
    "location": { "latitude": 43.60779, "longitude": 1.4456 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel président américain a donné son nom à cette place ?",
      "options": [
        "Woodrow Wilson",
        "Theodore Roosevelt",
        "Franklin Roosevelt",
        "Harry Truman"
      ],
      "correctAnswerIndex": 0,
      "theme": "Histoire et Urbanisme"
    }
  },
  "poi_place_esquirol": {
    "name": "Place Esquirol",
    "location": { "latitude": 43.6032, "longitude": 1.4401 },
    "ownerTeamId": null,
    "currentScore": 150,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Qui était Esquirol ?",
      "options": [
        "Maire de Toulouse",
        "Architecte",
        "Médecin aliéniste",
        "Écrivain"
      ],
      "correctAnswerIndex": 2,
      "theme": "Histoire et Médecine"
    }
  },
  
  // Marchés et commerce
  "poi_marche_victor_hugo": {
    "name": "Marché Victor Hugo",
    "location": { "latitude": 43.60562, "longitude": 1.44779 },
    "ownerTeamId": null,
    "currentScore": 180,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de produits trouve-t-on principalement au marché Victor Hugo ?",
      "options": [
        "Produits frais et locaux",
        "Antiquités",
        "Vêtements",
        "Livres"
      ],
      "correctAnswerIndex": 0,
      "theme": "Commerce et Gastronomie"
    }
  },
  "poi_marche_des_carmes": {
    "name": "Marché des Carmes",
    "location": { "latitude": 43.59886, "longitude": 1.43896 },
    "ownerTeamId": null,
    "currentScore": 160,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle spécialité régionale est célèbre dans ce marché ?",
      "options": [
        "Fromages",
        "Saucisses de Toulouse",
        "Vin local",
        "Toutes les réponses"
      ],
      "correctAnswerIndex": 3,
      "theme": "Gastronomie et Terroir"
    }
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