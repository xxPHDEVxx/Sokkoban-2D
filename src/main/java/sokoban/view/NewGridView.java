package sokoban.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import sokoban.viewmodel.BoardViewModel;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class NewGridView {

    private final BoardViewModel boardViewModel;

    public NewGridView(BoardViewModel boardViewModel){
        this.boardViewModel = boardViewModel;
    }

    public void showDialog(BoardView boardView) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Sokoban");

        Label label = new Label("Give new game dimensions");
        TextField txtWidth = new TextField();
        TextField txtHeight = new TextField();
        Button btnOk = new Button("Ok");
        Button btnCancel = new Button("Cancel");

        // Validation des dimensions entre 10 et 50 avec affichage des messages d'erreur
        String errorMessage = "Value must be between 10 and 50";
        Label errorLabelWidth = new Label();
        Label errorLabelHeight = new Label();

        txtWidth.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtWidth.setText(newValue.replaceAll("[^\\d]", ""));
            }
            int value = !newValue.isEmpty() ? Integer.parseInt(newValue) : 0;
            if (value < 10 || value > 50) {
                errorLabelWidth.setText(errorMessage);
            } else {
                errorLabelWidth.setText("");
            }
            btnOk.setDisable(!errorLabelWidth.getText().isEmpty() || !errorLabelHeight.getText().isEmpty() ||
                    txtHeight.getText().isEmpty() || txtWidth.getText().isEmpty());
        });

        txtHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtHeight.setText(newValue.replaceAll("[^\\d]", ""));
            }
            int value = !newValue.isEmpty() ? Integer.parseInt(newValue) : 0;
            if (value < 10 || value > 50) {
                errorLabelHeight.setText(errorMessage);
            } else {
                errorLabelHeight.setText("");
            }
            btnOk.setDisable(!errorLabelWidth.getText().isEmpty() || !errorLabelHeight.getText().isEmpty() ||
                    txtWidth.getText().isEmpty() || txtHeight.getText().isEmpty());
        });

        btnOk.setOnAction(e -> {
            int width = Integer.parseInt(txtWidth.getText());
            int height = Integer.parseInt(txtHeight.getText());
            boardViewModel.newGridMenu(width, height);
            dialog.close();
            boardView.refresh();
        });

        btnCancel.setOnAction(e -> dialog.close());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(label, txtWidth, errorLabelWidth, txtHeight, errorLabelHeight, new HBox(10, btnOk, btnCancel));
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        Scene dialogScene = new Scene(vbox, 300, 175);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
