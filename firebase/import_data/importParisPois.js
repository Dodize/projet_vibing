const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

// --- POIS DATA FOR PARIS ---
const parisPoisData = {
  // Monuments emblématiques
  "poi_tour_eiffel": {
    "name": "Tour Eiffel",
    "location": { "latitude": 48.8584, "longitude": 2.2945 },
    "ownerTeamId": null,
    "currentScore": 800,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "En quelle année la Tour Eiffel a-t-elle été construite ?",
      "options": [
        "1789",
        "1889",
        "1900",
        "1925"
      ],
      "correctAnswerIndex": 1,
      "theme": "Monuments et Histoire"
    }
  },
  "poi_arc_triomphe": {
    "name": "Arc de Triomphe",
    "location": { "latitude": 48.8738, "longitude": 2.2950 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel empereur a ordonné la construction de l'Arc de Triomphe ?",
      "options": [
        "Jules César",
        "Napoléon Ier",
        "Napoléon III",
        "Charlemagne"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Monuments"
    }
  },
  "poi_notre_dame": {
    "name": "Cathédrale Notre-Dame de Paris",
    "location": { "latitude": 48.8530, "longitude": 2.3499 },
    "ownerTeamId": null,
    "currentScore": 700,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural principal caractérise Notre-Dame ?",
      "options": [
        "Gothique",
        "Roman",
        "Baroque",
        "Renaissance"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture Religieuse"
    }
  },
  
  // Musées prestigieux
  "poi_louvre": {
    "name": "Musée du Louvre",
    "location": { "latitude": 48.8606, "longitude": 2.3376 },
    "ownerTeamId": null,
    "currentScore": 750,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle célèbre œuvre est exposée au Louvre ?",
      "options": [
        "Les Demoiselles d'Avignon",
        "La Joconde",
        "Les Tournesols",
        "Le Cri"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Art"
    }
  },
  "poi_musee_orsay": {
    "name": "Musée d'Orsay",
    "location": { "latitude": 48.8600, "longitude": 2.3266 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type d'art est principalement exposé au Musée d'Orsay ?",
      "options": [
        "Art contemporain",
        "Art moderne et impressionniste",
        "Art médiéval",
        "Art égyptien"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Art"
    }
  },
  "poi_centre_pompidou": {
    "name": "Centre Pompidou",
    "location": { "latitude": 48.8606, "longitude": 2.3525 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise le Centre Pompidou ?",
      "options": [
        "Haussmannien",
        "Art déco",
        "High-tech",
        "Classique"
      ],
      "correctAnswerIndex": 2,
      "theme": "Architecture et Musées"
    }
  },
  
  // Quartiers célèbres
  "poi_montmartre": {
    "name": "Quartier Montmartre",
    "location": { "latitude": 48.8867, "longitude": 2.3431 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle célèbre basilique se trouve à Montmartre ?",
      "options": [
        "Sacré-Cœur",
        "Notre-Dame",
        "Sainte-Chapelle",
        "Panthéon"
      ],
      "correctAnswerIndex": 0,
      "theme": "Quartiers et Culture"
    }
  },
  "poi_quartier_latin": {
    "name": "Quartier Latin",
    "location": { "latitude": 48.8519, "longitude": 2.3421 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle célèbre université se trouve dans le Quartier Latin ?",
      "options": [
        "Stanford",
        "Oxford",
        "La Sorbonne",
        "Harvard"
      ],
      "correctAnswerIndex": 2,
      "theme": "Éducation et Quartiers"
    }
  },
  "poi_le_marais": {
    "name": "Quartier du Marais",
    "location": { "latitude": 48.8566, "longitude": 2.3614 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type d'architecture est typique du Marais ?",
      "options": [
        "Haussmannien",
        "Médiéval et Renaissance",
        "Art déco",
        "Contemporain"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Quartiers"
    }
  },
  
  // Places et jardins
  "poi_place_vendome": {
    "name": "Place Vendôme",
    "location": { "latitude": 48.8676, "longitude": 2.3285 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de commerce est célèbre sur la Place Vendôme ?",
      "options": [
        "Librairies",
        "Bijouteries de luxe",
        "Restaurants gastronomiques",
        "Galeries d'art"
      ],
      "correctAnswerIndex": 1,
      "theme": "Commerce et Urbanisme"
    }
  },
  "poi_place_vosges": {
    "name": "Place des Vosges",
    "location": { "latitude": 48.8555, "longitude": 2.3658 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel écrivain célèbre a habité sur la Place des Vosges ?",
      "options": [
        "Victor Hugo",
        "Marcel Proust",
        "Albert Camus",
        "Jean-Paul Sartre"
      ],
      "correctAnswerIndex": 0,
      "theme": "Littérature et Histoire"
    }
  },
  "poi_jardin_tuileries": {
    "name": "Jardin des Tuileries",
    "location": { "latitude": 48.8635, "longitude": 2.3275 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel musée se trouve à l'extrémité des Tuileries ?",
      "options": [
        "Musée d'Orsay",
        "Louvre",
        "Centre Pompidou",
        "Musée Rodin"
      ],
      "correctAnswerIndex": 1,
      "theme": "Jardins et Musées"
    }
  },
  "poi_jardin_luxembourg": {
    "name": "Jardin du Luxembourg",
    "location": { "latitude": 48.8462, "longitude": 2.3372 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle institution politique se trouve près du Jardin du Luxembourg ?",
      "options": [
        "Élysée",
        "Matignon",
        "Sénat",
        "Assemblée Nationale"
      ],
      "correctAnswerIndex": 2,
      "theme": "Politique et Jardins"
    }
  },
  
  // Monuments religieux
  "poi_sainte_chapelle": {
    "name": "Sainte-Chapelle",
    "location": { "latitude": 48.8535, "longitude": 2.3452 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique rend la Sainte-Chapelle célèbre ?",
      "options": [
        "Son orgue",
        "Ses vitraux",
        "Son clocher",
        "Sa crypte"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_panthéon": {
    "name": "Panthéon",
    "location": { "latitude": 48.8461, "longitude": 2.3456 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de personnage est inhumé au Panthéon ?",
      "options": [
        "Rois",
        "Grands personnages de la nation",
        "Artistes uniquement",
        "Militaires uniquement"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire et Mémoire"
    }
  },
  
  // Transports et infrastructures
  "poi_gare_nord": {
    "name": "Gare du Nord",
    "location": { "latitude": 48.8809, "longitude": 2.3553 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle destination internationale principale dessert la Gare du Nord ?",
      "options": [
        "Rome",
        "Londres",
        "Berlin",
        "Madrid"
      ],
      "correctAnswerIndex": 1,
      "theme": "Transports"
    }
  },
  "poi_gare_lazare": {
    "name": "Gare Saint-Lazare",
    "location": { "latitude": 48.8760, "longitude": 2.3256 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel peintre a immortalisé la Gare Saint-Lazare ?",
      "options": [
        "Van Gogh",
        "Monet",
        "Manet",
        "Renoir"
      ],
      "correctAnswerIndex": 1,
      "theme": "Art et Transports"
    }
  },
  
  // Culture et divertissement
  "poi_opera_garnier": {
    "name": "Opéra Garnier",
    "location": { "latitude": 48.8719, "longitude": 2.3316 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise l'Opéra Garnier ?",
      "options": [
        "Gothique",
        "Baroque",
        "Néo-classique",
        "Art déco"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Spectacles"
    }
  },
  "poi_champs_elysees": {
    "name": "Avenue des Champs-Élysées",
    "location": { "latitude": 48.8658, "longitude": 2.3070 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel événement célèbre se déroule sur les Champs-Élysées ?",
      "options": [
        "Tour de France",
        "Fête de la Musique",
        "Défilé du 14 juillet",
        "Marathon de Paris"
      ],
      "correctAnswerIndex": 2,
      "theme": "Culture et Événements"
    }
  },
  
  // Îles et ponts
  "poi_ile_cite": {
    "name": "Île de la Cité",
    "location": { "latitude": 48.8549, "longitude": 2.3476 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel monument célèbre se trouve sur l'Île de la Cité ?",
      "options": [
        "Tour Eiffel",
        "Notre-Dame",
        "Arc de Triomphe",
        "Sacré-Cœur"
      ],
      "correctAnswerIndex": 1,
      "theme": "Géographie et Monuments"
    }
  },
  "poi_ile_saint_louis": {
    "name": "Île Saint-Louis",
    "location": { "latitude": 48.8525, "longitude": 2.3552 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle spécialité gastronomique est célèbre sur l'Île Saint-Louis ?",
      "options": [
        "Macarons",
        "Glace Berthillon",
        "Chocolat",
        "Fromage"
      ],
      "correctAnswerIndex": 1,
      "theme": "Gastronomie et Quartiers"
    }
  },
  
  // Éducation et culture
  "poi_college_france": {
    "name": "Collège de France",
    "location": { "latitude": 48.8500, "longitude": 2.3440 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Collège de France ?",
      "options": [
        "Cours payants",
        "Cours gratuits et ouverts à tous",
        "Diplômes uniquement",
        "Recherche uniquement"
      ],
      "correctAnswerIndex": 1,
      "theme": "Éducation et Recherche"
    }
  },
  "poi_bibliotheque_francois_mitterrand": {
    "name": "Bibliothèque François Mitterrand",
    "location": { "latitude": 48.8255, "longitude": 2.3749 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle forme caractéristique a la BNF François Mitterrand ?",
      "options": [
        "Sphérique",
        "Pyramidale",
        "Quatre tours en forme de livres ouverts",
        "Circulaire"
      ],
      "correctAnswerIndex": 2,
      "theme": "Architecture et Culture"
    }
  }
};

async function importParisPois() {
    console.log("Starting Paris POIs import...");

    try {
        // Import POIs
        const poiPromises = Object.keys(parisPoisData).map(poiId => 
            db.collection('pois').doc(poiId).set(parisPoisData[poiId])
        );
        await Promise.all(poiPromises);
        console.log(`Successfully imported ${Object.keys(parisPoisData).length} POIs for Paris.`);

        console.log("Paris POIs import complete!");
    } catch (error) {
        console.error("Error during Paris POIs import:", error);
    }
}

importParisPois();