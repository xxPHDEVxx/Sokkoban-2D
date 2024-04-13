package sokoban.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.value.ObservableValue;
import sokoban.model.Board;
import sokoban.model.Board4Design;
import sokoban.model.Grid;

import java.io.File;

// Board board remplacé ici par Board4Design pour l'adaptation.
// attention l'adaptation n'est là que pour dépanner et garder l'app fonctionnelle, une classe Board4DesignViewModel est nécessaire *Jamila
public class BoardViewModel {
    private final GridViewModel gridViewModel;
    private final Board4Design board;//*

    private static int countMove = 0;

    public BoardViewModel(Board4Design board) {//*
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
        return Board4Design.maxFilledCells();//*
    }
    public static int gridWidth() {
        return Grid.getGridWidth();
    }
    public static int gridHeight() {
        return Grid.getGridHeight();
    }

    public static void exitMenu(){System.exit(0);}
    public static void newGridMenu(int width, int height){
        Board4Design.setGrid(new Grid(width, height));//*
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