package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.*;

import java.util.List;

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

    protected static char getSymbolForElement(List<GameElement> elements) {
        if (elements.stream().anyMatch(element -> element instanceof Wall)) {
            return '#';
        } else if (elements.stream().anyMatch(element -> element instanceof Goal) && elements.size() < 3) {
            return '.';
        } else if (elements.stream().anyMatch(element -> element instanceof Box) && elements.size() < 3) {
            return '$';
        } else if (elements.stream().anyMatch(element -> element instanceof Box && element instanceof Goal)) {
            return '*';
        } else if (elements.stream().anyMatch(element -> element instanceof Player) && elements.size() < 3) {
            return '@';
        } else if (elements.stream().anyMatch(element -> element instanceof Player && element instanceof Goal)) {
            return ' ';
        } else if (elements.stream().anyMatch(element -> element instanceof Ground)) {
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
