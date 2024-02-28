package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import sokoban.model.Board;
import sokoban.model.CellValue;

public class CellViewModel {
    private final Board board;
    private final int line,col;
    private final BooleanProperty hoverColor = new SimpleBooleanProperty(false);
    CellViewModel(int line, int col, Board board) {
        this.board = board;
        this.line = line;
        this.col = col;
    }
    public ReadOnlyObjectProperty<CellValue> valueProperty() {
        return board.valueProperty(line, col);
    }
    public boolean isEmpty() {
        return board.isEmpty(line, col);
    }


}
