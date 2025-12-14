# Issues du Projet : Jeu de Conquête Géolocalisé

Basé sur le Cahier des Charges, voici la liste des tâches à réaliser, regroupées par domaine.

## 3.1. Gestion des Utilisateurs et des Équipes
- [x] Implémenter la gestion des 4 équipes avec attribution d'une couleur unique.
- [x] Implémenter la gestion des utilisateurs avec un pseudo unique.
- [x] Implémenter le choix de l'équipe lors du premier lancement de l'application (Onboarding).
- [x] Implémenter la contrainte d'équilibre des équipes : un joueur ne peut rejoindre une équipe que si elle n'est pas complète.
- [x] Mettre à jour l'interface de sélection d'équipe pour afficher le nombre de places restantes pour chaque équipe.
- [x] Initialiser la monnaie de l'utilisateur à 0 lors de la création du compte.
- [x] **CORRECTION**: Supprimer la 5ème équipe ("Les Innovateurs") et corriger l'affichage des POI qui apparaissaient comme "Neutre" au lieu de leur couleur d'équipe.
- [x] **CORRECTION**: Remplacement des noms d'équipes codés en dur dans PoiScoreFragment par les vrais noms depuis Firebase (Les Conquérants, Les Explorateurs, Les Stratèges, Les Gardiens) avec les emojis corrects.
- [x] **AMÉLIORATION**: Implémentation d'un système de cache dynamique pour charger les noms des équipes depuis Firebase en temps réel pendant l'exécution de l'application, plus de noms codés en dur.
- [x] **CORRECTION**: Implémentation de la gestion dynamique de l'argent - l'argent est maintenant chargé depuis Firebase au démarrage et sauvegardé dans Firebase quand un bonus est ajouté (commande "Je dépose les armes"). L'affichage est synchronisé entre la page principale et la page de score.

## 3.2. Système de Carte et de Zones
- [x] Implémenter l'affichage de la carte basée sur OpenStreetMap.
- [x] Implémenter la gestion des Lieux d'Intérêt (POI) : affichage sur la carte, initialisation comme zones appartenant aléatoirement à une équipe.
- [x] **CORRECTION**: Implémenter la visualisation de la couleur de l'équipe sur les zones conquises (correction du bug d'affichage "Neutre").
- [x] Implémenter le système de score des POI qui décroît avec le temps depuis la dernière interaction.

## 3.3. Interaction en Temps Réel et Localisation
- [x] Implémenter l'affichage de la position de l'utilisateur sur la carte.
- [ ] Implémenter la navigation/liste des POI les plus proches de l'utilisateur.
- [ ] Implémenter la condition de visibilité du score : le score d'un POI n'est visible que lorsque l'utilisateur est physiquement présent.

## 3.4. Mécanique de Capture de Zone
- [ ] Implémenter la fonctionnalité de prise de photo du POI lorsqu'un utilisateur est présent.
- [ ] Implémenter la vérification de la photo (mécanisme à définir).
- [x] Implémenter la gestion des commandes vocales pour les actions (« Je dépose les armes » / « Je capture la zone »).
- [ ] Implémenter le QCM (Questionnaire à Choix Multiples) associé aux POI.
- [ ] Implémenter la logique de capture de zone (succès si score utilisateur > score zone).
- [ ] Implémenter la pénalité en monnaie virtuelle en cas d'échec de capture.

## 3.5. Monnaie Virtuelle et Bonus
- [ ] Implémenter l'obtention de monnaie virtuelle via le passage aux POI.
- [ ] Implémenter l'obtention de monnaie virtuelle via le podomètre (marche).
- [ ] Implémenter l'utilisation de la monnaie virtuelle pour des bonus (figer score / augmenter score QCM).

## 4. Exigences Techniques
- [x] Configurer le projet Android pour utiliser Firebase Firestore comme base de données (architecture *serverless*).
- [ ] Assurer la compatibilité Android et l'utilisation des fonctionnalités de localisation et de capteurs (podomètre).