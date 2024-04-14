package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import sokoban.model.Board;
import sokoban.model.Direction;
import sokoban.model.Grid;
import sokoban.model.CellValue;

import java.io.File;

public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;

    private static int countMove = 0;

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
    public static boolean isChanged(){
        return true;
    }
    public Grid getGrid(){
        return board.getGrid();
    }
    public GridViewModel getGridVM(){
        return gridViewModel;
    }


    // Déplacements joueur

    // Commentaires dispo pour suivi car longue méthode
    public boolean movePlayer(Direction direction) {
        CellViewModel playerCell = findPlayerCell();
        if (playerCell == null) {
            System.out.println("Player not found!");
            return false;
        }

        int newRow = playerCell.getLine() + direction.getDeltaRow();
        int newCol = playerCell.getCol() + direction.getDeltaCol();

        if (!board.getGrid().isValidPosition(newRow, newCol)) {
            System.out.println("Move invalid: Position is out of bounds.");
            return false;
        }

        CellValue targetCellValue = board.getGrid().getValue(newRow, newCol);
        if (targetCellValue == CellValue.WALL) {
            System.out.println("Move invalid: Wall is blocking the way.");
            return false;
        }

        // Check if the movement is to push a box
        if (targetCellValue == CellValue.BOX || targetCellValue == CellValue.BOX_ON_GOAL) {
            int nextRow = newRow + direction.getDeltaRow();
            int nextCol = newCol + direction.getDeltaCol();
            if (!canPushBox(targetCellValue, newRow, newCol, nextRow, nextCol)) {
                System.out.println("Move invalid: Cannot push the box.");
                return false;
            }
            // Move the box
            board.getGrid().setValue(newRow, newCol, (targetCellValue == CellValue.BOX_ON_GOAL) ? CellValue.GOAL : CellValue.GROUND);
            board.getGrid().setValue(nextRow, nextCol, (board.getGrid().getValue(nextRow, nextCol) == CellValue.GOAL) ? CellValue.BOX_ON_GOAL : CellValue.BOX);
        }

        // Move the player to the new position
        CellValue newPlayerCellState = board.getGrid().getValue(newRow, newCol);
        board.getGrid().setValue(newRow, newCol, (newPlayerCellState == CellValue.GOAL) ? CellValue.PLAYER_ON_GOAL : CellValue.PLAYER);

        // Restore the original player cell
        CellValue originalPlayerCellState = board.getGrid().getValue(playerCell.getLine(), playerCell.getCol());
        board.getGrid().setValue(playerCell.getLine(), playerCell.getCol(), (originalPlayerCellState == CellValue.PLAYER_ON_GOAL) ? CellValue.GOAL : CellValue.GROUND);

        updateMoveCount();
        return true;
    }

    private boolean canPushBox(CellValue boxState, int boxRow, int boxCol, int targetRow, int targetCol) {
        if (!board.getGrid().isValidPosition(targetRow, targetCol)) {
            return false;
        }
        CellValue targetCellState = board.getGrid().getValue(targetRow, targetCol);
        return (targetCellState == CellValue.GROUND || targetCellState == CellValue.GOAL);
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

    private void updateMoveCount() {
        countMove++;
    }

    private CellViewModel findPlayerCell() {
        for (int row = 0; row < gridHeight(); row++) {
            for (int col = 0; col < gridWidth(); col++) {
                CellViewModel cell = gridViewModel.getCellViewModel(row, col);
                if (cell.getCellValue().get() == CellValue.PLAYER || cell.getCellValue().get() == CellValue.PLAYER_ON_GOAL) {
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

}