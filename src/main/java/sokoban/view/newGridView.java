package sokoban.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
            //faire les traitements avant de fermer
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
        txtWidth.setMinSize(120,15);
        txtHeight.setMinSize(120,15);

        hboxWidth.setSpacing(13);
        hboxHeight.setSpacing(10);
        hboxWidth.getChildren().addAll(labelWidth,txtWidth);
        hboxHeight.getChildren().addAll(labelHeight,txtHeight);
        vboxtxt.getChildren().addAll(hboxWidth,hboxHeight);
        hboxbtn.getChildren().addAll(btnOk,btnCancel);
        vbox.getChildren().addAll(label,vboxtxt,hboxbtn);

        hboxbtn.setAlignment(Pos.BASELINE_RIGHT);
        hboxbtn.setSpacing(10);
        vboxtxt.setAlignment(Pos.CENTER_LEFT);
        label.setAlignment(Pos.TOP_LEFT);
        labelWidth.setLineSpacing(10);
        vboxtxt.setPadding(new Insets(10));
        vbox.setPadding(new Insets(10));

        Scene dialogScene = new Scene(vbox, 250, 135);
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
}
