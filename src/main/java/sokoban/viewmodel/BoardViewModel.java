package sokoban.viewmodel;

import javafx.beans.binding.LongBinding;
import sokoban.model.Board;
import sokoban.model.Grid;


public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board board;


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

