package sokoban.viewmodel;

import sokoban.model.Board;
import sokoban.model.CellValue;

public class GridViewModel {
    private final Board board;
    GridViewModel(Board board) {this.board = board;}
    public CellViewModel getCellViewModel(int line, int col) {
        return new CellViewModel(line,col, board);
    }
    public void placeTool(int row, int col, CellValue tool) {
        board.getGrid().placeTool(row, col, tool);
    }
}
