package sokoban.model;

import javafx.scene.control.Label;
import javafx.scene.image.Image;

/**
 * The Box class represents a box in the Sokoban game.
 * It extends the GameElement class and includes a number label to identify the box.
 */
public class Box extends GameElement {

    private Label numberLabel = new Label();  // Label to display the number of the box

    /**
     * Default constructor for the Box class.
     * Initializes the box with an image.
     */
    public Box() {
        image = new Image("box.png");  // Set the image of the box to "box.png"
    }

    /**
     * Creates a copy of the current Box object.
     * The copy will have the same image and number label as the original.
     *
     * @return a new Box object that is a copy of the current Box.
     */
    public Box copy() {
        Box copy = new Box();  // Create a new Box object
        copy.numberLabel = this.getNumberLabel();  // Set the number label of the copy to be the same as the original
        return copy;  // Return the copy
    }

    /**
     * Gets the number label of the box.
     *
     * @return the number label of the box.
     */
    public Label getNumberLabel() {
        return numberLabel;  // Return the number label
    }

    /**
     * Sets the number label of the box.
     *
     * @param numberLabel the new number label to set.
     */
    public void setNumberLabel(Label numberLabel) {
        this.numberLabel = numberLabel;  // Set the number label to the provided label
    }
}
