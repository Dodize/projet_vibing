# Avancement du Projet : Jeu de Conquête Géolocalisé

## Résumé du Projet (Rappel)

Le projet consiste à développer une application mobile Android de jeu de conquête géolocalisé. Les joueurs rejoignent l'une des quatre équipes et s'affrontent pour prendre le contrôle de Lieux d'Intérêt (POI) réels. Le gameplay principal repose sur la présence physique des joueurs sur les lieux, où ils doivent répondre à des questionnaires (QCM) pour capturer une zone. Une monnaie virtuelle, gagnée en marchant ou en interagissant avec les POI, permet d'obtenir des avantages en jeu. L'application est conçue pour fonctionner sans serveur dédié, en s'appuyant sur Firebase Firestore pour la gestion des données.

## État d'Avancement

* **Gestion du score des POIs :** Logique de décroissance du score avec le temps qui passe. L'actualisation est réalisée lors de l'interaction de l'utilisateur sur un lieu.
*   **Gestion de la monnaie virtuelle :** Mécanismes de perte d'argent lorsque le quiz n'est pas réussi (score insuffisant). Lorsqu'un joueur choisit de déposer les armes, il ne gagne pas la zone mais gagne un peu d'argent.
*   **Podomètre :** Intégration des données du podomètre pour le calcul des gains en monnaie virtuelle liés à la marche.
*   **Reconnaissance photo :** Le joueur doit prendre en photo le lieu du POI pour pouvoir lancer le quiz.
*   **Activation avec la localisation :** une interface permet de lancer directement un quiz lorsque le POI est détecté comme suffisamment proche du joueur. La localisation est donc mise à jour régulièrement et permet de déclencher le comportement.

## Définition du workflow

L'utilisation de l'IA représente toujours 100% du code généré, même si nous supervisons le développement en regardant le code produit.

Nous avons amélioré notre workflow avec la création de nouvelles commandes :
- /spec : pour démarrer une nouvelle fonctionnalité en se basant sur un fichier présent dans le répertoire spec
- /validate-feature : lorsque la fonctionnalité est validée (testée manuellement), effectue un commit, crée des fichiers de tests automatisés et génère la documentation associée
- /rollback : permet d’effacer toutes les modifications pour retourner à l’état sauvegardé par le dernier commit


## Retours

L'amélioration de notre workflow nous a permi de gagner en efficacité par l'utilisation de commandes afin d'éviter la dupplication des prompts. Aussi, l'IA précise la liste des étapes qu'elle va suivre, ce qui permet une meilleure supervision. Les commits réguliers nous évite les régressions par le fait de pouvoir rapidement revenir dans un état stable.

Malgré la définition de règles (dans AGENTS.md) et des demandes explicites dans les prompts, l'IA ne respecte pas toujours notre volonté.

Nous avons également tenté d'utiliser Gemini 3, mais nous avons eu des messages d'indisponibilités (surchauffe). Probablement des limitations liées à la version gratuite.
