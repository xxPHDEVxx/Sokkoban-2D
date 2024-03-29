package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableSet;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sokoban.viewmodel.BoardViewModel;

import java.io.File;

public class BoardView extends BorderPane {

    private final BoardViewModel boardViewModel;
    private int GRID_WIDTH = BoardViewModel.gridWidth();
    private int GRID_HEIGHT = BoardViewModel.gridHeight();

    private final Label headerLabel = new Label("");
    private final Label headerLabel2 = new Label("");
    private final VBox vbox = new VBox();
    private final HBox headerBox = new HBox();
    private final HBox headerBox2 = new HBox();
    private final ToolView ToolView = new ToolView();
    private final Menu fileMenu = new Menu("Fichier");
    private final MenuItem menuItemNew= new MenuItem("New...");
    private final MenuItem menuItemOpen = new MenuItem("Open...");
    private final MenuItem menuItemSave = new MenuItem("Save As...");
    private final MenuItem menuItemExit = new MenuItem("Exit...");
    private static HBox boardGame = new HBox();
    private static final int SCENE_MIN_WIDTH = 1080;
    private static final int SCENE_MIN_HEIGHT = 830;

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

    private void configMainComponents(Stage stage){
        stage.setTitle("Sokoban");
        createMenuBar(stage);
        createHeader();
        insertHeader();
    }
    public void createGrid(Scene scene){
        DoubleBinding gridWidth = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get() - ToolView.widthProperty().get(), heightProperty().get() -
                            (headerBox.heightProperty().get() + headerBox2.heightProperty().get()));
                    return Math.floor(size / GRID_WIDTH) * GRID_WIDTH;
                },
                scene.widthProperty(),
                scene.heightProperty(),
                headerBox.heightProperty());

        DoubleBinding gridHeight = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(heightProperty().get() - (headerBox.heightProperty().get()+ headerBox2.heightProperty().get()) , widthProperty().get() -
                            ToolView.widthProperty().get());
                    return Math.floor(size / GRID_HEIGHT) * GRID_HEIGHT;
                },
                scene.widthProperty(),
                scene.heightProperty(),
                headerBox.heightProperty());
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
            if (BoardViewModel.isChanged()){
                SaveConfirm.showDialog();
                BoardViewModel.exitMenu();
            } else {
                BoardViewModel.exitMenu();
            }
        });

        menuItemNew.setOnAction(action -> {
            if (BoardViewModel.isChanged()){
                SaveConfirm.showDialog();
            }
            NewGridView.showDialog(this);

            //fonction qui doit check si le board a changé
        });

        menuItemOpen.setOnAction(action -> {
            if (BoardViewModel.isChanged()){
                SaveConfirm.showDialog();
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(stage);
                //gérer avec un try catch?
                BoardViewModel.openBoard();
            } else {
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(stage);
                //gérer avec un try catch?
                BoardViewModel.openBoard();
                // correction avec le boutton cancel
            }
        });
        menuItemSave.setOnAction(action -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showSaveDialog(stage);
            //gérer avec un try catch?
            BoardViewModel.saveMenu();
        });
    }
    public void createHeader(){

        headerLabel.textProperty().bind(boardViewModel.filledCellsCountProperty()
                .asString("Number of filled cells: %d of " + boardViewModel.maxFilledCells()));
        Font font = Font.font("Verdana", FontWeight.BOLD, 20);
        headerLabel.setFont(font);

        headerLabel2.textProperty().bind(Bindings.when(boardViewModel.errorsProperty().emptyProperty())
                .then("")
                .otherwise(
                        Bindings.createStringBinding(() -> {
                            StringBuilder errorsStringBuilder = new StringBuilder();
                            ObservableSet<String> errorsList = boardViewModel.errorsProperty();
                            for (String error : errorsList) {
                                errorsStringBuilder.append(error).append("\n");
                            }
                            return "Please correct the following error(s): \n" + errorsStringBuilder ;
                        })
                )
        );



    }
    public void insertHeader() {
        headerLabel2.setTextFill(Color.RED);
        headerLabel.getStyleClass().add("header");
        headerBox.getChildren().add(headerLabel);
        headerBox2.getChildren().add(headerLabel2);
        headerBox.setAlignment(Pos.CENTER);
        headerBox2.setAlignment(Pos.CENTER);
        vbox.getChildren().add(headerBox);
        vbox.getChildren().add(headerBox2);
        headerBox.setMinHeight(10);
        headerBox2.setMinHeight(70);
        headerBox.setMinWidth(100);
        headerBox2.setMinWidth(100);
    }


    public void refresh(){
        createGrid(scene);
        createHeader();
     }


}
