package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import sokoban.model.Direction;
import sokoban.viewmodel.BoardViewModel;

public class BoardView4play extends BoardView  {
    private GridView gridView;
    private Label title = new Label("Score");
    private Label numberOfMovesPlayed = new Label("");
    private Label goal = new Label("");
    private HBox level = new HBox();
    private VBox headerPlay = new VBox();
    private VBox boardLvl = new VBox();
    private Button button = new Button("Finish");
    private static final int SCENE_MIN_WIDTH = 1080;
    private static final int SCENE_MIN_HEIGHT = 800;
    private Stage playStage;
    private  Stage primaryStage;

    public BoardView4play(Stage playStage, GridView gridView, BoardViewModel boardViewModel) {
        super(playStage, boardViewModel);
        this.gridView = gridView;
        this.playStage = playStage;
        playStage.setScene(gridView.getScene());
        playStage.show();
        initialize();
        configureScene(playStage);
        createHeaderPlay();
        actionBtnFinish(boardViewModel);

    }

    public void initialize() {
        playStage.setTitle("jeu");
        //style
        headerPlay.getStyleClass().add("header");
        Font font = Font.font("Verdana", FontWeight.BOLD, 20);
        title.setFont(font);
        headerPlay.setAlignment(Pos.CENTER);

        // box button
        HBox boxBtn = new HBox();
        boxBtn.getChildren().addAll(button);
        boxBtn.setAlignment(Pos.CENTER);

        //insert sur la vue
        level.getChildren().add(gridView);
        level.setAlignment(Pos.CENTER);
        gridView.setAlignment(Pos.CENTER);
        headerPlay.getChildren().addAll(title, numberOfMovesPlayed, goal);
        boardLvl.getChildren().addAll(headerPlay, level, boxBtn);
        boardLvl.setAlignment(Pos.CENTER);

    }

    public void configureScene(Stage playStage) {
        Scene sceneLevel = new Scene(boardLvl, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        playStage.setScene(sceneLevel);

        // Directly request focus
        sceneLevel.getRoot().setFocusTraversable(true);

        sceneLevel.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            boolean moved = false;
            switch (event.getCode()) {
                case Z:
                    moved = boardViewModel.movePlayer(Direction.UP);
                    break;
                case S:
                    moved = boardViewModel.movePlayer(Direction.DOWN);
                    break;
                case Q:
                    moved = boardViewModel.movePlayer(Direction.LEFT);
                    break;
                case D:
                    moved = boardViewModel.movePlayer(Direction.RIGHT);
                    break;
            }
            if (moved) {
                refreshGrid(); // Rafraîchir la grille après le mouvement
            }
            event.consume();
        });

        // Assurer que le composant prend le focus lors de l'affichage
        playStage.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
            if (isNowShowing) {
                sceneLevel.getRoot().requestFocus();
            }
        });
    }

    public void refreshGrid() {
        for (Node child : getChildren()) {
            if (child instanceof CellView) {
                ((CellView) child).refresh();
            }
        }

    }



    public void createHeaderPlay() {
        numberOfMovesPlayed.textProperty().bind(Bindings.concat("Number of moves played : ",boardViewModel.moveCountProperty().asString()));
        goal.textProperty().bind(Bindings.concat("Number of goals reached : ",boardViewModel.boxInTargetCountProperty().asString()," of ", boardViewModel.goalCountProperty().asString()));
    }

    //bouton finish a refaire
    public void actionBtnFinish(BoardViewModel bordvm) {
        button.setOnAction(action -> {
            playStage.close();
       });
    }
}
