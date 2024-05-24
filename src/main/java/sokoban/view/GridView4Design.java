package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.GridViewModel;

public class GridView4Design extends GridView{

    public GridView4Design(GridViewModel gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
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
        for (int i = 0; i < this.gridHeight; ++i) {
            for (int j = 0; j < this.gridWidth; ++j) {
                CellView4Design cellView = new CellView4Design(gridViewModel.getCellViewModel(i, j), cellWidth, cellWidth);
                cellView.prefWidthProperty().bind(cellWidth);
                cellView.prefHeightProperty().bind(cellHeight);
                add(cellView, j, i); // lignes/colonnes inversées dans gridpane
            }
        }
    }
}
