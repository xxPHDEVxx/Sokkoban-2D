package sokoban.model;

import javafx.scene.image.Image;

public abstract class GameElement {

    protected Image image;
    public  Image getImage() {
        return image;
    }

    public GameElement() {
    }
}
