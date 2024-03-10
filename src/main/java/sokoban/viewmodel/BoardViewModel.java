package sokoban.viewmodel;

import javafx.beans.binding.LongBinding;
import sokoban.model.Board;
import sokoban.model.CellValue;
import sokoban.model.Grid;

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
    public static int gridHeight() {
        return Grid.getGridHeight();
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

    public void placeTool(int row, int col, CellValue tool) {
        board.getGrid().placeTool(row, col, tool);
    }
}
