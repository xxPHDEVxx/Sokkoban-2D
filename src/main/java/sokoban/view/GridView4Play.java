package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import sokoban.viewmodel.GridViewModel;

public class GridView4Play extends GridView{
    public GridView4Play(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        super(gridViewModel, gridWidth, gridHeight);
        fillGrid(gridViewModel, gridWidth, gridHeight);
}

    public void fillGrid(GridViewModel gridViewModel, DoubleBinding boardWidth, DoubleBinding boardHeight) {
        //taille de chaque cellule
        DoubleBinding cellWidth = boardWidth
                .divide(gridWidth);

        DoubleBinding cellHeight = boardHeight
                .divide(gridHeight);

        // Remplissage de la grille
        for (int i = 0; i < gridViewModel.gridHeight(); ++i) {
            for (int j = 0; j < gridViewModel.gridWidth(); ++j) {
                CellView4Play cellView = new CellView4Play(gridViewModel.getCellViewModel(i, j), cellWidth, cellHeight);
                add(cellView, j, i); // lignes/colonnes inversées dans gridpane
            }
        }
    }

}
