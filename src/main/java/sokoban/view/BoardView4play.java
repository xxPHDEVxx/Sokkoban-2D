package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
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
    private Button btnFinish = new Button("Finish");
    private Button btnMushroom = new Button("Show mushroom");
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
        showMushroom();
    }

    // Initialisation des composants de la vue
    private void initialize() {

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
        HBox boxBtn = new HBox(btnFinish, btnMushroom);
        boxBtn.setSpacing(15);
        boxBtn.setAlignment(Pos.CENTER);

        headerPlay.getChildren().addAll(title, numberOfMovesPlayed, goal, finisher);
        level.getChildren().add(gridView);
        boardLvl.getChildren().addAll(headerPlay, level, boxBtn);
        bindings();
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
        sceneLevel.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }

    // Méthode pour gérer les pressions des touches
    private void handleKeyPress(KeyEvent event) {
        if (new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN).match(event)) {
            boardViewModel.undo();
            return;
        } else if (new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN).match(event)) {
            boardViewModel.redo();
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
    }

    private void bindings(){
        btnFinish.disableProperty().bind(boardViewModel.boxInTargetCountProperty().isEqualTo(boardViewModel.goalCountProperty()).not());
        btnMushroom.disableProperty().bind(boardViewModel.boxInTargetCountProperty().isEqualTo(boardViewModel.goalCountProperty()));

        // Ajoute ou retire le filtre d'événements en fonction de la valeur de la liaison
        boardViewModel.boxInTargetCountProperty().isEqualTo(boardViewModel.goalCountProperty()).not()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        btnMushroom.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventFilter);
                    } else {
                        btnMushroom.getScene().removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventFilter);
                    }
                });
    }

    // Configuration du bouton "Finish"
    private void setupFinishButton(BoardViewModel boardViewModel, Stage primaryStage) {
        btnFinish.setOnAction(action -> {
            GameAgain();
            new BoardView4Design(primaryStage, boardViewModel);
            boardViewModel.moveCountProperty().set(0);
        });
    }

    public void GameAgain(){
        boardViewModel.endGame();
        boardViewModel.goToDesign();
        boardViewModel.configureBindings();
        boardViewModel.setChanged(false);
    }

    // Configuration bouton "show mushroom"
    private void showMushroom(){
        btnMushroom.setOnAction(action ->{
            if(boardViewModel.hideOrShow()){
                btnMushroom.setText("Hide mushroom");
                // annuler clic si mushroom visible
                btnMushroom.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventFilter);
            } else {
                btnMushroom.setText("Show mushroom");
                // Retirer le filtre de clic lorsque le mushroom est caché
                btnMushroom.getScene().removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventFilter);
            }
        });
    }

    // Filtre d'événements pour annuler les clics de souris
    private final javafx.event.EventHandler<MouseEvent> mouseEventFilter = event -> {
        // Consomme l'événement de clic de la souris
        event.consume();
    };
}
