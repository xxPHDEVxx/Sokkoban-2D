package sokoban.model;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import sokoban.model.CellValue;
import sokoban.model.Grid;
import sokoban.viewmodel.ToolViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Board {
    public static int MAX_FILLED_CELLS = 75;
    private static Grid grid = new Grid();
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

    public static int maxFilledCells() {
        MAX_FILLED_CELLS = (Grid.getGridHeight() * Grid.getGridWidth()) / 2;
        return Board.MAX_FILLED_CELLS;
    }


    public void play(int line, int col) {
        GameElement current = grid.getElement(line, col);
        GameElement selected = ToolViewModel.getToolSelected();
        removePlayerIfNeeded(selected);

        // Gestion des superpositions et des remplacements
        handleElementPlacement(line, col, current, selected);
    }

    private void removePlayerIfNeeded(GameElement selected) {
        if (selected instanceof Player && isPlayerPlaced()) {
            removeExistingPlayer();
        }
    }

    private void handleElementPlacement(int line, int col, GameElement current, GameElement selected) {
        if (selected instanceof Player) {
            grid.play(line, col, new Player());
        }
        if (current instanceof Goal) {
            placeOnGoal(line, col, selected);
        } else if (current instanceof PlayerOnGoal || current instanceof BoxOnGoal) {
            restoreOrReplaceOnGoal(line, col, selected);
        } else {
            grid.play(line, col, selected != null ? selected : new Ground());
        }
    }

    private void placeOnGoal(int line, int col, GameElement selected) {
        if (selected instanceof Box) {
            grid.play(line, col, new BoxOnGoal());
        } else if (selected instanceof Player) {
            grid.play(line, col, new PlayerOnGoal());
        } else {
            grid.play(line, col, selected); // Remplace l'élément actuel par le nouvel élément sélectionné
        }
    }

    private void restoreOrReplaceOnGoal(int line, int col, GameElement selected) {
        if (selected == null || selected instanceof Ground) {
            grid.play(line, col, new Goal()); // Restaure la cible si l'outil est désélectionné
        } else {
            grid.play(line, col, selected); // Remplace l'outil sur la cible
        }
    }





    public void removeTool(int line, int col, GameElement ground) {
        // Déterminez l'état actuel de la cellule avant de la réinitialiser
        GameElement currentValue = grid.getValue(line, col);
        // Condition pour réinitialiser la cellule à GOAL si elle contient PLAYER_ON_GOAL ou BOX_ON_GOAL
        if (currentValue instanceof PlayerOnGoal || currentValue instanceof BoxOnGoal) {
            grid.remove(line, col, ground);
        } else {
            // Réinitialisez à GROUND pour tous les autres états
            grid.remove(line, col, ground);
        }
    }

    private void removeExistingPlayer() {
        for (int row = 0; row < grid.getGridHeight(); row++) {
            for (int col = 0; col < grid.getGridWidth(); col++) {
                GameElement cellValue = grid.getValue(row, col);
                if (cellValue instanceof Player || cellValue instanceof PlayerOnGoal) {
                    grid.play(row, col, cellValue instanceof PlayerOnGoal ? new Goal() : new Ground());
                    return; // Ajouté pour sortir dès que le joueur est trouvé et supprimé.
                }
            }
        }
    }

    public Boolean isFull() {
        return isFull.get();
    }

    public ReadOnlyObjectProperty<GameElement> valueProperty(int line, int col) {
        return grid.valueProperty(line, col);
    }

    public LongBinding filledCellsCountProperty() {
        return grid.filledCellsCountProperty();
    }

    public static LongBinding boxCountProperty() {
        return grid.boxCountProperty();
    }
    public static LongBinding goalCountProperty() {
        return grid.goalCountProperty();
    }
    public LongBinding boxInTargetCountProperty () {
        return grid.boxInTargetCountProperty();
    }

    public LongBinding playerCountProperty(){
        return grid.playerCountProperty();
    }
    public boolean isEmpty(int line, int col) {
        return grid.isEmpty(line, col);
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

    public static void setGrid(Grid newGrid) {
        grid = newGrid;
    }
    public Grid getGrid() {
        return grid;
    }
    public Board getBoard(){
        return this;
    }
    public Grid open(File file){
        try (Scanner scanner = new Scanner(file)) {
            int row = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    // Parcourir chaque caractère de la ligne
                    for (int col = 0; col < line.length(); col++) {
                        char symbol = line.charAt(col);
                        // Convertir le caractère en CellValue et ajouter à la grille
                        GameElement cellValue = convertSymbolToCellValue(symbol);
                        grid.setValue(row, col, cellValue);
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
    private static GameElement convertSymbolToCellValue(char symbol) {
        switch (symbol) {
            case '#':
                return new Wall();
            case '.':
                return new Ground();
            case '$':
                return new Goal();
            case '@':
                return new Player();
            case '*':
                return new BoxOnGoal();
            case '+':
                return new PlayerOnGoal();
            default:
                return new Ground();
        }
    }
    // Check le nombre de joueur sur la grille
    public boolean isPlayerPlaced() {
        return grid.playerCountProperty().get() > 0;
    }

    public GameElement getElement(int line, int col) {
        return grid.getElement(line, col);
    }
}
