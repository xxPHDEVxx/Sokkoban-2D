package sokoban.model;

import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class Goal extends GameElement {

    private Label numberLabel = new Label();  // Label to display the number of the goal


    public Goal() {
        image = new Image("goal.png");

    }

    public Goal copy() {
        Goal copy = new Goal();
        copy.numberLabel = this.getNumberLabel();
        return copy;

    }
    public Label getNumberLabel() {
        System.out.println(numberLabel);
        return numberLabel;
    }

    public void setNumberLabel(Label label) {
        this.numberLabel = label;
    }
}