# Avancement du Projet : Jeu de Conquête Géolocalisé

## Résumé du Projet

Le projet consiste à développer une application mobile Android de jeu de conquête géolocalisé. Les joueurs rejoignent l'une des quatre équipes et s'affrontent pour prendre le contrôle de Lieux d'Intérêt (POI) réels. Le gameplay principal repose sur la présence physique des joueurs sur les lieux, où ils doivent répondre à des questionnaires (QCM) pour capturer une zone. Une monnaie virtuelle, gagnée en marchant ou en interagissant avec les POI, permet d'obtenir des avantages en jeu. L'application est conçue pour fonctionner sans serveur dédié, en s'appuyant sur Firebase Firestore pour la gestion des données.

## État d'Avancement

À ce jour, des progrès significatifs ont été réalisés sur les mécaniques de jeu principales. L'affichage de la carte via OpenStreetMap est désormais fonctionnel, et la localisation de l'utilisateur est correctement représentée en temps réel. Les fondations relatives à la gestion des joueurs et des équipes (création, sélection, équilibrage) sont également complètes.

De plus, une avancée majeure a été faite sur l'interaction avec les Lieux d'Intérêt (POI). Les commandes vocales permettant de lancer un QCM pour capturer une zone ou de "déposer les armes" pour obtenir de la monnaie virtuelle sont maintenant opérationnelles. Le système de QCM lui-même est en place, bien que les questions soient actuellement écrites en dur dans l'application.

## Prochaines Étapes

Les prochaines étapes se concentreront sur l'amélioration de l'expérience utilisateur et la finalisation des fonctionnalités de base. Actuellement, l'interaction avec un POI nécessite un clic manuel ; il faudra donc implémenter un déclenchement automatique lorsque le joueur entre dans la zone d'un POI pour rendre le jeu plus fluide.

Il reste à développer l'ensemble du système économique, notamment le gain de monnaie virtuelle via le podomètre et l'implémentation des bonus utilisables en jeu. La mécanique de prise et de vérification de photo, qui est une étape clé de la capture de zone, doit également être entièrement construite.

Il faudra également finaliser la logique de capture de zone post-QCM. Après une réponse correcte, le système devra mettre à jour le score du POI en le décrémentant et, si le score du joueur est suffisant, attribuer la zone à son équipe en changeant sa couleur sur la carte.