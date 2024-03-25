package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.Cell;
import sokoban.model.Board;
import sokoban.model.CellValue;
import sokoban.view.CellView;

public class CellViewModel {
    private final Board board;
    private final int line, col;

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
    public ReadOnlyObjectProperty<CellValue> getCellValue(){
        return valueProperty();
    }

    protected static char getSymbolForElement(CellValue element) {

        switch (element) {
            case WALL:
                return '#';
            case GOAL:
                return '.';
            case BOX:
                return '$';
            //case BOX,GOAL: return '*';
            case PLAYER:
                return '@';
            case GROUND:
                return 'a';
            //case PLAYER,GOAL: return '+';
            default:
                return ' ';
        }
    }
}
