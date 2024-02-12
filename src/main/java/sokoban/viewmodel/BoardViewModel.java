package sokoban.viewmodel;

import javafx.beans.binding.LongBinding;

public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;

    public BoardViewModel(Board board) {
        this.board = board;
        gridViewModel = new GridViewModel(board);
    }

    public static int gridWidth() {
        return Grid.getGridWidth();
    }

    public GridViewModel getGridViewModel() {
        return gridViewModel;
    }

    public LongBinding filledCellsCountProperty() {
        return board.filledCellsCountProperty();
    }

    public int maxFilledCells() {
        return Board.maxFilledCells();
    }
}
