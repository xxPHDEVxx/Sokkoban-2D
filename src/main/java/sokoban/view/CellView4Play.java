package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sokoban.model.CellValue;
import sokoban.model.GameElement;
import sokoban.viewmodel.CellViewModel;

public class CellView4Play extends CellView{
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
    CellView4Play(CellViewModel viewModel, DoubleBinding cellWidthProperty, DoubleBinding cellHeightProperty) {
        this.viewModel = viewModel;
        this.widthProperty = cellWidthProperty;
        this.heightProperty = cellHeightProperty;
        setAlignment(Pos.CENTER);

        configureBindings();
        init();

    }


    private void configureBindings() {

        minWidthProperty().bind(widthProperty);
        minHeightProperty().bind(heightProperty);
        //image de base en fond
        //imageView.setImage(ground);
        imageView.setPreserveRatio(true);
        topImageView.setPreserveRatio(true);
        topImageView.fitHeightProperty().bind(heightProperty);
        topImageView.fitWidthProperty().bind(widthProperty);


        getChildren().addAll(imageView, topImageView, midImageView);
        viewModel.valueProperty().addListener((obs, old, newVal) -> setImage(imageView, newVal));

    }

    public void init() {
        GameElement cell = viewModel.getCellValue().get();

        // Mettre l'image de base pour tous les types de cellules
        imageView.setImage(ground);
        midImageView.setImage(cell.getImage());
        topImageView.setImage(cell.getImage2()); // Réinitialiser l'image du dessus
    }

    private void setImage(ImageView imageView, GameElement element) {
        imageView.setImage(ground);
        midImageView.setImage(element.getImage());
        topImageView.setImage(null);
    }

}
