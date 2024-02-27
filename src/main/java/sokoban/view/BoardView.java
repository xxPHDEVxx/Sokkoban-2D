package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import sokoban.model.Board;
import sokoban.model.Grid;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.ToolViewModel;

import java.io.File;

public class BoardView extends BorderPane {
    private final BoardViewModel boardViewModel;
    FileChooser fileChooser = new FileChooser();
    private static final int GRID_WIDTH = BoardViewModel.gridWidth();
    private static final int SCENE_MIN_WIDTH = 500;
    private static final int SCENE_MIN_HEIGHT = 500;
    private final Label headerLabel = new Label("");
    private final BorderPane root = new BorderPane();
    private final VBox vbox = new VBox();
    private final HBox headerBox = new HBox();
    private final MenuBar menuBar = new MenuBar();
    private final ToolView ToolView = new ToolView();
    private final Menu fileMenu = new Menu("Fichier");
    private final MenuItem menuItemNew= new MenuItem("New...");
    private final MenuItem menuItemOpen = new MenuItem("Open...");
    private final MenuItem menuItemSave = new MenuItem("Save As...");
    private final MenuItem menuItemExit = new MenuItem("Exit...");
    public BoardView(Stage primaryStage, BoardViewModel boardViewModel) {
        this.boardViewModel = boardViewModel;
        start(primaryStage);
    }
    private void start(Stage stage){
        configMainComponents(stage);

        HBox box = new HBox(ToolView,this);
        Scene scene = new Scene(box);
        // implementation css ?
        stage.setScene(scene);
        stage.show();
        //Voir si bonne endroit pour les fonctionnalités
        menuItemExit.setOnAction(action -> {
            System.exit(0);
        });
        menuItemNew.setOnAction(action -> {
            Board board = new Board();
            //voir si ça créer nouveau et dialogue box pour confirmer
        });
        menuItemOpen.setOnAction(action -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
            // gérer ce qu'il doit faire avec le fichier
        });
        menuItemSave.setOnAction(action -> {
            //
        });;
//        stage.setMinHeight(stage.getHeight());
//        stage.setMinWidth(stage.getWidth());
    }
        private void createMenuBar() {
            fileMenu.getItems().addAll(menuItemNew, menuItemOpen, menuItemSave, menuItemExit);
            MenuBar sameMenuBar = new MenuBar(fileMenu);
            headerBox.getChildren().add(sameMenuBar);
            headerBox.setAlignment(Pos.CENTER_LEFT);
            root.setLeft(menuBar);

        }
    private void configMainComponents(Stage stage){
        stage.setTitle("Sokoban");
        createMenuBar();
        createGrid();
        createHeader();
    }
    public void createGrid(){
        DoubleBinding gridWidth = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get(), heightProperty().get() - headerBox.heightProperty().get());
                    return Math.floor(size / GRID_WIDTH) * GRID_WIDTH;
                },
                widthProperty(),
                heightProperty(),
                headerBox.heightProperty());
        GridView gridView = new GridView(boardViewModel.getGridViewModel(), gridWidth);

        /* gridView.minHeightProperty().bind(gridWidth);
        gridView.minWidthProperty().bind(gridWidth);
        gridView.maxHeightProperty().bind(gridWidth);
        gridView.maxWidthProperty().bind(gridWidth);

        -- Faire attention pour la taille de la grid par défault qui est de 10x15 --
        */

        setCenter(gridView);
    }
    public void createHeader(){
        headerLabel.textProperty().bind(boardViewModel.filledCellsCountProperty()
                .asString("Number of filled cells: %d of " + boardViewModel.maxFilledCells()));
        //headerLabel.textProperty().bind(boardViewModel,).asString("Please correct the following error(s)");
        headerLabel.getStyleClass().add("header");
        headerBox.getChildren().add(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        setTop(headerBox);
    }
}
