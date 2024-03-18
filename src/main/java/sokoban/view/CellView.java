package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import sokoban.model.Cell;
import sokoban.model.CellValue;
import sokoban.viewmodel.CellViewModel;

public class CellView extends StackPane {
    private static final Image groundImage = new Image("ground.png");
    private static final Image wall = new Image("wall.png");// juste pour tester
    private final CellViewModel viewModel;

    private final DoubleBinding widthProperty;

    private ImageView imageView = new ImageView();
    CellView(CellViewModel viewModel, DoubleBinding cellWidthProperty) {
        this.viewModel = viewModel;
        this.widthProperty = cellWidthProperty;
        setAlignment(Pos.CENTER);

        configureBindings();

    }

    private void configureBindings() {
        //image de base en fond
        imageView.setImage(groundImage);
        imageView.setPreserveRatio(true);

        getChildren().add(imageView);
        //listener au clic
        setOnMouseClicked(event -> {
                    System.out.println(viewModel.valueProperty());
                });

        // clic sur la cellule permet de changer sa valeur
        setOnClickHandler();
        //image grisé au moment du hover
        hoverProperty().addListener(this::hoverChanged);

    }

    //changement d'image au click
    private void setImage(ImageView imageView, CellValue cellValue) {
        imageView.setImage(imageView.getImage());
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

    // ajoute outil sur grille au clic (à améliorer et corriger)
    public void setOnClickHandler(){
        ImageView imageView2 = new ImageView();
        getChildren().add(imageView2);
        Cell[][] matrix = viewModel.getBoard().getGrid().getMatrix();
        this.setOnMouseClicked(e -> imageView2.setImage(ToolView.getImageSelected()));
        CellValue tool = ToolView.determineToolFromImageView(imageView2);
        matrix[viewModel.getLine()][viewModel.getCol()].setValue(tool);
    }

    public void refresh() {
        //image de base en fond
        imageView.setImage(groundImage);
        imageView.setPreserveRatio(true);

        configureBindings();
    }
}
