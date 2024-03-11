package sokoban.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SaveConfirm{
    public static void showDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Confirmation Dialog");

        Label label = new Label("Your Board has been modified.");
        Label label2 = new Label("Do you want to change your changes?");
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        Button cancelButton = new Button("Cancel");

        yesButton.setOnAction(e -> {
            dialog.close();
            // Mettez ici le code pour continuer après avoir cliqué sur "Yes"
        });
        noButton.setOnAction(e -> {
            dialog.close();
            // Mettez ici le code pour annuler après avoir cliqué sur "No"
        });
        cancelButton.setOnAction(actionEvent -> {
            dialog.close();
        });

        VBox vbox = new VBox();
        VBox vboxtxt = new VBox();
        VBox vbox2 = new VBox();
        HBox hbox = new HBox();
        yesButton.setMinSize(70,15);
        noButton.setMinSize(70,15);
        cancelButton.setMinSize(70,15);

        hbox.getChildren().addAll(yesButton,noButton,cancelButton);
        hbox.setAlignment(Pos.BASELINE_RIGHT);
        hbox.setSpacing(10);
        vboxtxt.getChildren().add(label);
        vbox2.getChildren().addAll(label2, hbox);
        vbox2.setSpacing(15);
        vbox.getChildren().addAll(vboxtxt, vbox2);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setPadding(new Insets(10));

        Scene dialogScene = new Scene(vbox, 315, 125);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
