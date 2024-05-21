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
    // Grid dimensions
    private static int GRID_WIDTH = 15;
    private static int GRID_HEIGHT = 10;

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
        this(GRID_WIDTH, GRID_HEIGHT);
    }

    /**
     * Constructor initializes the grid with specified width and height.
     * @param width The width of the grid.
     * @param height The height of the grid.
     */
    public Grid4Design(int width, int height) {
        GRID_WIDTH = width;
        GRID_HEIGHT = height;
        matrix = new Cell4Design[GRID_HEIGHT][];
        for (int i = 0; i < GRID_HEIGHT; ++i) {
            matrix[i] = new Cell4Design[GRID_WIDTH];
            for (int j = 0; j < GRID_WIDTH; ++j) {
                matrix[i][j] = new Cell4Design();
                matrix[i][j].addElement(new Ground());
            }
        }
        setFilledCellsCount();
        countCell();
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
     * Puts an element into the cell at the specified position, replacing the existing one.
     * @param line The row index.
     * @param col The column index.
     * @param element The game element to put.
     */
    void put(int line, int col, GameElement element) {
        Cell cell = matrix[line][col];
        List<GameElement> cellItems = cell.valueProperty();

        // Remove existing element if present
        if (cellItems.contains(element)) {
            cellItems.remove(element);
        }

        // Insert a new instance of the element
        if (element instanceof Box) {
            cell.addElement(new Box());
        } else if (element instanceof Player) {
            cell.addElement(new Player());
        } else if (element instanceof Wall) {
            cell.addElement(new Wall());
        } else if (element instanceof Goal) {
            cell.addElement(new Goal());
        } else {
            cell.addElement(new Ground());
        }

        // Invalidate counters
        filledCellsCount.invalidate();
        playerCount.invalidate();
        goalCount.invalidate();
        boxCount.invalidate();
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

    /**
     * Adds a game element to the specified position in the grid.
     * @param row The row index.
     * @param col The column index.
     * @param value The game element to add.
     */
    public void addElement(int row, int col, GameElement value) {
        if (isValidPosition(row, col)) {
            matrix[row][col].addElement(value);
        } else {
            throw new IllegalArgumentException("Invalid row or column index");
        }
    }

}
