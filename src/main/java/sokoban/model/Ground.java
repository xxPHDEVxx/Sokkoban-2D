package sokoban.model;

import javafx.scene.image.Image;

public class Ground extends GameElement {


    public Ground() {
        image = new Image("ground.png");
    }

    public Ground copy() {
        return new Ground();
    }
}