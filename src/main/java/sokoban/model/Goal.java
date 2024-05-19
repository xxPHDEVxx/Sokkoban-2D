package sokoban.model;

import javafx.scene.image.Image;

public class Goal extends GameElement {

    public Goal() {
        image = new Image("goal.png");

    }

    public Goal copy() {
        return new Goal();
    }
}