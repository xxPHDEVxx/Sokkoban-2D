package sokoban;

import javafx.application.Application;
import javafx.stage.Stage;
import sokoban.model.Board;
import sokoban.view.BoardView;
import sokoban.viewmodel.BoardViewModel;

public class SokobanApp extends Application  {

    @Override
    public void start(Stage primaryStage) {
        Board board = new Board();
        BoardViewModel vm = new BoardViewModel(board);
        new BoardView(primaryStage, vm);
    }

    public static void main(String[] args) {
        launch();
    }

}
