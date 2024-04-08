package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SetPropertyBase;
import sokoban.model.Board;
import sokoban.model.CellValue;

public class CellViewModel {
    private final Board board;
    private final int line,col;
    CellViewModel(int line, int col, Board board) {
        this.board = board;
        this.line = line;
        this.col = col;
    }

    public void play(){
        board.play(line, col);
        board.validate();
    }

    public ReadOnlyObjectProperty<CellValue> valueProperty() {
        return board.valueProperty(line, col);
    }
    public boolean isEmpty() {
        return board.isEmpty(line, col);
    }
    public Board getBoard() {
        return board;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

}
