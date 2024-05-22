package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import sokoban.model.*;

import java.io.File;
import java.util.List;
import java.util.Random;

public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private GridState gridState;
    private  Board board;
    private static BooleanProperty isChanged = new SimpleBooleanProperty(false);
    private final LongProperty moveCount = new SimpleLongProperty(0);
    private Grid4Design saveGridDesign;

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
        return Grid.getGridWidth();
    }
    public static int gridHeight() {
        return Grid.getGridHeight();
    }

    public static void exitMenu(){System.exit(0);}
    public void newGridMenu(int width, int height){
        board.setGrid(new Grid4Design(width, height));
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

    public Board getBoard() {
        return board;
    }

    // Déplacements joueur

    // Commentaires dispo pour suivi car longue méthode
    public boolean movePlayer(Direction direction) {

        // check fin de partie
        if (endGame()){
            return false;
        }

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

        // check fin de partie
        if (endGame()){
            return;
        }

        if (gridState.hasPreviousState()) {
            Board previousBoard = gridState.getPreviousState();
            if (previousBoard == null) {
                System.out.println("empty grid");
                return;
            }
            board.getGrid().copyFill(previousBoard.getGrid());

            // Décrémenter le compteur de mouvements (ou mettre à jour en conséquence)
            if (moveCount.get() > 0) {
                moveCount.set(moveCount.get() + 5);
            }
        }
    }


    public void redo() {

        // check fin de partie
        if (endGame()){
            return;
        }

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

    public Grid4Play gridGame(){
        Grid4Play gridGame = new Grid4Play(gridWidth(),gridHeight());
        gridGame.copyFill(board.getGrid());
        boxNumber(gridGame);
        mushroom(gridGame);
        return gridGame;
    }

    public void boxNumber(Grid grid){
        int number = 0;
        for (int i = 0; i < Grid.getGridHeight(); ++i) {
            for (int j = 0; j < Grid.getGridWidth(); ++j) {
                List<GameElement> targetCellItems = grid.getValues(i, j);
                for (GameElement element : targetCellItems){
                    if (element instanceof Box){
                        number++;
                        ((Box) element).setNumberLabel(new Label(String.valueOf(number)));
                    }
                }
            }
        }
    }

    // Mushroom feat

    /**
     * To place mushroom on the grid
     * @param grid
     * @return
     */
    public void mushroom(Grid grid){
        Random random = new Random();
        Cell cell = null;

        while (cell == null) {
            int i = random.nextInt(Grid.getGridHeight());
            int j = random.nextInt(Grid.getGridWidth());
            List<GameElement> cellItems = grid.valueProperty(i,j);
            if (cellItems.stream().allMatch(element -> element instanceof Ground) && cellItems.size() == 1) {
                cellItems.add(new Mushroom());
                cellItems.add(new Ground());
                cell = grid.getCell(i,j);
            }
        }
    }

    /**
     * To hide the mushroom
     * @return boolean to know if it's visible or not
     */
    public boolean hideOrShow(){
        boolean visible = mushVisible();
        for (int i = 0; i < this.getGrid().getGridHeight(); i++) {
            for (int j = 0; j < this.getGrid().getGridWidth(); j++) {
                List<GameElement> cellItems = board.valueProperty(i,j);
                int last = cellItems.size() - 1;
                if (cellItems.stream().anyMatch(element -> element instanceof Mushroom)) {
                    if (cellItems.get(last) instanceof Ground) {
                        cellItems.remove(last);
                        visible = mushVisible();
                    }
                    else {
                        cellItems.add(new Ground());
                    }
                }
            }
        }
        return visible;
    }

    public boolean mushVisible(){
        boolean visible = true;
        for (int i = 0; i < this.getGrid().getGridHeight(); i++) {
            for (int j = 0; j < this.getGrid().getGridWidth(); j++) {
                List<GameElement> cellItems = board.valueProperty(i,j);
                int last = cellItems.size() - 1;
                if (cellItems.stream().anyMatch(element -> element instanceof Mushroom)) {
                    if (cellItems.get(last) instanceof Ground) {
                        visible = false;
                    }
                }
            }
        }
        return visible;
    }


    public void saveGridDesign(){
        saveGridDesign = new Grid4Design(gridWidth(),gridHeight());
        saveGridDesign.copyFill(board.getGrid());
    }

    public Grid4Design getSaveGridDesign() {
        return saveGridDesign;
    }

    public boolean endGame(){
        if (this.boxInTargetCountProperty().get() == this.goalCountProperty().get()){
            if (gridState.getBoardHistory() != null) {
                gridState.getBoardHistory().clear();
                gridState.setCurrentIndex(0);
                return true;
            }
        }
        if (mushVisible())
            return true;
        return false;
    }
}