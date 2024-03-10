package sokoban.viewmodel;

import javafx.beans.binding.LongBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import sokoban.model.Board;
import sokoban.model.Grid;


public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;
    private final ListProperty<String> errorsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    public ListProperty<String> errorsProperty() {
        return board.validate();
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
    public static void newGridMenu(int width, int height){

        IntegerProperty otherWidthProperty = new SimpleIntegerProperty(width);;
        IntegerProperty otherHeightProperty = new SimpleIntegerProperty(height);;


        Grid.GRID_HEIGHTProperty().bind(otherHeightProperty);
        Grid.GRID_WIDTHProperty().bind(otherWidthProperty);


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

