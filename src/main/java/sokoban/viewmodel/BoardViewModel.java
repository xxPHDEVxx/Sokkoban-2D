package sokoban.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.model.Board;
import sokoban.model.Direction;
import sokoban.model.Grid;
import sokoban.model.CellValue;

import java.io.File;

public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;
    private static BooleanProperty isChanged = new SimpleBooleanProperty(false);
    private final LongProperty moveCount = new SimpleLongProperty(0);
    public BoardViewModel(Board board) {
        this.board = board;
        gridViewModel = new GridViewModel(board);
    }

    public GridViewModel getGridViewModel() {
        return gridViewModel;
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
    public BooleanBinding rulesOKProperty() {
        return board.getRulesOK();
    }


    public static int maxFilledCells() {
        return Board.maxFilledCells();
    }
    public static int gridWidth() {
        return Grid.getGridWidth();
    }
    public static int gridHeight() {
        return Grid.getGridHeight();
    }

    public static void exitMenu(){System.exit(0);}
    public static void newGridMenu(int width, int height){
        Board.setGrid(new Grid(width, height));
    }
    public Grid openBoard(File file){
        Grid grid = board.open(file);
        return grid;
    }
    public static BooleanProperty isChangedProperty() {
        return isChanged;
    }
    public static final boolean isChanged() {
        return isChangedProperty().get();
    }
    public static final void setChanged(boolean isChanged) {
        isChangedProperty().set(isChanged);
    }
    public Grid getGrid(){
        return board.getGrid();
    }
    public GridViewModel getGridVM(){
        return gridViewModel;
    }


    // Déplacements joueur

    // à modifier pour permettre la superposition avec une cible
    public boolean movePlayer(Direction direction) {
        CellViewModel playerCell = findPlayerCell();
        if (playerCell == null) {
            System.out.println("Player not found!");
            return false;
        }

        int newRow = playerCell.getLine() + direction.getDeltaRow();
        int newCol = playerCell.getCol() + direction.getDeltaCol();
        System.out.println("Trying to move to: " + newRow + ", " + newCol);

        if (canMove(playerCell, newRow, newCol)) {
            System.out.println("Move valid, proceeding.");

            if (board.getGrid().getValue(newRow, newCol) == CellValue.BOX)
                board.getGrid().setValue(newRow + direction.getDeltaRow(), newCol + direction.getDeltaCol(), CellValue.BOX);

            // Clear the previous player position
            board.getGrid().setValue(playerCell.getLine(), playerCell.getCol(), CellValue.GROUND);

            // Move the player to the new position
            board.getGrid().setValue(newRow, newCol, CellValue.PLAYER);

            incrementMoveCount();

            return true;
        } else {
            System.out.println("Move invalid.");
        }
        return false;
    }

    private boolean canMove(CellViewModel currentCell, int newRow, int newCol) {
        if (!board.getGrid().isValidPosition(newRow, newCol)) return false;
        CellValue destinationType = board.valueProperty(newRow, newCol).get();

        switch (destinationType) {
            case WALL:
                return false;
            case BOX:
                // Ajouter la logique pour pousser la boîte si derrière elle se trouve une case libre ou un but.
                return checkPush(currentCell, newRow, newCol);
            default:
                return true;
        }
    }

    public void incrementMoveCount() {
        moveCount.set(moveCount.get() + 1); // Incrémentez la propriété moveCount de 1
    }

    public LongProperty moveCountProperty() {
        return moveCount;
    }

    private CellViewModel findPlayerCell() {
        for (int row = 0; row < gridHeight(); row++) {
            for (int col = 0; col < gridWidth(); col++) {
                CellViewModel cell = gridViewModel.getCellViewModel(row, col);
                if (cell.getCellValue().get() == CellValue.PLAYER) {
                    return cell;
                }
            }
        }
        return null;
    }

    public boolean checkPush(CellViewModel currentCell, int newRow, int newCol) {
        // Calculer la position cible pour la boîte après le push
        int targetRow = newRow + (newRow - currentCell.getLine());
        int targetCol = newCol + (newCol - currentCell.getCol());

        // Vérifier si la position cible est valide
        if (!board.getGrid().isValidPosition(targetRow, targetCol)) {
            return false;
        }

        // Récupérer l'état de la cellule cible
        CellValue targetCellValue = board.getGrid().getValue(targetRow, targetCol);

        // La boîte peut être poussée si la cellule cible est GROUND ou GOAL
        return targetCellValue == CellValue.GROUND || targetCellValue == CellValue.GOAL;
    }
    //compteur de mouvement

}