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
    "qcm": {
      "question": "Quelle particularité a la Cathédrale Notre-Dame de Strasbourg ?",
      "options": [
        "Plus haute cathédrale de France",
        "Plus haute tour d'Europe",
        "Plus vieil édifice religieux",
        "Plus grand orgue du monde"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_kammerzell": {
    "name": "Maison Kammerzell",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise la Maison Kammerzell ?",
      "options": [
        "Classique",
        "Renaissance allemande",
        "Art déco",
        "Moderne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Histoire"
    }
  },
  "poi_place_kleber": {
    "name": "Place Kléber",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel monument se trouve sur la Place Kléber ?",
      "options": [
        "Statue du Général Kléber",
        "Fontaine monumentale",
        "Colonne",
        "Obélisque"
      ],
      "correctAnswerIndex": 0,
      "theme": "Monuments et Places"
    }
  },
  
  // Musées et culture
  "poi_musee_alsace": {
    "name": "Musée Alsacien",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Que peut-on découvrir au Musée Alsacien ?",
      "options": [
        "Art moderne",
        "Traditions et culture alsacienne",
        "Histoire militaire",
        "Sciences naturelles"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Culture"
    }
  },
  "poi_musee_beaux_arts": {
    "name": "Musée des Beaux-Arts de Strasbourg",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quel type de bâtiment se trouve le Musée des Beaux-Arts ?",
      "options": [
        "Ancien palais épiscopal",
        "Ancienne douane",
        "Hôtel particulier",
        "Bâtiment moderne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Architecture"
    }
  },
  "poi_musee_tom_uhl": {
    "name": "Musée Tomi Ungerer",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type d'œuvre présente le Musée Tomi Ungerer ?",
      "options": [
        "Peintures classiques",
        "Illustrations et dessins satiriques",
        "Sculptures",
        "Photographies"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Art"
    }
  },
  
  // Quartiers emblématiques
  "poi_petite_france": {
    "name": "Quartier de la Petite France",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique définit le quartier de la Petite France ?",
      "options": [
        "Architecture moderne",
        "Maisons à colombages et canaux",
        "Grands immeubles de bureaux",
        "Parcs et jardins"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Architecture"
    }
  },
  "poi_neustadt": {
    "name": "Quartier Neustadt",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le quartier Neustadt ?",
      "options": [
        "Architecture médiévale",
        "Architecture wilhelmienne",
        "Gratte-ciel modernes",
        "Art déco"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Architecture"
    }
  },
  "poi_krutenau": {
    "name": "Quartier Krutenau",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique a le quartier Krutenau ?",
      "options": [
        "Quartier d'affaires",
        "Ancien quartier de pêcheurs",
        "Zone industrielle",
        "Quartier universitaire"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Histoire"
    }
  },
  
  // Parcs et espaces naturels
  "poi_orangerie": {
    "name": "Parc de l'Orangerie",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Parc de l'Orangerie ?",
      "options": [
        "Plus grand parc de Strasbourg",
        "Jardin botanique",
        "Zoo et lac",
        "Jardin historique"
      ],
      "correctAnswerIndex": 2,
      "theme": "Nature et Loisirs"
    }
  },
  "poi_jardin_botanique": {
    "name": "Jardin Botanique de l'Université",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Jardin Botanique ?",
      "options": [
        "Plus vieux jardin de France",
        "Collection de plantes rares",
        "Jardin historique",
        "Jardin méditerranéen"
      ],
      "correctAnswerIndex": 1,
      "theme": "Nature et Éducation"
    }
  },
  "poi_parce_contades": {
    "name": "Parc de Contades",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique a le Parc de Contades ?",
      "options": [
        "Jardin à la française",
        "Parc anglais",
        "Jardin botanique",
        "Parc sportif"
      ],
      "correctAnswerIndex": 0,
      "theme": "Jardins et Paysages"
    }
  },
  
  // Édifices religieux
  "poi_eglise_saint_thomas": {
    "name": "Église Saint-Thomas",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Église Saint-Thomas ?",
      "options": [
        "Plus haute tour",
        "Église protestante",
        "Cathédrale catholique",
        "Mosquée"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_eglise_saint_pierre": {
    "name": "Église Saint-Pierre-le-Jeune",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise l'Église Saint-Pierre-le-Jeune ?",
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
  "poi_mosquee": {
    "name": "Mosquée de Strasbourg",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Mosquée de Strasbourg ?",
      "options": [
        "Plus grande mosquée de France",
        "Architecture moderne",
        "Minaret historique",
        "Jardin andalou"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  
  // Transports et infrastructures
  "poi_gare_strasbourg": {
    "name": "Gare de Strasbourg",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Gare de Strasbourg ?",
      "options": [
        "Architecture historique",
        "Façade vitrée moderne",
        "Plus vieille gare de France",
        "Architecture industrielle"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Transports"
    }
  },
  "poi_aeroport_strasbourg": {
    "name": "Aéroport de Strasbourg-Entzheim",
    "location": { "latitude": 48.5492, "longitude": 7.6436 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quelle commune se trouve l'aéroport de Strasbourg ?",
      "options": [
        "Strasbourg",
        "Entzheim",
        "Kehl",
        "Haguenau"
      ],
      "correctAnswerIndex": 1,
      "theme": "Transports et Géographie"
    }
  },
  "poi_pont_corbeau": {
    "name": "Pont du Corbeau",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Pont du Corbeau ?",
      "options": [
        "Pont piétonnier historique",
        "Pont levant",
        "Pont ferroviaire",
        "Pont moderne"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture et Transports"
    }
  },
  
  // Institutions européennes
  "poi_parlement_europeen": {
    "name": "Parlement Européen",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le bâtiment du Parlement Européen ?",
      "options": [
        "Architecture historique",
        "Façade vitrée moderne",
        "Style classique",
        "Architecture baroque"
      ],
      "correctAnswerIndex": 1,
      "theme": "Politique et Architecture"
    }
  },
  "poi_conseil_europe": {
    "name": "Conseil de l'Europe",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le siège du Conseil de l'Europe ?",
      "options": [
        "Architecture moderne",
        "Palais historique",
        "Château médiéval",
        "Bâtiment contemporain"
      ],
      "correctAnswerIndex": 1,
      "theme": "Politique et Architecture"
    }
  },
  "poi_cour_europeenne": {
    "name": "Cour Européenne des Droits de l'Homme",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le bâtiment de la Cour Européenne ?",
      "options": [
        "Architecture symbolique",
        "Style classique",
        "Bâtiment historique",
        "Architecture baroque"
      ],
      "correctAnswerIndex": 0,
      "theme": "Justice et Architecture"
    }
  },
  
  // Sport et loisirs
  "poi_stade_meinau": {
    "name": "Stade de la Meinau",
    "location": { "latitude": 48.5569, "longitude": 7.7656 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel club de football joue au Stade de la Meinau ?",
      "options": [
        "PSG",
        "Lyon",
        "Strasbourg",
        "Marseille"
      ],
      "correctAnswerIndex": 2,
      "theme": "Sport"
    }
  },
  "poi_rhin_europe": {
    "name": "Rhénus Sport",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Rhénus Sport ?",
      "options": [
        "Plus grande salle de concert",
        "Palais des sports polyvalent",
        "Stade olympique",
        "Patinoire"
      ],
      "correctAnswerIndex": 1,
      "theme": "Sports et Spectacles"
    }
  },
  
  // Culture et gastronomie
  "poi_marche_gaub": {
    "name": "Marché Gaub",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Marché Gaub ?",
      "options": [
        "Marché bio uniquement",
        "Marché couvert historique",
        "Marché aux puces",
        "Marché nocturne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Commerce et Culture"
    }
  },
  "poi_place_broglie": {
    "name": "Place Broglie",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel événement célèbre se déroule sur la Place Broglie ?",
      "options": [
        "Marché de Noël",
        "Festival de musique",
        "Marché hebdomadaire",
        "Foire commerciale"
      ],
      "correctAnswerIndex": 0,
      "theme": "Culture et Événements"
    }
  },
  
  // Places et espaces publics
  "poi_place_gutenberg": {
    "name": "Place Gutenberg",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Qui a donné son nom à la Place Gutenberg ?",
      "options": [
        "Un maire de Strasbourg",
        "Inventeur de l'imprimerie",
        "Un architecte local",
        "Un écrivain alsacien"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Urbanisme"
    }
  },
  "poi_place_republique": {
    "name": "Place de la République",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Place de la République ?",
      "options": [
        "Architecture wilhelmienne",
        "Fontaine monumentale",
        "Statue équestre",
        "Jardin historique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture et Urbanisme"
    }
  },
  
  // Éducation et recherche
  "poi_universite_strasbourg": {
    "name": "Université de Strasbourg",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Université de Strasbourg ?",
      "options": [
        "Plus ancienne université de France",
        "Grande université pluridisciplinaire",
        "Université spécialisée",
        "Université internationale uniquement"
      ],
      "correctAnswerIndex": 1,
      "theme": "Éducation"
    }
  },
  "poi_bibliotheque_nationale": {
    "name": "Bibliothèque Nationale et Universitaire",
    "location": { "latitude": 48.5844, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la BNU de Strasbourg ?",
      "options": [
        "Plus grande bibliothèque de France",
        "Architecture historique",
        "Collection spécialisée",
        "Bibliothèque moderne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Culture et Éducation"
    }
  },
  
  // Culture locale
  "poi_marche_noel": {
    "name": "Marché de Noël de Strasbourg",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Marché de Noël de Strasbourg ?",
      "options": [
        "Plus ancien marché de Noël",
        "Plus grand marché de France",
        "Marché bio uniquement",
        "Marché artisanal"
      ],
      "correctAnswerIndex": 0,
      "theme": "Culture et Traditions"
    }
  },
  "poi_batorama": {
    "name": "Batorama - Visites en bateau",
    "location": { "latitude": 48.5814, "longitude": 7.7507 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a Batorama ?",
      "options": [
        "Visites touristiques en bateau",
        "Restaurant flottant",
        "Musée naval",
        "Port de plaisance"
      ],
      "correctAnswerIndex": 0,
      "theme": "Tourisme et Loisirs"
    }
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