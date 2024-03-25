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

    //private static IntegerProperty GRID_WIDTH = new SimpleIntegerProperty(15);
    //private static IntegerProperty GRID_HEIGHT = new SimpleIntegerProperty(10);
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
    void play(int line, int col, CellValue playerValue) {
        matrix[line][col].setValue(playerValue);
        filledCellsCount.invalidate();
    }

    public LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }
    public boolean isEmpty(int line, int col) {return matrix[line][col].isEmpty();}

    public CellValue getValue ( int line, int col){
        return matrix[line][col].getValue();
    }

    /*public static IntegerProperty GRID_WIDTHProperty () {
        return GRID_WIDTH;
    }

    public static IntegerProperty GRID_HEIGHTProperty () {
        return GRID_HEIGHT;
    }

     */

    // Vérifie si la position (line, col) est valide dans la grille
    private boolean isValidPosition ( int line, int col){
        return line >= 0 && line < GRID_HEIGHT && col >= 0 && col < GRID_WIDTH;
    }

    public void placeTool ( int row, int col, CellValue tool){
        if (isValidPosition(row, col)) {
            matrix[row][col].setValue(tool);
        }
    }
    public void setValue(int row, int col, CellValue value) {
        if (isValidPosition(row, col)) {
            matrix[row][col].getCell().setValue(value);
        } else {
            throw new IllegalArgumentException("Invalid row or column index");
        }
    }

}