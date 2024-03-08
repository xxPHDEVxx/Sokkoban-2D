package sokoban.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import sokoban.viewmodel.ToolViewModel;
import sokoban.model.CellValue;

public class ToolView extends FlowPane {
    private static final Image player = new Image("player.png");
    private static final Image box = new Image("box.png");
    private static final Image ground = new Image("ground.png");
    private static final Image wall = new Image("wall.png");
    private static final Image goal = new Image("goal.png");
    private static ObjectProperty<Image> imageSelected  = new SimpleObjectProperty<>();
    public ToolView() {

        layoutControls();

    }
    private void layoutControls() {
        ImageView playerView = new ImageView(player);
        ImageView boxView = new ImageView(box);
        ImageView groundView = new ImageView(ground);
        ImageView wallView = new ImageView(wall);
        ImageView goalView = new ImageView(goal);

        addHoverHandler(playerView);
        addHoverHandler(boxView);
        addHoverHandler(groundView);
        addHoverHandler(wallView);
        addHoverHandler(goalView);

        setOrientation(Orientation.VERTICAL);
        setAlignment(Pos.CENTER);
        setVgap(20);
        setHgap(20);
        getChildren().addAll(groundView,goalView,wallView,playerView,boxView);

    }

    // hover a tool
    private void addHoverHandler(ImageView imageView) {
        imageView.setOnMouseEntered(event -> imageView.setOpacity(0.7)); // Réduire l'opacité de l'image lors du survol
        imageView.setOnMouseExited(event -> imageView.setOpacity(1.0)); // Rétablir l'opacité normale lorsque la souris quitte l'image
    }


}
