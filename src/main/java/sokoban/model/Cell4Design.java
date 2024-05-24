package sokoban.model;

import javafx.beans.property.ReadOnlyListProperty;

import java.util.List;

/**
 * The Cell4Design class extends the abstract Cell class and provides concrete implementations
 * of its abstract methods. This class is used for managing the game elements within a cell
 * specifically for design purposes in the Sokoban game.
 */
public class Cell4Design extends Cell {

    /**
     * Default constructor for the Cell4Design class.
     * Calls the superclass constructor to initialize the cell.
     */
    public Cell4Design() {
        super();
    }

    /**
     * Adds a game element to the cell.
     *
     * @param value the game element to add.
     */
    @Override
    void addElement(GameElement value) {
        values.add(value);
    }

    /**
     * Removes a game element from the cell.
     *
     * @param value the game element to remove.
     */
    public void remove(GameElement value) {
        this.values.remove(value);
    }

    /**
     * Checks if the cell is empty.
     *
     * @return true if the cell is empty or contains only Ground elements, false otherwise.
     */
    boolean isEmpty() {
        return values.isEmpty() || values.stream().allMatch(value -> value instanceof Ground);
    }

    /**
     * Checks if the cell contains a box.
     *
     * @return true if the cell contains a Box element, false otherwise.
     */
    boolean isBox() {
        return values.stream().anyMatch(value -> value instanceof Box);
    }

    /**
     * Checks if the cell contains a player.
     *
     * @return true if the cell contains a Player element, false otherwise.
     */
    boolean isPlayer() {
        return values.stream().anyMatch(value -> value instanceof Player);
    }

    /**
     * Checks if the cell contains a goal.
     *
     * @return true if the cell contains a Goal element, false otherwise.
     */
    boolean isGoal() {
        return values.stream().anyMatch(value -> value instanceof Goal);
    }

    /**
     * Checks if the cell contains a box in a target.
     *
     * @return true if the cell contains both a Box and a Goal element, false otherwise.
     */
    boolean isBoxInTarget() {
        return values.stream().anyMatch(value -> value instanceof Box)
                && values.stream().anyMatch(value -> value instanceof Goal);
    }

    /**
     * Gets the property representing the list of game elements in the cell.
     *
     * @return the read-only list property of game elements.
     */
    ReadOnlyListProperty<GameElement> valueProperty() {
        return values;
    }
}
