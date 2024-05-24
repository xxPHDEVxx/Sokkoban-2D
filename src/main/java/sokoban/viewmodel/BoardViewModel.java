package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class BoardViewModel {

    private final GridViewModel gridViewModel;
    private GridState gridState;
    private Board board;
    private Grid4Design saveGridDesign;

    /**
     * Constructor initializing the BoardViewModel with the given Board
     *
     * @param board the Board object associated with this ViewModel
     */
    public BoardViewModel(Board board) {
        this.board = board;
        this.gridState = board.getGridState();
        this.gridViewModel = new GridViewModel(board);
    }

    /**
     * Gets the GridViewModel associated with this BoardViewModel
     *
     * @return the GridViewModel
     */
    public GridViewModel getGridViewModel() {
        return gridViewModel;
    }

    // Properties related to move counts and cell counts
    public LongProperty moveCountProperty() {
        return board.moveCountProperty();
    }

    public LongBinding filledCellsCountProperty() {
        return board.filledCellsCountProperty();
    }

    public LongBinding boxCountProperty() {
        return board.boxCountProperty();
    }

    public LongBinding goalCountProperty() {
        return board.goalCountProperty();
    }

    public LongBinding playerCountProperty() {
        return board.playerCountProperty();
    }

    public LongBinding boxInTargetCountProperty() {
        return board.boxInTargetCountProperty();
    }

    public BooleanBinding rulesOKProperty() {
        return board.getRulesOK();
    }

    /**
     * Gets the maximum number of filled cells
     *
     * @return the maximum filled cells count
     */
    public int maxFilledCells() {
        return board.maxFilledCells();
    }
    public void configureBindings(){
        board.configureBindings();
    }

    /**
     * Gets the width of the grid
     *
     * @return the grid width
     */
    public int gridWidth() {
        return board.getGrid().getGridWidth();
    }

    /**
     * Gets the height of the grid
     *
     * @return the grid height
     */
    public int gridHeight() {
        return board.getGrid().getGridHeight();
    }

    /**
     * Exits the application
     */
    public static void exitMenu() {
        System.exit(0);
    }

    /**
     * Creates a new grid with the specified dimensions
     *
     * @param width  the width of the new grid
     * @param height the height of the new grid
     */
    public void newGridMenu(int width, int height) {
        board.setGrid(new Grid4Design(width, height));
        board.configureBindings();
    }

    /**
     * Opens a board from a file
     *
     * @param file the file containing the board data
     * @return the opened Grid
     */
    public Grid openBoard(File file){
         return board.open(file);
    }

    // Static property to track if the model has changed
    public  BooleanProperty isChangedProperty() {
        return board.isChangedProperty();
    }

    public  boolean isChanged() {
        return board.IsChanged();
    }

    public void setChanged(boolean isChanged) {
        board.setChanged(isChanged);
    }

    /**
     * Gets the Board object
     *
     * @return the Board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Moves the player in the specified direction
     *
     * @param direction the direction to move the player
     * @return true if the player moved successfully, false otherwise
     */
    public boolean movePlayer(Direction direction) {

        // Check if the game has ended
        if (endGame()) {
            return false;
        }

        // Save initial state if the history is empty
        if (gridState.getBoardHistory().isEmpty()) {
            gridState.addBoardState(board);
        }

        // Find the cell containing the player
        CellViewModel playerCell = findPlayerCell();
        if (playerCell == null) {
            System.out.println("Player not found!");
            return false;
        }

        // Calculate the new position for the player
        int newRow = playerCell.getLine() + direction.getDeltaRow();
        int newCol = playerCell.getCol() + direction.getDeltaCol();

        // Check if the new position is valid
        if (!isValidPosition(newRow, newCol)) {
            return false;
        }

        // Get the items in the target cell
        List<GameElement> targetCellItems = board.getGrid().getValues(newRow, newCol);

        // Check if a wall or a mushroom is blocking the way
        if (targetCellItems.stream().anyMatch(element -> element instanceof Wall || element instanceof Mushroom)) {
            System.out.println("Move invalid: something is blocking the way.");
            return false;
        }

        // Check if the move involves pushing a box
        if (targetCellItems.stream().anyMatch(element -> element instanceof Box)) {
            int nextRow = newRow + direction.getDeltaRow();
            int nextCol = newCol + direction.getDeltaCol();

            // Check if the box can be pushed
            if (!canPushBox(nextRow, nextCol) || !isValidPosition(nextRow, nextCol)) {
                System.out.println("Move invalid: Cannot push the box.");
                return false;
            }

            // Move the box to the next cell
            Box box = getBox(targetCellItems);
            if (box != null) {
                targetCellItems.remove(box);
                board.getGrid().getValues(nextRow, nextCol).add(box);
            }
        }

        // Move the player to the new position
        targetCellItems.add(new Player());

        // Remove the player from the original cell
        List<GameElement> playerCellItems = board.getGrid().getValues(playerCell.getLine(), playerCell.getCol());
        playerCellItems.removeIf(element -> element instanceof Player);

        // Increment the move count
        incrementMoveCount(1);
        boxInTargetCountProperty().invalidate();

        // Save the current state to the history
        gridState.addBoardState(board);
        return true;
    }

    /**
     * Retrieves the first Box from the list of game elements
     *
     * @param targetCellItems the list of game elements in the target cell
     * @return the first Box found, or null if no Box is present
     */
    public Box getBox(List<GameElement> targetCellItems) {
        return (Box) targetCellItems.stream()
                .filter(element -> element instanceof Box)
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if the specified position is valid within the grid
     *
     * @param row the row index
     * @param col the column index
     * @return true if the position is valid, false otherwise
     */
    public boolean isValidPosition(int row, int col) {
        if (!board.getGrid().isValidPosition(row, col)) {
            System.out.println("Move invalid: Position is out of bounds.");
            return false;
        }
        return true;
    }

    /**
     * Checks if a box can be pushed to the specified target position
     *
     * @param targetRow the target row index
     * @param targetCol the target column index
     * @return true if the box can be pushed, false otherwise
     */
    private boolean canPushBox(int targetRow, int targetCol) {
        if (!board.getGrid().isValidPosition(targetRow, targetCol)) {
            return false;
        }

        List<GameElement> targetCellItems = board.getGrid().getValues(targetRow, targetCol);

        // Check if the target cell contains only a goal or only ground elements
        boolean containsOnlyGoal = targetCellItems.size() == 2 && targetCellItems.get(1) instanceof Goal;
        boolean containsOnlyGround = targetCellItems.stream().allMatch(element -> element instanceof Ground);

        return containsOnlyGoal || containsOnlyGround;
    }

    /**
     * Finds the cell containing the player
     *
     * @return the CellViewModel representing the player's cell, or null if not found
     */
    private CellViewModel findPlayerCell() {
        for (int row = 0; row < gridHeight(); row++) {
            for (int col = 0; col < gridWidth(); col++) {
                List<GameElement> cellItems = this.board.valueProperty(row, col).get();
                if (cellItems.stream().anyMatch(element -> element instanceof Player)) {
                    return new CellViewModel(row, col, board);
                }
            }
        }
        return null;
    }

    /**
     * Increments the move count by the specified number
     *
     * @param nb the number to increment the move count by
     */
    public void incrementMoveCount(int nb) {
        board.incrementMoveCount(nb);
    }

    // Feature: Undo and Redo moves

    /**
     * Undoes the last move
     */
    public void undo() {
        if (endGame()) {
            return;
        }

        if (gridState.hasPreviousState()) {
            Board previousBoard = gridState.getPreviousState();
            if (previousBoard == null) {
                System.out.println("Empty grid");
                return;
            }
            board.getGrid().copy(previousBoard.getGrid());

            // Decrement the move count (or update accordingly)
            if (moveCountProperty().get() > 0) {
                incrementMoveCount(-1);
            }
        }
    }

    /**
     * Redoes the last undone move
     */
    public void redo() {
        if (endGame()) {
            return;
        }

        if (gridState.hasNextState()) {
            Board nextBoard = gridState.getNextState();
            if (nextBoard == null) {
                System.out.println("Empty grid");
                return;
            }
            board.getGrid().copy(nextBoard.getGrid());

            // Increment the move count (or update accordingly)
            incrementMoveCount(1);
        }
    }

    // Additional game features

    /**
     * Initializes a new Grid4Play for the game
     *
     * @return the new Grid4Play
     */
    public Grid4Play gridGame() {
        Grid4Play gridGame = new Grid4Play(gridWidth(), gridHeight());
        gridGame.copy(board.getGrid());
        boxNumber(gridGame);
        mushroom(gridGame);
        return gridGame;
    }

    public void goToDesign(){
        board.setGrid(this.getSaveGridDesign());
    }

    /**
     * Updates the box numbers in the grid
     *
     * @param grid the grid to update
     */
    public void boxNumber(Grid grid) {
        board.boxNumber(grid);
    }

    /**
     * Places mushrooms on the grid
     *
     * @param grid the grid to update
     */
    public void mushroom(Grid grid) {
        board.mushroom(grid);
    }

    /**
     * Toggles the visibility of the mushrooms
     *
     * @return true if mushrooms are now visible, false otherwise
     */
    public boolean hideOrShow() {
        return board.hideOrShow();
    }

    /**
     * Checks if mushrooms are visible
     *
     * @return true if mushrooms are visible, false otherwise
     */
    public boolean mushVisible() {
        return board.mushVisible();
    }

    /**
     * Saves the current grid design
     */
    public void saveGridDesign() {
        saveGridDesign = new Grid4Design(gridWidth(), gridHeight());
        saveGridDesign.copy(board.getGrid());
    }

    /**
     * Gets the saved grid design
     *
     * @return the saved Grid4Design
     */
    public Grid4Design getSaveGridDesign() {
        return saveGridDesign;
    }

    /**
     * Checks if the game has ended
     *
     * @return true if the game has ended, false otherwise
     */
    public boolean endGame() {
        if (this.boxInTargetCountProperty().get() == this.goalCountProperty().get()) {
            if (gridState.getBoardHistory() != null) {
                gridState.getBoardHistory().clear();
                gridState.setCurrentIndex(0);
                return true;
            }
        }
        return mushVisible();
    }
}
