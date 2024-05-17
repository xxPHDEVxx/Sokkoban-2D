package sokoban.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class Box extends GameElement {

    private static int globalNumber = 1;
    private final IntegerProperty number = new SimpleIntegerProperty();
    private Label numberLabel; // Ajout du label associé à la boîte


    public Box() {
        number.set(globalNumber);
        image = new Image("box.png");
        globalNumber++;
        numberLabel = new Label(String.valueOf(number.get())); // Création du label avec le numéro

    }

    public Label getNumberLabel() {
        return numberLabel;
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public static void reduceGlobalNumber() {
        Box.globalNumber = Box.globalNumber - 1;
    }
}
