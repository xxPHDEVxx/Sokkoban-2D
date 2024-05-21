package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;
import sokoban.viewmodel.ToolViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Board class represents the Sokoban game board.
 * It manages the grid, the game elements, and the game rules.
 */
public class Board {
    // Maximum number of filled cells allowed on the board
    public static int MAX_FILLED_CELLS = 75;

    // The grid representing the game board
    private Grid grid = new Grid4Design();

    // Boolean binding indicating if the board is full
    private final BooleanBinding isFull;

    // Boolean bindings for game rule checks
    private BooleanBinding countBoxOK;
    private BooleanBinding countPlayerOK;
    private BooleanBinding countGoalOK;
    private BooleanBinding countGoalBoxOK;
    private BooleanBinding rulesOK;

    /**
     * Default constructor initializes the board and configures bindings.
     */
    public Board() {
        isFull = grid.filledCellsCountProperty().isEqualTo(Board.MAX_FILLED_CELLS);
        configureBindings();
    }

    /**
     * Calculates and returns the maximum number of filled cells allowed based on grid size.
     * @return The maximum number of filled cells.
     */
    public int maxFilledCells() {
        MAX_FILLED_CELLS = (grid.getGridHeight() * grid.getGridWidth()) / 2;
        return Board.MAX_FILLED_CELLS;
    }

    /**
     * Places an element on the board at the specified position based on the current tool selected.
     * @param line The row index.
     * @param col The column index.
     */
    public void put(int line, int col) {
        List<GameElement> cellItems = valueProperty(line, col);
        GameElement selected = ToolViewModel.getToolSelected();
        int size = cellItems.size();

        // Remove existing player if the selected element is a player
        if (selected instanceof Player && isPlayerPlaced()) {
            removeExistingPlayer();
        }

        if (size <= 3) {
            if (cellItems.stream().anyMatch(element -> element instanceof Wall)) {
                handleWall(line, col, cellItems, selected);
            } else if (size == 2 && cellItems.get(1) instanceof Goal) {
                handleOnlyGoal(line, col, cellItems, selected);
            } else if (cellItems.stream().anyMatch(element -> element instanceof Goal)) {
                if (size > 2 && cellItems.get(1) instanceof Goal) {
                    handleGoalAndOther(line, col, cellItems, selected);
                } else {
                    handleOtherAndGoal(line, col, cellItems, selected);
                }
            } else if (size == 2 && (cellItems.get(1) instanceof Box || cellItems.get(1) instanceof Player)) {
                handleOnlyBoxOrPlayer(line, col, cellItems, selected);
            } else {
                putElement(line, col, selected);
            }
        }
    }

    // Private helper methods for handling different scenarios when placing elements
    private void handleOnlyBoxOrPlayer(int line, int col, List<GameElement> cellItems, GameElement selected) {
        if (selected instanceof Goal) {
            putElement(line, col, selected);
        } else {
            removeCellElement(line, col, cellItems.get(cellItems.size() - 1));
            putElement(line, col, selected);
        }
    }

    private void handleOtherAndGoal(int line, int col, List<GameElement> cellItems, GameElement selected) {
        cellItems.clear();
        putElement(line, col, new Ground());
        putElement(line, col, selected);
    }

    private void handleWall(int line, int col, List<GameElement> cellItems, GameElement selected) {
        if (selected instanceof Ground) {
            removeCellElement(line, col, cellItems.get(cellItems.size() - 1));
        } else {
            removeCellElement(line, col, cellItems.get(cellItems.size() - 1));
            putElement(line, col, selected);
        }
    }

    private void handleOnlyGoal(int line, int col, List<GameElement> cellItems, GameElement selected) {
        if (selected instanceof Box || selected instanceof Player) {
            putElement(line, col, selected);
        } else if (selected instanceof Wall) {
            cellItems.remove(cellItems.get(cellItems.size() - 1));
            putElement(line, col, selected);
        } else if (selected instanceof Ground) {
            cellItems.remove(cellItems.get(cellItems.size() - 1));
        }
    }

    private void handleGoalAndOther(int line, int col, List<GameElement> cellItems, GameElement selected) {
        if (selected instanceof Box || selected instanceof Player) {
            removeCellElement(line, col, cellItems.get(cellItems.size() - 1));
            putElement(line, col, selected);
        } else if (selected instanceof Ground) {
            cellItems.clear();
            putElement(line, col, new Ground());
        } else {
            cellItems.clear();
            putElement(line, col, new Ground());
            putElement(line, col, selected);
        }
    }

    /**
     * Removes a tool (game element) from the specified position on the board.
     * @param line The row index.
     * @param col The column index.
     * @param ground The ground element to be placed after removal.
     */
    public void removeTool(int line, int col, GameElement ground) {
        List<GameElement> cellItems = valueProperty(line, col);
        GameElement currentValue = valueProperty(line, col).get(cellItems.size() - 1);
        if (currentValue instanceof Box)
            Box.reduceGlobalNumber();
        removeCellElement(line, col, currentValue);
    }

