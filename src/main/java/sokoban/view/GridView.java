package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.GridViewModel;

public abstract class GridView extends GridPane {
    static final int PADDING = 20;
    protected int gridWidth;
    protected int gridHeight;
    protected DoubleBinding boardWidth;
    protected DoubleBinding boardHeight;
    protected GridViewModel gridViewModel;

    GridView(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        this.gridViewModel = gridViewModel;
        this.boardWidth = gridWidth;
        this.boardHeight = gridHeight;
        this.gridWidth = gridViewModel.gridWidth();
        this.gridHeight = gridViewModel.gridHeight();
        setPadding(new Insets(PADDING));
    }
}
