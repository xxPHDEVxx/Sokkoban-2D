package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyObjectProperty;
import sokoban.model.Board;
import sokoban.model.CellValue;

public class CellViewModel {
    private final Board board;
    private final int line, col;

    CellViewModel(int line, int col, Board board) {
        this.board = board;
        this.line = line;
        this.col = col;
    }

    public void play(){
        board.play(line, col, this.getCellValue().getValue());
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

        return switch (element) {
            case WALL -> '#';
            case GOAL -> '.';
            case BOX -> '$';
            case BOX_ON_GOAL -> '*';
            case PLAYER -> '@';
            case GROUND -> ' ';
            case PLAYER_ON_GOAL -> '+';
        };
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
