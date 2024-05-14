package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import sokoban.model.*;

public class CellViewModel {
    private final Board board;
    private final int line, col;

    CellViewModel(int line, int col, Board board) {
        this.board = board;
        this.line = line;
        this.col = col;
    }

    public void put(){
        board.put(line, col);
    }

    public void removeTool(){
        board.removeTool(line, col, new Ground());
    }
    public ReadOnlyListProperty<GameElement> valueProperty() {
        return board.valueProperty(line, col);
    }

    public ReadOnlyListProperty<GameElement> getCellValue(){
        return valueProperty();
    }

    protected static char getSymbolForElement(GameElement element) {
        if (element instanceof Wall) {
            return '#';
        } else if (element instanceof Goal) {
            return '.';
        } else if (element instanceof Box) {
            return '$';
        } else if (element instanceof BoxOnGoal) {
            return '*';
        } else if (element instanceof Player) {
            return '@';
        } else if (element instanceof Ground) {
            return ' ';
        } else if (element instanceof PlayerOnGoal) {
            return '+';
        } else {
            return ' ';
        }
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
