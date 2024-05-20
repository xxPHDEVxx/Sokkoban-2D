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
public class Grid4Design {
    // Grid dimensions
    private static int GRID_WIDTH = 15;
    private static int GRID_HEIGHT = 10;

    // Matrix holding the cells of the grid
    private final Cell4Design[][] matrix;

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
     * Copies the contents of another Grid4Design object into this one.
     * @param copy The grid to copy from.
     */
    public void copyFill(Grid4Design copy) {
        // Check if dimensions match
        if (this.getGridHeight() != copy.getGridHeight() || this.getGridWidth() != copy.getGridWidth()) {
            throw new IllegalArgumentException("Grid dimensions do not match.");
        }

        // Clear current elements
        for (int i = 0; i < this.getGridHeight(); i++) {
            for (int j = 0; j < this.getGridWidth(); j++) {
                this.getValues(i, j).clear();
            }
        }

        // Copy elements from source grid
        for (int i = 0; i < copy.getGridHeight(); i++) {
            for (int j = 0; j < copy.getGridWidth(); j++) {
                List<GameElement> copyElements = copy.getValues(i, j);
                for (GameElement element : copyElements) {
                    this.addElement(i, j, element.copy()); // Ensure to copy elements to avoid cross-references
                }
            }
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

    // Getters for grid dimensions
    public static int getGridWidth() { return GRID_WIDTH; }
    public static int getGridHeight() { return GRID_HEIGHT; }

    /**
     * Returns the value property of the cell at the specified position.
     * @param line The row index.
     * @param col The column index.
     * @return The value property of the cell.
     */
    ReadOnlyListProperty<GameElement> valueProperty(int line, int col) {
        return matrix[line][col].valueProperty();
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
            // Do nothing if the element is unknown
            return;
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
     * Returns the list of game elements at the specified position.
     * @param line The row index.
     * @param col The column index.
     * @return The list of game elements at the specified position.
     */
    public ObservableList<GameElement> getValues(int line, int col) {
        return matrix[line][col].valueProperty();
    }

    /**
     * Checks if the specified position is valid within the grid.
     * @param line The row index.
     * @param col The column index.
     * @return True if the position is valid, false otherwise.
     */
    public boolean isValidPosition(int line, int col) {
        return line >= 0 && line < GRID_HEIGHT && col >= 0 && col < GRID_WIDTH;
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
