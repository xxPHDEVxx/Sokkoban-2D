package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.viewmodel.ToolViewModel;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

// ajustement encore possible ( certaines méthodes non nécessaires aux deux enfants) *Jamila
public abstract class Board {
    public static int MAX_FILLED_CELLS = 75;
    private static Grid grid = new Grid();
    private final BooleanBinding isFull;

    public Board(){
        isFull = grid.filledCellsCountProperty().isEqualTo(Board.MAX_FILLED_CELLS);
        configureBindings();
    }

    public abstract void play(int line, int col);

    public abstract Boolean isFull();


    public abstract ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col);



    public abstract boolean isEmpty(int line, int col);

    public abstract void configureBindings();
    public abstract BooleanBinding getRulesOK();

    public abstract Grid getGrid();
    public abstract Board getBoard();
    public abstract Grid open(File file);
    //private static CellValue convertSymbolToCellValue(char symbol);
    // Check le nombre de joueur sur la grille
    public boolean isPlayerPlaced() {
        return grid.playerCountProperty().get() > 0;
    }
    }
