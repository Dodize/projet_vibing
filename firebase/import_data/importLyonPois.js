const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR LYON ---
const lyonPoisData = {
  // Monuments emblématiques
  "poi_basilique_fourviere": {
    "name": "Basilique Notre-Dame de Fourvière",
    "location": { "latitude": 45.7640, "longitude": 4.8357 },
    "ownerTeamId": null,
    "currentScore": 700,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique architecturale distinctive a la Basilique Fourvière ?",
      "options": [
        "Style gothique",
        "Style byzantin et mosaïques",
        "Style roman",
        "Style moderne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_vieux_lyon": {
    "name": "Vieux Lyon",
    "location": { "latitude": 45.7625, "longitude": 4.8326 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité architecturale caractérise le Vieux Lyon ?",
      "options": [
        "Haussmannien",
        "Renaissance et traboules",
        "Art déco",
        "Contemporain"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Quartiers"
    }
  },
  "poi_place_bellecour": {
    "name": "Place Bellecour",
    "location": { "latitude": 45.7577, "longitude": 4.8327 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle statue se trouve sur la Place Bellecour ?",
      "options": [
        "Louis XIV",
        "Napoléon",
        "Victor Hugo",
        "Jean Jaurès"
      ],
      "correctAnswerIndex": 0,
      "theme": "Monuments et Places"
    }
  },
  
  // Musées et culture
  "poi_musee_beaux_arts": {
    "name": "Musée des Beaux-Arts de Lyon",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quel type de bâtiment se trouve le Musée des Beaux-Arts ?",
      "options": [
        "Ancienne abbaye",
        "Ancien couvent",
        "Ancien palais",
        "Ancienne gare"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Architecture"
    }
  },
  "poi_musee_gallo_romain": {
    "name": "Musée Gallo-Romain",
    "location": { "latitude": 45.7640, "longitude": 4.8357 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quels sites romains célèbres sont présentés dans ce musée ?",
      "options": [
        "Arènes et théâtre antique",
        "Forum et temples",
        "Aqueduc et thermes",
        "Toutes les réponses"
      ],
      "correctAnswerIndex": 3,
      "theme": "Histoire et Archéologie"
    }
  },
  "poi_confluences": {
    "name": "Musée des Confluences",
    "location": { "latitude": 45.7346, "longitude": 4.8234 },
    "ownerTeamId": null,
    "currentScore": 550,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique architecturale distinctive a le Musée des Confluences ?",
      "options": [
        "Style classique",
        "Forme de vaisseau spatial",
        "Architecture gothique",
        "Style industriel"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Musées"
    }
  },
  
  // Quartiers emblématiques
  "poi_croix_rousse": {
    "name": "Quartier Croix-Rousse",
    "location": { "latitude": 45.7734, "longitude": 4.8345 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle activité historique a fait la réputation de Croix-Rousse ?",
      "options": [
        "Viticulture",
        "Soierie et canut",
        "Métallurgie",
        "Commerce d'épices"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Quartiers"
    }
  },
  "poi_presquile": {
    "name": "Presqu'île de Lyon",
    "location": { "latitude": 45.7577, "longitude": 4.8327 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quels deux fleuves délimitent la Presqu'île ?",
      "options": [
        "Seine et Marne",
        "Rhône et Saône",
        "Garonne et Dordogne",
        "Loire et Allier"
      ],
      "correctAnswerIndex": 1,
      "theme": "Géographie et Urbanisme"
    }
  },
  "poi_part_dieu": {
    "name": "Quartier Part-Dieu",
    "location": { "latitude": 45.7616, "longitude": 4.8609 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel grand équipement se trouve à Part-Dieu ?",
      "options": [
        "Aéroport",
        "Gare TGV et centre commercial",
        "Stade",
        "Université"
      ],
      "correctAnswerIndex": 1,
      "theme": "Transports et Commerce"
    }
  },
  
  // Parcs et espaces naturels
  "poi_parque_tete_or": {
    "name": "Parc de la Tête d'Or",
    "location": { "latitude": 45.7800, "longitude": 4.8600 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Parc de la Tête d'Or ?",
      "options": [
        "Plus grand parc urbain de France",
        "Jardin botanique et zoo",
        "Lac artificiel",
        "Toutes les réponses"
      ],
      "correctAnswerIndex": 3,
      "theme": "Nature et Loisirs"
    }
  },
  "poi_parcs_miribel_jonage": {
    "name": "Parc de Miribel-Jonage",
    "location": { "latitude": 45.8167, "longitude": 4.9833 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle activité est populaire dans le Parc de Miribel-Jonage ?",
      "options": [
        "Ski",
        "Sports nautiques et baignade",
        "Escalade",
        "Golf"
      ],
      "correctAnswerIndex": 1,
      "theme": "Nature et Sports"
    }
  },
  "poi_jardin_rosa_mir": {
    "name": "Jardin Rosa Mir",
    "location": { "latitude": 45.7734, "longitude": 4.8345 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique a le Jardin Rosa Mir ?",
      "options": [
        "Jardin suspendu",
        "Jardin méditerranéen caché",
        "Jardin japonais",
        "Jardin botanique"
      ],
      "correctAnswerIndex": 1,
      "theme": "Jardins et Patrimoine"
    }
  },
  
  // Édifices religieux
  "poi_cathedrale_saint_jean": {
    "name": "Cathédrale Saint-Jean-Baptiste",
    "location": { "latitude": 45.7640, "longitude": 4.8357 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural principal caractérise la Cathédrale Saint-Jean ?",
      "options": [
        "Gothique",
        "Roman",
        "Byzantin",
        "Baroque"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_eglise_saint_nizier": {
    "name": "Église Saint-Nizier",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a l'Église Saint-Nizier ?",
      "options": [
        "Clocher tors",
        "Façade gothique et néo-gothique",
        "Dôme byzantin",
        "Vitraux modernes"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  
  // Transports et infrastructures
  "poi_gare_part_dieu": {
    "name": "Gare de Lyon Part-Dieu",
    "location": { "latitude": 45.7616, "longitude": 4.8609 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de train dessert principalement la Gare Part-Dieu ?",
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
  "poi_aeroport_lyon": {
    "name": "Aéroport Lyon-Saint Exupéry",
    "location": { "latitude": 45.7256, "longitude": 5.0811 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel écrivain et aviateur a donné son nom à l'aéroport ?",
      "options": [
        "Marcel Pagnol",
        "Antoine de Saint-Exupéry",
        "Jean Giono",
        "Albert Camus"
      ],
      "correctAnswerIndex": 1,
      "theme": "Transports et Culture"
    }
  },
  
  // Sport et loisirs
  "poi_groupama_stadium": {
    "name": "Groupama Stadium",
    "location": { "latitude": 45.7640, "longitude": 4.8357 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel club de football joue au Groupama Stadium ?",
      "options": [
        "PSG",
        "Lyon",
        "Marseille",
        "Saint-Étienne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Sport"
    }
  },
  "poi_patinage_lyon": {
    "name": "Patinoire Charlemagne",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a la Patinoire Charlemagne ?",
      "options": [
        "Plus grande patinoire de France",
        "Patinoire olympique",
        "Patinoire en plein air",
        "Patinoire historique"
      ],
      "correctAnswerIndex": 0,
      "theme": "Sports et Loisirs"
    }
  },
  
  // Culture et gastronomie
  "poi_halle_de_la_martiniere": {
    "name": "Halle de la Martinière",
    "location": { "latitude": 45.7577, "longitude": 4.8327 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle était la fonction originale de la Halle de la Martinière ?",
      "options": [
        "Marché couvert",
        "École",
        "Théâtre",
        "Bibliothèque"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Éducation"
    }
  },
  "poi_maison_canuts": {
    "name": "Maison des Canuts",
    "location": { "latitude": 45.7734, "longitude": 4.8345 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Que peut-on découvrir à la Maison des Canuts ?",
      "options": [
        "Histoire de la soierie lyonnaise",
        "Collections d'art moderne",
        "Expositions scientifiques",
        "Archives municipales"
      ],
      "correctAnswerIndex": 0,
      "theme": "Histoire et Culture"
    }
  },
  
  // Ponts et rivières
  "poi_passerelle_saint_georges": {
    "name": "Passerelle Saint-Georges",
    "location": { "latitude": 45.7640, "longitude": 4.8357 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de pont est la Passerelle Saint-Georges ?",
      "options": [
        "Pont piétonnier",
        "Pont routier",
        "Pont ferroviaire",
        "Pont levant"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture et Transports"
    }
  },
  "poi_pont_bonnevay": {
    "name": "Pont Bonnevaux",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 180,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle rivière franchit le Pont Bonnevaux ?",
      "options": [
        "Rhône",
        "Saône",
        "Isère",
        "Loire"
      ],
      "correctAnswerIndex": 1,
      "theme": "Géographie et Transports"
    }
  },
  
  // Éducation et recherche
  "poi_universite_lyon": {
    "name": "Université Claude Bernard Lyon 1",
    "location": { "latitude": 45.7847, "longitude": 4.8700 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle discipline principale est enseignée à Lyon 1 ?",
      "options": [
        "Lettres et sciences humaines",
        "Droit et économie",
        "Sciences et santé",
        "Technologie"
      ],
      "correctAnswerIndex": 2,
      "theme": "Éducation"
    }
  },
  "poi_insa_lyon": {
    "name": "INSA Lyon",
    "location": { "latitude": 45.7847, "longitude": 4.8700 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Que signifie INSA ?",
      "options": [
        "Institut National des Sciences Appliquées",
        "Institut Normal Supérieur des Arts",
        "Institut National du Sport et de l'Art",
        "Institut des Nouvelles Sciences Appliquées"
      ],
      "correctAnswerIndex": 0,
      "theme": "Éducation et Technologie"
    }
  },
  
  // Places et espaces publics
  "poi_place_terreaux": {
    "name": "Place des Terreaux",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel musée se trouve sur la Place des Terreaux ?",
      "options": [
        "Musée des Beaux-Arts",
        "Musée des Confluences",
        "Musée Gallo-Romain",
        "Musée d'Art Contemporain"
      ],
      "correctAnswerIndex": 0,
      "theme": "Culture et Urbanisme"
    }
  },
  "poi_place_jacobins": {
    "name": "Place des Jacobins",
    "location": { "latitude": 45.7678, "longitude": 4.8466 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique a la Place des Jacobins ?",
      "options": [
        "Fontaine monumentale",
        "Statue équestre",
        "Théâtre antique",
        "Marché couvert"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture et Urbanisme"
    }
  }
};

async function importLyonPois() {
    console.log("Starting Lyon POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(lyonPoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(lyonPoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(lyonPoisData).length} POIs for Lyon.`);

        console.log("Lyon POIs import complete!");
    } catch (error) {
        console.error("Error during Lyon POIs import:", error);
    }
}

importLyonPois();