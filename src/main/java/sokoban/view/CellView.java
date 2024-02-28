package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import sokoban.model.CellValue;
import sokoban.viewmodel.CellViewModel;

public class CellView extends StackPane {
    private static final Image groundImage = new Image("ground.png");
    private final CellViewModel viewModel;

//    private static ToolViewModel toolViewModel;

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
        //this.setOnMouseClicked();


        viewModel.valueProperty().addListener((obs, old, newVal) -> System.out.println("clic"));

        //Instantiating the ColorAdjust class
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.001);
        imageView.setEffect(colorAdjust);
        hoverProperty().addListener(this::hoverChanged);
    }

    //changement d'image au click
    private void setImage(ImageView imageView, CellValue cellValue) {
        //todo
    }

    //image grisé au moment du hover
    private void hoverChanged(ObservableValue<? extends Boolean> ons, Boolean oldVal, Boolean newVal) {

    }
}
