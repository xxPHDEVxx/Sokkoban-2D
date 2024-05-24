package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.ToolViewModel;

public class CellView4Design extends CellView{

    CellView4Design(CellViewModel viewModel, DoubleBinding cellWidthProperty, DoubleBinding cellHeightProperty) {
        super(viewModel, cellWidthProperty, cellHeightProperty);
        configureBindings();
    }


    private void configureBindings() {

        imageView.fitWidthProperty().bind(width);
        imageView.fitHeightProperty().bind(width);
        midImageView.fitWidthProperty().bind(width);
        midImageView.fitHeightProperty().bind(width);
        topImageView.fitWidthProperty().bind(width);
        topImageView.fitHeightProperty().bind((width));

        this.prefHeightProperty().bind(this.width);
        this.prefWidthProperty().bind(this.width);


        getChildren().addAll(imageView, midImageView, topImageView);

        // un clic sur la cellule permet de jouer celle-ci
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                removeTool();
                event.consume();
            } else if (event.getButton() == MouseButton.PRIMARY) {
                if (ToolViewModel.getToolSelected() != null && !viewModel.getBoard().isFull()) {
                    viewModel.put();
                } else if (viewModel.getBoard().isFull()) {
                    System.out.println("Nombre maximal d'outils atteint sur la grille.");
                } else {
                    System.out.println("Aucun outil n'est sélectionné, action ignorée.");
                }
            }
        });

        //dragAndDrop
        this.setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        this.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                if (!viewModel.getBoard().isFull()) {
                    viewModel.put();
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });


        // quand la cellule change de valeur, adapter l'image affichée
        viewModel.valueProperty().addListener((obs, old, newVal) -> setImage(newVal));

        //image grisé au moment du hover
        hoverProperty().addListener(this::hoverChanged);

    }

    //image grisé au moment du hover
    private void hoverChanged(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {
        if (newVal) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(-0.1); // Réduit la luminosité pour griser l'image
            imageView.setEffect(colorAdjust);
        } else {
            // Remettre l'image normale lorsque le survol est terminé
            imageView.setEffect(null);
        }
    }

    private void removeTool() {
        viewModel.removeTool();
    }


}
