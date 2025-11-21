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
    "qcm": {
      "question": "Quelle créature emblématique trouve-t-on aux Machines de l'Île ?",
      "options": [
        "Dragon mécanique",
        "Éléphant géant",
        "Cheval marin",
        "Toutes les réponses"
      ],
      "correctAnswerIndex": 3,
      "theme": "Culture et Innovation"
    }
  },
  "poi_chateau_ducs_bretagne": {
    "name": "Château des Ducs de Bretagne",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 700,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle fonction a le Château des Ducs de Bretagne aujourd'hui ?",
      "options": [
        "Résidence présidentielle",
        "Musée d'histoire",
        "Université",
        "Hôtel de ville"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Architecture"
    }
  },
  "poi_jules_verne": {
    "name": "Musée Jules Verne",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel lien existe entre Jules Verne et Nantes ?",
      "options": [
        "Il y est né",
        "Il y a vécu toute sa vie",
        "Il y a écrit ses œuvres",
        "Il y est enterré"
      ],
      "correctAnswerIndex": 0,
      "theme": "Littérature et Culture"
    }
  },
  
  // Musées et culture
  "poi_musee_beaux_arts": {
    "name": "Musée d'Arts de Nantes",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 550,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Musée d'Arts de Nantes ?",
      "options": [
        "Plus grand musée de France",
        "Architecture moderne et collections variées",
        "Musée en plein air",
        "Musée interactif uniquement"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Architecture"
    }
  },
  "poi_musee_histoire": {
    "name": "Musée d'Histoire de Nantes",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Où se trouve le Musée d'Histoire de Nantes ?",
      "options": [
        "Dans le Château des Ducs de Bretagne",
        "Ancienne usine",
        "Hôtel particulier",
        "Bâtiment moderne"
      ],
      "correctAnswerIndex": 0,
      "theme": "Musées et Histoire"
    }
  },
  "poi_musee_jardin_plantes": {
    "name": "Musée et Jardin des Plantes",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Jardin des Plantes de Nantes ?",
      "options": [
        "Plus vieux jardin de France",
        "Jardin botanique et galeries d'art",
        "Jardin zoologique",
        "Jardin historique uniquement"
      ],
      "correctAnswerIndex": 1,
      "theme": "Nature et Culture"
    }
  },
  
  // Quartiers emblématiques
  "poi_bouffay": {
    "name": "Quartier Bouffay",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique définit le quartier Bouffay ?",
      "options": [
        "Architecture moderne",
        "Ruelles médiévales et commerces",
        "Grands immeubles de bureaux",
        "Parcs et jardins"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Culture"
    }
  },
  "poi_graslin": {
    "name": "Quartier Graslin",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le quartier Graslin ?",
      "options": [
        "Quartier d'affaires",
        "Théâtre et commerces de luxe",
        "Zone industrielle",
        "Quartier universitaire"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Culture"
    }
  },
  "poi_madeleine": {
    "name": "Quartier de la Madeleine",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique a le quartier de la Madeleine ?",
      "options": [
        "Marché aux puces",
        "Église et commerces",
        "Quartier asiatique",
        "Zone portuaire"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Culture"
    }
  },
  
  // Parcs et espaces naturels
  "poi_jardin_plantes": {
    "name": "Jardin des Plantes",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Jardin des Plantes ?",
      "options": [
        "Plus grand parc de Nantes",
        "Jardin botanique et art",
        "Parc animalier",
        "Jardin historique"
      ],
      "correctAnswerIndex": 1,
      "theme": "Nature et Culture"
    }
  },
  "poi_parce_procure": {
    "name": "Parc de la Procure",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Parc de la Procure ?",
      "options": [
        "Vue sur la Loire",
        "Jardin botanique",
        "Parc sportif",
        "Jardin historique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Nature et Paysages"
    }
  },
  "poi_ile_versailles": {
    "name": "Île de Versailles",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Île de Versailles ?",
      "options": [
        "Jardin japonais",
        "Château historique",
        "Parc aquatique",
        "Réserve naturelle"
      ],
      "correctAnswerIndex": 0,
      "theme": "Nature et Culture"
    }
  },
  
  // Édifices religieux
  "poi_cathedrale_pierre": {
    "name": "Cathédrale Saint-Pierre-et-Saint-Paul",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Cathédrale de Nantes ?",
      "options": [
        "Plus haute tour de France",
        "Tombeau de François II",
        "Plus vieil orgue",
        "Architecture moderne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_basilique_saint_nicolas": {
    "name": "Basilique Saint-Nicolas",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise la Basilique Saint-Nicolas ?",
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
  "poi_eglise_saint_croix": {
    "name": "Église Sainte-Croix",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Église Sainte-Croix ?",
      "options": [
        "Plus vieille église de Nantes",
        "Musée d'art religieux",
        "Architecture moderne",
        "Crypte mérovingienne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  
  // Transports et infrastructures
  "poi_gare_nantes": {
    "name": "Gare de Nantes",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Gare de Nantes ?",
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
  "poi_aeroport_nantes": {
    "name": "Aéroport Nantes Atlantique",
    "location": { "latitude": 47.1536, "longitude": -1.6111 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quelle commune se trouve l'aéroport de Nantes ?",
      "options": [
        "Nantes",
        "Saint-Herblain",
        "Rezé",
        "Orvault"
      ],
      "correctAnswerIndex": 1,
      "theme": "Transports et Géographie"
    }
  },
  "poi_pont_chevir": {
    "name": "Pont de Cheviré",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Pont de Cheviré ?",
      "options": [
        "Plus long pont de France",
        "Pont levant",
        "Pont à haubans",
        "Pont historique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture et Transports"
    }
  },
  
  // Sport et loisirs
  "poi_stade_boujoire": {
    "name": "Stade de la Beaujoire",
    "location": { "latitude": 47.2569, "longitude": -1.5256 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel club de football joue à la Beaujoire ?",
      "options": [
        "PSG",
        "Lyon",
        "Nantes",
        "Marseille"
      ],
      "correctAnswerIndex": 2,
      "theme": "Sport"
    }
  },
  "poi_zenith_nantes": {
    "name": "Zénith de Nantes",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Zénith de Nantes ?",
      "options": [
        "Architecture historique",
        "Toit végétalisé",
        "Plus grande salle de concert",
        "Sphère transparente"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Spectacles"
    }
  },
  
  // Culture et gastronomie
  "poi_marche_talensac": {
    "name": "Marché de Talensac",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Marché de Talensac ?",
      "options": [
        "Marché bio uniquement",
        "Plus grand marché couvert de Nantes",
        "Marché nocturne",
        "Marché aux puces"
      ],
      "correctAnswerIndex": 1,
      "theme": "Commerce et Gastronomie"
    }
  },
  "poi_passage_pommeraye": {
    "name": "Passage Pommeraye",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Passage Pommeraye ?",
      "options": [
        "Plus vieille galerie commerciale",
        "Architecture néo-classique",
        "Centre commercial moderne",
        "Marché couvert"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Commerce"
    }
  },
  
  // Lieux symboliques
  "poi_jardin_anne_bretagne": {
    "name": "Jardin Anne de Bretagne",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Qui a donné son nom au Jardin Anne de Bretagne ?",
      "options": [
        "Reine de France",
        "Duchesse de Bretagne",
        "Impératrice",
        "Princesse locale"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Jardins"
    }
  },
  "poi_tour_bretagne": {
    "name": "Tour Bretagne",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Tour Bretagne ?",
      "options": [
        "Plus haute tour de Nantes",
        "Tour historique",
        "Tour médiévale",
        "Architecture baroque"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture et Urbanisme"
    }
  },
  
  // Places et espaces publics
  "poi_place_royale": {
    "name": "Place Royale",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Place Royale ?",
      "options": [
        "Fontaine monumentale",
        "Architecture homogène",
        "Statue équestre",
        "Jardin historique"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Urbanisme"
    }
  },
  "poi_place_graslin": {
    "name": "Place Graslin",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel monument se trouve sur la Place Graslin ?",
      "options": [
        "Théâtre Graslin",
        "Fontaine",
        "Statue",
        "Colonne"
      ],
      "correctAnswerIndex": 0,
      "theme": "Culture et Urbanisme"
    }
  },
  
  // Éducation et recherche
  "poi_universite_nantes": {
    "name": "Université de Nantes",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Université de Nantes ?",
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
  "poi_ecole_centrale": {
    "name": "École Centrale de Nantes",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle formation principale propose l'École Centrale ?",
      "options": [
        "Arts et lettres",
        "Ingénierie et sciences",
        "Médecine",
        "Droit et économie"
      ],
      "correctAnswerIndex": 1,
      "theme": "Éducation et Technologie"
    }
  },
  
  // Culture locale
  "poi_memoire_esclavage": {
    "name": "Mémoire de l'Esclavage",
    "location": { "latitude": 47.2148, "longitude": -1.5536 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Mémorial de l'Abolition de l'Esclavage ?",
      "options": [
        "Plus grand mémorial d'Europe",
        "Architecture symbolique",
        "Musée historique",
        "Parc commémoratif"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Mémoire"
    }
  },
  "poi_ile_nantes": {
    "name": "Île de Nantes",
    "location": { "latitude": 47.2075, "longitude": -1.5586 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Île de Nantes ?",
      "options": [
        "Zone industrielle",
        "Quartier culturel et artistique",
        "Zone résidentielle",
        "Quartier d'affaires"
      ],
      "correctAnswerIndex": 1,
      "theme": "Urbanisme et Culture"
    }
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