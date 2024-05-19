package sokoban.model;

import javafx.scene.image.Image;

public class Player extends GameElement {

    public Player() {
        image = new Image("player.png");

    }

    public Player copy() {
        return new Player();
    }
}