package sokoban.model;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyListProperty;
import sokoban.viewmodel.ToolViewModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board {
    public static int MAX_FILLED_CELLS = 75;
    private Grid4Design grid = new Grid4Design();
    private final BooleanBinding isFull;

    private  BooleanBinding countBoxOK;
    private  BooleanBinding countPlayerOK;
    private  BooleanBinding countGoalOK;
    private  BooleanBinding countGoalBoxOK;

    private BooleanBinding rulesOK;

    public Board(){
        isFull = grid.filledCellsCountProperty().isEqualTo(Board.MAX_FILLED_CELLS);
        configureBindings();
    }

    public  int maxFilledCells() {
        MAX_FILLED_CELLS = (grid.getGridHeight() * grid.getGridWidth()) / 2;
        return Board.MAX_FILLED_CELLS;
    }


    public void put(int line, int col) {
        List<GameElement> cellItems = grid.getElement(line, col);
        GameElement selected = ToolViewModel.getToolSelected();
        int size = cellItems.size();

        // Retirer le joueur existant si l'élément sélectionné est un joueur
        if (selected instanceof Player && isPlayerPlaced()) {
            removeExistingPlayer();
        }

        if (size <= 3) {
            if (cellItems.stream().anyMatch(element -> element instanceof Wall)) {
                handleWall(line, col, cellItems, selected);
            } else if (size == 2 && cellItems.get(1) instanceof Goal) {
                handleOnlyGoal(line, col, cellItems, selected);
            } else if (cellItems.stream().anyMatch(element -> element instanceof Goal)) {
                if ( size > 2 && cellItems.get(1) instanceof Goal) {
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
        grid.put(line, col, new Ground());
        grid.put(line, col, selected);
    }

    private void handleWall(int line, int col, List<GameElement> cellItems, GameElement selected){
        if (selected instanceof Ground) {
            removeCellElement(line,col, cellItems.get(cellItems.size()-1));
        } else {
            removeCellElement(line,col, cellItems.get(cellItems.size()-1));
            grid.put(line, col, selected);
        }
    }

    private void handleOnlyGoal(int line, int col, List<GameElement> cellItems, GameElement selected) {
        if (selected instanceof Box || selected instanceof Player) {
            grid.put(line, col, selected);
        } else if (selected instanceof Wall) {
            cellItems.remove(cellItems.get(cellItems.size() - 1));
            grid.put(line, col, selected);
        } else if (selected instanceof Ground) {
            cellItems.remove(cellItems.get(cellItems.size() - 1));
        }
    }

    private void handleGoalAndOther(int line, int col, List<GameElement> cellItems, GameElement selected) {
        if (selected instanceof Box || selected instanceof Player) {
            removeCellElement(line, col, cellItems.get(cellItems.size() - 1));
            grid.put(line, col, selected);
        } else if (selected instanceof Ground) {
            cellItems.clear();
            grid.put(line, col, new Ground());
        } else {
            cellItems.clear();
            grid.put(line, col, new Ground());
            grid.put(line, col, selected);
        }
    }


    public void removeTool(int line, int col, GameElement ground) {
        List<GameElement> cellItems = grid.getElement(line, col);
        // Déterminez l'état actuel de la cellule avant de la réinitialiser
        GameElement currentValue = grid.getValue(line, col).get(cellItems.size() - 1);
        if (currentValue instanceof Box)
            Box.reduceGlobalNumber();
        removeCellElement(line, col, currentValue);
    }

    private void removeExistingPlayer() {
        for (int row = 0; row < grid.getGridHeight(); row++) {
            for (int col = 0; col < grid.getGridWidth(); col++) {
                List<GameElement> cellItems = grid.getElement(row, col);
                for (int i = 0; i < cellItems.size(); i++) {
                    if (cellItems.get(i) instanceof Player) {
                        removeCellElement(row, col, cellItems.get(i));
                        return; // Ajouté pour sortir dès que le joueur est trouvé et supprimé.
                    }
                }
            }
        }
    }

    public void putElement(int line, int col, GameElement element){
        grid.put(line, col, element);
    }

    public void removeCellElement(int line, int col, GameElement element) {
        grid.remove(line, col, element);
    }


    public Boolean isFull() {
        return isFull.get();
    }

    public ReadOnlyListProperty<GameElement> valueProperty(int line, int col) {
        return grid.valueProperty(line, col);
    }

    public LongBinding filledCellsCountProperty() {
        return grid.filledCellsCountProperty();
    }

    public  LongBinding boxCountProperty() {
        return grid.boxCountProperty();
    }
    public  LongBinding goalCountProperty() {
        return grid.goalCountProperty();
    }
    public LongBinding boxInTargetCountProperty () {
        return grid.boxInTargetCountProperty();
    }

    public LongBinding playerCountProperty(){
        return grid.playerCountProperty();
    }

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

    public  void setGrid(Grid4Design newGrid) {
        grid = newGrid;
    }
    public Grid4Design getGrid() {
        return grid;
    }
    public Board getBoard(){
        return this;
    }
    public Grid4Design open(File file){
        try (Scanner scanner = new Scanner(file)) {
            int row = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    // Parcourir chaque caractère de la ligne
                    for (int col = 0; col < line.length(); col++) {
                        char symbol = line.charAt(col);
                        // Convertir le caractère en CellValue et ajouter à la grille
                        List<GameElement> items = convertSymbolToCellValue(symbol);
                        grid.setValue(row, col, items.get(0));
                        if (items.size() > 1) {
                            grid.setValue(row, col, items.get(1));
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
    // Check le nombre de joueur sur la grille
    public boolean isPlayerPlaced() {
        return grid.playerCountProperty().get() > 0;
    }

    public Board copy(){
        Board clonedBoard = new Board();
        Grid4Design clonedGrid = this.getGrid().copy();

        // Définir la grille clonée dans le Board cloné
        clonedBoard.grid = clonedGrid;

        // Configurer les liaisons
        clonedBoard.configureBindings();

        return clonedBoard;
    }
}
