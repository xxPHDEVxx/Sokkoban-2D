package sokoban.view;


import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
    private final int PADDING = 20;
    private static final Image player = new Image("player.png");
    private static final Image box = new Image("box.png");
    private static final Image ground = new Image("ground.png");
    private static final Image wall = new Image("wall.png");
    private static final Image goal = new Image("goal.png");
    private ToolViewModel toolViewModel;

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

        dragAndRoll(playerView);
        dragAndRoll(boxView);
        dragAndRoll(groundView);
        dragAndRoll(wallView);
        dragAndRoll(goalView);

        setToolEventHandlers(playerView);
        setToolEventHandlers(boxView);
        setToolEventHandlers(groundView);
        setToolEventHandlers(wallView);
        setToolEventHandlers(goalView);

        setOrientation(Orientation.VERTICAL);
        setAlignment(Pos.CENTER);
        setVgap(20);
        setHgap(20);
        getChildren().addAll(groundView,goalView,wallView,playerView,boxView);
        setPadding(new Insets(PADDING));
    }

    // hover a tool
    private void addHoverHandler(ImageView imageView) {
        imageView.setOnMouseEntered(event -> imageView.setOpacity(0.7)); // Réduire l'opacité de l'image lors du survol
        imageView.setOnMouseExited(event -> imageView.setOpacity(1.0)); // Rétablir l'opacité normale lorsque la souris quitte l'image
    }

    private void dragAndRoll(ImageView imageView){
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

            // Réinitialisez le style de tous les outils
            for (Node child : getChildren()) {
                child.setEffect(null);
            }

            // Créez un effet de surbrillance avec une intensité personnalisée
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.GREY); // Couleur de l'ombre
            dropShadow.setRadius(10); // Taille du flou
            dropShadow.setSpread(0.7); // Étalement de l'ombre

            // Appliquez l'effet de surbrillance à l'outil sélectionné
            imageView.setEffect(dropShadow);
        });
    }


    // Méthode pour déterminer l'outil correspondant à l'ImageView
    public static GameElement determineToolFromImageView(ImageView imageView) {
        Image image = imageView.getImage();

        if (image == player) {
            return new Player();
        } else if (image == box) {
            return new Box();
        } else if (image == ground) {
            return new Ground();
        } else if (image == wall) {
            return new Wall();
        } else if (image == goal) {
            return new Goal();
        }
        return null; // Renvoie null si aucune image ne correspond
    }

}

