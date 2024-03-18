package sokoban.viewmodel;

import sokoban.model.Board;
import sokoban.model.CellValue;
import sokoban.model.Grid;
import sokoban.model.Cell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GridViewModel {
    private final Board board;
    GridViewModel(Board board) {this.board = board;}
    public CellViewModel getCellViewModel(int line, int col) {
        return new CellViewModel(line,col, board);
    }
    public void saveMenu(Grid grid, File selectedFile){

        try (PrintWriter writer = new PrintWriter(new FileWriter(selectedFile))) {
            for (int i = 0; i < grid.getGridHeight(); i++) {
                for (int j = 0; j < grid.getGridWidth(); j++) {
                    char symbol = CellViewModel.getSymbolForElement(CellViewModel.valueProperty());
                    writer.print(symbol);
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Grid getGrid(){
        Grid grid = board.getGrid();
        return grid;
    }
}
