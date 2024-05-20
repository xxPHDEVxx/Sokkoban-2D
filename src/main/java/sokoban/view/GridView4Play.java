package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import sokoban.viewmodel.GridViewModel;

public class GridView4Play extends GridView{
    public GridView4Play(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        setPadding(new Insets(PADDING));
        fillGrid(gridViewModel, gridWidth, gridHeight);
}

    public void fillGrid(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        //taille de chaque cellule
        DoubleBinding cellWidth = gridWidth
                .subtract(PADDING * 2)
                .divide(GRID_WIDTH);

        DoubleBinding cellHeight = gridHeight
                .subtract(PADDING * 2)
                .divide(GRID_WIDTH);

        // Remplissage de la grille
        for (int i = 0; i < GRID_HEIGHT; ++i) {
            for (int j = 0; j < GRID_WIDTH; ++j) {
                CellView4Play cellView = new CellView4Play(gridViewModel.getCellViewModel(i, j), cellWidth, cellHeight);
                add(cellView, j, i); // lignes/colonnes inversées dans gridpane
            }
        }
    }

}
