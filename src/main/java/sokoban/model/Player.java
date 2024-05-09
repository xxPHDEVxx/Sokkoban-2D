package sokoban.model;

import javafx.scene.image.Image;

public class Player extends GameElement {

    public Player() {
        this.value = CellValue.PLAYER;
        image = new Image("player.png");

    }
    @Override
    public void draw() {
    }
}