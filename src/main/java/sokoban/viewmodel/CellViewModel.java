package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyObjectProperty;
import sokoban.model.*;

public class CellViewModel {
    private final Board board;
    private final int line, col;

    CellViewModel(int line, int col, Board board) {
        this.board = board;
        this.line = line;
        this.col = col;
    }

    public void play(){
        board.play(line, col);
    }

    public void removeTool(){
        board.removeTool(line, col, new Ground());
    }
    public ReadOnlyObjectProperty<GameElement> valueProperty() {
        return board.valueProperty(line, col);
    }

    public ReadOnlyObjectProperty<GameElement> value2Property() {
        return board.value2Property(line, col);
    }

    public ReadOnlyObjectProperty<GameElement> getCellValue(){
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
    public GameElement getCurrentElement() {
        return board.getElement(line, col);
    }

}
