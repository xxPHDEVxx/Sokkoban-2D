package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.GridViewModel;

class GridView extends GridPane {

    private static final int PADDING = 20;
    private static final int GRID_WIDTH = BoardViewModel.gridWidth();
    private static final int GRID_HEIGHT = BoardViewModel.gridHeight();

    GridView(GridViewModel gridViewModel, DoubleBinding gridWidth) {

        setPrefSize(300,300);
        setPadding(new Insets(PADDING));

        //taille de chaque cellule
        DoubleBinding cellWidth = gridWidth
                .subtract(PADDING * 2)
                .divide(GRID_WIDTH);

        // Remplissage de la grille
        for (int i = 0; i < GRID_HEIGHT; ++i) {
            for (int j = 0; j < GRID_WIDTH; ++j) {
                CellView cellView = new CellView(gridViewModel.getCellViewModel(i, j), cellWidth);
                add(cellView, j, i); // lignes/colonnes inversées dans gridpane
            }
        }
    }}
