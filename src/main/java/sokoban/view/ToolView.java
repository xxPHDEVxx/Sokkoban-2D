package sokoban.view;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.ToolViewModel;
import sokoban.model.*;

public class ToolView extends FlowPane {

    private static final Image[] images = {
            new Image("ground.png"),
            new Image("goal.png"),
            new Image("wall.png"),
            new Image("player.png"),
            new Image("box.png")
    };

    public ToolView(ToolViewModel viewModel) {
        layoutControls();

        for (Image image : images) {
            ImageView imageView = createImageView(image);
            addHoverHandler(imageView);
            dragAndRoll(imageView);
            setToolEventHandlers(imageView);
            getChildren().add(imageView);
            imageView.fitHeightProperty().bind(this.heightProperty().multiply(0.3));
            imageView.fitWidthProperty().bind(this.widthProperty().multiply(0.3));
        }

    }

    private void layoutControls() {
        setOrientation(Orientation.VERTICAL);
        setAlignment(Pos.CENTER);
        setVgap(20);
    }

    private ImageView createImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        // Liaison des propriétés de largeur et de hauteur des ImageView à la taille du ToolView
        imageView.fitWidthProperty().bind(this.widthProperty().multiply(0.4)); // 40% de la largeur du ToolView
        imageView.fitHeightProperty().bind(this.heightProperty().multiply(0.4)); // 40% de la hauteur du ToolView
        return imageView;
    }


    private void addHoverHandler(ImageView imageView) {
        imageView.setOnMouseEntered(event -> imageView.setOpacity(0.7));
        imageView.setOnMouseExited(event -> imageView.setOpacity(1.0));
    }

    private void dragAndRoll(ImageView imageView) {
        imageView.setOnDragDetected(event -> {
            ToolViewModel.setToolSelected(determineToolFromImageView(imageView));
            Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("tool");
            db.setContent(content);
            event.consume();
        });
    }

    private void setToolEventHandlers(ImageView imageView) {
        imageView.setOnMouseClicked(event -> {
            ToolViewModel.setToolSelected(determineToolFromImageView(imageView));
            BoardViewModel.setChanged(true);
            applyHighlightEffect(imageView);
        });
    }

    private void applyHighlightEffect(ImageView imageView) {
        clearHighlightEffects();
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.LIGHTBLUE);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.7);
        imageView.setEffect(dropShadow);
    }

    private void clearHighlightEffects() {
        for (javafx.scene.Node child : getChildren()) {
            if (child instanceof ImageView) {
                ((ImageView) child).setEffect(null);
            }
        }
    }

    public  static GameElement determineToolFromImageView(ImageView imageView) {
        Image image = imageView.getImage();
        if (image == images[0]) {
            return new Ground();
        } else if (image == images[1]) {
            return new Goal();
        } else if (image == images[2]) {
            return new Wall();
        } else if (image == images[3]) {
            return new Player();
        } else if (image == images[4]) {
            Box box = new Box();
            return box;
        }
        return null;
    }
}
