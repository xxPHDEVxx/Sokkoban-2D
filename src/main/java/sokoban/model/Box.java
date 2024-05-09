package sokoban.model;

import javafx.scene.image.Image;

public class Box extends GameElement {

    private static int number;
    public Box() {
        this.value = CellValue.BOX;
        image = new Image("box.png");
    }
    @Override
    public void draw() {
    }
}