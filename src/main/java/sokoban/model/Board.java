package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import sokoban.viewmodel.ToolViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private BooleanProperty isChanged = new SimpleBooleanProperty(false);
    private final LongProperty moveCount = new SimpleLongProperty(0);
    private GridState gridState;

    /**
     * Default constructor initializes the board and configures bindings.
     */
    public Board() {
        isFull = grid.filledCellsCountProperty().isEqualTo(Board.MAX_FILLED_CELLS);
        this.gridState = new GridState();
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

    public GridState getGridState() {
        return gridState;
    }

    /**
     * Places an element on the board at the specified position based on the current tool selected.
     * @param line The row index.
     * @param col The column index.
     */
    public void put(int line, int col) {
        List<GameElement> cellItems = valueProperty(line, col);
        GameElement selected = ToolViewModel.getToolSelected();

        // Remove existing player if the selected element is a player
        if (selected instanceof Player && isPlayerPlaced()) {
            removeExistingPlayer();
        }
        int size = cellItems.size();
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
        setChanged(true);
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
        if (selected instanceof Player) {
            removeCellElement(line, col, cellItems.get(cellItems.size() - 1));
            putElement(line, col, selected);
        }
        else if (selected instanceof Ground) {
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
     */
    public void removeTool(int line, int col) {
        List<GameElement> cellItems = valueProperty(line, col);
        if (!(cellItems.size() == 1)) {
            setChanged(true);
            GameElement currentValue = valueProperty(line, col).get(cellItems.size() - 1);
            removeCellElement(line, col, currentValue);
        }
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
    public LongProperty moveCountProperty() {
        return moveCount;
    }
    //compteur de mouvement
    public void incrementMoveCount(int nb) {
        moveCountProperty().set(moveCountProperty().get() + nb);
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

    public boolean IsChanged() {
        return isChanged.get();
    }

    public BooleanProperty isChangedProperty() {
        return isChanged;
    }

    public void setChanged(boolean isChanged) {
        isChangedProperty().set(isChanged);
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
     */
    public Grid open(File file) {


        try {
            if (!file.getName().endsWith(".xsb")) {
                throw new IllegalArgumentException("Le fichier doit avoir une extension .xsb");
            }

            int height = getGrid().gridHeight;
            int width = getGrid().gridWidth;
            int[] dimensions = calculateGridDimensions(file);

            if (dimensions[0] != width || dimensions[1] != height) {
                setGrid(new Grid4Design(dimensions[0], dimensions[1]));
                configureBindings();
            } else {
                // Clear current elements
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        this.valueProperty(i, j).clear();
                        this.valueProperty(i, j).add(new Ground());
                    }
                }
            }

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
                            grid.put(row, col, items.get(0));
                            if (items.size() > 1) {
                                grid.put(row, col, items.get(1));
                            }
                        }
                        row++;
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            configureBindings();
        } catch (IllegalArgumentException e) {
            System.out.println("Le fichier doit avoir une extension .xsb");
            return null; // Ou une autre gestion d'erreur appropriée selon le contexte
        }
        return grid;
    }

    public static int[] calculateGridDimensions(File file) {
        int maxWidth = 0;
        int maxHeight = 0;

        try (Scanner scanner = new Scanner(file)) {
            int currentWidth = 0;
            int currentHeight = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    currentWidth = line.length();
                    currentHeight++;
                }
                maxWidth = Math.max(maxWidth, currentWidth);
                maxHeight = Math.max(maxHeight, currentHeight);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return new int[]{maxWidth, maxHeight};
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
            case ' ':
                cellItems.add(new Ground());
                return cellItems;
            default:
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
        Grid clonedGrid = new Grid4Design(grid.gridWidth, grid.gridHeight);
        clonedGrid.copy(this.getGrid());

        // Set the cloned grid in the cloned board
        clonedBoard.grid = clonedGrid;

        // Configure bindings
        clonedBoard.configureBindings();

        return clonedBoard;
    }

    /**
     * Replace randomly boxes of the grid
     */
    public void mushroomEffect(){
        Grid grid = this.getGrid();
        Random random = new Random();
        int boxCount = 1;
        int boxNumber = 0;
        //List<GameElement> boxes = new ArrayList<>();
        // save etat initial
        if (gridState.getBoardHistory().size() == 0) {
            gridState.addBoardState(this);
        }

        // Clear current box(es)
        for (int i = 0; i < grid.getGridHeight(); i++) {
            for (int j = 0; j < grid.getGridWidth(); j++) {
                List<GameElement> cellItems = this.valueProperty(i,j);
                for (GameElement item : cellItems){
                    if (item instanceof Box){
                        cellItems.remove(item);
                        boxNumber++;
                        break;
                    }
                }
            }
        }

        // Place boxes randomly anywhere except on sides
        while (boxNumber > 0) {
            int i = random.nextInt(grid.getGridHeight());
            int j = random.nextInt(grid.getGridWidth());
            List<GameElement> cellItems = grid.valueProperty(i,j);
            if (!(i == 0 || j == 0 || i == 9 || j == 14)) {
                if ((cellItems.size() == 1) ||
                        (cellItems.size() == 2 && cellItems.stream().anyMatch(element -> element instanceof Goal))){
                    Box box = new Box();
                    box.setNumberLabel(new Label(String.valueOf(boxCount)));
                    cellItems.add(box);
                    boxNumber--;
                    boxCount++;
                }
            }
        }
        moveCountProperty().set(moveCountProperty().get() + 20);
    }

    // Mushroom feature

    /**
     * To place mushroom on the grid
     * @param grid
     * @return
     */
    public void mushroom(Grid grid){
        Random random = new Random();
        Boolean free = false;

        for (int i = 0; i < grid.getGridHeight(); i++) {
            for (int j = 0; j < grid.getGridWidth(); j++) {
                List<GameElement> cellItems = this.valueProperty(i,j);
                if (cellItems.stream().anyMatch(element -> element instanceof Mushroom))
                    cellItems.remove(cellItems.size() - 2);
                }
            }

        while (!free) {
            int i = random.nextInt(grid.getGridHeight());
            int j = random.nextInt(grid.getGridWidth());
            List<GameElement> cellItems = grid.valueProperty(i,j);
            if (cellItems.stream().allMatch(element -> element instanceof Ground) && cellItems.size() == 1) {
                cellItems.add(new Mushroom());
                cellItems.add(new Ground());
                free = true;
            }
        }
    }

    /**
     * This method toggles the visibility of mushrooms on the grid.
     * If a mushroom is visible (i.e., not covered by a Ground element), it will be hidden by adding a Ground element.
     * If a mushroom is hidden (i.e., covered by a Ground element), the Ground element will be removed, making the mushroom visible.
     * The method returns true if any mushroom was made visible, otherwise it returns false.
     * Additionally, if any mushroom is made visible, the move count is incremented by 10.
     *
     * @return true if any mushroom was made visible, false otherwise.
     */
    public boolean hideOrShow() {
        boolean visible = false;  // Flag to track if any mushroom was made visible
        for (int i = 0; i < this.getGrid().getGridHeight(); i++) {  // Loop through all rows
            for (int j = 0; j < this.getGrid().getGridWidth(); j++) {  // Loop through all columns
                List<GameElement> cellItems = this.valueProperty(i, j);  // Get the list of game elements at the current cell
                int last = cellItems.size() - 1;  // Index of the last element in the list
                if (cellItems.stream().anyMatch(element -> element instanceof Mushroom)) {  // Check if there's a mushroom in the cell
                    if (cellItems.get(last) instanceof Ground) {  // If the last element is a Ground
                        cellItems.remove(last);  // Remove the Ground element to reveal the mushroom
                        visible = true;  // Set the flag to true, indicating a mushroom was made visible
                    } else {
                        cellItems.add(new Ground());  // Otherwise, add a Ground element to hide the mushroom
                    }
                }
            }
        }

        if (visible) {
            incrementMoveCount(10);  // Increment move count by 10 if any mushroom was made visible
        }
        return visible;  // Return whether any mushroom was made visible
    }


    /**
     * This method checks if the mushroom on the grid is visible.
     * A mushroom is considered visible if it is not covered by a Ground element.
     * The method returns true if the mushroom is visible, otherwise it returns false.
     *
     * @return true if the mushroom is visible, false otherwise.
     */
    public boolean mushVisible() {
        boolean visible = true;  // Flag to track if all mushrooms are visible
        for (int i = 0; i < this.getGrid().getGridHeight(); i++) {  // Loop through all rows
            for (int j = 0; j < this.getGrid().getGridWidth(); j++) {  // Loop through all columns
                List<GameElement> cellItems = this.valueProperty(i, j);  // Get the list of game elements at the current cell
                int last = cellItems.size() - 1;  // Index of the last element in the list
                if (cellItems.stream().anyMatch(element -> element instanceof Mushroom)) {  // Check if there's a mushroom in the cell
                    if (cellItems.get(last) instanceof Ground) {  // If the last element is a Ground
                        visible = false;  // Set the flag to false, indicating a mushroom is hidden
                        break;
                    }
                }
            }
        }
        return visible;  // Return whether all mushrooms are visible
    }


    /**
     * This method numbers all the boxes on the grid sequentially.
     * Each Box element on the grid is assigned a unique number label in the order they are encountered.
     * The numbering starts from 1 and increases sequentially.
     *
     * @param grid The grid containing the boxes to be numbered.
     */
    public void boxNumber(Grid grid) {
        int number = 0;  // Counter for numbering the boxes
        for (int i = 0; i < grid.getGridHeight(); ++i) {  // Loop through all rows
            for (int j = 0; j < grid.getGridWidth(); ++j) {  // Loop through all columns
                List<GameElement> targetCellItems = grid.getValues(i, j);  // Get the list of game elements at the current cell
                for (GameElement element : targetCellItems) {  // Iterate over each element in the cell
                    if (element instanceof Box) {  // Check if the element is a Box
                        number++;  // Increment the box counter
                        ((Box) element).setNumberLabel(new Label(String.valueOf(number)));  // Set the number label of the box
                    }
                }
            }
        }
    }


}
