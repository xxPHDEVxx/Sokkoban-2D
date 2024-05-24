package sokoban.model;

import javafx.scene.image.Image;

/**
 * The Player class represents a player element in the Sokoban game.
 * It inherits from the GameElement class.
 */
public class Player extends GameElement {

    /**
     * Constructs a Player object with the default image.
     */
    public Player() {
        image = new Image("player.png"); // Set the image for the player
    }

    /**
     * Creates a copy of the Player object.
     * @return A new Player object.
     */
    public Player copy() {
        return new Player();
    }
}
