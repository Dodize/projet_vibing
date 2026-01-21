# Cahier des Charges : Jeu de Conquête Géolocalisé

## 1. Introduction

Ce document spécifie les exigences fonctionnelles et techniques pour le développement d'une application mobile Android de jeu de conquête basé sur la localisation réelle des utilisateurs. Le jeu est centré sur la compétition entre équipes pour le contrôle de zones géographiques matérialisées par des Lieux d'Intérêt (POI).

## 2. Objectifs du Projet

L'objectif principal est de créer une application mobile Android permettant aux utilisateurs de former des équipes, d'explorer le monde réel pour capturer des zones géographiques, et de gérer une monnaie virtuelle pour obtenir des avantages en jeu.

## 3. Exigences Fonctionnelles Détaillées

### 3.1. Gestion des Utilisateurs et des Équipes

*   **Équipes :**
    *   Il y aura **4 équipes** au total.
    *   Les utilisateurs doivent choisir leur équipe lors du premier lancement de l'application.
    *   Chaque équipe se verra attribuer une **couleur** unique pour la représentation visuelle.
*   **Utilisateurs :**
    *   Chaque utilisateur doit posséder un **pseudo** unique.

### 3.2. Système de Carte et de Zones

*   **Carte :** L'application affichera une carte découpée en **zones**. Le système de carte est OpenStreetMap.
*   **Lieux d'Intérêt (POI) :**
    *   Chaque zone est centrée autour d'un **Lieu d'Intérêt** (statue, monument, élément connu).
    *   Les POI apparaissent sur la carte.
    *   Les zones sont initialement **neutres**.
*   **Score et Conquête :**
    *   Le but du jeu est d'avoir le plus grand nombre de zones conquises (score par équipe).
    *   Une zone conquise prend la **couleur de l'équipe** qui l'a capturée.
    *   Le score d'un lieu d'intérêt **décroît avec le temps** écoulé depuis la dernière prise de zone. Ce score est actualisé dès qu'un utilisateur tente d'interagir avec le lieu.

### 3.3. Interaction en Temps Réel et Localisation

*   **Localisation :** La position de l'utilisateur dans le monde réel est représentée sur la carte.
*   **Navigation :** Une liste dans l'interface regroupe les **POI les plus proches** de l'utilisateur.
*   **Visualisation du Score :** Le score d'un POI n'est visible que lorsque l'utilisateur est **physiquement présent** sur ce lieu.

### 3.4. Mécanique de Capture de Zone

Lorsqu'un utilisateur est sur un POI :

1.  **Prise de Photo :** L'utilisateur doit prendre une photo du lieu.
2.  **Vérification :** La photo doit être analysée/vérifiée (mécanisme à définir, potentiellement IA ou vérification par d'autres utilisateurs/modération).
    *   Si la photo est **correcte**, une interface s'ouvre.
    *   Sinon, l'utilisateur peut retenter ou quitter.
3.  **Action Vocale :** Une fois la photo validée, le score actuel du lieu est affiché, ainsi que le thème de la question associée. L'utilisateur doit choisir une action par commande vocale :
    *   **« Je dépose les armes » :** L'utilisateur reçoit un léger bonus en monnaie virtuelle. La zone reste inchangée.
    *   **« Je capture la zone » :** Lance un **QCM** (Questionnaire à Choix Multiples).
        *   **Succès :** Si le score de l'utilisateur est **supérieur** au score actuel de la zone, l'utilisateur capture la zone pour son équipe, et le score du lieu est mis à jour.
        *   **Échec :** Si le score de l'utilisateur est **inférieur** au score de la zone, l'utilisateur perd un peu de monnaie virtuelle.

### 3.5. Monnaie Virtuelle et Bonus

*   **Obtention de Monnaie :**
    *   En passant par des Lieux d'Intérêt.
    *   En marchant (utilisation des données du **podomètre** du téléphone).
    *   La marche rapporte **davantage** de monnaie virtuelle que le simple passage aux POI.
*   **Utilisation des Bonus :** La monnaie virtuelle permet de débloquer des bonus utilisables **durant le QCM** :
    *   Bonus permettant de **figer temporairement le score** du lieu (empêchant sa décroissance) pendant une certaine période.
    *   Bonus permettant d'**augmenter son propre score** pour le QCM.

## 4. Exigences Techniques

*   **Plateforme :** Application mobile **Android** native.
*   **Base de Données :** Utilisation de **Firebase Firestore** pour l'hébergement de la base de données.
*   **Backend :** **Aucun serveur backend** dédié ne doit être utilisé (architecture *serverless* via Firebase).