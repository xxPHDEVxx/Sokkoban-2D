package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
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
import sokoban.model.Grid;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.GridViewModel;

import java.io.File;

public class BoardView extends BorderPane {

    private final BoardViewModel boardViewModel;
    private int GRID_WIDTH = BoardViewModel.gridWidth();
    private int GRID_HEIGHT = BoardViewModel.gridHeight();

    private final Label cellCountLabel = new Label("");
    private final Label errPlayer = new Label("- A player is required.");
    private final Label errGoal = new Label("- At least one target is required.");
    private final Label errBox = new Label("- At least one box is required.");
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
    private static final int SCENE_MIN_HEIGHT = 830;
    private static HBox box = new HBox();
    private Stage primaryStage;
    private Scene scene;
    private GridView gridView;

    public BoardView(Stage primaryStage, BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;
        start(primaryStage);
    }
    public void start(Stage stage){
        configMainComponents(stage);

        boardGame.getChildren().addAll(ToolView, this);
        Scene scene = new Scene(vbox,SCENE_MIN_WIDTH,SCENE_MIN_HEIGHT);



        createGrid(scene);
        vbox.getChildren().add(boardGame);
        //integration de la grid dans la vue mais pas sur de la facon de faire

        stage.setScene(scene);
        stage.show();
        this.scene = scene;


    }

    private void configMainComponents(Stage stage) {
        stage.setTitle("Sokoban");
        createMenuBar(stage);
        createHeader();
        insertHeader();
    }

    public void createGrid(Scene scene) {
        DoubleBinding gridWidth = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get() - ToolView.widthProperty().get(), heightProperty().get() -
                            (boxCellCount.heightProperty().get() + boxRules.heightProperty().get()));
                    return Math.floor(size / GRID_WIDTH) * GRID_WIDTH;
                },
                scene.widthProperty(),
                scene.heightProperty(),
                boxCellCount.heightProperty());

        DoubleBinding gridHeight = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(heightProperty().get() - (boxCellCount.heightProperty().get()+ boxRules.heightProperty().get()) , widthProperty().get() -
                            ToolView.widthProperty().get());
                    return Math.floor(size / GRID_HEIGHT) * GRID_HEIGHT;
                },
                scene.widthProperty(),
                scene.heightProperty(),
                boxCellCount.heightProperty());
        gridView = new GridView(boardViewModel.getGridViewModel(), gridWidth, gridHeight );

        // Définir la largeur et la hauteur de la grid en fonction de la largeur calculée
        // Lier la largeur et la hauteur de la grid à la largeur calculée
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
                SaveConfirm.showDialog();
                BoardViewModel.exitMenu();
            } else {
                BoardViewModel.exitMenu();
            }
        });

        menuItemNew.setOnAction(action -> {
            if (BoardViewModel.isChanged()) {
                SaveConfirm.showDialog();
            }
            NewGridView.showDialog(this);

            //fonction qui doit check si le board a changé
        });

        menuItemOpen.setOnAction(action -> {
            if (BoardViewModel.isChanged()) {
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

            Grid grid = boardViewModel.getGrid();
            GridViewModel gvm = boardViewModel.getGridVM();
            gvm.saveMenu(grid, selectedFile);
        });
    }
    public void createHeader(){

        cellCountLabel.textProperty().bind(boardViewModel.filledCellsCountProperty()
                .asString("Number of filled cells: %d of " + boardViewModel.maxFilledCells()));
        Font font = Font.font("Verdana", FontWeight.BOLD, 20);
        cellCountLabel.setFont(font);

        //visible de base
        errBox.setVisible(true);
        errBox.setManaged(true);

        errGoal.setVisible(true);
        errGoal.setManaged(true);

        errPlayer.setVisible(true);
        errPlayer.setManaged(true);

        errCountBoxGoal.setVisible(true);
        errCountBoxGoal.setManaged(true);


    }
    public void insertHeader() {

        //se 'supprime' et enleve son espace quand la condition est respecté
        errBox.visibleProperty().bind(boardViewModel.boxCountProperty().lessThan(1));
        errBox.managedProperty().bind(errBox.visibleProperty());

        errGoal.visibleProperty().bind(boardViewModel.goalCountProperty().lessThan(1));
        errGoal.managedProperty().bind(errGoal.visibleProperty());

        errPlayer.visibleProperty().bind(boardViewModel.playerCountProperty().isNotEqualTo(1));
        errPlayer.managedProperty().bind(errPlayer.visibleProperty());

        errCountBoxGoal.visibleProperty().bind(boardViewModel.boxCountProperty().isNotEqualTo(boardViewModel.goalCountProperty()));
        errCountBoxGoal.managedProperty().bind(errCountBoxGoal.visibleProperty());

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
        boxCellCount.setMinHeight(10);
        boxCellCount.setMinWidth(100);
    }
    public void refresh(){
        createGrid(scene);
        createHeader();
        box.setAlignment(Pos.CENTER);
    }
}