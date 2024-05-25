package sokoban.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.control.Label;
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

    public void put() {
        board.put(line, col);
    }

    public void removeTool() {board.removeTool(line, col);}

    public ReadOnlyListProperty<GameElement> valueProperty() {
        return board.valueProperty(line, col);
    }

    public ReadOnlyListProperty<GameElement> getCellValue() {
        return valueProperty();
    }

    protected static char getSymbolForElement(List<GameElement> elements) {
        if (elements.stream().anyMatch(element -> element instanceof Wall)) {
            return '#';
        } else if (elements.stream().anyMatch(element -> element instanceof Goal)
                && elements.size() < 3) {
            return '.';
        } else if (elements.stream().anyMatch(element -> element instanceof Box)
                && elements.size() < 3) {
            return '$';
        } else if ((elements.stream().anyMatch(element -> element instanceof Box))
                && (elements.stream().anyMatch(element -> element instanceof Goal))) {
            return '*';
        } else if (elements.stream().anyMatch(element -> element instanceof Player)
                && elements.size() < 3) {
            return '@';
        } else if ((elements.stream().anyMatch(element -> element instanceof Player))
                && (elements.stream().anyMatch(element -> element instanceof Goal))) {
            return '+';
        } else if (elements.stream().anyMatch(element -> element instanceof Ground)) {
            return ' ';
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

    public boolean isBox() {
        return (valueProperty().stream().anyMatch(element -> element instanceof Box));
    }

    public Label createBoxNumberLabel() {
        if (isBox()) {
            Box box = (Box) getCellValue().stream().filter(element -> element instanceof Box).findFirst().orElse(null);
            if (box != null && box.getNumberLabel() != null) {
                Label numberLabel = box.getNumberLabel();
                numberLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: black;" +
                        " -fx-font-weight: bold; -fx-background-color: white; -fx-padding: 30%; -fx-height: 50%");
                return numberLabel;
            }
        }
        return null;
    }
    /**
     * Replace boxes of the grid if there is a mushroom on this cell
     */
    public void isMushroom(){
        if (board.getGrid().getValues(line,col).stream().anyMatch(element -> element instanceof Mushroom))
            this.board.mushroomEffect();
    }

    public void placeMushroom(){
        board.mushroom(board.getGrid());
    }

    public boolean mushroomDisplay(){
       return board.mushroomDisplay(line,col);
    }

    public void addBoardHistory(){
        board.getGridState().addBoardState(board);
    }
}
