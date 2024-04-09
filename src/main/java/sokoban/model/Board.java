package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import sokoban.viewmodel.ToolViewModel;


public class Board {
    public static int MAX_FILLED_CELLS = 75;
    private static Grid grid = new Grid();
    private final BooleanBinding isFull;

    private BooleanBinding countBoxOK;
    private BooleanBinding countPlayerOK;

    private BooleanBinding countGoalOK;
    private BooleanBinding countGoalBoxOK;


    public Board(){
        isFull = grid.filledCellsCountProperty().isEqualTo(Board.MAX_FILLED_CELLS);
        configureBindings();
    }

    public static int maxFilledCells() {
        MAX_FILLED_CELLS = (Grid.getGridHeight() * Grid.getGridWidth()) / 2;
        return Board.MAX_FILLED_CELLS;
    }


    public void play(int line, int col){
        grid.play(line, col, grid.getValue(line, col) != (CellValue.GROUND) ? CellValue.GROUND : ToolViewModel.getToolSelected());

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

    public LongBinding boxCountProperty() {
        return grid.boxCountProperty();
    }
    public LongBinding goalCountProperty() {
        return grid.goalCountProperty();
    }

    public LongBinding playerCountProperty(){
        return grid.playerCountProperty();
    }
    public boolean isEmpty(int line, int col) {
        return grid.isEmpty(line, col);
        //appelation cohérente? car ground n'est pas vide
    }

    public void configureBindings() {
        countBoxOK = grid.boxCountProperty().greaterThan(1);
        countGoalOK= grid.goalCountProperty().greaterThan(1);
        countPlayerOK = grid.playerCountProperty().greaterThan(1);
        countGoalBoxOK = grid.boxCountProperty().isEqualTo(grid.goalCountProperty());


    }

    //public BooleanBinding isCountBoxOK() {
    //    return Bindings.createBooleanBinding(countBoxOK);
  //  }


    public static void setGrid(Grid newGrid) {
        grid = newGrid;
    }

    public Grid getGrid() {
        return grid;
    }
}
