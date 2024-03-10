package sokoban.viewmodel;

import sokoban.model.Board;
import sokoban.model.Grid;

public class GridViewModel {
    private final Board board;
    GridViewModel(Board board) {this.board = board;}
    public CellViewModel getCellViewModel(int line, int col) {
        return new CellViewModel(line,col, board);
    }
}
