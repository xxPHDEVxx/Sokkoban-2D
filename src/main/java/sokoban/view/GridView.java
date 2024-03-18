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
    //private static final int GRID_WIDTH = BoardViewModel.gridWidth();
    //private static final int GRID_HEIGHT = BoardViewModel.gridHeight();
    private GridViewModel gridViewModel;
    private DoubleBinding cellWidth;

    GridView(GridViewModel gridViewModel, DoubleBinding gridWidth) {

        System.out.println(GRID_WIDTH);
        System.out.println(GRID_HEIGHT);

        setPrefSize(300,300);
        this.gridViewModel = gridViewModel;
        setGridLinesVisible(true);

        setPadding(new Insets(PADDING));

        //taille de chaque cellule
        cellWidth = gridWidth
                .subtract(PADDING * 2)
                .divide(GRID_WIDTH);

        // Remplissage de la grille
        for (int i = 0; i < GRID_HEIGHT; ++i) {
            for (int j = 0; j < GRID_WIDTH; ++j) {
                CellView cellView = new CellView(gridViewModel.getCellViewModel(i, j), cellWidth);
                add(cellView, j, i); // lignes/colonnes inversées dans gridpane
            }
        }


        //setBoardClickHandlers(gridViewModel);
    }

    // Méthode pour créer et retourner une instance de CellView pour une cellule spécifique
    public CellView createCellView(int row, int col, CellViewModel cellViewModel, DoubleBinding cellWidth) {
        CellView cellView = new CellView(cellViewModel, cellWidth);
        // Vous pouvez également configurer d'autres propriétés de la CellView ici si nécessaire
        return cellView;
    }

    // peut être inutile
    /*private void setBoardClickHandlers(GridViewModel gridViewModel) {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                CellView cellView = createCellView(i, j, gridViewModel.getCellViewModel(i,j), cellWidth);
                int row = i;
                int col = j;
                cellView.setOnMouseClicked(event -> {
                    CellValue selectedTool = CellValue.PLAYER; // à modifier pour tous les outils
                    gridViewModel.placeTool(row, col, selectedTool);
                });
            }
        }
    }*/
}
