package sokoban.model;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Grid {
    public static int GRID_WIDTH = 15;
    public static int GRID_HEIGHT = 10;
    private final Cell[][] matrix;
    private final LongBinding filledCellsCount;

    Grid() {
        matrix = new Cell[GRID_WIDTH][GRID_HEIGHT];
        for (int i = 0; i < GRID_WIDTH; i++) {
            matrix[i] = new Cell[GRID_WIDTH];
            for (int j = 0; j < GRID_HEIGHT; j++) {
                matrix[i][j] = new Cell();
            }
        }

        filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> !cell.isEmpty())
                .count());
    }

    public static int getGridWidth() {
        return GRID_WIDTH;
    }

    public static int getGridHeight() {
        return GRID_HEIGHT;
    }

    ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col) {
        return matrix[line][col].valueProperty();
    }

    CellValue getValue(int line, int col) {
        return matrix[line][col].getValue();
    }

    void play(int line, int col, CellValue playerValue) {
        matrix[line][col].setValue(playerValue);
        filledCellsCount.invalidate();
    }

    public LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }

    public boolean isEmpty(int line, int col) {
        return matrix[line][col].isEmpty();
    }

    // Place la cellule de la valeur spécifiée à la position (line, col) sur la grille
    public void placeCell(int line, int col, CellValue cellValue) {
        if (isValidPosition(line, col)) {
            matrix[line][col].setValue(cellValue);
        }
    }

    // Vérifie si la position (line, col) est valide dans la grille
    private boolean isValidPosition(int line, int col) {
        return line >= 0 && line < GRID_HEIGHT && col >= 0 && col < GRID_WIDTH;
    }

}



