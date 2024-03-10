package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.model.CellValue;
import sokoban.model.Tool;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.GridViewModel;
import sokoban.viewmodel.ToolViewModel;

class GridView extends GridPane {

    private static final int PADDING = 20;
    private static final int GRID_WIDTH = BoardViewModel.gridWidth();
    private static final int GRID_HEIGHT = BoardViewModel.gridHeight();
    private GridViewModel gridViewModel;
    private DoubleBinding cellWidth;

    GridView(GridViewModel gridViewModel, DoubleBinding gridWidth) {

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
    }

    // Méthode pour créer et retourner une instance de CellView pour une cellule spécifique
    public CellView createCellView(int row, int col, CellViewModel cellViewModel, DoubleBinding cellWidth) {
        CellView cellView = new CellView(cellViewModel, cellWidth);
        // Vous pouvez également configurer d'autres propriétés de la CellView ici si nécessaire
        return cellView;
    }
    private void setBoardClickHandlers(BoardViewModel boardViewModel) {
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                CellView cellView = createCellView(i, j, gridViewModel.getCellViewModel(i,j), cellWidth);
                int row = i;
                int col = j;
                cellView.setOnMouseClicked(event -> {
                    CellValue selectedTool = CellValue.PLAYER;
                    boardViewModel.placeTool(row, col, selectedTool);
                });
            }
        }
    }
}
