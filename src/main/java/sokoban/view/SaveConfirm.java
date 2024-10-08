package sokoban.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sokoban.model.Board;
import sokoban.model.Grid;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.GridViewModel;

import java.io.File;

public class SaveConfirm {
    public enum Response {
        YES, NO, CANCEL
    }

    private static Response response;

    public static Response showDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Confirmation Dialog");

        Label label = new Label("Your Board has been modified.");
        Label label2 = new Label("Do you want to save your changes?");
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        Button cancelButton = new Button("Cancel");

        yesButton.setOnAction(e -> {
            response = Response.YES;
            dialog.close();
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showSaveDialog(dialog);
            if (selectedFile != null) {
                Board board = new Board();
                BoardViewModel boardVM = new BoardViewModel(board);
                Grid grid = boardVM.getBoard().getGrid();
                GridViewModel gvm = boardVM.getGridViewModel();
                gvm.saveMenu(grid, selectedFile);
            }
        });

        noButton.setOnAction(e -> {
            response = Response.NO;
            dialog.close();
        });

        cancelButton.setOnAction(e -> {
            response = Response.CANCEL;
            dialog.close();
        });

        HBox hbox = new HBox(10, yesButton, noButton, cancelButton);
        hbox.setAlignment(Pos.BASELINE_RIGHT);

        VBox vbox = new VBox(20, label, label2, hbox);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setPadding(new Insets(10));

        Scene dialogScene = new Scene(vbox, 315, 125);
        dialog.setScene(dialogScene);
        dialog.showAndWait();

        return response;
    }
}
