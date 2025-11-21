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
    "qcm": {
      "question": "Quel surnom donne-t-on à Notre-Dame de la Garde ?",
      "options": [
        "La Belle",
        "La Bonne Mère",
        "La Grande Dame",
        "La Protectrice"
      ],
      "correctAnswerIndex": 1,
      "theme": "Monuments et Religion"
    }
  },
  "poi_vieux_port": {
    "name": "Vieux Port de Marseille",
    "location": { "latitude": 43.2950, "longitude": 5.3766 },
    "ownerTeamId": null,
    "currentScore": 600,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle célèbre statue se trouve au Vieux Port ?",
      "options": [
        "Le Roi René",
        "David",
        "La Vénus de Milo",
        "Le Penseur"
      ],
      "correctAnswerIndex": 0,
      "theme": "Ports et Culture"
    }
  },
  "poi_chateau_dif": {
    "name": "Château d'If",
    "location": { "latitude": 43.2799, "longitude": 5.3216 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel personnage de roman a été emprisonné au Château d'If ?",
      "options": [
        "Jean Valjean",
        "Edmond Dantès",
        "Phileas Fogg",
        "Cyrano de Bergerac"
      ],
      "correctAnswerIndex": 1,
      "theme": "Littérature et Histoire"
    }
  },
  
  // Musées et culture
  "poi_mucem": {
    "name": "MUCEM - Musée des Civilisations",
    "location": { "latitude": 43.2956, "longitude": 5.3638 },
    "ownerTeamId": null,
    "currentScore": 550,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Que signifie MUCEM ?",
      "options": [
        "Musée d'Art Contemporain",
        "Musée des Civilisations de l'Europe et de la Méditerranée",
        "Musée d'Histoire Naturelle",
        "Musée des Cultures du Monde"
      ],
      "correctAnswerIndex": 1,
      "theme": "Musées et Culture"
    }
  },
  "poi_palais_longchamp": {
    "name": "Palais Longchamp",
    "location": { "latitude": 43.3076, "longitude": 5.3989 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel musée se trouve dans le Palais Longchamp ?",
      "options": [
        "Musée d'Art Moderne",
        "Musée des Beaux-Arts",
        "Musée d'Histoire",
        "Musée de la Marine"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Musées"
    }
  },
  "poi_vieille_charite": {
    "name": "La Vieille Charité",
    "location": { "latitude": 43.2955, "longitude": 5.3691 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle était la fonction originale de la Vieille Charité ?",
      "options": [
        "Hôpital",
        "Prison",
        "Hospice pour les pauvres",
        "Caserne"
      ],
      "correctAnswerIndex": 2,
      "theme": "Histoire et Architecture"
    }
  },
  
  // Quartiers emblématiques
  "poi_panier": {
    "name": "Quartier du Panier",
    "location": { "latitude": 43.2958, "longitude": 5.3696 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique définit le quartier du Panier ?",
      "options": [
        "Rues larges et haussmanniennes",
        "Ruelles étroites et colorées",
        "Grands immeubles modernes",
        "Parcs et jardins"
      ],
      "correctAnswerIndex": 1,
      "theme": "Quartiers et Urbanisme"
    }
  },
  "poi_cours_julien": {
    "name": "Cours Julien",
    "location": { "latitude": 43.2989, "longitude": 5.3801 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le Cours Julien ?",
      "options": [
        "Architecture baroque",
        "Art urbain et street art",
        "Marché aux fleurs",
        "Opéra"
      ],
      "correctAnswerIndex": 1,
      "theme": "Culture et Quartiers"
    }
  },
  "poi_belsunce": {
    "name": "Quartier Belsunce",
    "location": { "latitude": 43.2987, "longitude": 5.3762 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique culturelle définit Belsunce ?",
      "options": [
        "Quartier asiatique",
        "Quartier maghrébin",
        "Quartier latin",
        "Quartier d'affaires"
      ],
      "correctAnswerIndex": 1,
      "theme": "Culture et Diversité"
    }
  },
  
  // Parcs et espaces naturels
  "poi_parc_borely": {
    "name": "Parc Borély",
    "location": { "latitude": 43.2744, "longitude": 5.3939 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de jardin trouve-t-on dans le Parc Borély ?",
      "options": [
        "Jardin à la française",
        "Jardin anglais et botanique",
        "Jardin japonais",
        "Jardin méditerranéen"
      ],
      "correctAnswerIndex": 1,
      "theme": "Nature et Jardins"
    }
  },
  "poi_calanques": {
    "name": "Calanques de Marseille",
    "location": { "latitude": 43.2240, "longitude": 5.4450 },
    "ownerTeamId": null,
    "currentScore": 450,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle est la caractéristique principale des calanques ?",
      "options": [
        "Forêts denses",
        "Falaises calcaires et criques",
        "Dunes de sable",
        "Volcans"
      ],
      "correctAnswerIndex": 1,
      "theme": "Nature et Géographie"
    }
  },
  "poi_parc_longchamp": {
    "name": "Parc Longchamp",
    "location": { "latitude": 43.3076, "longitude": 5.3989 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel monument se trouve au centre du Parc Longchamp ?",
      "options": [
        "Fontaine monumentale",
        "Obélisque",
        "Statue équestre",
        "Colonne"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture et Jardins"
    }
  },
  
  // Édifices religieux
  "poi_cathedrale_marseille": {
    "name": "Cathédrale La Major",
    "location": { "latitude": 43.2956, "longitude": 5.3638 },
    "ownerTeamId": null,
    "currentScore": 400,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel style architectural caractérise la Cathédrale La Major ?",
      "options": [
        "Gothique",
        "Roman",
        "Byzantin et néo-byzantin",
        "Baroque"
      ],
      "correctAnswerIndex": 2,
      "theme": "Architecture Religieuse"
    }
  },
  "poi_abbaye_saint_victor": {
    "name": "Abbaye Saint-Victor",
    "location": { "latitude": 43.2917, "longitude": 5.3630 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "De quelle époque date l'Abbaye Saint-Victor ?",
      "options": [
        "Moyen Âge",
        "Antiquité",
        "Renaissance",
        "Époque moderne"
      ],
      "correctAnswerIndex": 0,
      "theme": "Histoire Religieuse"
    }
  },
  
  // Transports et infrastructures
  "poi_gare_saint_charles": {
    "name": "Gare Saint-Charles",
    "location": { "latitude": 43.3026, "longitude": 5.3801 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique distinctive a la Gare Saint-Charles ?",
      "options": [
        "Architecture moderne",
        "Grand escalier monumental",
        "Toit vitré",
        "Façade colorée"
      ],
      "correctAnswerIndex": 1,
      "theme": "Architecture et Transports"
    }
  },
  "poi_aeroport_marseille": {
    "name": "Aéroport Marseille-Provence",
    "location": { "latitude": 43.4399, "longitude": 5.2214 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Dans quelle commune se trouve l'aéroport de Marseille ?",
      "options": [
        "Marseille",
        "Aix-en-Provence",
        "Marignane",
        "Vitrolles"
      ],
      "correctAnswerIndex": 2,
      "theme": "Transports et Géographie"
    }
  },
  
  // Sport et loisirs
  "poi_velodrome": {
    "name": "Stade Vélodrome",
    "location": { "latitude": 43.2698, "longitude": 5.3959 },
    "ownerTeamId": null,
    "currentScore": 500,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel club de football joue au Stade Vélodrome ?",
      "options": [
        "PSG",
        "Lyon",
        "Marseille",
        "Monaco"
      ],
      "correctAnswerIndex": 2,
      "theme": "Sport"
    }
  },
  "poi_palais_des_sports": {
    "name": "Palais des Sports",
    "location": { "latitude": 43.2678, "longitude": 5.3989 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique architecturale a le Palais des Sports ?",
      "options": [
        "Dôme transparent",
        "Tour cylindrique",
        "Façade pyramidale",
        "Toit en voile"
      ],
      "correctAnswerIndex": 0,
      "theme": "Architecture et Sport"
    }
  },
  
  // Culture maritime
  "poi_musee_marine": {
    "name": "Musée de la Marine",
    "location": { "latitude": 43.2956, "longitude": 5.3638 },
    "ownerTeamId": null,
    "currentScore": 300,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Où se trouve le Musée de la Marine de Marseille ?",
      "options": [
        "Fort Saint-Jean",
        "Palais Longchamp",
        "Vieux Port",
        "Notre-Dame de la Garde"
      ],
      "correctAnswerIndex": 0,
      "theme": "Culture Maritime"
    }
  },
  "poi_fort_saint_jean": {
    "name": "Fort Saint-Jean",
    "location": { "latitude": 43.2956, "longitude": 5.3638 },
    "ownerTeamId": null,
    "currentScore": 350,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle était la fonction originale du Fort Saint-Jean ?",
      "options": [
        "Palais",
        "Fortification militaire",
        "Monastère",
        "Observatoire"
      ],
      "correctAnswerIndex": 1,
      "theme": "Histoire Militaire"
    }
  },
  
  // Marchés et commerce
  "poi_marche_prado": {
    "name": "Marché du Prado",
    "location": { "latitude": 43.2845, "longitude": 5.3890 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quel type de produits trouve-t-on au marché du Prado ?",
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
  "poi_marche_capucins": {
    "name": "Marché des Capucins",
    "location": { "latitude": 43.2987, "longitude": 5.3762 },
    "ownerTeamId": null,
    "currentScore": 180,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle particularité a le marché des Capucins ?",
      "options": [
        "Marché nocturne",
        "Plus grand marché de Provence",
        "Marché bio uniquement",
        "Marché aux puces"
      ],
      "correctAnswerIndex": 1,
      "theme": "Commerce et Culture"
    }
  },
  
  // Plages et bord de mer
  "poi_plage_prado": {
    "name": "Plages du Prado",
    "location": { "latitude": 43.2744, "longitude": 5.3939 },
    "ownerTeamId": null,
    "currentScore": 250,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle caractéristique a les plages du Prado ?",
      "options": [
        "Plages sauvages",
        "Plages urbaines aménagées",
        "Plages privées uniquement",
        "Plages volcaniques"
      ],
      "correctAnswerIndex": 1,
      "theme": "Tourisme et Nature"
    }
  },
  "poi_pointe_rouge": {
    "name": "Pointe Rouge",
    "location": { "latitude": 43.2678, "longitude": 5.3989 },
    "ownerTeamId": null,
    "currentScore": 200,
    "lastUpdated": admin.firestore.Timestamp.fromDate(new Date("2025-11-14T10:00:00Z")),
    "qcm": {
      "question": "Quelle activité est populaire à la Pointe Rouge ?",
      "options": [
        "Ski nautique",
        "Plongée sous-marine",
        "Pêche",
        "Toutes les réponses"
      ],
      "correctAnswerIndex": 3,
      "theme": "Sports Nautiques"
    }
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