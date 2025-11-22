const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR LILLE ---
const lillePoisData = {
  // Monuments emblématiques
  "poi_grand_place": {
    "name": "Grand'Place de Lille",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique principale a la Grand'Place de Lille ?",
      "options": [
        "Fontaine monumentale",
        "Architecture homogène du XVIIIe siècle",
        "Statue équestre",
        "Colonne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Urbanisme"
    }
  },
  "poi_vielle_bourse": {
    "name": "Vieille Bourse",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 550,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Vieille Bourse ?",
      "options": [
        "Plus haute tour de Lille",
        "Cour intérieure et fleuristes",
        "Musée d'art moderne",
        "Opéra"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Commerce"
    }
  },
  "poi_porte_paris": {
    "name": "Porte de Paris",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "En l'honneur de qui la Porte de Paris a-t-elle été construite ?",
      "options": [
        "Louis XIV",
        "Napoléon",
        "Louis XV",
        "Charles de Gaulle"
      ],
      "correctAnswerIndex": 0,
      "theme": "Histoire et Monuments"
    }
  },
  
  // Musées et culture
  "poi_palais_beaux_arts": {
    "name": "Palais des Beaux-Arts de Lille",
    "location": { "latitude": 50.6328, "longitude": 3.0583 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Palais des Beaux-Arts de Lille ?",
      "options": [
        "Plus grand musée de France",
        "Deuxième plus grand musée de France après le Louvre",
        "Musée d'art contemporain uniquement",
        "Musée d'histoire naturelle"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Art"
    }
  },
  "poi_musee_art_moderne": {
    "name": "Musée d'Art Moderne et Contemporain",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quel type de bâtiment se trouve le MAMAC ?",
      "options": [
        "Ancienne banque",
        "Ancienne usine textile",
        "Hôtel particulier",
        "Bâtiment moderne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Architecture"
    }
  },
  "poi_hospice_comtesse": {
    "name": "Hospice Comtesse",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle était la fonction originale de l'Hospice Comtesse ?",
      "options": [
        "Hôpital",
        "Hospice pour les pauvres",
        "Couvent",
        "Château"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Architecture"
    }
  },
  
  // Quartiers emblématiques
  "poi_vieux_lille": {
    "name": "Vieux Lille",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique définit le Vieux Lille ?",
      "options": [
        "Architecture moderne",
        "Ruelles pavées et maisons colorées",
        "Grands immeubles de bureaux",
        "Parcs et jardins"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Architecture"
    }
  },
  "poi_wazemmes": {
    "name": "Quartier Wazemmes",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le quartier Wazemmes ?",
      "options": [
        "Quartier d'affaires",
        "Marché populaire et multiculturalité",
        "Zone industrielle",
        "Quartier résidentiel chic"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Culture"
    }
  },
  "poi_esquermes": {
    "name": "Quartier Esquermes",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel grand équipement se trouve à Esquermes ?",
      "options": [
        "Stade",
        "Université",
        "Aéroport",
        "Port"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Éducation"
    }
  },
  
  // Parcs et espaces naturels
  "poi_parque_citadelle": {
    "name": "Parc de la Citadelle",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Parc de la Citadelle ?",
      "options": [
        "Jardin botanique",
        "Fortifications militaires et zoo",
        "Lac artificiel",
        "Jardin à la française"
      ],
      "correctAnswerIndex": 1,
      "theme": "Nature et Histoire"
    }
  },
  "poi_jardin_vauban": {
    "name": "Jardin Vauban",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique a le Jardin Vauban ?",
      "options": [
        "Vue panoramique sur Lille",
        "Jardin japonais",
        "Parc animalier",
        "Jardin botanique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Jardins et Paysages"
    }
  },
  "poi_parque_heron": {
    "name": "Parc d'Héron",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Parc d'Héron ?",
      "options": [
        "Plus grand parc de Lille",
        "Lac et activités nautiques",
        "Jardin historique",
        "Parc botanique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Nature et Loisirs"
    }
  },
  
  // Édifices religieux
  "poi_cathedrale_treille": {
    "name": "Cathédrale Notre-Dame-de-la-Treille",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la façade de la Cathédrale Notre-Dame-de-la-Treille ?",
      "options": [
        "Style gothique pur",
        "Façade moderne en marbre",
        "Architecture romane",
        "Style baroque"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_eglise_saint_maurice": {
    "name": "Église Saint-Maurice",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural principal caractérise l'Église Saint-Maurice ?",
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
  "poi_eglise_saint_catherine": {
    "name": "Église Sainte-Catherine",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Église Sainte-Catherine ?",
      "options": [
        "Plus haute tour de Lille",
        "Église en bois",
        "Façade colorée",
        "Crypte mérovingienne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  
  // Transports et infrastructures
  "poi_gare_lille_flandres": {
    "name": "Gare Lille-Flandres",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Gare Lille-Flandres ?",
      "options": [
        "Plus vieille gare de France",
        "Façade d'origine parisienne",
        "Architecture moderne",
        "Plus grande gare d'Europe"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Transports"
    }
  },
  "poi_gare_lille_europe": {
    "name": "Gare Lille-Europe",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de train dessert principalement la Gare Lille-Europe ?",
      "options": [
        "TER uniquement",
        "TGV et Eurostar",
        "Métro",
        "Tramway"
      ],
      "correctAnswerIndex": 1,
      "theme": "Transports"
    }
  },
  "poi_aeroport_lille": {
    "name": "Aéroport de Lille-Lesquin",
    "location": { "latitude": 50.5678, "longitude": 3.0876 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quelle commune se trouve l'aéroport de Lille ?",
      "options": [
        "Lille",
        "Lesquin",
        "Roubaix",
        "Tourcoing"
      ],
      "correctAnswerIndex": 1,
      "theme": "Transports et Géographie"
    }
  },
  
  // Sport et loisirs
  "poi_stade_pierre_mauroy": {
    "name": "Stade Pierre-Mauroy",
    "location": { "latitude": 50.6128, "longitude": 3.0132 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Stade Pierre-Mauroy ?",
      "options": [
        "Toit rétractable",
        "Plus grand stade de France",
        "Architecture historique",
        "Stade flottant"
      ],
      "correctAnswerIndex": 0,
      "theme": "Sport et Architecture"
    }
  },
  "poi_piscine_olympique": {
    "name": "Piscine Olympique",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise la Piscine Olympique ?",
      "options": [
        "Art déco",
        "Moderne",
        "Classique",
        "Contemporain"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture et Sport"
    }
  },
  
  // Culture et gastronomie
  "poi_marche_wazemmes": {
    "name": "Marché de Wazemmes",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel jour se tient le célèbre marché de Wazemmes ?",
      "options": [
        "Lundi",
        "Mercredi et dimanche",
        "Samedi",
        "Tous les jours"
      ],
      "correctAnswerIndex": 1,
      "theme": "Commerce et Culture"
    }
  },
  "poi_maison_folle": {
    "name": "Maison Folie de Wazemmes",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle fonction a la Maison Folie de Wazemmes ?",
      "options": [
        "Musée d'art",
        "Centre culturel et de création",
        "Bibliothèque",
        "Théâtre"
      ],
      "correctAnswerIndex": 1,
      "theme": "Culture et Spectacles"
    }
  },
  
  // Places et espaces publics
  "poi_place_republique": {
    "name": "Place de la République",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel monument se trouve sur la Place de la République ?",
      "options": [
        "Colonne",
        "Fontaine",
        "Statue équestre",
        "Obélisque"
      ],
      "correctAnswerIndex": 1,
      "theme": "Urbanisme et Monuments"
    }
  },
  "poi_place_rihour": {
    "name": "Place Rihour",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel bâtiment important se trouve sur la Place Rihour ?",
      "options": [
        "Opéra",
        "Mairie",
        "Bibliothèque",
        "Musée"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Politique"
    }
  },
  
  // Éducation et recherche
  "poi_universite_lille": {
    "name": "Université de Lille",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Université de Lille ?",
      "options": [
        "Plus ancienne université de France",
        "Campus multiple",
        "Université spécialisée",
        "Université internationale uniquement"
      ],
      "correctAnswerIndex": 1,
      "theme": "Éducation"
    }
  },
  "poi_catho_lille": {
    "name": "Université Catholique de Lille",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Université Catholique de Lille ?",
      "options": [
        "Université publique",
        "Université privée",
        "Grande école",
        "Institut de recherche"
      ],
      "correctAnswerIndex": 1,
      "theme": "Éducation"
    }
  },
  
  // Culture locale
  "poi_semaine_flandrienne": {
    "name": "Siège de la Semaine Flandrienne",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Qu'est-ce que la Semaine Flandrienne ?",
      "options": [
        "Festival de musique",
        "Événement sportif",
        "Fête foraine",
        "Fête traditionnelle"
      ],
      "correctAnswerIndex": 3,
      "theme": "Culture et Traditions"
    }
  },
  "poi_brigade_nord": {
    "name": "Brigade du Nord",
    "location": { "latitude": 50.6372, "longitude": 3.0635 },
    "ownerTeamId": null,
    "currentScore": 150,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle est la spécialité de la Brigade du Nord ?",
      "options": [
        "Chocolats",
        "Bière",
        "Fromages",
        "Gaufres"
      ],
      "correctAnswerIndex": 2,
      "theme": "Gastronomie et Commerce"
    }
  }
};

async function importLillePois() {
    console.log("Starting Lille POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(lillePoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(lillePoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(lillePoisData).length} POIs for Lille.`);

        console.log("Lille POIs import complete!");
    } catch (error) {
        console.error("Error during Lille POIs import:", error);
    }
}

importLillePois();