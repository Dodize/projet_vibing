# Rapport de Schéma de Base de Données Firestore

Ce document décrit la structure complète de la base de données Firestore utilisée par l'application Vibing.

## 1. Collection : `teams` (Liste des Équipes Disponibles)

Cette collection contient toutes les équipes disponibles pour la sélection dans le jeu.

| Champ | Type | Description | Valeur Exemple |
| :--- | :--- | :--- | :--- |
| **Document ID** | String | Identifiant unique de l'équipe | "team_1" |
| `teamId` | String | Identifiant textuel de l'équipe | "team_1" |
| `name` | String | Nom complet de l'équipe | "Les Conquérants" |
| `colorHex` | String | Code couleur hexadécimal | "#FF0000" |

**Équipes disponibles :**
- team_1: Les Conquérants (Rouge #FF0000)
- team_2: Les Explorateurs (Bleu #0000FF)  
- team_3: Les Stratèges (Vert #00FF00)
- team_4: Les Gardiens (Orange #FFA500)

## 2. Collection : `users`

Cette collection stocke les données spécifiques à chaque utilisateur, indexées par leur UID Firebase.

| Champ | Type | Description | Valeur Exemple |
| :--- | :--- | :--- | :--- |
| **Document ID** | String | UID de l'utilisateur Firebase | "user_abc123" |
| `id` | String | Identifiant textuel de l'utilisateur | "user_abc123" |
| `username` | String | Pseudonyme de l'utilisateur | "AlphaStriker" |
| `teamId` | String | ID de l'équipe sélectionnée | "team_1" |
| `teamName` | String | Nom de l'équipe de l'utilisateur | "Les Conquérants" |
| `money` | Number | Monnaie de l'utilisateur | 500 |
| `createdAt` | Timestamp | Date de création du compte | Timestamp Firebase |

## 3. Collection : `pois` (Points d'Intérêt)

Cette collection contient tous les points d'intérêt interactifs du jeu, principalement situés à Toulouse.

| Champ | Type | Description | Valeur Exemple |
| :--- | :--- | :--- | :--- |
| **Document ID** | String | Identifiant unique du POI | "poi_capitole" |
| `name` | String | Nom du POI | "Capitole de Toulouse" |
| `location` | Object | Coordonnées géographiques | `{latitude: 43.6047, longitude: 1.4442}` |
| `location.latitude` | Number | Latitude en degrés | 43.6047 |
| `location.longitude` | Number | Longitude en degrés | 1.4442 |
| `ownerTeamId` | String/null | ID de l'équipe propriétaire | null (neutre) |
| `currentScore` | Number | Points disponibles pour ce POI | 500 |
| `captureTime` | Timestamp | Date de capture initiale | Timestamp Firebase |
| `lastUpdated` | Timestamp | Dernière mise à jour du score | Timestamp Firebase |

**Catégories de POIs disponibles :**
- Centre-ville historique (Capitole, Place du Capitole, Basilique Saint-Sernin, Couvent des Jacobins, Pont Neuf)
- Quartiers et monuments (Quartier des Carmes, Musée des Augustins, Hôtel d'Assézat)
- Parcs et espaces verts (Jardin des Plantes, Jardin Royal, Grand Rond)
- Éducation et recherche (Université Toulouse Capitole, Université Paul Sabatier)
- Transports et infrastructures (Gare Matabiau, Aéroport Toulouse-Blagnac)
- Culture et divertissement (Théâtre du Capitole, Zénith, Musée des Abattoirs)
- Sport et loisirs (Stadium de Toulouse, Piscine Alfred Junquet)
- Quartiers résidentiels et commerces (Saint-Cyprien, Minimes, Saint-Michel)
- Ponts et Garonne (Pont Saint-Pierre, Pont des Demoiselles)
- Industrie et technologie (Musée Aeroscopia, Cité de l'Espace)
- Patrimoine religieux (Cathédrale Saint-Étienne, Basilique Notre-Dame de la Daurade)
- Places et espaces publics (Place Wilson, Place Esquirol)
- Marchés et commerce (Marché Victor Hugo, Marché des Carmes)

## Relations entre les collections

- **`users.teamId`** → **`teams.teamId`** : Chaque utilisateur est lié à une équipe
- **`pois.ownerTeamId`** → **`teams.teamId`** : Chaque POI peut être contrôlé par une équipe (ou être neutre si null)

## Notes importantes

1. **Coordonnées géographiques** : Tous les POIs utilisent des coordonnées dans la zone de Toulouse (latitude ~43.6°, longitude ~1.4°)
2. **Gestion des équipes** : Les POIs neutres ont `ownerTeamId` à `null`
3. **Système de points** : Chaque POI a un `currentScore` qui peut être gagné par les équipes
4. **Questions générales** : Les questions QCM sont gérées globalement dans l'application et ne dépendent pas des POIs spécifiques
5. **Calcul dynamique du score** : Le score des POIs décrémente de 1 point par heure depuis la dernière mise à jour (`lastUpdated`)
6. **Capture de zone** : Pour capturer une zone, le joueur doit obtenir un score au QCM supérieur au score dynamique actuel de la zone
7. **Timestamps** : `captureTime` est utilisé pour la capture initiale, `lastUpdated` pour toutes les mises à jour de score
