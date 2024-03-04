package sokoban.viewmodel;

import javafx.beans.binding.LongBinding;
import sokoban.model.Board;
import sokoban.model.Grid;

import java.util.List;

public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;
    private List<String> errors;

    public BoardViewModel(Board board) {
        this.board = board;
        gridViewModel = new GridViewModel(board);
        validateBoard();
    }
    private void validateBoard() {
        errors = board.validate();
    }

    public static int gridWidth() {
        return Grid.getGridWidth();
    }
    public static int gridHeight() {
        return Grid.getGridHeight();
    }

    public GridViewModel getGridViewModel() {
        return gridViewModel;
    }

    public LongBinding filledCellsCountProperty() {
        return board.filledCellsCountProperty();
    }

    public LongBinding error(){
        return board.err();
    }
    public int maxFilledCells() {
        return Board.maxFilledCells();
    }
    public List<String> getErrors() {
        return errors;
    }
}

