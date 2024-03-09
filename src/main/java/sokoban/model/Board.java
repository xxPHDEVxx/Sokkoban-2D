package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static int MAX_FILLED_CELLS = 75;
    private static final Grid grid = new Grid();
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

    public boolean isEmpty(int line, int col) {
        return grid.isEmpty(line, col);
        //appelation cohérente? car ground n'est pas vide
    }
    public ListProperty<String> validate(){
        ListProperty<String> errors = new SimpleListProperty<>(FXCollections.observableArrayList());

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
}
