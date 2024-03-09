package sokoban.viewmodel;

import javafx.beans.binding.LongBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import sokoban.model.Board;
import sokoban.model.Grid;

import java.io.File;
import java.util.List;

public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;
    private final ListProperty<String> errorsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ListProperty<String> errorsProperty() {
        return board.validate();
    }
    public boolean validateBoard() {
        List<String> errors = board.validate();
        errorsProperty.setAll(errors);
        return errors.isEmpty();
    }

    public BoardViewModel(Board board) {
        this.board = board;
        gridViewModel = new GridViewModel(board);
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

    public int maxFilledCells() {
        return Board.maxFilledCells();
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

