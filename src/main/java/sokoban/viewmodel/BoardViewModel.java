package sokoban.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.model.*;

import java.io.File;
import java.util.List;

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
    public LongBinding boxInTargetCountProperty() {
        return board.boxInTargetCountProperty();
    }
    public BooleanBinding rulesOKProperty() {
        return board.getRulesOK();
    }


    public static int maxFilledCells() {
        return Board.maxFilledCells();
    }
    public static int gridWidth() {
        return Grid4Design.getGridWidth();
    }
    public static int gridHeight() {
        return Grid4Design.getGridHeight();
    }

    public static void exitMenu(){System.exit(0);}
    public static void newGridMenu(int width, int height){
        Board.setGrid(new Grid4Design(width, height));
    }
    public Grid4Design openBoard(File file){
        Grid4Design grid = board.open(file);
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
    public Grid4Design getGrid(){
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

        List<GameElement> cellItems = board.getGrid().getValue(newRow, newCol);
        GameElement targetCellValue = cellItems.get(cellItems.size() - 1);
        if (targetCellValue instanceof Wall) {
            System.out.println("Move invalid: Wall is blocking the way.");
            return false;
        }

        // Check if the movement is to push a box
        if (targetCellValue instanceof Box || targetCellValue instanceof BoxOnGoal) {
            int nextRow = newRow + direction.getDeltaRow();
            int nextCol = newCol + direction.getDeltaCol();
            if (!canPushBox(targetCellValue, newRow, newCol, nextRow, nextCol)) {
                System.out.println("Move invalid: Cannot push the box.");
                return false;
            }
            // Move the box
            board.getGrid().setValue(newRow, newCol, (targetCellValue instanceof BoxOnGoal) ? new Goal() : new Ground());
            board.getGrid().setValue(nextRow, nextCol, (board.getGrid().getValue(nextRow, nextCol) instanceof Goal) ? new BoxOnGoal() : new Box());
        }

        // Move the player to the new position
        GameElement newPlayerCellState = cellItems.get(cellItems.size() - 1);
        board.getGrid().setValue(newRow, newCol, (newPlayerCellState instanceof Goal) ? new PlayerOnGoal() : new Player());

        // Restore the original player cell
        List<GameElement> playerCellItems = board.getGrid().getValue(playerCell.getLine(), playerCell.getCol());
        GameElement originalPlayerCellState = playerCellItems.get(cellItems.size() - 1);
        board.getGrid().setValue(playerCell.getLine(), playerCell.getCol(), (originalPlayerCellState instanceof PlayerOnGoal) ? new Goal() : new Ground());

        incrementMoveCount();
        boxInTargetCountProperty().invalidate();

        return true;
    }

    private boolean canPushBox(GameElement boxState, int boxRow, int boxCol, int targetRow, int targetCol) {
        if (!board.getGrid().isValidPosition(targetRow, targetCol)) {
            return false;
        }
        List<GameElement> targetCellItems = board.getGrid().getValue(targetRow, targetCol);
        GameElement targetCellState = targetCellItems.get(targetCellItems.size() - 1);
        return (targetCellState instanceof Ground  || targetCellState instanceof Goal );
    }




    private boolean canMove(CellViewModel currentCell, int newRow, int newCol) {
        if (!board.getGrid().isValidPosition(newRow, newCol)) return false;
        List<GameElement> cellItems = board.valueProperty(newRow, newCol).get();
        GameElement destinationType = cellItems.get(cellItems.size());

        // On suppose que destinationType est une instance de GameElement
        if (destinationType instanceof Wall) {
            return false;  // Un mur bloque le mouvement.
        } else if (destinationType instanceof Box) {
            // Ajouter la logique pour pousser la boîte si derrière elle se trouve une case libre ou un but.
            return checkPush(currentCell, newRow, newCol);
        } else {
            return true;  // Pour tous les autres types, le déplacement est permis.
        }

    }



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


    public boolean checkPush(CellViewModel currentCell, int newRow, int newCol) {
        // Calculer la position cible pour la boîte après le push
        int targetRow = newRow + (newRow - currentCell.getLine());
        int targetCol = newCol + (newCol - currentCell.getCol());

        // Vérifier si la position cible est valide
        if (!board.getGrid().isValidPosition(targetRow, targetCol)) {
            return false;
        }

        // Récupérer l'état de la cellule cible
        List<GameElement> cellItems = board.valueProperty(targetRow, targetCol);
        GameElement targetCellValue = cellItems.get(cellItems.size() - 1);

        // La boîte peut être poussée si la cellule cible est GROUND ou GOAL
        return targetCellValue instanceof Ground || targetCellValue instanceof Goal;
    }

    //compteur de mouvement
    public void incrementMoveCount() {
        moveCount.set(moveCount.get() + 1);
    }

    public LongProperty moveCountProperty() {
        return moveCount;
    }

}