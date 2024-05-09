package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import sokoban.model.CellValue;
import sokoban.model.GameElement;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.ToolViewModel;

public class CellView4Design extends CellView{
    private static final Image player = new Image("player.png");
    private static final Image box = new Image("box.png");
    private static final Image ground = new Image("ground.png");
    private static final Image wall = new Image("wall.png");
    private static final Image goal = new Image("goal.png");
    private final CellViewModel viewModel;
    private final DoubleBinding widthProperty;
    private final DoubleBinding heightProperty;

    private ImageView imageView = new ImageView();
    private ImageView midImageView = new ImageView();
    private ImageView topImageView = new ImageView();
    CellView4Design(CellViewModel viewModel, DoubleBinding cellWidthProperty, DoubleBinding cellHeightProperty) {
        this.viewModel = viewModel;
        this.widthProperty = cellWidthProperty;
        this.heightProperty = cellHeightProperty;
        setAlignment(Pos.CENTER);

        configureBindings();

    }


    private void configureBindings() {

        minWidthProperty().bind(widthProperty);
        minHeightProperty().bind(heightProperty);
        //image de base en fond
        imageView.setImage(ground);
        imageView.setPreserveRatio(true);
        topImageView.setPreserveRatio(true);
        topImageView.fitHeightProperty().bind(heightProperty);
        topImageView.fitWidthProperty().bind(widthProperty);


        getChildren().addAll(imageView, topImageView, midImageView);

        // un clic sur la cellule permet de jouer celle-ci
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                removeTool(viewModel.getLine(), viewModel.getCol());
                event.consume();
            } else if (event.getButton() == MouseButton.PRIMARY) {
                if (ToolViewModel.getToolSelected() != null && !viewModel.getBoard().isFull()) {
                    viewModel.play();
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
                    viewModel.play();
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });


        // quand la cellule change de valeur, adapter l'image affichée
        viewModel.valueProperty().addListener((obs, old, newVal) -> setImage(imageView, newVal));

        //image grisé au moment du hover
        hoverProperty().addListener(this::hoverChanged);

    }

    //changement d'image au click
    private void setImage(ImageView imageView, GameElement element) {
        imageView.setImage(ground); // Image de base pour tous les types de cellules
        midImageView.setImage(element.getImage());
        topImageView.setImage(element.getImage2());
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

    public void refresh() {
        //image de base en fond
        imageView.setImage(ground);
        imageView.setPreserveRatio(true);

        configureBindings();
    }

    private void removeTool(int line, int col) {
        viewModel.removeTool();
    }


}
