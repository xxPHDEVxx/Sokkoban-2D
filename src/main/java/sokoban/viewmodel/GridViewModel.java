package sokoban.viewmodel;

import sokoban.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GridViewModel {
    private final Board board;
    GridViewModel(Board board) {this.board = board;}
    public CellViewModel getCellViewModel(int line, int col) {
        return new CellViewModel(line,col, board);
    }
    public void saveMenu(Grid4Design grid, File selectedFile){

        try (PrintWriter writer = new PrintWriter(new FileWriter(selectedFile))) {
            for (int i = 0; i < grid.getGridHeight(); i++) {
                for (int j = 0; j < grid.getGridWidth(); j++) {
                    List<GameElement> cellItems = grid.getValue(i, j);
                    char symbol = CellViewModel.getSymbolForElement(cellItems);
                    writer.print(symbol);
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
