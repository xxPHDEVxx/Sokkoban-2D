package sokoban.model;

import javafx.scene.image.Image;

/**
 * The GameElement class is an abstract base class for all elements that can be placed on the game grid.
 * Each GameElement has an associated image used for rendering in the UI.
 */
public abstract class GameElement {

    // The image representing this game element.
    protected Image image;

    /**
     * Gets the image associated with this game element.
     *
     * @return the image of the game element.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Default constructor for GameElement.
     */
    public GameElement() {
    }

    /**
     * Creates and returns a copy of this game element.
     * This method is abstract and must be implemented by subclasses to ensure proper copying.
     *
     * @return a new instance that is a copy of this game element.
     */
    abstract GameElement copy();

}
