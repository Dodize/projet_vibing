# Issues du Projet : Jeu de Conquête Géolocalisé

Basé sur le Cahier des Charges, voici la liste des tâches à réaliser, regroupées par domaine.

## 3.1. Gestion des Utilisateurs et des Équipes
- [x] Implémenter la gestion des 5 équipes avec attribution d'une couleur unique.
- [x] Implémenter la gestion des utilisateurs avec un pseudo unique.
- [x] Implémenter la logique de sélection d'équipe lors du premier lancement (Onboarding).
- [x] Implémenter la contrainte d'équilibre des équipes : un joueur ne peut rejoindre une équipe que si elle n'est pas complète (vérification côté serveur/Firestore).
- [x] Mettre à jour l'interface de sélection d'équipe (Onboarding) pour afficher le nombre de places restantes pour chaque équipe.

## 3.2. Système de Carte et de Zones
- [ ] Implémenter l'affichage de la carte basée sur OpenStreetMap.
- [ ] Implémenter la gestion des Lieux d'Intérêt (POI) : affichage sur la carte, initialisation comme zones neutres.
- [ ] Implémenter le système de score des POI qui décroît avec le temps depuis la dernière interaction.
- [ ] Implémenter la visualisation de la couleur de l'équipe sur les zones conquises.

## 3.3. Interaction en Temps Réel et Localisation
- [ ] Implémenter l'affichage de la position de l'utilisateur sur la carte.
- [ ] Implémenter la navigation/liste des POI les plus proches de l'utilisateur.
- [ ] Implémenter la condition de visibilité du score : le score d'un POI n'est visible que lorsque l'utilisateur est physiquement présent.

## 3.4. Mécanique de Capture de Zone
- [ ] Implémenter la fonctionnalité de prise de photo du POI lorsqu'un utilisateur est présent.
- [ ] Implémenter la vérification de la photo (mécanisme à définir).
- [ ] Implémenter la gestion des commandes vocales pour les actions (« Je dépose les armes » / « Je capture la zone »).
- [ ] Implémenter le QCM (Questionnaire à Choix Multiples) associé aux POI.
- [ ] Implémenter la logique de capture de zone (succès si score utilisateur > score zone).
- [ ] Implémenter la pénalité en monnaie virtuelle en cas d'échec de capture.

## 3.5. Monnaie Virtuelle et Bonus
- [ ] Implémenter l'obtention de monnaie virtuelle via le passage aux POI.
- [ ] Implémenter l'obtention de monnaie virtuelle via le podomètre (marche).
- [ ] Implémenter l'utilisation de la monnaie virtuelle pour des bonus (figer score / augmenter score QCM).

## 4. Exigences Techniques
- [ ] Configurer le projet Android pour utiliser Firebase Firestore comme base de données (architecture *serverless*).
- [ ] Assurer la compatibilité Android et l'utilisation des fonctionnalités de localisation et de capteurs (podomètre).