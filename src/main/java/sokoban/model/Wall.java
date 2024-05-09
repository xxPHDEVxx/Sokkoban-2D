package sokoban.model;

import javafx.scene.image.Image;

public class Wall extends GameElement {

    public Wall() {
        this.value = CellValue.WALL;
        image = new Image("wall.png");
    }
    @Override
    public void draw() {

    }
}