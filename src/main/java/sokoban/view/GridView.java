package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import sokoban.model.CellValue;
import sokoban.model.Tool;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.GridViewModel;
import sokoban.viewmodel.ToolViewModel;

public abstract class GridView extends GridPane {
    static final int PADDING = 20;
    int GRID_WIDTH = BoardViewModel.gridWidth();
    int GRID_HEIGHT = BoardViewModel.gridHeight();

    GridView() {}
}
