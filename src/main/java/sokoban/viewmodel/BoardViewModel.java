package sokoban.viewmodel;

import javafx.beans.binding.LongBinding;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import sokoban.model.Board;
import sokoban.model.Grid;

import java.io.File;
import java.util.List;

public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;
    private List<String> errors;

    public BoardViewModel(Board board) {
        this.board = board;
        gridViewModel = new GridViewModel(board);
        validateBoard();
    }
    private void validateBoard() {
        errors = board.validate();
    }

    public static int gridWidth() {
        return Grid.getGridWidth();
    }
    public static int gridHeight() {
        return Grid.getGridHeight();
    }

    public GridViewModel getGridViewModel() {
        return gridViewModel;
    }

    public LongBinding filledCellsCountProperty() {
        return board.filledCellsCountProperty();
    }

    public LongBinding error(){
        return board.err();
    }
    public int maxFilledCells() {
        return Board.maxFilledCells();
    }
    public List<String> getErrors() {
        return errors;
    }
    public static void exitMenu(){System.exit(0);}
    public static void newBoardMenu(){
        Board board = new Board();
        //voir si ça créer nouveau et dialogue box pour confirmer
        }
    public static void openBoard(){
        // gérer ce qu'il doit faire avec le fichier
    }
    public static void saveMenu(){
        //save
    }

    public static boolean isChanged(){
        return true;
    }
}

