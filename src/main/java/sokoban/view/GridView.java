package sokoban.view;

import javafx.scene.layout.*;
import sokoban.viewmodel.BoardViewModel;

public abstract class GridView extends GridPane {
    static final int PADDING = 20;
    int GRID_WIDTH = BoardViewModel.gridWidth();
    int GRID_HEIGHT = BoardViewModel.gridHeight();

    GridView() {}
}
