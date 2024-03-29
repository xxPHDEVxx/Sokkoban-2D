package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sokoban.viewmodel.ToolViewModel;

import java.util.ArrayList;
import java.util.List;


public class Board {
    public static int MAX_FILLED_CELLS = 75;
    private static Grid grid = new Grid();
    private final BooleanBinding isFull;

    private SetProperty<String> errors = new SimpleSetProperty<>(FXCollections.observableSet());

    public Board(){
        isFull = grid.filledCellsCountProperty().isEqualTo(Board.MAX_FILLED_CELLS);
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

    public boolean isEmpty(int line, int col) {
        return grid.isEmpty(line, col);
        //appelation cohérente? car ground n'est pas vide
    }

    public SetProperty<String> validate(){


        int playerCount = 0;
        int targetCount = 0;
        int boxCount = 0;

        int width = grid.getGridWidth();
        int height = grid.getGridHeight();

        for (int line = 0; line < height; line++) {
            for (int col = 0; col < width; col++) {
                CellValue value = grid.getValue(line, col);

                switch (value) {
                    case PLAYER -> playerCount++;
                    case GOAL -> targetCount++;
                    case BOX -> boxCount++;
                    default -> {
                    }
                }
            }
        }

        if (playerCount != 1) {
            errors.add("- A player is required.");
        }

        if (targetCount < 1) {
            errors.add("- At least one target is required.");
        }

        if (boxCount < 1) {
            errors.add("- At least one box is required.");
        }
        if (targetCount != boxCount){
            errors.add("- Number of box and target must be equals.");
        }
        return errors;
    }



    public static void setGrid(Grid newGrid) {
        grid = newGrid;
    }

    public Grid getGrid() {
        return grid;
    }
}
