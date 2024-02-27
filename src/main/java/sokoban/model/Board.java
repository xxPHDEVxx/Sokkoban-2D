package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyObjectProperty;

public class Board {
    public static int MAX_FILLED_CELLS = 75;
    private final Grid grid = new Grid();
    private final BooleanBinding isFull;
    public Board(){
        isFull = grid.filledCellsCountProperty().isEqualTo(Board.MAX_FILLED_CELLS);
    }
    public CellValue play(int line, int col){
        if (grid.getValue(line, col) == CellValue.GROUND && isFull())
            return CellValue.GROUND;
        // grid.play(line, col, grid.getValue(line, col) == CellValue.EMPTY ? CellValue.X : CellValue.EMPTY);
        // return grid.getValue(line, col);
        // a adapter avec les enum du sokoban
        return CellValue.GROUND;
    }
    public static int maxFilledCells(){
        MAX_FILLED_CELLS = (Grid.getGridHeight() + Grid.getGridWidth())/2;
        return Board.MAX_FILLED_CELLS;
        // adapter par rapport au validation métier
    }
    public Boolean isFull(){
        return isFull.get();
    }
    public ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col) {
        return grid.valueProperty(line, col);
    }

    public LongBinding filledCellsCountProperty() {
        return grid.filledCellsCountProperty();
    }
    public LongBinding err(){
        return grid.err();
    }
    public boolean isEmpty(int line, int col) {
        return grid.isEmpty(line, col);
        //appelation cohérente? car ground n'est pas vide
    }
}