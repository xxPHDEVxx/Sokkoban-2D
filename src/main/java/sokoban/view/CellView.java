package sokoban.view;

import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import sokoban.model.CellValue;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.ToolViewModel;

public class CellView extends StackPane {
    private static final Image groundImage = new Image("ground.png");
    private final CellViewModel cellViewModel;

//    private static ToolViewModel toolViewModel;

    private final DoubleBinding widthProperty;

    private ImageView imageView = new ImageView();
    CellView(CellViewModel cellViewModel, DoubleBinding cellWidthProperty) {
        this.cellViewModel = cellViewModel;
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


        hoverProperty().addListener(this::hoverChanged);
    }
    //changement d'image au click
    private void setImage(ImageView imageView, CellValue cellValue) {
        //todo
    }

    //image grisé au moment du hover
    private void hoverChanged(ObservableValue<? extends Boolean> ons, Boolean oldVal, Boolean newVal) {
        //TODO
    }
}
