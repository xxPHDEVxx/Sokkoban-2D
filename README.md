# Projet Jeu Sokoban

## Description

Le projet **Sokoban** est une version personnalisée du célèbre jeu de puzzle, développée en Java avec JavaFX pour l'interface graphique. Ce jeu permet aux utilisateurs de créer leur propre grille, d'importer des grilles existantes, de sauvegarder des grilles créées, et bien sûr, de jouer. Le principe du jeu est simple : il faut déplacer des caisses numérotées sur leurs cibles correspondantes pour les associer par paire.

<br>

<div>
    <img src="https://github.com/xxPHDEVxx/Sokkoban-2D/blob/main/pic1.png" width="500" height="300">
    <img src="https://github.com/xxPHDEVxx/Sokkoban-2D/blob/main/pic2.png" width="500" height="300">
</div>

## Fonctionnalités

- **Création de grille** : Créez votre propre grille de Sokoban, avec la possibilité de placer des caisses et des cibles numérotées.
- **Importation de grilles** : Importez des grilles existantes pour jouer ou les modifier.
- **Sauvegarde de grille** : Sauvegardez les grilles que vous avez créées pour y rejouer plus tard ou les partager.
- **Règles personnalisées** : Les caisses et cibles numérotées doivent être associées par paire pour augmenter la difficulté.
- **Champignon magique** : Un champignon invisible est caché sur la carte. Si un joueur clique dessus, les caisses sont replacées de manière aléatoire.

## Prérequis

- **Java Development Kit (JDK)** version 17 ou supérieure
- **JavaFX** version 17 ou supérieure
- Un **IDE** comme IntelliJ IDEA, Eclipse, ou NetBeans pour le développement et l'exécution
- **Java Runtime Environment (JRE)** version 17 ou supérieure (si vous utilisez le fichier JAR pour exécuter le jeu)

## Installation

1. Clonez le dépôt GitHub du projet :

    ```bash
    git clone https://github.com/votre-utilisateur/votre-projet-sokoban.git
    cd votre-projet-sokoban
    ```

2. Ouvrez le projet dans votre IDE Java préféré.

3. Configurez les dépendances JavaFX :
    - Si vous utilisez Maven, assurez-vous que les dépendances JavaFX sont bien ajoutées dans le fichier `pom.xml`.
    - Si vous utilisez Gradle, ajoutez les dépendances dans le fichier `build.gradle`.

4. Compilez et exécutez l'application à partir de votre IDE.

### Exécution via le fichier JAR

Si vous préférez, vous pouvez exécuter le jeu directement à l'aide du fichier JAR fourni, sans avoir à configurer un IDE.

1. Assurez-vous que **Java Runtime Environment (JRE)** version 17 ou supérieure est installé sur votre machine.

2. Accédez au répertoire où se trouve le fichier `sokoban.jar`.

3. Exécutez le fichier JAR avec la commande suivante :

    ```bash
    java -jar sokoban.jar
    ```

Cela lancera l'application Sokoban directement sans avoir besoin de compiler le code source.

## Utilisation

1. **Création de grille** :
    - Utilisez l'éditeur intégré pour créer votre propre grille. Placez des caisses et des cibles numérotées sur la grille.
    - Sauvegardez votre grille pour y rejouer plus tard ou pour la partager avec d'autres joueurs.

2. **Jeu** :
    - Importez une grille existante ou chargez une grille précédemment sauvegardée.
    - Jouez en déplaçant les caisses sur leurs cibles correspondantes.
    - Attention au champignon magique ! Si vous cliquez dessus, la grille se remélangera de manière aléatoire.

3. **Sauvegarde** :
    - Après avoir créé une grille ou joué, sauvegardez la progression pour pouvoir la reprendre plus tard.

## Notes de version

### Version actuelle : 1.0.0

- **Fonctionnalités de base** : Création, importation, et sauvegarde de grilles, jeu avec des caisses et cibles numérotées.
- **Champignon magique** : Fonctionnalité unique qui remélange la grille si le joueur clique dessus.

## Liste des utilisateurs et mots de passe

Aucun système de connexion n'est implémenté pour le moment, l'accès au jeu est ouvert à tous.

## Liste des bugs connus

- **Redimensionnement fenêtre de jeu** : bug affichage au niveau de la hauteur de la fenêtre.
