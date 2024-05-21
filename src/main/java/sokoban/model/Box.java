package sokoban.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class Box extends GameElement {

    private Label numberLabel = new Label();


    public Box() {
        image = new Image("box.png");
    }

    public Box copy() {
        Box copy = new Box();
        copy.numberLabel = this.getNumberLabel();
        return copy;
    }


    public Label getNumberLabel() {
        return numberLabel;
    }

    public void setNumberLabel(Label numberLabel) {
        this.numberLabel = numberLabel;
    }
}
