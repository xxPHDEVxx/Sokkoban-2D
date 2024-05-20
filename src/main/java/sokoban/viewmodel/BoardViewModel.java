package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.model.*;

import java.io.File;
import java.util.List;

public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private GridState gridState;
    private  Board board;
    private static BooleanProperty isChanged = new SimpleBooleanProperty(false);
    private final LongProperty moveCount = new SimpleLongProperty(0);

    public BoardViewModel(Board board) {
        this.board = board;
        this.gridState = new GridState();
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


    public  int maxFilledCells() {
        return board.maxFilledCells();
    }
    public static int gridWidth() {
        return Grid4Design.getGridWidth();
    }
    public static int gridHeight() {
        return Grid4Design.getGridHeight();
    }

    public static void exitMenu(){System.exit(0);}
    public void newGridMenu(int width, int height){
        board.setGrid(new Grid4Design(width, height));
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
        // solution temporaire pour save etat initial
        if (gridState.getBoardHistory().size() == 0) {
            gridState.addBoardState(board);
        }

        // Recherche de la cellule contenant le joueur
        CellViewModel playerCell = findPlayerCell();
        if (playerCell == null) {
            System.out.println("Player not found!");
            return false;
        }

        // Calcul des nouvelles positions du joueur
        int newRow = playerCell.getLine() + direction.getDeltaRow();
        int newCol = playerCell.getCol() + direction.getDeltaCol();

        // Vérification si la nouvelle position est valide
        if (!isValidPosition(newRow, newCol)) {
            return false;
        }

        // Obtention des éléments de la cellule cible
        List<GameElement> targetCellItems = board.getGrid().getValues(newRow, newCol);

        // Vérification si un mur bloque le chemin
        if (targetCellItems.stream().anyMatch(element -> element instanceof Wall)) {
            System.out.println("Move invalid: Wall is blocking the way.");
            return false;
        }

        // Vérification si le mouvement est pour pousser une boîte
        if (targetCellItems.stream().anyMatch(element -> element instanceof Box)) {
            int nextRow = newRow + direction.getDeltaRow();
            int nextCol = newCol + direction.getDeltaCol();

            // Vérification si la boîte peut être poussée
            if (!canPushBox(nextRow, nextCol) || !isValidPosition(nextRow, nextCol)) {
                System.out.println("Move invalid: Cannot push the box.");
                return false;
            }

            // Récupérer la boîte de la cellule cible
            Box box = getBox(targetCellItems);

            if (box != null) {
                // Suppression de la boîte de la cellule cible
                targetCellItems.remove(box);

                // Ajout de la boîte déplacée dans la cellule suivante
                board.getGrid().getValues(nextRow, nextCol).add(box);
            }
        }


        // Déplacement du joueur vers la nouvelle position
        targetCellItems.add(new Player());

        // Suppression du joueur de sa cellule d'origine
        List<GameElement> playerCellItems = board.getGrid().getValues(playerCell.getLine(), playerCell.getCol());
        playerCellItems.removeIf(element -> element instanceof Player);

        // Incrémentation du compteur de mouvements
        incrementMoveCount();
        boxInTargetCountProperty().invalidate();

        gridState.addBoardState(board);
        return true;
    }

    public Box getBox(List<GameElement> targetCellItems){
        return (Box) targetCellItems.stream()
                .filter(element -> element instanceof Box)
                .findFirst()
                .orElse(null);
    }

    public boolean isValidPosition(int row, int col) {
        if (!board.getGrid().isValidPosition(row, col)) {
            System.out.println("Move invalid: Position is out of bounds.");
            return false;
        }
        return true;
    }


    private boolean canPushBox(int targetRow, int targetCol) {
        if (!board.getGrid().isValidPosition(targetRow, targetCol)) {
            return false;
        }

        List<GameElement> targetCellItems = board.getGrid().getValues(targetRow, targetCol);

        // Vérifie si la cellule cible contient uniquement une cible sans boîte
        boolean containsOnlyGoal = targetCellItems.size() == 2 && targetCellItems.get(1) instanceof Goal;

        // Vérifie si la cellule cible contient uniquement des sols
        boolean containsOnlyGround = targetCellItems.stream().allMatch(element -> element instanceof Ground);

        // La boîte peut être poussée si la cellule cible contient soit uniquement une cible
        // soit uniquement des sols
        return containsOnlyGoal || containsOnlyGround;
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

    //compteur de mouvement
    public void incrementMoveCount() {
        moveCount.set(moveCount.get() + 1);
    }

    public LongProperty moveCountProperty() {
        return moveCount;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void undo() {
        if (gridState.hasPreviousState()) {
            Board previousBoard = gridState.getPreviousState();
            if (previousBoard == null) {
                System.out.println("empty grid");
                return;
            }
            board.getGrid().copyFill(previousBoard.getGrid());

            // Décrémenter le compteur de mouvements (ou mettre à jour en conséquence)
            if (moveCount.get() > 0) {
                moveCount.set(moveCount.get() - 1);
            }
        }
    }


    public void redo() {
        if (gridState.hasNextState()){
            Board nextBoard = gridState.getNextState();
            if (nextBoard == null) {
                System.out.println("empty grid");
                return;
            }
            board.getGrid().copyFill(nextBoard.getGrid());

            // Incrémenter le compteur de mouvements (ou mettre à jour en conséquence)
            moveCount.set(moveCount.get() + 1);
        }
    }
}