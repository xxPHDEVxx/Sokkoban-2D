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

    private BoardViewModel boardViewModel;

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

        // Désactiver le bouton Ok tant que les champs de texte sont vides
        btnOk.disableProperty().bind(txtWidth.textProperty().isEmpty().or(txtHeight.textProperty().isEmpty()));

        btnOk.setOnAction(e -> {
            int width = Integer.parseInt(txtWidth.getText());
            int height = Integer.parseInt(txtHeight.getText());
            boardViewModel.newGridMenu(width, height);
            dialog.close();
            boardView.refresh();
        });

        btnCancel.setOnAction(e -> dialog.close());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(label, txtWidth, txtHeight, new HBox(10, btnOk, btnCancel));
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        Scene dialogScene = new Scene(vbox, 300, 175);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
    public static TextFormatter<Integer> createNumericTextFormatter() {
        Pattern pattern = Pattern.compile("-?\\d*");

        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            } else {
                return null;
            }
        };
        return new TextFormatter<>(new IntegerStringConverter(), null, filter);
    }

    private static void updateButtonState(String width, String height, Button btnOk) {
        if (!width.isEmpty() && !height.isEmpty()) {
            int widthValue = Integer.parseInt(width);
            int heightValue = Integer.parseInt(height);
            if (widthValue >= 10 && widthValue <= 50 && heightValue >= 10 && heightValue <= 50) {
                btnOk.setDisable(false);
            } else {
                btnOk.setDisable(true);
            }
        } else {
            btnOk.setDisable(true);
        }
    }
}
