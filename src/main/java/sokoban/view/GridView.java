package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.model.CellValue;
import sokoban.model.Tool;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.GridViewModel;
import sokoban.viewmodel.ToolViewModel;

public class GridView extends GridPane {
    private static final int PADDING = 20;
    private int GRID_WIDTH = BoardViewModel.gridWidth();
    private int GRID_HEIGHT = BoardViewModel.gridHeight();

    GridView(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {

        setPadding(new Insets(PADDING));

        //taille de chaque cellule
        DoubleBinding cellWidth = gridWidth
                .subtract(PADDING * 2)
                .divide(GRID_WIDTH);
        //taille de chaque cellule
        DoubleBinding cellHeight = gridHeight
                .subtract(PADDING * 2)
                .divide(GRID_HEIGHT);

        // Remplissage de la grille
        for (int i = 0; i < GRID_HEIGHT; ++i) {
            for (int j = 0; j < GRID_WIDTH; ++j) {
                CellView cellView = new CellView(gridViewModel.getCellViewModel(i, j), cellWidth, cellHeight);
                add(cellView, j, i); // lignes/colonnes inversées dans gridpane
            }
        }
    }
}