    /**
     * Removes the existing player from the board.
     */
    private void removeExistingPlayer() {
        for (int row = 0; row < grid.getGridHeight(); row++) {
            for (int col = 0; col < grid.getGridWidth(); col++) {
                List<GameElement> cellItems = valueProperty(row, col);
                for (int i = 0; i < cellItems.size(); i++) {
                    if (cellItems.get(i) instanceof Player) {
                        removeCellElement(row, col, cellItems.get(i));
                        return; // Exit once the player is found and removed
                    }
                }
            }
        }
    }

    /**
     * Places an element in the specified cell on the grid.
     * @param line The row index.
     * @param col The column index.
     * @param element The game element to place.
     */
    public void putElement(int line, int col, GameElement element) {
        grid.put(line, col, element);
    }

    /**
     * Removes an element from the specified cell on the grid.
     * @param line The row index.
     * @param col The column index.
     * @param element The game element to remove.
     */
    public void removeCellElement(int line, int col, GameElement element) {
        grid.remove(line, col, element);
    }

    // Getter methods for properties
    public Boolean isFull() {
        return isFull.get();
    }

    public ReadOnlyListProperty<GameElement> valueProperty(int line, int col) {
        return grid.valueProperty(line, col);
    }

    public LongBinding filledCellsCountProperty() {
        return grid.filledCellsCountProperty();
    }

    public LongBinding boxCountProperty() {
        return grid.boxCountProperty();
    }

    public LongBinding goalCountProperty() {
        return grid.goalCountProperty();
    }

    public LongBinding boxInTargetCountProperty() {
        return grid.boxInTargetCountProperty();
    }

    public LongBinding playerCountProperty() {
        return grid.playerCountProperty();
    }

    /**
     * Configures bindings to check game rules.
     */
    public void configureBindings() {
        countBoxOK = boxCountProperty().greaterThan(0);
        countGoalOK = goalCountProperty().greaterThan(0);
        countPlayerOK = playerCountProperty().isEqualTo(1);
        countGoalBoxOK = boxCountProperty().isEqualTo(goalCountProperty());

        rulesOK = countBoxOK.and(countGoalOK).and(countPlayerOK).and(countGoalBoxOK);
    }

    public BooleanBinding getRulesOK() {
        return rulesOK;
    }

    // Setter and getter for the grid
    public void setGrid(Grid newGrid) {
        grid = newGrid;
    }

    public Grid getGrid() {
        return grid;
    }

    /**
     * Opens a file and loads its content into the grid.
     * @param file The file to open.
     * @return The loaded grid.
     */
    public Grid open(File file) {
        try (Scanner scanner = new Scanner(file)) {
            int row = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    // Iterate over each character in the line
                    for (int col = 0; col < line.length(); col++) {
                        char symbol = line.charAt(col);
                        // Convert character to GameElement and add to the grid
                        List<GameElement> items = convertSymbolToCellValue(symbol);
                        grid.addElement(row, col, items.get(0));
                        if (items.size() > 1) {
                            grid.addElement(row, col, items.get(1));
                        }
                    }
                    row++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        configureBindings();
        return grid;
    }

    /**
     * Converts a character symbol to corresponding game elements.
     * @param symbol The character symbol.
     * @return A list of game elements.
     */
    private static List<GameElement> convertSymbolToCellValue(char symbol) {
        List<GameElement> cellItems = new ArrayList<>();
        switch (symbol) {
            case '#':
                cellItems.add(new Wall());
                return cellItems;
            case '.':
                cellItems.add(new Goal());
                return cellItems;
            case '$':
                cellItems.add(new Box());
                return cellItems;
            case '@':
                cellItems.add(new Player());
                return cellItems;
            case '*':
                cellItems.add(new Box());
                cellItems.add(new Goal());
                return cellItems;
            case '+':
                cellItems.add(new Player());
                cellItems.add(new Goal());
                return cellItems;
            default:
                cellItems.add(new Ground());
                return cellItems;
        }
    }

    /**
     * Checks if a player is placed on the grid.
     * @return True if a player is placed, false otherwise.
     */
    public boolean isPlayerPlaced() {
        return grid.playerCountProperty().get() > 0;
    }

    /**
     * Creates a copy of the current board.
     * @return A new Board instance with the same grid configuration.
     */
    public Board copy() {
        Board clonedBoard = new Board();
        Grid clonedGrid = new Grid4Design();
        clonedGrid.copyFill(this.getGrid());

        // Set the cloned grid in the cloned board
        clonedBoard.grid = clonedGrid;

        // Configure bindings
        clonedBoard.configureBindings();

        return clonedBoard;
    }
}
