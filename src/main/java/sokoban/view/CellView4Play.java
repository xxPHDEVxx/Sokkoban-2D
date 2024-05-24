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
        // Configuration des liaisons de taille pour les images
        imageView.fitWidthProperty().bind(width);
        imageView.fitHeightProperty().bind(width);
        midImageView.fitWidthProperty().bind(width);
        midImageView.fitHeightProperty().bind(width);
        topImageView.fitWidthProperty().bind(width);
        topImageView.fitHeightProperty().bind(width);


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