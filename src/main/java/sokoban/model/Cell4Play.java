package sokoban.model;

import javafx.beans.property.ReadOnlyListProperty;

import java.util.Objects;

/**
 * The Cell4Play class extends the abstract Cell class and provides concrete implementations
 * of its abstract methods. This class is used for managing the game elements within a cell
 * during gameplay in the Sokoban game.
 */
public class Cell4Play extends Cell {

    /**
     * Default constructor for the Cell4Play class.
     * Calls the superclass constructor to initialize the cell.
     */
    public Cell4Play() {
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
     * Checks if the cell is empty.
     *
     * @return true if the cell is empty or contains only Ground elements, false otherwise.
     */
    @Override
    boolean isEmpty() {
        return values.isEmpty() || values.stream().allMatch(value -> value instanceof Ground);
    }

    /**
     * Checks if the cell contains a box.
     *
     * @return true if the cell contains a Box element, false otherwise.
     */
    @Override
    boolean isBox() {
        return values.stream().anyMatch(value -> value instanceof Box);
    }

    /**
     * Checks if the cell contains a player.
     *
     * @return true if the cell contains a Player element, false otherwise.
     */
    @Override
    boolean isPlayer() {
        return values.stream().anyMatch(value -> value instanceof Player);
    }

    /**
     * Checks if the cell contains a goal.
     *
     * @return true if the cell contains a Goal element, false otherwise.
     */
    @Override
    boolean isGoal() {
        return values.stream().anyMatch(value -> value instanceof Goal);
    }

    /**
     * Checks if the cell contains a box in a target.
     *
     * @return true if the cell contains both a Box and a Goal element, false otherwise.
     */
    @Override
    boolean isBoxInTarget() {
        String box = "";
        String goal = "";
        for (GameElement element : values) {
            if (element instanceof Box) {
                box = ((Box) element).getNumberLabel().getText();
            }
            if (element instanceof Goal) {
                goal = ((Goal) element).getNumberLabel().getText();
            }
        }
        return values.stream().anyMatch(value -> value instanceof Box)
                && values.stream().anyMatch(value -> value instanceof Goal) && Objects.equals(goal, box);
    }

    /**
     * Gets the property representing the list of game elements in the cell.
     *
     * @return the read-only list property of game elements.
     */
    @Override
    ReadOnlyListProperty<GameElement> valueProperty() {
        return values;
    }

    /**
     * Removes a game element from the cell.
     *
     * @param element the game element to remove.
     */
    @Override
    void remove(GameElement element) {
        values.remove(element);
    }
}
