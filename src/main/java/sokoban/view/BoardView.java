package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sokoban.model.Board;
import sokoban.model.Grid;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.GridViewModel;
import sokoban.viewmodel.ToolViewModel;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class BoardView extends BorderPane {

    protected BoardViewModel boardViewModel;
    protected ToolViewModel toolViewModel;

    // Labels to display various status messages
    private final Label cellCountLabel = new Label("");
    private final Label errPlayer = new Label("");
    private final Label errGoal = new Label("- At least one target is required.");
    private final Label errBox = new Label("");
    private final Label errCountBoxGoal = new Label("- Number of box and target must be equals.");
    private final VBox vbox = new VBox();
    private final HBox boxCellCount = new HBox();
    private final VBox boxRules = new VBox();
    private final ToolView toolView;
    private final Menu fileMenu = new Menu("Fichier");
    private final MenuItem menuItemNew = new MenuItem("New...");
    private final MenuItem menuItemOpen = new MenuItem("Open...");
    private final MenuItem menuItemSave = new MenuItem("Save As...");
    private final MenuItem menuItemExit = new MenuItem("Exit...");
    private final HBox boardGame = new HBox();
    protected final int SCENE_MIN_WIDTH = 1080;
    protected final int SCENE_MIN_HEIGHT = 800;
    private final HBox box = new HBox();
    protected Scene scene;
    protected GridView gridView;
    private final Button btnPlay = new Button("Play");
    protected Stage primaryStage;
    protected DoubleBinding gridWidth;
    protected DoubleBinding gridHeight;
    protected VBox boardLvl = new VBox();

    // Constructor to initialize the BoardView
    public BoardView(Stage primaryStage, BoardViewModel boardViewModel) {
        this.primaryStage = primaryStage;
        this.boardViewModel = boardViewModel;
        toolViewModel = new ToolViewModel(boardViewModel);
        toolView = new ToolView(toolViewModel);
        start(this.primaryStage);

        layoutControls();
    }

    // Method to start the application
    public void start(Stage stage) {
        configMainComponents(stage);

        boardGame.getChildren().addAll(toolView, this);
        scene = new Scene(vbox, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);

        createGrid(scene);
        vbox.getChildren().add(boardGame);

        // Integration du bouton play
        HBox boxBtn = new HBox();
        boxBtn.setAlignment(Pos.CENTER);
        boxBtn.setPadding(new Insets(10));
        boxBtn.getChildren().add(btnPlay);
        vbox.getChildren().add(boxBtn);

        stage.setScene(scene);
        stage.show();
    }
    private void layoutControls() {
        responsiveBox();
        responsiveLabel();
    }


    // Method to configure main components
    private void configMainComponents(Stage stage) {
        // Lier le titre avec isChanged
        stage.titleProperty().bind(
                Bindings.when(boardViewModel.isChangedProperty())
                        .then("Sokoban(*)")
                        .otherwise("Sokoban")
        );

        // Modifier le titre du stage en fonction de BoardView4Play et de la propriété isChanged
        if (this instanceof BoardView4Play) {
            stage.titleProperty().bind(
                    Bindings.when(boardViewModel.isChangedProperty())
                            .then("Jeu(*)")
                            .otherwise("Jeu")
            );
        }

        createMenuBar(stage);
        createHeader();
        insertHeader();
        actionBtnPlay();
    }

    // Method to create the grid
    public void createGrid(Scene scene) {

        gridWidth = scene.widthProperty().multiply(0.75);
        gridHeight = scene.heightProperty().multiply(0.6);


        gridView = new GridView4Design(boardViewModel.getGridViewModel(), gridWidth, gridHeight);

        // Définir la largeur et la hauteur de la grid en fonction de la largeur calculée
        gridView.prefWidthProperty().bind(gridWidth);
        gridView.prefHeightProperty().bind(gridHeight);

        setCenter(gridView);
    }


    // Method to create the menu bar
    private void createMenuBar(Stage stage) {
        fileMenu.getItems().addAll(menuItemNew, menuItemOpen, menuItemSave, menuItemExit);
        MenuBar sameMenuBar = new MenuBar();
        sameMenuBar.getMenus().add(fileMenu);
        vbox.getChildren().add(sameMenuBar);

        // Set actions for menu items
        menuItemExit.setOnAction(action -> handleExit());
        menuItemNew.setOnAction(action -> handleNew());
        menuItemOpen.setOnAction(action -> {
            try {
                handleOpen(stage);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        menuItemSave.setOnAction(action -> handleSave(stage));
    }

    // Method to handle exit action
    private void handleExit() {
        if (boardViewModel.isChanged()) {
            if (SaveConfirm.showDialog() == SaveConfirm.Response.CANCEL) {
                return;
            }
            boardViewModel.setChanged(false);
        }
        BoardViewModel.exitMenu();
    }

    // Method to handle new grid
    private void handleNew() {
        if (boardViewModel.isChanged()) {
            if (SaveConfirm.showDialog() == SaveConfirm.Response.CANCEL) {
                return;
            }
            boardViewModel.setChanged(false);
        }
        NewGridView newGridView = new NewGridView(boardViewModel);
        newGridView.showDialog(this);
    }

    // Method to handle open action
    private void handleOpen(Stage stage) throws FileNotFoundException {
        if (boardViewModel.isChanged()) {
            if (SaveConfirm.showDialog() == SaveConfirm.Response.CANCEL) {
                return;
            }
            boardViewModel.setChanged(false);
        }
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            boardViewModel.openBoard(selectedFile);
            refresh();
        }
    }

    // Method to handle save action
    private void handleSave(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(stage);

        Grid grid = boardViewModel.getBoard().getGrid();
        GridViewModel gvm = boardViewModel.getGridViewModel();
        gvm.saveMenu(grid, selectedFile);
        boardViewModel.setChanged(false);
    }

    // Method to create the header
    public void createHeader() {
        cellCountLabel.textProperty().bind(boardViewModel.filledCellsCountProperty()
                .asString("Number of filled cells: %d of " + boardViewModel.maxFilledCells()));

        errBox.textProperty().bind(Bindings.when(boardViewModel.boxCountProperty().greaterThan(0))
                .then("")
                .otherwise("At least one box is required."));

        errGoal.textProperty().bind(Bindings.when(boardViewModel.goalCountProperty().greaterThan(0))
                .then("")
                .otherwise("At least one target is required."));

        errPlayer.textProperty().bind(Bindings.when(boardViewModel.playerCountProperty().isEqualTo(1))
                .then("")
                .otherwise("A player is required."));

        errCountBoxGoal.textProperty().bind(Bindings.when(boardViewModel.boxCountProperty().isEqualTo(boardViewModel.goalCountProperty()))
                .then("")
                .otherwise("The number of boxes and targets must be equal."));

        btnPlay.disableProperty().bind(boardViewModel.rulesOKProperty().not());
    }

    // Method to insert the header into the VBox
    public void insertHeader() {
        // Afficher en rouge
        errBox.setTextFill(Color.RED);
        errGoal.setTextFill(Color.RED);
        errPlayer.setTextFill(Color.RED);
        errCountBoxGoal.setTextFill(Color.RED);

        cellCountLabel.getStyleClass().add("header");

        boxCellCount.getChildren().add(cellCountLabel);
        boxRules.getChildren().addAll(errBox, errGoal, errPlayer, errCountBoxGoal);
        boxCellCount.setAlignment(Pos.CENTER);
        boxRules.setAlignment(Pos.CENTER);
        vbox.getChildren().add(boxCellCount);
        vbox.getChildren().add(boxRules);
        boxCellCount.setMinWidth(100);

    }

    public void responsiveBox(){
        // Liaison de la largeur de toolView à 20% de la largeur de boardGame
        toolView.prefWidthProperty().bind(boardGame.widthProperty().multiply(0.15));

        // Liaison de la largeur de boardView à 80% de la largeur de boardGame
        this.prefWidthProperty().bind(boardGame.widthProperty().multiply(0.8));

        // Liaison de la hauteur de boardGame à la hauteur de la scène moins la hauteur de vbox
        boardGame.prefHeightProperty().bind(scene.heightProperty().subtract(vbox.heightProperty()));
        boardGame.prefWidthProperty().bind(scene.widthProperty().subtract(vbox.widthProperty()));

        // Liaison de la largeur de vbox à la largeur de la scène moins la largeur de toolView
        vbox.prefWidthProperty().bind(scene.widthProperty().subtract(toolView.widthProperty()));
        vbox.prefHeightProperty().bind(scene.heightProperty().subtract(toolView.heightProperty()));
    }

    public void responsiveLabel(){

        cellCountLabel.fontProperty().bind(Bindings.createObjectBinding(() ->
                        Font.font("Verdana", FontWeight.BOLD, boxCellCount.getWidth() / 50),
                boxCellCount.widthProperty()));

        // Liaison de la largeur de boxCellCount et boxRules à la largeur du parent
        boxCellCount.prefWidthProperty().bind(vbox.widthProperty());
        boxRules.prefWidthProperty().bind(vbox.widthProperty());

        // Ajustement de la taille du texte dans boxRules
        errBox.fontProperty().bind(Bindings.createObjectBinding(() ->
                        Font.font("Verdana", boxRules.getWidth() / 70),
                boxRules.widthProperty()));
        errGoal.fontProperty().bind(Bindings.createObjectBinding(() ->
                        Font.font("Verdana", boxRules.getWidth() / 70),
                boxRules.widthProperty()));
        errPlayer.fontProperty().bind(Bindings.createObjectBinding(() ->
                        Font.font("Verdana", boxRules.getWidth() / 70),
                boxRules.widthProperty()));
        errCountBoxGoal.fontProperty().bind(Bindings.createObjectBinding(() ->
                        Font.font("Verdana", boxRules.getWidth() / 60),
                boxRules.widthProperty()));
    }

    // Method to refresh the view
    public void refresh() {
        createGrid(scene);
        createHeader();
        box.setAlignment(Pos.CENTER);
        btnPlay.disableProperty().bind(boardViewModel.rulesOKProperty().not());
    }

    // Method to define play button action
    public void actionBtnPlay() {
        btnPlay.setOnAction(action -> {
            if (boardViewModel.isChanged()) {
                if (SaveConfirm.showDialog() == SaveConfirm.Response.CANCEL) {
                    return;
                }
            }
            playGame();
        });
    }

    // Method to start the game
    public void playGame() {
        if (boardViewModel.rulesOKProperty().get()) {
            startGame(primaryStage);
        }
    }

    // Method to start the game stage
    private void startGame(Stage playStage) {
        boardViewModel.saveGridDesign(); // Sauvegarde état actuel de la grille avant de lancer le jeu
        boardViewModel.getBoard().setGrid(boardViewModel.gridGame()); // création et attribution grille de jeu
        GridView4Play gridViewPlay = new GridView4Play(boardViewModel.getGridViewModel(), gridWidth, gridHeight);

        new BoardView4Play(playStage, gridViewPlay, boardViewModel);
        // refresh des cellules
        gridViewPlay.fillGrid(boardViewModel.getGridViewModel(), gridWidth, gridHeight);
    }
}
