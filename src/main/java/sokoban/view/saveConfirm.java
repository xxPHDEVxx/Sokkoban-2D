package sokoban.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class saveConfirm{
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

        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(label,label2, yesButton, noButton, cancelButton);
        //mise en page de la box a faire

        Scene dialogScene = new Scene(vbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
