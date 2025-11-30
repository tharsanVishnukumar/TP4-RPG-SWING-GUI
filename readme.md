# RPG GUI

Un jeu de rôle (RPG) simple avec une interface graphique (GUI) développé en Java Swing.

## Description

Ce projet est une évolution d'un RPG textuel vers une version graphique. Le joueur peut se déplacer sur une carte, combattre des monstres, gérer son inventaire et acheter des armes dans un magasin.

## Fonctionnalités

*   **Interface Graphique** : Utilisation de Java Swing pour l'affichage de la carte et des contrôles.
*   **Déplacement** : Le joueur peut se déplacer dans les 4 directions (Z, Q, S, D).
*   **Combat** : Système de combat au tour par tour contre des monstres.
*   **Inventaire** : Gestion des armes et de l'argent.
*   **Magasin** : Possibilité d'acheter de nouvelles armes.
*   **Système de Dessin** : Utilisation de l'interface `Drawable` et de la méthode `paint` pour un rendu graphique modulaire.

## Architecture et Design

Le projet suit les principes de la programmation orientée objet :

*   **`GameGUI`** : Gère la fenêtre principale, les entrées utilisateur et la boucle de jeu.
*   **`Carte`** : Représente la grille de jeu. Elle implémente `Drawable` et gère l'affichage de toutes les entités qu'elle contient via sa méthode `paint`.
*   **`GameEntity`** : Classe abstraite pour toutes les entités (Joueur, Monstres, Obstacles).
*   **`Drawable`** : Interface définissant la capacité d'un objet à être dessiné (`draw` pour le symbole, `getColor` pour la couleur, et `paint` pour le rendu Swing).
*   **`Player` / `Monster`** : Héritent de `GameEntity` et implémentent la logique spécifique (XP, attaque, etc.).

## Installation et Lancement

### Prérequis

*   Java JDK installé (version 8 ou supérieure).

### Compilation

Un script de build est fourni (si disponible) ou vous pouvez compiler manuellement :

```bash
mkdir -p out
javac -d out -cp src src/Main.java
```

### Exécution

Pour lancer le jeu :

```bash
java -cp out Main
```

## Contrôles

*   **Z / S / Q / D** : Déplacement (Haut, Bas, Gauche, Droite)
*   **A / Espace** : Attaquer
*   **I** : Ouvrir l'inventaire
*   **B** : Ouvrir la boutique (si sur la case du magasin)

## Auteurs

Projet réalisé dans le cadre du cours de Java (L3).
