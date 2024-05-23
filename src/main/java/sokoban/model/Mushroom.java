package sokoban.model;

import javafx.scene.image.Image;

/**
 * The Mushroom class represents a mushroom element in the Sokoban game.
 * It inherits from the GameElement class.
 */
public class Mushroom extends GameElement {

    /**
     * Constructs a Mushroom object with the default image.
     */
    public Mushroom() {
        super(); // Call the constructor of the superclass (GameElement)
        image = new Image("mushroom.png"); // Set the image for the mushroom
    }

    /**
     * Retrieves the image associated with the mushroom.
     * @return The image of the mushroom.
     */
    @Override
    public Image getImage() {
        return super.getImage();
    }

    /**
     * Creates a copy of the Mushroom object.
     * @return A new Mushroom object.
     */
    @Override
    public GameElement copy() {
        return new Mushroom();
    }
}
