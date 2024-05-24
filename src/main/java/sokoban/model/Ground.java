package sokoban.model;

import javafx.scene.image.Image;

/**
 * The Ground class represents a ground element in the Sokoban game.
 * It inherits from the GameElement class.
 */
public class Ground extends GameElement {

    /**
     * Constructs a Ground object with the default image.
     */
    public Ground() {
        image = new Image("ground.png"); // Set the image for the ground
    }

    /**
     * Creates a copy of the Ground object.
     * @return A new Ground object.
     */
    public Ground copy() {
        return new Ground();
    }
}
