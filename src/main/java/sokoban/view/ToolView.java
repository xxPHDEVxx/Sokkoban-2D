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
    private static final int PADDING = 20;
    private static final Image[] images = {
            new Image("ground.png"),
            new Image("goal.png"),
            new Image("wall.png"),
            new Image("player.png"),
            new Image("box.png")
    };

    public ToolView() {
        layoutControls();
    }

    private void layoutControls() {
        setOrientation(Orientation.VERTICAL);
        setAlignment(Pos.CENTER);
        setVgap(20);
        setHgap(20);
        setPadding(new Insets(PADDING));

        for (Image image : images) {
            ImageView imageView = createImageView(image);
            addHoverHandler(imageView);
            dragAndRoll(imageView);
            setToolEventHandlers(imageView);
            getChildren().add(imageView);
        }
    }

    private ImageView createImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(50); // Taille fixe pour simplifier l'affichage
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
        dropShadow.setColor(Color.GREY);
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

    public static GameElement determineToolFromImageView(ImageView imageView) {
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
            return new Box();
        }
        return null;
    }
}
