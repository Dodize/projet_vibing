# Etat actuel
Lors du premier lancement de l'application, l'utilisateur accède à la page de création de son compte avec un nom d'utilisateur et un choix d'équipe. Une fois inscrit, il peut commencer à jouer. 

# Modification
Sur l'interface principale, ajouter une mécanique de tutoriel permettant au joueur de comprendre le jeu. Pour cela, ajoute un pop-up sur lequel il peut défiler d'étapes en étapes. Ce pop-up doit avoir une croix permettant au joueur qui connait déjà le jeu de fermer le tutoriel. Rédige le tutoriel en respectant le formalisme suivant :
- 1ère étape (1ère fenêtre du tutoriel) : dire bienvenue au joueur et pitcher le concept (jeu de conquête où il faut posséder les zones).
- 2ème étape : rappeler à l'utilisateur son équipe et la couleur associée. Puis zoomer sur un POI aléatoire sur la carte pour montrer qu'ils sont représentés avec des points de couleur en fonction de l'équipe ayant capturé le POI.
- 3ème étape : expliquer qu'il faut se déplacer vers un POI pour lancer l'interface de capture. Deux possibilités : capturer la zone en remplissant plusieurs épreuves, ou déposer les armes ce qui permet d'obtenir de la monnaie virtuelle, mais pas de capturer la zone. Il faut regarder le score pour prendre la décision car il faut battre le score de la zone pour la capturer. Avec le temps qui passe, le score décrémente.
- 4ème étape : expliquer dans le cas où on souhaite capturer la zone : il faudra d'abord valider l'arrivée au POI en prenant une photo du POI. Ensuite, il faudra répondre à un quiz, et avoir suffisamment de points pour capturer la zone.
- 5ème étape : faire un zoom sur la partie argent pour indiquer que faire 20 pas fait incrémenter l’argent de 1€. L’argent virtuel sert pour acheter des bonus concernant les quiz.
- 6ème étape : conclusion de l'explication. Et souhaiter bonne chance à l'utilisateur, et de se battre avec les membres de son équipe.

Le tutoriel doit pouvoir être accessible à tout moment via un bouton sur l'interface principale (en haut à droite).
