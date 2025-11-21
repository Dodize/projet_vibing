# Rapport de Schéma de Base de Données Firestore

Ce document décrit la structure de la base de données Firestore telle que déduite et utilisée pour la fonctionnalité de sélection d'équipe dans l'application.

## 1. Collection : `teams` (Liste des Équipes Disponibles)

Cette collection est supposée contenir toutes les équipes disponibles pour la sélection.

| Champ | Type | Description | Pertinence |
| :--- | :--- | :--- | :--- |
| **Document ID** | String | Identifiant unique de l'équipe (ex: "RED"). | Utilisé comme clé principale. |
| `teamId` | String | Identifiant textuel de l'équipe (redondant avec l'ID du document, mais conservé pour la cohérence des données). | Clé de référence. |
| `name` | String | Nom complet de l'équipe à afficher dans l'interface utilisateur. | Affichage dynamique. |
| `colorHex` | String | Code couleur hexadécimal associé à l'équipe. | Utilisé pour le style futur. |

**Note :** Actuellement, le chargement des données dans `TeamSelectionFragment` est simulé. Une requête Firestore réelle devrait être implémentée pour lire cette collection.

## 2. Collection : `users`

Cette collection stocke les données spécifiques à chaque utilisateur, indexées par leur UID Firebase.

| Champ | Type | Description | Pertinence |
| :--- | :--- | :--- | :--- |
| **Document ID** | String | UID de l'utilisateur Firebase. | Clé primaire de l'utilisateur. |
| `teamId` | String | L'ID de l'équipe sélectionnée par cet utilisateur (ex: "RED"). | Lien vers la collection `teams`. |
| `currency` | Number | Monnaie de l'utilisateur (initialisé à 100). | Donnée utilisateur. |
| `distanceWalked` | Long | Distance totale parcourue par l'utilisateur (initialisé à 0). | Donnée utilisateur. |

## Compte Rendu des Modifications (Sélection d'Équipe)

La fonctionnalité de sélection d'équipe a été mise à jour pour charger dynamiquement les équipes au lieu d'utiliser des valeurs codées en dur dans l'interface utilisateur et le fragment.

**Changements effectués :**
1.  **Interface Utilisateur (`fragment_team_selection.xml`) :** Les boutons codés en dur ont été remplacés par un `RecyclerView` (`recycler_view_teams`) pour afficher la liste dynamique.
2.  **Structure de Données :** Une classe interne `TeamInfo` a été introduite pour encapsuler `teamId`, `name`, et `colorHex`.
3.  **Logique du Fragment (`TeamSelectionFragment.java`) :**
    *   La Map statique des équipes codées en dur a été supprimée.
    *   Un `TeamAdapter` a été créé pour gérer l'affichage des éléments dans le `RecyclerView`.
    *   La méthode `loadTeams()` a été implémentée (actuellement simulée) pour charger les données des équipes et notifier l'adaptateur.
    *   La vérification de connexion a été temporairement commentée pour permettre le test de cette fonctionnalité sans nécessiter une connexion Firebase active.

**Prochaine Étape :** Remplacer la simulation dans `loadTeams()` par une requête Firestore réelle ciblant la collection `teams` pour obtenir les données dynamiques.
