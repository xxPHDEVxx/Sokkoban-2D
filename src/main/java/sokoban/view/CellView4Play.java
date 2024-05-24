package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import sokoban.model.Grid;
import sokoban.viewmodel.CellViewModel;

public class CellView4Play extends CellView{

    CellView4Play(CellViewModel viewModel, DoubleBinding cellWidthProperty, DoubleBinding cellHeightProperty) {
        super(viewModel, cellWidthProperty, cellHeightProperty);
        configureBindings();
    }

    private void configureBindings() {


        // Ajout des composants visuels au StackPane
        getChildren().addAll(imageView, midImageView,stackPane, topImageView);


        // un clic sur la cellule permet de jouer celle-ci
        this.setOnMouseClicked(event -> {
            viewModel.isMushroom();
            viewModel.placeMushroom();
            viewModel.addBoardHistory();
        });

        // Écoutez les changements dans les cellules
        viewModel.valueProperty().addListener((obs, old, newVal) -> setImage(newVal));
    }
}
