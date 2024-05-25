package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import sokoban.model.GameElement;
import sokoban.model.Goal;
import sokoban.viewmodel.CellViewModel;

import java.util.List;

/**
 * Classe abstraite représentant une vue de cellule dans le jeu Sokoban.
 * Cette classe gère l'affichage des éléments de jeu dans une cellule.
 */
public abstract class CellView extends StackPane {

    // Images des différents éléments de jeu
    protected static final Image box = new Image("box.png");
    protected static final Image ground = new Image("ground.png");
    protected static final Image goal = new Image("goal.png");

    protected final CellViewModel viewModel;
    protected final DoubleBinding width;
    protected final DoubleBinding height;

    // Panneau principal pour empiler les éléments de la cellule
    protected StackPane stackPane = new StackPane();
    // Composants visuels pour afficher les images des éléments
    protected ImageView imageView = new ImageView();
    protected ImageView midImageView = new ImageView();
    protected ImageView topImageView = new ImageView();

    /**
     * Constructeur pour CellView.
     *
     * @param viewModel le modèle de vue associé à cette cellule
     * @param cellWidthProperty la propriété de largeur de la cellule
     */
    public CellView(CellViewModel viewModel, DoubleBinding cellWidthProperty, DoubleBinding cellHeightProperty) {
        this.viewModel = viewModel;
        this.width = cellWidthProperty;
        this.height = cellHeightProperty;
        setAlignment(Pos.CENTER);
        init();

    }

    /**
     * Gère l'affichage du numéro de la boîte s'il y a une boîte dans la cellule.
     */
    protected void handleBoxNumber() {
        Label numberLabel = viewModel.createBoxNumberLabel();

        if (numberLabel != null && stackPane.getChildren().isEmpty()) {
            // Lier la taille de la police du label à la plus petite dimension de la cellule
            numberLabel.fontProperty().bind(Bindings.createObjectBinding(() ->
                            Font.font("Verdana", Math.min(width.get(), width.get()) / 2),
                    width));
            stackPane.getChildren().add(numberLabel);
            StackPane.setAlignment(numberLabel, Pos.CENTER);
        }
    }

    /**
     * Initialise les images des éléments dans la cellule.
     */
    protected void init() {
        List<GameElement> elements = viewModel.getCellValue().get();

        // Définit l'image de base pour tous les types de cellules
        imageView.setImage(ground);
        // Gère l'affichage des images selon les éléments présents dans la cellule
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
            if (viewModel.isBox()) {
                handleBoxNumber();
            }
        }
    }

    protected void setImage(List<GameElement> elements) {
        midImageView.setImage(null);
        topImageView.setImage(null);
        stackPane.getChildren().clear();


        if (elements.size() > 2 && elements.get(1) instanceof Goal) {
            midImageView.setImage(elements.get(2).getImage());
            topImageView.setImage(elements.get(1).getImage());
            handleBoxNumber();
        } else {
            if (elements.size() > 1) {
                if (viewModel.mushroomDisplay()){
                    midImageView.setImage(ground);
                } else
                    midImageView.setImage(elements.get(1).getImage());
            }
            if (elements.size() > 2) {
                topImageView.setImage(elements.get(2).getImage());
            }
            if (viewModel.isBox()) {
                handleBoxNumber();
            }
        }
    }
}
