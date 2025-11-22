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
    "qcm": {
      "question": "Qui a donné son nom à la Promenade des Anglais ?",
      "options": [
        "Les touristes anglais",
        "Le roi d'Angleterre",
        "Un consul anglais",
        "Un architecte anglais"
      ],
      "correctAnswerIndex": 0,
      "theme": "Tourisme et Histoire"
    }
  },
  "poi_vieux_nice": {
    "name": "Vieux Nice",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique définit le Vieux Nice ?",
      "options": [
        "Architecture moderne",
        "Ruelles colorées et marché",
        "Grands immeubles de bureaux",
        "Parcs et jardins"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Culture"
    }
  },
  "poi_colline_chateau": {
    "name": "Colline du Château",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 550,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Que peut-on voir depuis la Colline du Château ?",
      "options": [
        "Vue panoramique sur Nice",
        "Château médiéval",
        "Musée d'art",
        "Jardin botanique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Tourisme et Paysages"
    }
  },
  
  // Musées et culture
  "poi_musee_matisse": {
    "name": "Musée Matisse",
    "location": { "latitude": 43.7194, "longitude": 7.2778 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quel type de bâtiment se trouve le Musée Matisse ?",
      "options": [
        "Villa génoise",
        "Palais moderne",
        "Ancienne église",
        "Bâtiment contemporain"
      ],
      "correctAnswerIndex": 0,
      "theme": "Musées et Architecture"
    }
  },
  "poi_musee_chagall": {
    "name": "Musée National Marc Chagall",
    "location": { "latitude": 43.7208, "longitude": 7.2789 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel thème principal est représenté dans les œuvres du Musée Chagall ?",
      "options": [
        "Paysages méditerranéens",
        "Scènes bibliques",
        "Portraits",
        "Nature morte"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Art"
    }
  },
  "poi_musee_beaux_arts": {
    "name": "Musée des Beaux-Arts de Nice",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quel type de bâtiment se trouve le Musée des Beaux-Arts ?",
      "options": [
        "Palais russe",
        "Hôtel particulier",
        "Ancienne banque",
        "Villa italienne"
      ],
      "correctAnswerIndex": 0,
      "theme": "Musées et Architecture"
    }
  },
  
  // Quartiers emblématiques
  "poi_port_lympia": {
    "name": "Port Lympia",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Port Lympia ?",
      "options": [
        "Port de plaisance moderne",
        "Port historique avec voûtes",
        "Port de commerce",
        "Port militaire"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Maritime"
    }
  },
  "poi_cimiez": {
    "name": "Quartier Cimiez",
    "location": { "latitude": 43.7194, "longitude": 7.2778 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel site romain se trouve à Cimiez ?",
      "options": [
        "Arènes et thermes",
        "Forum et temple",
        "Théâtre et villas",
        "Aqueduc et nécropole"
      ],
      "correctAnswerIndex": 0,
      "theme": "Histoire et Archéologie"
    }
  },
  "poi_libération": {
    "name": "Quartier Libération",
    "location": { "latitude": 43.7069, "longitude": 7.2656 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le quartier Libération ?",
      "options": [
        "Marché aux puces",
        "Gare et commerces",
        "Quartier d'affaires",
        "Zone universitaire"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Commerce"
    }
  },
  
  // Parcs et espaces naturels
  "poi_jardin_albert_1er": {
    "name": "Jardin Albert Ier",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Jardin Albert Ier ?",
      "options": [
        "Fontaine monumentale",
        "Jardin botanique",
        "Vue sur la mer",
        "Zoo"
      ],
      "correctAnswerIndex": 0,
      "theme": "Jardins et Monuments"
    }
  },
  "poi_parce_phoenix": {
    "name": "Parc Phoenix",
    "location": { "latitude": 43.6478, "longitude": 7.2219 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Parc Phoenix ?",
      "options": [
        "Plus grand parc de Nice",
        "Serre tropicale et zoo",
        "Jardin historique",
        "Parc aquatique"
      ],
      "correctAnswerIndex": 1,
      "theme": "Nature et Loisirs"
    }
  },
  "poi_jardin_thiers": {
    "name": "Jardin des Arènes de Cimiez",
    "location": { "latitude": 43.7194, "longitude": 7.2778 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Jardin des Arènes de Cimiez ?",
      "options": [
        "Arènes romaines",
        "Jardin botanique",
        "Fontaine monumentale",
        "Statue équestre"
      ],
      "correctAnswerIndex": 0,
      "theme": "Histoire et Jardins"
    }
  },
  
  // Édifices religieux
  "poi_cathedrale_orthodoxe": {
    "name": "Cathédrale Orthodoxe Russe",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique distinctive a la Cathédrale Orthodoxe Russe ?",
      "options": [
        "Dômes colorés",
        "Clocher gothique",
        "Façade baroque",
        "Architecture moderne"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_basilique_notre_dame": {
    "name": "Basilique Notre-Dame",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise la Basilique Notre-Dame ?",
      "options": [
        "Gothique",
        "Néo-gothique",
        "Roman",
        "Byzantin"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_chapelle_misericorde": {
    "name": "Chapelle de la Miséricorde",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Chapelle de la Miséricorde ?",
      "options": [
        "Dôme baroque",
        "Façade gothique",
        "Vitraux modernes",
        "Crypte romane"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture Religieuse"
    }
  },
  
  // Transports et infrastructures
  "poi_aeroport_nice": {
    "name": "Aéroport Nice Côte d'Azur",
    "location": { "latitude": 43.6584, "longitude": 7.2158 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'aéroport de Nice ?",
      "options": [
        "Aéroport en plein centre-ville",
        "Aéroport sur remblais",
        "Plus grand aéroport de France",
        "Aéroport international uniquement"
      ],
      "correctAnswerIndex": 1,
      "theme": "Transports et Géographie"
    }
  },
  "poi_gare_nice_ville": {
    "name": "Gare de Nice-Ville",
    "location": { "latitude": 43.7069, "longitude": 7.2656 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise la Gare de Nice-Ville ?",
      "options": [
        "Moderne",
        "Art déco",
        "Classique",
        "Baroque"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Transports"
    }
  },
  "poi_port_helice": {
    "name": "Port de l'Hélice",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Port de l'Hélice ?",
      "options": [
        "Port de plaisance moderne",
        "Port historique",
        "Port de commerce",
        "Port militaire"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture Maritime"
    }
  },
  
  // Sport et loisirs
  "poi_allianz_riviera": {
    "name": "Allianz Riviera",
    "location": { "latitude": 43.6944, "longitude": 7.2194 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel club de football joue à l'Allianz Riviera ?",
      "options": [
        "PSG",
        "Lyon",
        "Marseille",
        "Nice"
      ],
      "correctAnswerIndex": 3,
      "theme": "Sport"
    }
  },
  "poi_patinoire_jean_bouin": {
    "name": "Patinoire Jean Bouin",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Patinoire Jean Bouin ?",
      "options": [
        "Plus grande patinoire de France",
        "Patinoire olympique",
        "Patinoire en plein air",
        "Patinoire historique"
      ],
      "correctAnswerIndex": 1,
      "theme": "Sports et Loisirs"
    }
  },
  
  // Culture et gastronomie
  "poi_marche_aux_fleurs": {
    "name": "Marché aux Fleurs",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Où se trouve le célèbre Marché aux Fleurs ?",
      "options": [
        "Cours Saleya",
        "Place Masséna",
        "Promenade des Anglais",
        "Port Lympia"
      ],
      "correctAnswerIndex": 0,
      "theme": "Commerce et Culture"
    }
  },
  "poi_cours_saleyas": {
    "name": "Cours Saleya",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Cours Saleya ?",
      "options": [
        "Marché quotidien et restaurants",
        "Architecture moderne",
        "Centre commercial",
        "Quartier d'affaires"
      ],
      "correctAnswerIndex": 0,
      "theme": "Commerce et Gastronomie"
    }
  },
  
  // Plages et bord de mer
  "poi_plage_ponchettes": {
    "name": "Plage des Ponchettes",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Plage des Ponchettes ?",
      "options": [
        "Plage privée",
        "Plage publique près du Vieux Nice",
        "Plage sauvage",
        "Plage de galets uniquement"
      ],
      "correctAnswerIndex": 1,
      "theme": "Tourisme et Nature"
    }
  },
  "poi_plage_carras": {
    "name": "Plage Carras",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique a la Plage Carras ?",
      "options": [
        "Plage familiale avec équipements",
        "Plage naturiste",
        "Plage de surf",
        "Plage historique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Tourisme et Loisirs"
    }
  },
  
  // Places et espaces publics
  "poi_place_massena": {
    "name": "Place Masséna",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Place Masséna ?",
      "options": [
        "Fontaine et statues colorées",
        "Architecture historique",
        "Marché quotidien",
        "Jardin botanique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture et Urbanisme"
    }
  },
  "poi_place_garibaldi": {
    "name": "Place Garibaldi",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Qui a donné son nom à la Place Garibaldi ?",
      "options": [
        "Un artiste niçois",
        "Un révolutionnaire italien",
        "Un roi de France",
        "Un architecte local"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Urbanisme"
    }
  },
  
  // Éducation et recherche
  "poi_universite_nice": {
    "name": "Université Côte d'Azur",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Université Côte d'Azur ?",
      "options": [
        "Université spécialisée",
        "Grande université pluridisciplinaire",
        "Institut de recherche uniquement",
        "Université internationale"
      ],
      "correctAnswerIndex": 1,
      "theme": "Éducation"
    }
  },
  "poi_observatoire_nice": {
    "name": "Observatoire de Nice",
    "location": { "latitude": 43.7292, "longitude": 7.2956 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Observatoire de Nice ?",
      "options": [
        "Plus vieil observatoire de France",
        "Observatoire astronomique moderne",
        "Planétarium",
        "Musée de l'espace"
      ],
      "correctAnswerIndex": 1,
      "theme": "Science et Recherche"
    }
  },
  
  // Culture locale
  "poi_opera_nice": {
    "name": "Opéra de Nice",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise l'Opéra de Nice ?",
      "options": [
        "Gothique",
        "Art déco",
        "Italien et classique",
        "Moderne"
      ],
      "correctAnswerIndex": 2,
      "theme": "Architecture et Spectacles"
    }
  },
  "poi_theatre_nice": {
    "name": "Théâtre National de Nice",
    "location": { "latitude": 43.6958, "longitude": 7.2710 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Théâtre National de Nice ?",
      "options": [
        "Architecture historique",
        "Bâtiment moderne",
        "Plus ancien théâtre de France",
        "Théâtre en plein air"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Spectacles"
    }
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