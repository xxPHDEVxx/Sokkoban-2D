package sokoban.model;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import java.util.Arrays;

public class Grid {
    public static int GRID_WIDTH = 15;
    public static int GRID_HEIGHT = 10;
    private final Cell[][] matrix;
    private final LongBinding filledCellsCount;
    private final LongBinding err;

    Grid() {
        matrix = new Cell[GRID_HEIGHT][];
        for (int i = 0; i < GRID_HEIGHT; ++i) {
            matrix[i] = new Cell[GRID_WIDTH];
            for (int j = 0; j < GRID_WIDTH; ++j) {
                matrix[i][j] = new Cell();
            }
        }

        err = Bindings.createLongBinding(()-> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> !cell.isEmpty())
                .count());
        // mettre les errors

        filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> !cell.isEmpty())
                .count());
    }
    public static int getGridWidth(){return GRID_WIDTH;}
    public static int getGridHeight(){return GRID_HEIGHT;}

    ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col) {return matrix[line][col].valueProperty();}
    CellValue getValue(int line, int col) {return  matrix[line][col].getValue();}
    void play(int line, int col, CellValue playerValue) {
        matrix[line][col].setValue(playerValue);
        filledCellsCount.invalidate();
    }

    public LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }
    public LongBinding err(){
        return err;
    }
    public boolean isEmpty(int line, int col) {return matrix[line][col].isEmpty();}
}

