package sokoban.model;

import javafx.scene.image.Image;

/**
 * The Wall class represents a wall element in the Sokoban game.
 * It inherits from the GameElement class.
 */
public class Wall extends GameElement {

    /**
     * Constructs a Wall object with the default image.
     */
    public Wall() {
        image = new Image("wall.png"); // Set the image for the wall
    }

    /**
     * Creates a copy of the Wall object.
     * @return A new Wall object.
     */
    public Wall copy() {
        return new Wall();
    }
}
