package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
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
import sokoban.model.Direction;
import sokoban.model.Grid;
import sokoban.model.Grid4Design;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.GridViewModel;

import java.io.File;

public abstract class BoardView extends BorderPane {

    protected BoardViewModel boardViewModel;
    private int GRID_WIDTH = BoardViewModel.gridWidth();
    private int GRID_HEIGHT = BoardViewModel.gridHeight();

    private final Label cellCountLabel = new Label("");
    private final Label errPlayer = new Label("");
    private final Label errGoal = new Label("- At least one target is required.");
    private final Label errBox = new Label("");
    private final Label errCountBoxGoal = new Label("- Number of box and target must be equals.");
    private final VBox vbox = new VBox();
    private final HBox boxCellCount = new HBox();
    private final VBox boxRules = new VBox();
    private final ToolView ToolView = new ToolView();
    private final Menu fileMenu = new Menu("Fichier");
    private final MenuItem menuItemNew = new MenuItem("New...");
    private final MenuItem menuItemOpen = new MenuItem("Open...");
    private final MenuItem menuItemSave = new MenuItem("Save As...");
    private final MenuItem menuItemExit = new MenuItem("Exit...");
    private static HBox boardGame = new HBox();
    private static final int SCENE_MIN_WIDTH = 1080;
    private static final int SCENE_MIN_HEIGHT = 800;
    private static HBox box = new HBox();
    private Scene scene;
    private GridView gridView;
    private Button btnPlay = new Button("Play");
    private static Stage primaryStage;
    private  Stage playStage;

    private DoubleBinding gridWidth ;
    private DoubleBinding gridHeight ;
    public BoardView(Stage primaryStage, BoardViewModel boardViewModel) {
        this.primaryStage = primaryStage;
        this.boardViewModel = boardViewModel;
        start(this.primaryStage);
    }


    public void start(Stage stage){
        configMainComponents(stage);

        boardGame.getChildren().addAll(ToolView, this);
        Scene scene = new Scene(vbox,SCENE_MIN_WIDTH,SCENE_MIN_HEIGHT);



        createGrid(scene);
        vbox.getChildren().add(boardGame);
        //integration du bouton play
        HBox boxBtn = new HBox();
        boxBtn.setAlignment(Pos.CENTER);
        boxBtn.setPadding(new Insets(10));
        boxBtn.getChildren().add(btnPlay);
        vbox.getChildren().add(boxBtn);

        stage.setScene(scene);
        stage.show();
        this.scene = scene;


    }

    private void configMainComponents(Stage stage) {
        stage.setTitle("Sokoban");
        createMenuBar(stage);
        createHeader();
        insertHeader();

        actionBtnPlay();
    }


    public void createGrid(Scene scene) {
        gridWidth = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get() - ToolView.widthProperty().get(), heightProperty().get() -
                            (boxCellCount.heightProperty().get() + boxRules.heightProperty().get()));
                    return Math.floor(size / GRID_WIDTH) * GRID_WIDTH;
                },
                scene.widthProperty(),
                scene.heightProperty(),
                boxCellCount.heightProperty());

        gridHeight = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(heightProperty().get() - (boxCellCount.heightProperty().get()+ boxRules.heightProperty().get()) , widthProperty().get() -
                            ToolView.widthProperty().get());
                    return Math.floor(size / GRID_HEIGHT) * GRID_HEIGHT;
                },
                scene.widthProperty(),
                scene.heightProperty(),
                boxCellCount.heightProperty());
        gridView = new GridView4Design(boardViewModel.getGridViewModel(), gridWidth, gridHeight );

        // Définir la largeur et la hauteur de la grid en fonction de la largeur calculée
        gridView.minWidthProperty().bind(gridWidth);
        gridView.minHeightProperty().bind(gridHeight);


        boardGame.setAlignment(Pos.CENTER);

        setCenter(gridView);


    }

    private void createMenuBar(Stage stage) {
        fileMenu.getItems().addAll(menuItemNew, menuItemOpen, menuItemSave, menuItemExit);
        MenuBar sameMenuBar = new MenuBar();
        sameMenuBar.getMenus().add(fileMenu);
        vbox.getChildren().add(sameMenuBar);

        menuItemExit.setOnAction(action -> {
            if (BoardViewModel.isChanged()) {
                BoardViewModel.setChanged(false);
                SaveConfirm.showDialog();
                BoardViewModel.exitMenu();
            } else {
                BoardViewModel.exitMenu();
            }
        });

        menuItemNew.setOnAction(action -> {
            if (BoardViewModel.isChanged()) {
                BoardViewModel.setChanged(false);
                SaveConfirm.showDialog();

            }
            NewGridView.showDialog(this);

        });

        menuItemOpen.setOnAction(action -> {
            if (BoardViewModel.isChanged()) {
                BoardViewModel.setChanged(false);
                SaveConfirm.showDialog();

            }
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                boardViewModel.openBoard(selectedFile);
            }
        });
        menuItemSave.setOnAction(action -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showSaveDialog(stage);

            Grid4Design grid = boardViewModel.getGrid();
            GridViewModel gvm = boardViewModel.getGridVM();
            gvm.saveMenu(grid, selectedFile);
        });
    }
    public void createHeader(){

        cellCountLabel.textProperty().bind(boardViewModel.filledCellsCountProperty()
                .asString("Number of filled cells: %d of " + boardViewModel.maxFilledCells()));
        Font font = Font.font("Verdana", FontWeight.BOLD, 20);
        cellCountLabel.setFont(font);


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


        btnPlay.disableProperty().bind(boardViewModel.rulesOKProperty().not());;

    }
    public void insertHeader() {


        //afficher en rouge
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
    public void refresh(){
        createGrid(scene);
        createHeader();
        box.setAlignment(Pos.CENTER);
        btnPlay.disableProperty().bind(boardViewModel.rulesOKProperty().not());;

    }

    public void actionBtnPlay() {
        btnPlay.setOnAction(action -> {
            if (BoardViewModel.isChanged()) {
                BoardViewModel.setChanged(false);
                SaveConfirm.showDialog();
            }
            playGame();
        });
    }

    //partie jeu
    public void playGame() {
        if (boardViewModel.rulesOKProperty().get()) {
           playStage = new Stage();
            playStage.setTitle("Sokoban");
            primaryStage.hide();
            startGame(playStage);
        }
    }

    private void startGame(Stage playStage) {
        GridView4Play gridViewPlay = new GridView4Play(boardViewModel.getGridViewModel(), gridWidth, gridHeight);
        new BoardView4play(playStage, gridViewPlay, boardViewModel);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }



}