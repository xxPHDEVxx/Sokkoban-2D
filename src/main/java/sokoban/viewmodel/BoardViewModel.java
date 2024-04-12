package sokoban.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.value.ObservableValue;
import sokoban.model.Board;
import sokoban.model.Grid;

import java.io.File;


public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;

    private static int countMove = 0;

    public BoardViewModel(Board board) {
        this.board = board;
        gridViewModel = new GridViewModel(board);
    }

    public GridViewModel getGridViewModel() {
        return gridViewModel;
    }

    public LongBinding filledCellsCountProperty() {
        return board.filledCellsCountProperty();
    }
    public LongBinding boxCountProperty() {
        return board.boxCountProperty();
    }
    public LongBinding goalCountProperty() {
        return board.goalCountProperty();
    }
    public LongBinding playerCountProperty() {
        return board.playerCountProperty();
    }
    public BooleanBinding rulesOKProperty() {
        return board.getRulesOK();
    }


    public static int maxFilledCells() {
        return Board.maxFilledCells();
    }
    public static int gridWidth() {
        return Grid.getGridWidth();
    }
    public static int gridHeight() {
        return Grid.getGridHeight();
    }

    public static void exitMenu(){System.exit(0);}
    public static void newGridMenu(int width, int height){
        Board.setGrid(new Grid(width, height));
    }
    public Grid openBoard(File file){
        Grid grid = board.open(file);
        return grid;
    }
    public static boolean isChanged(){
        return true;
    }
    public Grid getGrid(){
        return board.getGrid();
    }
    public GridViewModel getGridVM(){
        return gridViewModel;
    }



}