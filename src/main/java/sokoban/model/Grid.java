package sokoban.model;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Arrays;

public class Grid {
    private static int GRID_WIDTH = 15;
    private static int GRID_HEIGHT = 10;
    private final Cell[][] matrix;
    private LongBinding filledCellsCount;


    public Grid() {
        this(GRID_WIDTH, GRID_HEIGHT);
    }

    public Grid(int width, int height) {
        GRID_WIDTH = width;
        GRID_HEIGHT = height;
        matrix = new Cell[GRID_HEIGHT][];
        for (int i = 0; i < GRID_HEIGHT; ++i) {
            matrix[i] = new Cell[GRID_WIDTH];
            for (int j = 0; j < GRID_WIDTH; ++j) {
                matrix[i][j] = new Cell();
            }
        }
        setFilledCellsCount();
    }

    public void setFilledCellsCount() {
        filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> !cell.isEmpty())
                .count());
    }
    public static int getGridWidth(){return GRID_WIDTH;}
    public static int getGridHeight(){return GRID_HEIGHT;}

    public static void setGridWidth(int gridWidth) {
        GRID_WIDTH = gridWidth;
    }

    public static void setGridHeight(int gridHeight) {
        GRID_HEIGHT = gridHeight;
    }

    ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col) {

        return matrix[line][col].valueProperty();    }
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

