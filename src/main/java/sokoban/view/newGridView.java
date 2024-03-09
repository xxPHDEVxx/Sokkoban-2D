package sokoban.view;

import javafx.animation.PauseTransition;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import sokoban.model.Grid;
import sokoban.viewmodel.BoardViewModel;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class newGridView {
    public static void showDialog(){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Sokoban");

        Label label = new Label("Give new game dimensions");
        Label labelWidth = new Label("Width");
        Label labelHeight = new Label("Height");
        TextField txtWidth = new TextField();
        TextField txtHeight = new TextField();
        Button btnOk = new Button("Ok");
        Button btnCancel = new Button("Cancel");
        btnOk.setDisable(true);

        btnOk.setOnAction(e -> {
            int width = Integer.parseInt(txtWidth.getText());
            int height = Integer.parseInt(txtHeight.getText());

            BoardViewModel.newGridMenu(width,height);

            dialog.close();
        });
        btnCancel.setOnAction(e -> {
            dialog.close();
        });

        VBox vbox = new VBox();
        VBox vboxtxt = new VBox();
        HBox hboxWidth = new HBox();
        HBox hboxHeight = new HBox();
        HBox hboxbtn = new HBox();
        btnOk.setMinSize(70,15);
        btnCancel.setMinSize(70,15);
        txtWidth.setMinSize(150,15);
        txtHeight.setMinSize(150,15);

        txtWidth.setTextFormatter(createNumericTextFormatter());
        txtHeight.setTextFormatter(createNumericTextFormatter());

        Label errorLabelWidth = new Label();
        Label errorLabelHeight = new Label();
        errorLabelWidth.setTextFill(Color.RED);
        errorLabelHeight.setTextFill(Color.RED);

        txtWidth.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                int value = Integer.parseInt(newValue);
                if (value < 10) {
                    errorLabelWidth.setText("Width must be at least 10");
                }
                else if (value > 50){
                    errorLabelWidth.setText("Width must be at most 50");
                } else {
                    errorLabelWidth.setText("");
                }
            } else {
                errorLabelWidth.setText("");
            }
            updateButtonState(txtWidth.getText(), txtHeight.getText(), btnOk);
        });

        txtHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                int value = Integer.parseInt(newValue);
                if (value < 10) {
                    errorLabelHeight.setText("Height must be at least 10");
                }
                else if (value > 50){
                    errorLabelHeight.setText("Height must be at most 50");
                } else {
                    errorLabelHeight.setText("");
                }
            } else {
                errorLabelHeight.setText("");
            }
            updateButtonState(txtWidth.getText(), txtHeight.getText(), btnOk);
        });

        hboxWidth.setSpacing(13);
        hboxHeight.setSpacing(10);
        hboxWidth.getChildren().addAll(labelWidth,txtWidth);
        hboxHeight.getChildren().addAll(labelHeight,txtHeight);
        errorLabelWidth.setPadding(new Insets(5, 0, 0, 0));
        vboxtxt.getChildren().addAll(hboxWidth,errorLabelWidth,hboxHeight,errorLabelHeight);
        errorLabelWidth.setPadding(new Insets(0, 0, 10, 0));
        hboxbtn.getChildren().addAll(btnOk,btnCancel);
        vbox.getChildren().addAll(label,vboxtxt,hboxbtn);

        hboxbtn.setAlignment(Pos.BASELINE_RIGHT);
        hboxbtn.setSpacing(10);
        vboxtxt.setAlignment(Pos.CENTER_LEFT);
        label.setAlignment(Pos.TOP_LEFT);
        labelWidth.setLineSpacing(10);
        vboxtxt.setPadding(new Insets(10));
        vbox.setPadding(new Insets(10));

        Scene dialogScene = new Scene(vbox, 300, 175);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
    private static void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
