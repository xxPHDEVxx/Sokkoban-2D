package sokoban.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import sokoban.viewmodel.ToolViewModel;

public class ToolView extends FlowPane {
    private static final Image player = new Image("player.png");
    private static final Image box = new Image("box.png");
    private static final Image ground = new Image("ground.png");
    private static final Image wall = new Image("wall.png");
    private static final Image goal = new Image("goal.png");
    private final ToolViewModel toolViewModel;

    private static ObjectProperty<Image> imageSelected  = new SimpleObjectProperty<>();
    ToolView(ToolViewModel toolViewModel) {
        this.toolViewModel=toolViewModel;

        layoutControls();

    }
    private void layoutControls() {
        ImageView playerView = new ImageView(player);
        ImageView boxView = new ImageView(box);
        ImageView groundView = new ImageView(ground);
        ImageView wallView = new ImageView(wall);
        ImageView goalView = new ImageView(goal);

        getChildren().addAll(groundView,goalView,wallView,playerView,boxView);
        setOrientation(Orientation.VERTICAL);

    }


}
