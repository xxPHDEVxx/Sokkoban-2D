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

// Classe ajustée pour l'edition des grilles, ne pas oublier de lui faire un viewModel et une view séparées *Jamila
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


    public void play(int line, int col, CellValue current) {
        // permet la superposition de box et player sur goal
        if (current == CellValue.GOAL){
            if (ToolViewModel.getToolSelected() == CellValue.BOX)
                grid.play(line, col, grid.getValue(line, col) != (CellValue.GOAL) ? CellValue.GOAL : CellValue.BOX_ON_GOAL);
            else if(ToolViewModel.getToolSelected() == CellValue.PLAYER){
                if (isPlayerPlaced()) {
                    removeExistingPlayer();
                }
                grid.play(line, col, grid.getValue(line, col) != (CellValue.GOAL) ? CellValue.GOAL : CellValue.PLAYER_ON_GOAL);
            }

        } else {
            if (ToolViewModel.getToolSelected() == CellValue.PLAYER) {
                // Vérifier s'il y a déjà un joueur sur la grille et le supprimer.
                if (isPlayerPlaced()) {
                    removeExistingPlayer();
                }
            }
            grid.play(line, col, grid.getValue(line, col) != (CellValue.GROUND) ? CellValue.GROUND : ToolViewModel.getToolSelected());
        }
    }

    private void removeExistingPlayer() {
        for (int row = 0; row < grid.getGridHeight(); row++) {
            for (int col = 0; col < grid.getGridWidth(); col++) {
                CellValue cellValue = grid.getValue(row, col);
                if (cellValue == CellValue.PLAYER || cellValue == CellValue.PLAYER_ON_GOAL) {
                    grid.setValue(row, col, cellValue == CellValue.PLAYER_ON_GOAL ? CellValue.GOAL : CellValue.GROUND);
                    return; // Ajouté pour sortir dès que le joueur est trouvé et supprimé.
                }
            }
        }
    }

    public Boolean isFull() {
        return isFull.get();
    }

    public ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col) {
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
                        CellValue cellValue = convertSymbolToCellValue(symbol);
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
    private static CellValue convertSymbolToCellValue(char symbol) {
        switch (symbol) {
            case '#':
                return CellValue.WALL;
            case '.':
                return CellValue.GOAL;
            case '$':
                return CellValue.BOX;
            case '@':
                return CellValue.PLAYER;
            default:
                return CellValue.GROUND;
        }
    }
    // Check le nombre de joueur sur la grille
    public boolean isPlayerPlaced() {
        return grid.playerCountProperty().get() > 0;
    }

}
