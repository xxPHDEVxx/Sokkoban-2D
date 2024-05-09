package sokoban.model;

import javafx.scene.image.Image;

public class Ground extends GameElement {


    public Ground() {
        this.value = CellValue.GROUND;
        image = new Image("ground.png");
    }
    @Override
    public void draw() {
    }
}