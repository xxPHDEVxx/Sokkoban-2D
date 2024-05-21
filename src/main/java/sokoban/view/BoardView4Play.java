package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import sokoban.model.Direction;
import sokoban.viewmodel.BoardViewModel;

public class BoardView4Play extends BoardView  {
    private GridView4Play gridView;
    private Label title = new Label("Score");
    private Label numberOfMovesPlayed = new Label("");
    private Label goal = new Label("");
    private Label finisher = new Label("");
    private HBox level = new HBox();
    private VBox headerPlay = new VBox();
    private VBox boardLvl = new VBox();
    private Button button = new Button("Finish");
    private  final int SCENE_MIN_WIDTH = 1080;
    private  final int SCENE_MIN_HEIGHT = 800;
    private Stage playStage;

    public BoardView4Play(Stage playStage, GridView4Play gridView, BoardViewModel boardViewModel) {
        super(playStage, boardViewModel);
        this.gridView = gridView;
        this.playStage = playStage;
        playStage.setScene(gridView.getScene());
        playStage.show();
        initialize();
        configureScene(playStage);
        createHeaderPlay();
        actionBtnFinish(boardViewModel, primaryStage);
    }

    public void initialize() {
        playStage.setTitle("jeu");
        //style
        headerPlay.getStyleClass().add("header");
        Font fontTitle = Font.font("Verdana", FontWeight.BOLD, 20);
        title.setFont(fontTitle);
        headerPlay.setAlignment(Pos.CENTER);

        Font fontFinish = Font.font("Verdana", FontWeight.BOLD, 15);
        finisher.setFont(fontFinish);

        // box button
        HBox boxBtn = new HBox();
        boxBtn.getChildren().addAll(button);
        boxBtn.setAlignment(Pos.CENTER);

        //insert sur la vue
        level.getChildren().add(gridView);
        level.setAlignment(Pos.CENTER);
        gridView.setAlignment(Pos.CENTER);
        headerPlay.getChildren().addAll(title, numberOfMovesPlayed, goal, finisher);
        boardLvl.getChildren().addAll(headerPlay, level, boxBtn);
        boardLvl.setAlignment(Pos.CENTER);

    }

    public void configureScene(Stage playStage) {
        Scene sceneLevel = new Scene(boardLvl, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        playStage.setScene(sceneLevel);

        // Directly request focus
        sceneLevel.getRoot().setFocusTraversable(true);

        sceneLevel.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            if (new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN).match(event)) {
                boardViewModel.undo();
                //refresh visuel
                gridView.fillGrid(boardViewModel.getGridViewModel(), gridWidth, gridHeight);
                return;
            } else if (new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN).match(event)) {
                boardViewModel.redo();
                // refresh visuel
                gridView.fillGrid(boardViewModel.getGridViewModel(), gridWidth, gridHeight);
                return;
            }

            // Traitement des touches de direction
            switch (event.getCode()) {
                case UP, Z:
                    boardViewModel.movePlayer(Direction.UP);
                    break;
                case DOWN, S:
                    boardViewModel.movePlayer(Direction.DOWN);
                    break;
                case LEFT, Q:
                    boardViewModel.movePlayer(Direction.LEFT);
                    break;
                case RIGHT, D:
                    boardViewModel.movePlayer(Direction.RIGHT);
                    break;
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

    public void createHeaderPlay() {
        numberOfMovesPlayed.textProperty().bind(Bindings.concat("Number of moves played : ",boardViewModel.moveCountProperty().asString()));
        goal.textProperty().bind(Bindings.concat("Number of goals reached : ",boardViewModel.boxInTargetCountProperty().asString()," of ", boardViewModel.goalCountProperty().asString()));
        finisher.textProperty().bind(Bindings.when(boardViewModel.boxInTargetCountProperty().isEqualTo(boardViewModel.goalCountProperty()))
                .then(Bindings.concat("You won in ", boardViewModel.moveCountProperty().asString(), " moves, congratulations"))
                .otherwise(""));
    }

    //bouton finish a refaire
    public void actionBtnFinish(BoardViewModel bordvm, Stage primaryStage) {
        button.setOnAction(action -> {
            boardViewModel.getBoard().setGrid(boardViewModel.getSaveGridDesign());
            new BoardView4Design(primaryStage,boardViewModel);
        });
    }
}
