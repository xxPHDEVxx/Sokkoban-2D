package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import sokoban.model.Box;
import sokoban.model.GameElement;
import sokoban.model.Goal;
import sokoban.viewmodel.CellViewModel;

import java.util.List;

public class CellView4Play extends CellView{
    private static final Image box = new Image("box.png");
    private static final Image ground = new Image("ground.png");
    private static final Image wall = new Image("wall.png");
    private static final Image goal = new Image("goal.png");
    private final CellViewModel viewModel;
    private final DoubleBinding widthProperty;
    private final DoubleBinding heightProperty;

    private  StackPane stackPane = new StackPane();
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
        imageView.setPreserveRatio(true);
        topImageView.setPreserveRatio(true);
        topImageView.fitHeightProperty().bind(heightProperty);
        topImageView.fitWidthProperty().bind(widthProperty);

        // Ajoutez le stackPane au CellView une seule fois lors de l'initialisation
        getChildren().addAll(imageView, midImageView,stackPane, topImageView);

        // Écoutez les changements dans les cellules
        viewModel.valueProperty().addListener((obs, old, newVal) -> setImage(newVal));
    }

    public void init() {
        List<GameElement> elements = viewModel.getCellValue().get();


        // Mettre l'image de base pour tous les types de cellules
        imageView.setImage(ground);
        if (elements.size() > 2 && elements.get(1) instanceof Goal) {
            midImageView.setImage(elements.get(2).getImage());
            topImageView.setImage(elements.get(1).getImage());
            handleBoxNumber();
        } else {
            if (elements.size() > 1)
                midImageView.setImage(elements.get(1).getImage());
            if (elements.size() > 2)
                topImageView.setImage(elements.get(2).getImage());
            if (viewModel.isBox())
                handleBoxNumber();
        }
    }

    private void handleBoxNumber() {
        Label numberLabel = viewModel.createBoxNumberLabel();
        if (numberLabel != null && stackPane.getChildren().isEmpty()) {
            stackPane.getChildren().addAll(numberLabel);
            StackPane.setAlignment(numberLabel, Pos.CENTER);
        }
    }


    private void setImage(List<GameElement> elements) {
        midImageView.setImage(null);
        topImageView.setImage(null);

        if (elements.size() > 2 && elements.get(1) instanceof Goal) {
            midImageView.setImage(elements.get(2).getImage());
            topImageView.setImage(elements.get(1).getImage());
            handleBoxNumber();
        } else {
            if (elements.size() > 1) {
                midImageView.setImage(elements.get(1).getImage());
            }
            if (elements.size() > 2) {
                topImageView.setImage(elements.get(2).getImage());
            }
            if (viewModel.isBox())
                handleBoxNumber();
        }
    }

}
