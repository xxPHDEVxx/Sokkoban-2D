package sokoban.model;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Grid {
    public int GRID_WIDTH = 10;
    public int GRID_HEIGHT = 15;
    private final Cell[][] matrix;
    private final LongBinding filledCellsCount;

    Grid() {
        matrix = new Cell[GRID_WIDTH][GRID_HEIGHT];
        for (int i = 0; i < GRID_WIDTH; i++) {
            matrix[i]=new Cell[GRID_WIDTH];
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
    public int getGridWidth(){return GRID_WIDTH;}
    public int getGridHeight(){return GRID_HEIGHT;}

    ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col) {return matrix[line][col].valueProperty();}
    CellValue getValue(int line, int col) {return  matrix[line][col].getValue();}
    void play(int line, int col, CellValue playerValue) {
        matrix[line][col].setValue(playerValue);
        filledCellsCount.invalidate();
    }

    public LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }
    public boolean isEmpty(int line, int col) {return matrix[line][col].isEmpty();}
}

