package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;

/**
 * The Grid4Design class represents the game grid for the Sokoban game.
 * It holds a matrix of Cell4Design objects and manages the game elements within the grid.
 */
public class Grid4Design extends Grid{

    // Bindings for counting various elements in the grid
    private LongBinding filledCellsCount;
    private LongBinding boxCount;
    private LongBinding playerCount;
    private LongBinding goalCount;
    private LongBinding boxInTargetCount;

    /**
     * Default constructor initializes the grid with default dimensions.
     */
    public Grid4Design() {
        matrix = new Cell4Design[gridHeight][];
        for (int i = 0; i < gridHeight; ++i) {
            matrix[i] = new Cell4Design[gridWidth];
            for (int j = 0; j < gridWidth; ++j) {
                matrix[i][j] = new Cell4Design();
                matrix[i][j].addElement(new Ground());
            }
        }
        setFilledCellsCount();
        countCell();
    }

    /**
     * Constructor initializes the grid with specified width and height.
     * @param width The width of the grid.
     * @param height The height of the grid.
     */
    public Grid4Design(int width, int height) {
        gridWidth = width;
        gridHeight = height;
        matrix = new Cell4Design[height][];
        for (int i = 0; i < height; ++i) {
            matrix[i] = new Cell4Design[width];
            for (int j = 0; j < width; ++j) {
                matrix[i][j] = new Cell4Design();
                matrix[i][j].addElement(new Ground());
            }
        }
        setFilledCellsCount();
        countCell();
    }

    public void put(int line, int col, GameElement element) {
        Cell cell = matrix[line][col];

        // Insert a new instance of the element based on specific rules
        if (addElementToCell(cell, element)) {
            // Invalidate counters
            filledCellsCount.invalidate();
            playerCount.invalidate();
            goalCount.invalidate();
            boxCount.invalidate();
        }
    }

    /**
     * Sets up the binding to count filled cells in the grid.
     */
    public void setFilledCellsCount() {
        filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> !cell.isEmpty())
                .count());
    }

    /**
     * Sets up the binding to count boxes in target cells.
     */
    public void setBoxInTargetCount() {
        boxInTargetCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.isBoxInTarget())
                .count());
    }

    /**
     * Sets up the binding to count box cells.
     */
    public void setBoxCellsCount() {
        boxCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.isBox())
                .count());
    }

    /**
     * Sets up the binding to count player cells.
     */
    public void setPlayerCount() {
        playerCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.isPlayer())
                .count());
    }

    /**
     * Sets up the binding to count goal cells.
     */
    public void setGoalCount() {
        goalCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.isGoal())
                .count());
    }

    /**
     * Removes an element from the cell at the specified position.
     * @param line The row index.
     * @param col The column index.
     * @param element The game element to remove.
     */
    void remove(int line, int col, GameElement element) {
        matrix[line][col].remove(element);
        filledCellsCount.invalidate();
        playerCount.invalidate();
        goalCount.invalidate();
        boxCount.invalidate();
    }

    // Getters for binding properties
    public LongBinding filledCellsCountProperty() { return filledCellsCount; }
    public LongBinding boxCountProperty() { return boxCount; }
    public LongBinding goalCountProperty() { return goalCount; }
    public LongBinding playerCountProperty() { return playerCount; }
    public LongBinding boxInTargetCountProperty() { return boxInTargetCount; }

    /**
     * Initializes all the element count bindings.
     */
    public void countCell() {
        setBoxCellsCount();
        setPlayerCount();
        setGoalCount();
        setBoxInTargetCount();
    }
}
