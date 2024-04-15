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
        CellValue current = grid.getValue(line, col);
        CellValue selected = ToolViewModel.getToolSelected();

        // Gestion de la superposition sur une cible (Goal)
        if (current == CellValue.GOAL) {
            if (selected == CellValue.BOX) {
                grid.play(line, col, CellValue.BOX_ON_GOAL);
            } else if (selected == CellValue.PLAYER) {
                if (isPlayerPlaced()) {
                    removeExistingPlayer();
                }
                grid.play(line, col, CellValue.PLAYER_ON_GOAL);
            }
        } else if (current == CellValue.GROUND || current == null) {
            // Placement direct sur le sol
            if (selected == CellValue.PLAYER) {
                if (isPlayerPlaced()) {
                    removeExistingPlayer();
                }
                grid.play(line, col, selected);
            } else {
                grid.play(line, col, selected);
            }
        } else if (current == CellValue.PLAYER) {
            // Permettre la superposition d'une cible sur un joueur
            if (selected == CellValue.GOAL) {
                grid.play(line, col, CellValue.PLAYER_ON_GOAL);
            }
        } else if (current == CellValue.BOX) {
            // Permettre la superposition d'une cible sur une boîte
            if (selected == CellValue.GOAL) {
                grid.play(line, col, CellValue.BOX_ON_GOAL);
            }
        } else {
            // Clic sans sélection ou avec une sélection non applicable, efface l'outil actuel
            if (selected == null) {
                if (current == CellValue.BOX_ON_GOAL || current == CellValue.PLAYER_ON_GOAL) {
                    grid.play(line, col, CellValue.GOAL); // Maintenir la cible si on enlève la boîte ou le joueur
                } else {
                    grid.play(line, col, CellValue.GROUND); // Sinon, retourner au sol
                }
            } else {
                if (selected == CellValue.PLAYER) {
                    if (isPlayerPlaced()) {
                        removeExistingPlayer();
                    }
                }
                grid.play(line, col, selected); // Place le nouvel outil sélectionné
            }
        }
    }



    public void removeTool(int line, int col, CellValue ground) {
        // Déterminez l'état actuel de la cellule avant de la réinitialiser
        CellValue currentValue = grid.getValue(line, col);

        // Condition pour réinitialiser la cellule à GOAL si elle contient PLAYER_ON_GOAL ou BOX_ON_GOAL
        if (currentValue == CellValue.PLAYER_ON_GOAL || currentValue == CellValue.BOX_ON_GOAL) {
            grid.remove(line, col, ground);
        } else {
            // Réinitialisez à GROUND pour tous les autres états
            grid.remove(line, col, ground);
        }
    }

    private void removeExistingPlayer() {
        for (int row = 0; row < grid.getGridHeight(); row++) {
            for (int col = 0; col < grid.getGridWidth(); col++) {
                CellValue cellValue = grid.getValue(row, col);
                if (cellValue == CellValue.PLAYER || cellValue == CellValue.PLAYER_ON_GOAL) {
                    grid.play(row, col, cellValue == CellValue.PLAYER_ON_GOAL ? CellValue.GOAL : CellValue.GROUND);
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
            case '*':
                return CellValue.BOX_ON_GOAL;
            case '+':
                return CellValue.PLAYER_ON_GOAL;
            default:
                return CellValue.GROUND;
        }
    }
    // Check le nombre de joueur sur la grille
    public boolean isPlayerPlaced() {
        return grid.playerCountProperty().get() > 0;
    }

}
