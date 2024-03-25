package sokoban.model;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Arrays;

public class Grid {

    private static IntegerProperty GRID_WIDTH = new SimpleIntegerProperty(15);
    private static IntegerProperty GRID_HEIGHT = new SimpleIntegerProperty(10);
    private final Cell[][] matrix;
    private LongBinding filledCellsCount;


    public Grid() {
        matrix = new Cell[GRID_HEIGHT.get()][];
        for (int i = 0; i < GRID_HEIGHT.get(); ++i) {
            matrix[i] = new Cell[GRID_WIDTH.get()];
            for (int j = 0; j < GRID_WIDTH.get(); ++j) {
                            matrix[i][j] = new Cell();
                        }
                    }
                    setFilledCellsCount();

                }


                public void setFilledCellsCount () {
                    filledCellsCount = Bindings.createLongBinding(() -> Arrays
                            .stream(matrix)
                            .flatMap(Arrays::stream)
                            .filter(cell -> !cell.isEmpty())
                            .count());
                }
                public static int getGridWidth () {
                    return GRID_WIDTH.get();
                }
                public static int getGridHeight () {
                    return GRID_HEIGHT.get();
                }
                ReadOnlyObjectProperty<CellValue> valueProperty ( int line, int col){
                    return matrix[line][col].valueProperty();
                }

                CellValue getValue ( int line, int col){
                    return matrix[line][col].getValue();
                }

                void play ( int line, int col, CellValue playerValue){
                    matrix[line][col].setValue(playerValue);
                    filledCellsCount.invalidate();
                }

                public LongBinding filledCellsCountProperty () {
                    return filledCellsCount;
                }

                public static IntegerProperty GRID_WIDTHProperty () {
                    return GRID_WIDTH;
                }

                public static IntegerProperty GRID_HEIGHTProperty () {
                    return GRID_HEIGHT;
                }

                public boolean isEmpty ( int line, int col){
                    return matrix[line][col].isEmpty();
                }

                // Vérifie si la position (line, col) est valide dans la grille
                private boolean isValidPosition ( int line, int col){
                    return line >= 0 && line < GRID_HEIGHT.getValue() && col >= 0 && col < GRID_WIDTH.getValue();
                }
}

