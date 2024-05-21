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

public class BoardView4play extends BoardView {

    private Label title = new Label("Score");
    private Label numberOfMovesPlayed = new Label("");
    private Label goal = new Label("");
    private Label finisher = new Label("");
    private HBox level = new HBox();
    private VBox headerPlay = new VBox();
    private Button button = new Button("Finish");
    private GridView4Play gridView;

    // Constructeur de la vue de jeu
    public BoardView4play(Stage primaryStage, GridView4Play gridView, BoardViewModel boardViewModel) {
        super(primaryStage, boardViewModel);
        this.gridView = gridView;
        this.primaryStage = primaryStage;
        primaryStage.setScene(gridView.getScene());
        primaryStage.show();
        initialize();
        configureScene(primaryStage);
        createHeaderPlay();
        setupFinishButton(boardViewModel, primaryStage);
    }

    // Initialisation des composants de la vue
    private void initialize() {
        primaryStage.setTitle("Jeu");

        // Style pour les composants
        headerPlay.getStyleClass().add("header");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        finisher.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        // Organisation des composants
        headerPlay.setAlignment(Pos.CENTER);
        level.setAlignment(Pos.CENTER);
        gridView.setAlignment(Pos.CENTER);
        boardLvl.setAlignment(Pos.CENTER);

        // Ajout des composants à leurs conteneurs
        HBox boxBtn = new HBox(button);
        boxBtn.setAlignment(Pos.CENTER);

        headerPlay.getChildren().addAll(title, numberOfMovesPlayed, goal, finisher);
        level.getChildren().add(gridView);
        boardLvl.getChildren().addAll(headerPlay, level, boxBtn);
    }

    // Configuration de la scène et des événements clavier
    private void configureScene(Stage playStage) {
        Scene sceneLevel = new Scene(boardLvl, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        playStage.setScene(sceneLevel);

        // Assurer que le composant prend le focus lors de l'affichage
        sceneLevel.getRoot().setFocusTraversable(true);
        playStage.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
            if (isNowShowing) {
                sceneLevel.getRoot().requestFocus();
            }
        });

        // Gestion des événements clavier
        sceneLevel.addEventFilter(KeyEvent.KEY_PRESSED, event -> handleKeyPress(event));
    }

    // Méthode pour gérer les pressions des touches
    private void handleKeyPress(KeyEvent event) {
        if (new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN).match(event)) {
            boardViewModel.undo();
            gridView.fillGrid(boardViewModel.getGridViewModel(), gridWidth, gridHeight);
            return;
        } else if (new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN).match(event)) {
            boardViewModel.redo();
            gridView.fillGrid(boardViewModel.getGridViewModel(), gridWidth, gridHeight);
            return;
        }

        switch (event.getCode()) {
            case UP, Z -> boardViewModel.movePlayer(Direction.UP);
            case DOWN, S -> boardViewModel.movePlayer(Direction.DOWN);
            case LEFT, Q -> boardViewModel.movePlayer(Direction.LEFT);
            case RIGHT, D -> boardViewModel.movePlayer(Direction.RIGHT);
        }
        event.consume();
    }

    // Création de l'en-tête pour le jeu
    private void createHeaderPlay() {
        numberOfMovesPlayed.textProperty().bind(Bindings.concat("Number of moves played: ", boardViewModel.moveCountProperty().asString()));
        goal.textProperty().bind(Bindings.concat("Number of goals reached: ", boardViewModel.boxInTargetCountProperty().asString(), " of ", boardViewModel.goalCountProperty().asString()));
        finisher.textProperty().bind(Bindings.when(boardViewModel.boxInTargetCountProperty().isEqualTo(boardViewModel.goalCountProperty()))
                .then(Bindings.concat("You won in ", boardViewModel.moveCountProperty().asString(), " moves, congratulations"))
                .otherwise(""));
        button.disableProperty().bind(boardViewModel.boxInTargetCountProperty().isEqualTo(boardViewModel.goalCountProperty()).not());
    }

    // Configuration du bouton "Finish"
    private void setupFinishButton(BoardViewModel boardViewModel, Stage primaryStage) {
        button.setOnAction(action -> {
            boardViewModel.getBoard().setGrid(boardViewModel.getSaveGridDesign());
            new BoardView4Design(primaryStage, boardViewModel);
        });
    }
}
