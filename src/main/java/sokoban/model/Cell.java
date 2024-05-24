package sokoban.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

/**
 * The Cell class represents a cell in the Sokoban game grid.
 * It is an abstract class that provides a common interface for different types of cells.
 * Each cell contains a list of game elements.
 */
public abstract class Cell {
    protected ListProperty<GameElement> values = new SimpleListProperty<>(FXCollections.observableArrayList());  // List of game elements in the cell

    /**
     * Default constructor for the Cell class.
     */
    public Cell() {}

    /**
     * Adds a game element to the cell.
     *
     * @param value the game element to add.
     */
    abstract void addElement(GameElement value);

    /**
     * Checks if the cell is empty.
     *
     * @return true if the cell is empty, false otherwise.
     */
    abstract boolean isEmpty();

    /**
     * Checks if the cell contains a box.
     *
     * @return true if the cell contains a box, false otherwise.
     */
    abstract boolean isBox();

    /**
     * Checks if the cell contains a player.
     *
     * @return true if the cell contains a player, false otherwise.
     */
    abstract boolean isPlayer();

    /**
     * Checks if the cell contains a goal.
     *
     * @return true if the cell contains a goal, false otherwise.
     */
    abstract boolean isGoal();

    /**
     * Checks if the cell contains a box in a target.
     *
     * @return true if the cell contains a box in a target, false otherwise.
     */
    abstract boolean isBoxInTarget();

    /**
     * Gets the property representing the list of game elements in the cell.
     *
     * @return the read-only list property of game elements.
     */
    abstract ReadOnlyListProperty<GameElement> valueProperty();

    /**
     * Removes a game element from the cell.
     *
     * @param element the game element to remove.
     */
    abstract void remove(GameElement element);
}
