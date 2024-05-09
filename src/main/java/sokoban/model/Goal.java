package sokoban.model;

import javafx.scene.image.Image;

public class Goal extends GameElement {

    public Goal() {
        this.value = CellValue.GOAL;
        image = new Image("goal.png");

    }
    @Override
    public void draw() {
    }
}