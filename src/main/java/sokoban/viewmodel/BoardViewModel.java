package sokoban.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sokoban.model.Board;
import sokoban.model.Grid;
import sokoban.view.BoardView;
import sokoban.view.GridView;
import sokoban.view.newGridView;

import java.io.File;
import java.util.List;

public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private GridView gridView;
    private final Board board;
    private final ListProperty<String> errorsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    public ListProperty<String> errorsProperty() {
        return board.validate();
    }

    public BoardViewModel(Board board) {
        this.board = board;
        this.gridView = gridView;
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
    public static void newGridMenu(int width, int height){
        int d = Grid.getGridHeight();
        int c = Grid.getGridWidth();
        System.out.println(d +""+ c);
        System.out.println(Board.MAX_FILLED_CELLS);

        Grid.GRID_WIDTH = width;
        Grid.GRID_HEIGHT = height;
        Grid grid = new Grid();
        Board.maxFilledCells();


        int a = Grid.getGridHeight();
        int b = Grid.getGridWidth();
        System.out.println(a +  "" + b);
        System.out.println(Board.MAX_FILLED_CELLS);

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

